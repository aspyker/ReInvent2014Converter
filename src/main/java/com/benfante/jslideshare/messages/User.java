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
package com.benfante.jslideshare.messages;

import java.util.LinkedList;
import java.util.List;

/**
 * The user document.
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class User {
    private String name;
    private int count;
    private List<Slideshow> slideshows = new LinkedList<Slideshow>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Slideshow> getSlideshows() {
        return slideshows;
    }

    public void setSlideshows(List<Slideshow> slideshows) {
        this.slideshows = slideshows;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(this.getName())
                .append(";count=").append(this.count);
        sb.append("\n\t;slideshares = {");
        for (Slideshow slideshow : slideshows) {
            sb.append("\n\t\t").append(slideshow);
        }
        sb.append("\n\t }");
        return sb.toString();
    }
}
