package org.aspyker.tools

class SessionInfo {
  var session: String = _
  var title: String = _
  var speakers: String = _
  var abstract1: String = _
  var youtubeUrl: String = _
  var slideshareUrl: String = _
  
  override def toString(): String = "sessionInfo [session = " + session +
    ", title = " + title +
    ", speakers = " + speakers + 
    ", abstract1 = " + abstract1 +
    ", youtubeUrl = " + youtubeUrl +
    ", slideshareUrl = " + slideshareUrl +
    "]"
}