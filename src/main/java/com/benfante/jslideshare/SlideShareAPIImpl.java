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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Base implementation of the <a href="http://www.slideshare.net/developers/documentation">SlideShare API</a>.
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class SlideShareAPIImpl implements SlideShareAPI {

    private static final Logger logger = Logger.getLogger(
            SlideShareAPIImpl.class);
    public static final String URL_GET_SLIDESHOW =
            "http://www.slideshare.net/api/1/get_slideshow";
    public static final String URL_GET_SLIDESHOW_INFO =
            "http://www.slideshare.net/api/1/get_slideshow_info";
    public static final String URL_GET_SLIDESHOW_BY_USER =
            "https://www.slideshare.net/api/1/get_slideshow_by_user";
    public static final String URL_GET_SLIDESHOW_BY_TAG =
            "http://www.slideshare.net/api/1/get_slideshow_by_tag";
    public static final String URL_GET_SLIDESHOW_BY_GROUP =
            "http://www.slideshare.net/api/1/get_slideshow_from_group";
    public static final String URL_UPLOAD_SLIDESHOW =
            "http://www.slideshare.net/api/1/upload_slideshow";
    public static final String URL_DELETE_SLIDESHOW =
            "http://www.slideshare.net/api/1/delete_slideshow";
    protected SlideShareConnector connector;

    public SlideShareAPIImpl() {
    }

    public SlideShareAPIImpl(SlideShareConnector connector) {
        this.connector = connector;
    }

    public SlideShareConnector getConnector() {
        return connector;
    }

    public void setConnector(SlideShareConnector connector) {
        this.connector = connector;
    }

    public Slideshow getSlideshow(String id) throws SlideShareException,
            SlideShareErrorException {
        logger.info("Called getSlideshow with id=" + id);
        Map<String, String> parameters = new HashMap<String, String>();
        addParameter(parameters, "slideshow_id", id);
        return sendMessage(URL_GET_SLIDESHOW, parameters).getSlideShow();
    }

    public SlideshowInfo getSlideshowInfo(String id, String url) throws 
            SlideShareException,
            SlideShareErrorException {
        logger.info("Called getSlideshowInfo with id=" + id + ", url=" + url);
        Map<String, String> parameters = new HashMap<String, String>();
        addParameter(parameters, "slideshow_id", id);
        addParameter(parameters, "slideshow_url", url);
        return sendGetMessage(URL_GET_SLIDESHOW_INFO, parameters).getSlideShowInfo();
    }

    public User getSlideshowByUser(String username) throws SlideShareException,
            SlideShareErrorException {
        logger.info("Called getSlideshowByUser with username=" + username);
        return getSlideshowByUser(username, -1, -1);
    }

    public User getSlideshowByUser(String username, int offset, int limit)
            throws SlideShareException, SlideShareErrorException {
        logger.info("Called getSlideshowByUser with username=" + username +
                ", offset=" + offset + ", limit=" + limit);
        Map<String, String> parameters = new HashMap<String, String>();
        addParameter(parameters, "username_for", username);
        addLimits(parameters, offset, limit);
        return sendMessage(URL_GET_SLIDESHOW_BY_USER, parameters).getUser();
    }

    public Tag getSlideshowByTag(String tag) throws SlideShareException,
            SlideShareErrorException {
        logger.info("Called getSlideshowByTag with tag=" + tag);
        return getSlideshowByTag(tag, -1, -1);
    }

    public Tag getSlideshowByTag(String tag, int offset, int limit) throws
            SlideShareException, SlideShareErrorException {
        logger.info("Called getSlideshowByTag with tag=" + tag +
                ", offset=" + offset + ", limit=" + limit);
        Map<String, String> parameters = new HashMap<String, String>();
        addParameter(parameters, "tag", tag);
        addLimits(parameters, offset, limit);
        return sendMessage(URL_GET_SLIDESHOW_BY_TAG, parameters).getTag();
    }

    public Group getSlideshowByGroup(String groupName) throws
            SlideShareException,
            SlideShareErrorException {
        logger.info("Called getSlideshowByGroup with groupName=" + groupName);
        return getSlideshowByGroup(groupName, -1, -1);
    }

    public Group getSlideshowByGroup(String groupName, int offset, int limit)
            throws SlideShareException, SlideShareErrorException {
        logger.info("Called getSlideshowByGrop with groupName=" + groupName +
                ", offset=" + offset + ", limit=" + limit);
        Map<String, String> parameters = new HashMap<String, String>();
        addParameter(parameters, "group_name", groupName);
        addLimits(parameters, offset, limit);
        return sendMessage(URL_GET_SLIDESHOW_BY_GROUP, parameters).getGroup();
    }

    public String uploadSlideshow(String username, String password, String title,
            File src, String description, String tags, boolean makeSrcPublic,
            boolean makeSlideshowPrivate, boolean generateSecretUrl,
            boolean allowEmbeds, boolean shareWithContacts) throws
            SlideShareException,
            SlideShareErrorException {
        logger.info("Called uploadSlideshow with username=" + username +
                ", password=XXX, title=" + title + ", description=" +
                description + ", tags=" + tags + ", makeSrcPublic=" +
                makeSrcPublic + ", makeSlideshowPrivate=" + makeSlideshowPrivate +
                ", generateSecretUrl=" + generateSecretUrl + ", allowEmbeds=" +
                allowEmbeds + ", shareWithContacts=" + shareWithContacts);
        Map<String, String> parameters = new HashMap<String, String>();
        addParameter(parameters, "username", username);
        addParameter(parameters, "password", password);
        addParameter(parameters, "slideshow_title", title);
        addParameter(parameters, "slideshow_description", description);
        addParameter(parameters, "slideshow_tags", tags);
        addParameter(parameters, "make_src_public", makeSrcPublic);
        addParameter(parameters, "make_slideshow_private", makeSlideshowPrivate);
        addParameter(parameters, "generate_secret_url", generateSecretUrl);
        addParameter(parameters, "allow_embeds", allowEmbeds);
        addParameter(parameters, "share_with_contacts", shareWithContacts);
        Map<String, File> files = new HashMap<String, File>();
        files.put("slideshow_srcfile", src);
        return sendMessage(URL_UPLOAD_SLIDESHOW, parameters, files).getSlideShowId();
    }

// TODO: verify if this method is still available in te API
//    public String deleteSlideshow(String username, String password, String id)
//            throws SlideShareException, SlideShareErrorException {
//        logger.info("Called deleteSlideshow with username=" + username +
//                ", password=XXX, id=" + id);
//        Map<String, String> parameters = new HashMap<String, String>();
//        addParameter(parameters, "username", username);
//        addParameter(parameters, "password", password);
//        addParameter(parameters, "slideshow_id", id);
//        return sendGetMessage(URL_DELETE_SLIDESHOW, parameters).getSlideShowId();
//    }
    private Map<String, String> addParameter(Map<String, String> parameters,
            String name, String value) {
        if (value != null) {
            parameters.put(name, value);
        }
        return parameters;
    }

    private Map<String, String> addParameter(Map<String, String> parameters,
            String name, boolean value) {
        parameters.put(name, value ? "Y" : "N");
        return parameters;
    }

    private Map<String, String> addLimits(Map<String, String> parameters,
            int offset, int limit) {
        if (offset >= 0) {
            parameters.put("offset", Integer.toString(offset));
        }
        if (limit >= 0) {
            parameters.put("limit", Integer.toString(limit));
        }
        return parameters;
    }

    private DocumentParserResult sendMessage(String url,
            Map<String, String> parameters) throws SlideShareErrorException {
        DocumentParserResult result;
        try {
            InputStream response = connector.sendMessage(url, parameters);
            result = DocumentParser.parse(response);
        } catch (IOException iOException) {
            throw new SlideShareErrorException(-1,
                    "Error sending a message to the url " + url, iOException);
        }
        return result;
    }

    private DocumentParserResult sendGetMessage(String url,
            Map<String, String> parameters) throws SlideShareErrorException {
        DocumentParserResult result;
        try {
            InputStream response = connector.sendGetMessage(url, parameters);
            result = DocumentParser.parse(response);
        } catch (IOException iOException) {
            throw new SlideShareErrorException(-1,
                    "Error sending a message to the url " + url, iOException);
        }
        return result;
    }

    private DocumentParserResult sendMessage(String url,
            Map<String, String> parameters, Map<String, File> files) throws
            SlideShareErrorException {
        DocumentParserResult result;
        try {
            InputStream response = connector.sendMultiPartMessage(url,
                    parameters, files);
            result = DocumentParser.parse(response);
        } catch (IOException iOException) {
            throw new SlideShareErrorException(-1,
                    "Error sending a multipart message to the url " + url,
                    iOException);
        }
        return result;
    }
}
