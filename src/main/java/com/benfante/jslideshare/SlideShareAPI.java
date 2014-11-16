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
import java.io.File;

/**
 * Methods of the <a href="http://www.slideshare.net/developers/documentation">SlideShare API</a>.
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public interface SlideShareAPI {

    /**
     * Retrieve a slideshow using its id.
     * 
     * @param id The slideshow id
     * @return The slideshow
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    Slideshow getSlideshow(String id) throws SlideShareException,
            SlideShareErrorException;

    /**
     * Retrieve a slideshow using its id or its URL. If both are specified (not null or empty), the id has the precedence.
     * 
     * @param id The slideshow id
     * @param url The slideshow URL
     * @return The slideshow
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    SlideshowInfo getSlideshowInfo(String id, String url) throws SlideShareException,
            SlideShareErrorException;
    
    /**
     * Retrieve slideshows for a given user.
     * 
     * @param username The username of the user.
     * @return The user data (name, slideshows, etc).
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    User getSlideshowByUser(String username) throws SlideShareException,
            SlideShareErrorException;

    /**
     * Retrieve slideshows for a given user with limits for pagination.
     * 
     * @param username The username of the user.
     * @param offset The offset from which retrieving the slideshows. Starting from 0. A negative value means no offset.
     * @param limit How many slideshows to retrieve. A negative value means no limit.
     * @return The user data (name, slideshows, etc).
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    User getSlideshowByUser(String username, int offset, int limit) throws
            SlideShareException, SlideShareErrorException;

    /**
     * Retrieve slideshows with a given tag.
     * 
     * @param tag The tag
     * @return The tag data (name, slideshows, etc).
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    Tag getSlideshowByTag(String tag) throws SlideShareException,
            SlideShareErrorException;

    /**
     * Retrieve slideshows with a given tag with limits for pagination.
     * 
     * @param tag The tag
     * @param offset The offset from which retrieving the slideshows. Starting from 0. A negative value means no offset.
     * @param limit How many slideshows to retrieve. A negative value means no limit.
     * @return The tag data (name, slideshows, etc).
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    Tag getSlideshowByTag(String tag, int offset, int limit) throws
            SlideShareException, SlideShareErrorException;

    /**
     * Retrieve slideshows from a given group.
     * 
     * @param groupName The name (id) of the group.
     * @return The group data (name, slideshows, etc).
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    Group getSlideshowByGroup(String groupName) throws SlideShareException,
            SlideShareErrorException;

    /**
     * Retrieve slideshows from a given group with limits for pagination.
     * 
     * @param groupName The name (id) of the group.
     * @param offset The offset from which retrieving the slideshows. Starting from 0. A negative value means no offset.
     * @param limit How many slideshows to retrieve. A negative value means no limit.
     * @return The group data (name, slideshows, etc).
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    Group getSlideshowByGroup(String groupName, int offset, int limit) throws
            SlideShareException, SlideShareErrorException;

    /**
     * Retrieve slideshows with a given tag.
     * 
     * @param username The username of the account that will host the upload
     * @param password The password of the account that will host the upload
     * @param title The title of the presentation
     * @param src The PPT presentation
     * @param description The description of the presentation (optional, it can be null)
     * @param tags Some tags for the presentation. Tags must be space separates. Use quotes for multiple word tags.
     * @param makeSrcPublic Should be true if you want users to be able to download the ppt file later.
     * @param makeSlideshowPrivate Should be true if you want to upload the slideshow privately
     * @param generateSecretUrl Generate a secret URL for the slideshow. Requires makeSlideshowPrivate to be true
     * @param allowEmbeds Sets if other websites should be allowed to embed the slideshow. Requires makeSlideshowPrivate to be true.
     * @param shareWithContacts Sets if your contacts on Slideshare can view the slideshow. Requires make_slideshow_private to be true.
     * @return The id of the just created Slideshow
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
    String uploadSlideshow(String username, String password, String title,
            File src, String description, String tags, boolean makeSrcPublic,
            boolean makeSlideshowPrivate, boolean generateSecretUrl,
            boolean allowEmbeds, boolean shareWithContacts)
            throws SlideShareException, SlideShareErrorException;
    
    /**
     * Retrieve a slideshow using its id.
     * 
     * @param username The username of the account that will delete the slideshow
     * @param password The password of the account that will delete the slideshow
     * @param id The slideshow id
     * @return The slideshow
     * @throws com.benfante.jslideshare.SlideShareException In case of a SlideShareServiceError
     * @throws com.benfante.jslideshare.SlideShareErrorException In case of an error using the service (IO error, timeouts, http status other than OK, etc.)
     */
// TODO: verify if this method is still available in te API
//    String deleteSlideshow(String username, String password, String id) throws SlideShareException,
//            SlideShareErrorException;
    
}
