package org.aspyker.tools

import org.apache.log4j.Logger
import org.joda.time.DateTime
import com.typesafe.config._
import com.benfante.jslideshare.messages.{Slideshow, User}
import scala.xml._
import scala.collection.JavaConverters._
import com.github.mustachejava._
import java.io.PrintWriter
import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import com.google.gdata.data.youtube.{VideoEntry, VideoFeed}
import java.net.URL;

object ReInvent2014Converter {
  val log = Logger.getLogger(this.getClass)
  val config = ConfigFactory.load()

  def getAmazonSlides(log:Logger, config:Config) : Array[Slideshow] = {
    return new Array[Slideshow](0)
  }
  
  def getDivField(classType:String, spans:NodeSeq) : String = {
    for (span <- spans) {
      val clazz = (span \ "@class").text
      if (clazz == classType) {
        return span.text
      }
    }
    return ""
  }
  
  def getSessionInfos(log:Logger, filename:String, slideShares:Array[Slideshow], config:Config, newerThanDate:DateTime) : List[SessionInfo] = {
    var infos = List[SessionInfo]()
    
    val xml = XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
    val root = XML.load(filename)

    val service = new YouTubeService(config.getString("converter.youtube.client.id"))
    val query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"))
    query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE)
    query.setSafeSearch(YouTubeQuery.SafeSearch.NONE)
    
    val divs = root \\ "div"
    for (div <- divs) {
      val id = (div \ "@id").text
      if (id.startsWith("session_")) {
        val pureId = id.substring(8);
        log.debug("processing session " + pureId)
        val sessInfo = new SessionInfo()
        
        val spans = div \\ "span"
        val smalls = div \\ "small"
        val shortHumanId = getDivField("abbreviation", spans).split(" +")(0)
        if (!shortHumanId.contains("-R")) {
          sessInfo.session = shortHumanId
          sessInfo.title = getDivField("title", spans)
          sessInfo.abstract1 = getDivField("abstract", spans)
          sessInfo.speakers = getDivField("speakers", smalls)
          
          var queryString = "AWS " + shortHumanId
          var youtubeUrl = getYouTubeUrl(log, shortHumanId, queryString, query, service, newerThanDate)
          if (youtubeUrl == null) {
            // repeat session instead
            queryString = "AWS " + shortHumanId + "R"
            youtubeUrl = getYouTubeUrl(log, shortHumanId, queryString, query, service, newerThanDate)
          }
      
          log.debug("youtube url = " + youtubeUrl)
          sessInfo.youtubeUrl = youtubeUrl
          
          infos ::= sessInfo
        }
      }
    }
    return infos
  }
  
  def getYouTubeUrl(log:Logger, shortHumanId:String, queryString:String, query:YouTubeQuery, service:YouTubeService, newerThanDate:DateTime) : String = {
    query.setFullTextQuery(queryString)
    val videoFeed = service.query(query, classOf[VideoFeed])
    log.debug("query to youtube = " + queryString)
    log.debug("short humanId = " + shortHumanId)
  
    var youtubeUrl = ""
    val vfEntriesLen = videoFeed.getEntries.size() - 1;
    for (ii <- 0 to vfEntriesLen) {
      val video = videoFeed.getEntries.get(ii)
      // TODO:  Ensure that common attributes of publisher, etc are correct
      val videoTitle = video.getTitle().getPlainText()
      log.debug("videoTitle = " + videoTitle)
      if (videoTitle.contains(shortHumanId)) {
        val videoId = video.getId().substring(video.getId().lastIndexOf(':') + 1)
        val videoDateTime = new DateTime(video.getPublished().getValue())
        if (videoDateTime.isAfter(newerThanDate)) {
          youtubeUrl = "http://www.youtube.com/watch?v=" + videoId
          log.debug("youtubeUrl = " + youtubeUrl)
          //break // TODO: how do I break??
        }
      }
    }
    return youtubeUrl
  }

  def main(args:Array[String]) = {
    val newerThanDate = new DateTime().minusDays(config.getInt("converter.daysAgo"))
    val shows = getAmazonSlides(log, config)
    log.debug("shows = " + shows)
    var allInfos = new SessionInfoList()
    val fileNames = config.getStringList("converter.files").asScala
    for (file <- fileNames) {
      val filename = "src/main/resources/" + file + ".html"
      log.debug("processing " + file + " sessions");
      val infos = getSessionInfos(log, filename, shows, config, newerThanDate)
      log.debug("infos = " + infos)
      allInfos.infos = allInfos.infos ::: infos
    }
    log.debug("allInfos = " + allInfos)

    val mf = new DefaultMustacheFactory()
    val mustache = mf.compile("src/main/resources/out.mustache")
    allInfos.infosAsJava = allInfos.infos.asJava
    mustache.execute(new PrintWriter(System.out), allInfos).flush()
  }
  
}