package org.aspyker.tools

import com.google.api.services.youtube.model.{SearchResult, SearchListResponse}
import org.apache.log4j.Logger
import org.joda.time.DateTime
import com.typesafe.config._
import com.benfante.jslideshare.messages.{Slideshow, User}
import scala.xml._
import scala.collection.JavaConverters._
import com.github.mustachejava._
import java.io.PrintWriter
import com.google.api.services.youtube.YouTube
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.benfante.jslideshare.SlideShareAPIFactory
import com.benfante.jslideshare.messages.Slideshow

// TODO: I'm much better at Scala than I was a year ago.
// TODO: Need to update the mess of first time Scale code below

object ReInvent2014Converter {
  val log = Logger.getLogger(this.getClass)
  val config = ConfigFactory.load()

  def getAmazonSlides(log:Logger, config:Config) : List[Slideshow] = {
    val ssapi = SlideShareAPIFactory.getSlideShareAPI(
      config.getString("slideshare.apikey"),
      config.getString("slideshare.sharedsecret"),
      0
    )
  
    var shows = List[Slideshow]()
    var page = 0
    var break = false
    var pageSize = 250
    // TODO: How to avoid break here?
    while (!break) {
      var start = page * pageSize
      val awsUser = ssapi.getSlideshowByUser("AmazonWebServices", start, pageSize)
      val returned = awsUser.getSlideshows().size()
      log.debug(s"another $returned slides")
      shows = shows ::: (awsUser.getSlideshows().asScala.toList)
      if (returned < pageSize) {
        break = true
      }
      page = page + 1
    }
    log.debug("AmazonWebServices number of slide shares = " + shows.length)
    return shows
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
  
  def replaceEntities(raw:String) : String = {
    var fixed = raw.replaceAll("\\p{Pd}", "-");
    fixed = fixed.replaceAll("\u2019", "'");
    //fixed.map(c => "%s\t\\u%04X".format(c, c.toInt)).foreach(println)
    return fixed
  }
  
  def getSessionInfos(log:Logger, filename:String, slideShares:List[Slideshow], config:Config, newerThanDate:DateTime) : List[SessionInfo] = {
    var infos = List[SessionInfo]()
    
    val xml = XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
    val root = XML.load(filename)

    val youtube: YouTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), null)
      .setApplicationName(config.getString("converter.youtube.client.id"))
      .build()

    val searchReqList: YouTube#Search#List = youtube.search().list("id, snippet")
    searchReqList.setOrder("relevance")
    searchReqList.setSafeSearch("none")
    searchReqList.setKey(config.getString("youtube.apiKey"))

    val divs = root \\ "div"
    for (div <- divs) {
      val id = (div \ "@id").text
      if (id.startsWith("session_")) {
        val pureId = id.substring(8)
        log.debug("processing session " + pureId)
        val sessInfo = new SessionInfo()
        
        val spans = div \\ "span"
        val smalls = div \\ "small"
        val shortHumanId = getDivField("abbreviation", spans).split(" +")(0)
        if (!shortHumanId.contains("-R")) {
          sessInfo.session = shortHumanId
          sessInfo.title = replaceEntities(getDivField("title", spans))
          sessInfo.abstract1 = replaceEntities(getDivField("abstract truncatedTxt", spans))

          var queryString = "AWS " + shortHumanId
          var youtubeUrl = getYouTubeUrl(log, shortHumanId, queryString, searchReqList, youtube, newerThanDate)
          if (youtubeUrl == null) {
            // repeat session instead
            queryString = "AWS " + shortHumanId + "R"
            youtubeUrl = getYouTubeUrl(log, shortHumanId, queryString, searchReqList, youtube, newerThanDate)
          }
          log.debug("youtube url = " + youtubeUrl)
          sessInfo.youtubeUrl = youtubeUrl
          
          val slideshareUrl = getSlideshareUrl(log, slideShares, newerThanDate, shortHumanId)
          log.debug("slideshare url = " + slideshareUrl)
          sessInfo.slideshareUrl = slideshareUrl
          
          infos ::= sessInfo
        }
      }
    }
    return infos
  }
  
  def getSlideshareUrl(log:Logger, slideShares:List[Slideshow], newerThanDate:DateTime, shortHumanId:String) : String = {
    var slide = slideShares.find { slideShare =>
      (slideShare.getTitle().contains(shortHumanId)) &&
      (slideShare.getCreatedDate().isAfter(newerThanDate)) }
    if (!slide.isDefined) {
      return ""
    }
    return slide.get.getPermalink()
  }
  
  def getYouTubeUrl(log:Logger, shortHumanId:String, queryString:String, searchReq:YouTube#Search#List, youtube:YouTube, newerThanDate:DateTime) : String = {
    searchReq.setQ(queryString)
    log.debug("query to youtube = " + queryString)
    log.debug("short humanId = " + shortHumanId)
    val resp: SearchListResponse = searchReq.execute()

    var youtubeUrl = ""

    for (video: SearchResult <- resp.getItems.asScala) {
      // TODO:  Ensure that common attributes of publisher, etc are correct
      val videoTitle = video.getSnippet.getTitle
      log.debug("videoTitle = " + videoTitle)
      if (videoTitle.contains(shortHumanId)) {
        val videoId = video.getId.getVideoId.substring(video.getId.getVideoId.lastIndexOf(':') + 1)
        val videoDateTime = new DateTime(video.getSnippet.getPublishedAt.getValue)
        if (videoDateTime.isAfter(newerThanDate)) {
          youtubeUrl = "http://www.youtube.com/watch?v=" + videoId
          log.debug("youtubeUrl = " + youtubeUrl)
          //break // TODO: how do I break??
        }
      }
    }
    return youtubeUrl
  }

  def main(args:Array[String]): Unit = {
    val newerThanDate = new DateTime().minusDays(config.getInt("converter.daysAgo"))
    val slides = getAmazonSlides(log, config)
    var allInfos = new SessionInfoList()
    val fileNames = config.getStringList("converter.files").asScala
    for (file <- fileNames) {
      val filename = "src/main/resources/" + file + ".html"
      log.debug("processing " + file + " sessions")
      val infos = getSessionInfos(log, filename, slides, config, newerThanDate)
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