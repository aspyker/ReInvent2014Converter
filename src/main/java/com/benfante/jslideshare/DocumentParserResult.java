// Copyright 2008 The JSlideShare Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.benfante.jslideshare;

import com.benfante.jslideshare.messages.Group;
import com.benfante.jslideshare.messages.Slideshow;
import com.benfante.jslideshare.messages.SlideshowInfo;
import com.benfante.jslideshare.messages.Tag;
import com.benfante.jslideshare.messages.User;
import java.util.LinkedList;
import java.util.List;

/**
 * The result(s) after the parsing of a SlideShare document
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class DocumentParserResult {

    private List<Slideshow> slideShows = new LinkedList<Slideshow>();
    private SlideshowInfo slideShowInfo = new SlideshowInfo();
    private User user;
    private Tag tag;
    private Group group;
    private String slideShowId;

    public List<Slideshow> getSlideShows() {
        return slideShows;
    }

    public void setSlideShows(List<Slideshow> slideShows) {
        this.slideShows = slideShows;
    }

    public SlideshowInfo getSlideShowInfo() {
        return slideShowInfo;
    }

    public void setSlideShowInfo(SlideshowInfo slideShowInfo) {
        this.slideShowInfo = slideShowInfo;
    }
        
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getSlideShowId() {
        return slideShowId;
    }

    public void setSlideShowId(String slideShowId) {
        this.slideShowId = slideShowId;
    }

    public Slideshow getSlideShow() {
        Slideshow result = null;
        if (slideShows != null && !slideShows.isEmpty()) {
            result = slideShows.get(0);
        }
        return result;
    }
        
}
