package org.aspyker.tools

import scala.collection.JavaConverters._

class SessionInfoList {
  var infos = List[SessionInfo]()
  var infosAsJava = List[SessionInfo]().asJava
  
  override def toString(): String = "sessionInfoList [infos = " + infos + "]"
}