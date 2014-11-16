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

import java.io.File;

/**
 * Only for naively trying the API on "the real".
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class App {

    private static final String API_KEY = "your api-key here";
    private static final String SHARED_SECRET = "your shared-secret here";
    private static final String USERNAME = "your username here";
    private static final String PASSWORD = "your password here";
    private static final File SRC_FILE = new File("your file path here");

    public static void main(String[] args) throws Exception {
        SlideShareAPI ssapi = SlideShareAPIFactory.getSlideShareAPI(API_KEY,
                SHARED_SECRET);
        System.out.println(ssapi.getSlideshow("142806"));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowInfo(null, "http://www.slideshare.net/benfante/using-daos-without-implementing-them"));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowByUser("john.leach"));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowByUser("john.leach", 1, 1));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowByTag("padova"));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowByTag("padova", 1, 2));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowByGroup("javaday-italy"));
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshowByGroup("javaday-italy", 1, 2));
        Thread.sleep(2000);
        String newSlideshowId = ssapi.uploadSlideshow(USERNAME, PASSWORD,
                "My new title", SRC_FILE, null, null, true, false, false, false,
                false);
        System.out.println("Added "+newSlideshowId);
        Thread.sleep(2000);
        System.out.println(ssapi.getSlideshow(newSlideshowId));
//        Thread.sleep(10000);
//        String deletedSlidehowId = ssapi.deleteSlideshow(USERNAME, PASSWORD,
//                newSlideshowId);
//        System.out.println("Deleted "+deletedSlidehowId);
//        Thread.sleep(10000);
//        System.out.println(ssapi.getSlideshow(deletedSlidehowId));
    }
}
