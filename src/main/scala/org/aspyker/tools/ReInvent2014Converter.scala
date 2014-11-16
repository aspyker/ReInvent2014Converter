package org.aspyker.tools

import org.apache.log4j.Logger
import org.joda.time.DateTime
import com.typesafe.config._
import com.benfante.jslideshare.messages.{Slideshow, User}
import scala.xml._
import scala.collection.JavaConverters._
import com.github.mustachejava._
import java.io.PrintWriter

object ReInvent2014Converter {
  val log = Logger.getLogger(this.getClass)
  val config = ConfigFactory.load();

  def getAmazonSlides(log:Logger, config:Config) : Array[Slideshow] = {
    return new Array[Slideshow](0)
  }
  
  def getSessionInfos(log:Logger, filename:String, slideShares:Array[Slideshow], config:Config, newerThanDate:DateTime) : List[SessionInfo] = {
    var infos = List[SessionInfo]()
    
    val xml = XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
    val root = XML.load(filename)
    
    val divs = root \\ "div"
    for (div <- divs) {
      val id = (div \ "@id").text
      if (id.startsWith("session_")) {
        log.debug("processing session " + id)
        val sessInfo = new SessionInfo();
        
        val spans = div \\ "span"
        for (span <- spans) {
          val clazz = (span \ "@class").text
          if (clazz == "abbreviation") {
            val sessionName = span.text.split(" +")(0);
            sessInfo.session = sessionName;
            log.debug("found abbreviation " + sessionName);
          }
        }
        for (span <- spans) {
          val clazz = (span \ "@class").text
          if (clazz == "title") {
            sessInfo.title = span.text;
            log.debug("found title " + span.text);
          }
        }
        for (span <- spans) {
          val clazz = (span \ "@class").text
          if (clazz == "abstract") {
            sessInfo.abstract1 = span.text;
            log.debug("found abstract " + span.text);
          }
        }
        for (span <- spans) {
          val clazz = (span \ "@class").text
          if (clazz == "speakers") {
            sessInfo.speakers = span.text;
            log.debug("found speakers " + span.text);
          }
        }
        infos ::= sessInfo
      }
    }
    return infos
  }

  def main(args:Array[String]) = {
    val newerThanDate = new DateTime().minusDays(config.getInt("converter.daysAgo"))
    val shows = getAmazonSlides(log, config)
    log.debug("shows = " + shows)
    var allInfos = new SessionInfoList()
    val fileNames = config.getStringList("converter.files").asScala
    for (file <- fileNames) {
      val filename = "src/main/resources/" + file + ".html"
      val infos = getSessionInfos(log, filename, shows, config, newerThanDate)
      log.debug("infos = " + infos)
      allInfos.infos = allInfos.infos ::: infos
    }
    log.info("allInfos = " + allInfos)

    val mf = new DefaultMustacheFactory()
    val mustache = mf.compile("src/main/resources/out.mustache")
    allInfos.infosAsJava = allInfos.infos.asJava
    mustache.execute(new PrintWriter(System.out), allInfos).flush()
  }
  
}