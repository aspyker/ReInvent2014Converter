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
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A parser for SlideShare XML documents
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class DocumentParser extends DefaultHandler {

    public static final String COUNT_TAG = "count";
    public static final String DESCRIPTION_TAG = "Description";
    public static final String EMBED_CODE_TAG = "EmbedCode";
    public static final String GROUP_TAG = "Group";
    public static final String ID_ATTRIBUTE = "id";
    public static final String ID_TAG = "ID";
    public static final String LANGUAGE_TAG = "Language";
    public static final String MESSAGE_TAG = "Message";
    public static final String NAME_TAG = "name";
    public static final String PERMALINK_TAG = "Permalink";
    public static final String PLAYER_DOC_TAG = "PlayerDoc";
    public static final String RELATED_SLIDESHOWS_TAG = "RelatedSlideshows";
    public static final String SLIDESHOWS_TAG = "Slideshows";
    public static final String SLIDESHOW_ID_TAG = "SlideShowID";
    public static final String SLIDESHOW_TAG = "Slideshow";
    public static final String SLIDESHOW_UPLOADED_TAG = "SlideShowUploaded";
    public static final String SLIDESHOW_DELETED_TAG = "SlideShowDeleted";
    public static final String STATUS_DESCRIPTION_TAG = "StatusDescription";
    public static final String STATUS_TAG = "Status";
    public static final String TAG_TAG = "Tag";
    public static final String TAGS_TAG = "Tags";
    public static final String THUMBNAIL_TAG = "Thumbnail";
    public static final String THUMBNAIL_URL_TAG = "ThumbnailURL";
    public static final String THUMBNAIL_SMALL_URL_TAG = "ThumbnailSmallURL";
    public static final String TITLE_TAG = "Title";
    public static final String TOTAL_SLIDES_TAG = "TotalSlides";
    public static final String USER_TAG = "User";
    public static final String USER_ID_TAG = "UserID";
    public static final String USER_LOGIN_TAG = "UserLogin";
    public static final String URL_TAG = "URL";
    public static final String URL_DOC_TAG = "URLDoc";
    public static final String VIEWS_TAG = "Views";
    public static final String CREATED_TAG = "Created";
    private StringBuilder tempVal = new StringBuilder();
    private Slideshow tempSlideshow;
    private List<Slideshow> tempSlideshows;
    private User tempUser;
    private Tag tempTag;
    private Group tempGroup;
    private String tempId;
    private String tempName;
    private int tempCount;
    private String tempSlideShowId;
    private DocumentParserResult parserResult;
    private SlideshowInfo tempSlideshowInfo;
    private DateTimeFormatter format;

    private DocumentParser() {
    	format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy");
    }

    public static DocumentParserResult parse(InputStream is) throws IOException {
        DocumentParser documentParser = new DocumentParser();
        documentParser.parseDocument(is);
        is.close();
        return documentParser.parserResult;
    }

    private void parseDocument(InputStream is) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse(is, this);
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        tempVal.setLength(0);
        if (qName.equalsIgnoreCase(SLIDESHOW_TAG)) {
            tempSlideshow = new Slideshow();
            tempSlideshowInfo = new SlideshowInfo();
        } else if (qName.equalsIgnoreCase(SLIDESHOWS_TAG)) {
            tempSlideshows = new LinkedList<Slideshow>();
        } else if (qName.equalsIgnoreCase(USER_TAG)) {
            tempUser = new User();
            tempSlideshows = new LinkedList<Slideshow>();
        } else if (qName.equalsIgnoreCase(TAG_TAG)) {
            tempTag = new Tag();
            tempSlideshows = new LinkedList<Slideshow>();
        } else if (qName.equalsIgnoreCase(GROUP_TAG)) {
            tempGroup = new Group();
            tempSlideshows = new LinkedList<Slideshow>();
        } else if (qName.equalsIgnoreCase(MESSAGE_TAG)) {
            tempId = attributes.getValue(ID_ATTRIBUTE);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName,
            String qName) throws SAXException {
        if (qName.equalsIgnoreCase(SLIDESHOW_TAG)) {
            if (tempSlideshowInfo.getUrl() != null) { // I parsed a SlideshowInfo
                createResult();
                this.parserResult.setSlideShowInfo(tempSlideshowInfo);
            } else {
                tempSlideshows.add(tempSlideshow);
            }
        } else if (qName.equalsIgnoreCase(SLIDESHOWS_TAG)) {
            createResult();
            if (!tempSlideshows.isEmpty()) {
                this.parserResult.setSlideShows(tempSlideshows);
            }
        } else if (qName.equalsIgnoreCase(USER_TAG)) {
            tempUser.setName(tempName);
            tempUser.setCount(tempCount);
            tempUser.setSlideshows(tempSlideshows);
            createResult();
            this.parserResult.setUser(tempUser);
        } else if (qName.equalsIgnoreCase(TAG_TAG)) {
            tempTag.setName(tempName);
            tempTag.setCount(tempCount);
            tempTag.setSlideshows(tempSlideshows);
            createResult();
            this.parserResult.setTag(tempTag);
        } else if (qName.equalsIgnoreCase(GROUP_TAG)) {
            tempGroup.setName(tempName);
            tempGroup.setCount(tempCount);
            tempGroup.setSlideshows(tempSlideshows);
            createResult();
            this.parserResult.setGroup(tempGroup);
        } else if (qName.equalsIgnoreCase(SLIDESHOW_UPLOADED_TAG)) {
            createResult();
            this.parserResult.setSlideShowId(tempSlideShowId);
        } else if (qName.equalsIgnoreCase(SLIDESHOW_DELETED_TAG)) {
            createResult();
            this.parserResult.setSlideShowId(tempSlideShowId);
        } else if (qName.equalsIgnoreCase(MESSAGE_TAG)) {
            throw new SlideShareException(secureParseInt(tempId, -1),
                    tempVal.toString());
        } else if (qName.equalsIgnoreCase(STATUS_TAG)) {
            tempSlideshow.setStatus(secureParseInt(tempVal.toString(), -1));
            tempSlideshowInfo.setStatus(secureParseInt(tempVal.toString(), -1));
        } else if (qName.equalsIgnoreCase(STATUS_DESCRIPTION_TAG)) {
            tempSlideshow.setStatusDescription(tempVal.toString());
        } else if (qName.equalsIgnoreCase(TITLE_TAG)) {
            tempSlideshow.setTitle(tempVal.toString());
            tempSlideshowInfo.setTitle(tempVal.toString());
        } else if (qName.equalsIgnoreCase(DESCRIPTION_TAG)) {
            tempSlideshow.setDescription(tempVal.toString());
            tempSlideshowInfo.setDescription(tempVal.toString());
        } else if (qName.equalsIgnoreCase(TAGS_TAG)) {
            tempSlideshow.setTags(tempVal.toString());
            tempSlideshowInfo.setTags(tempVal.toString());
        } else if (qName.equalsIgnoreCase(EMBED_CODE_TAG)) {
            tempSlideshow.setEmbedCode(tempVal.toString());
            tempSlideshowInfo.setEmbedCode(tempVal.toString());
        } else if (qName.equalsIgnoreCase(THUMBNAIL_TAG)) {
            tempSlideshow.setThumbnail(tempVal.toString());
        } else if (qName.equalsIgnoreCase(PERMALINK_TAG)) {
            tempSlideshow.setPermalink(tempVal.toString());
        } else if (qName.equalsIgnoreCase(VIEWS_TAG)) {
            tempSlideshow.setViews(secureParseInt(tempVal.toString(), 0));
        } else if (qName.equalsIgnoreCase(CREATED_TAG)) {
        	DateTime date = format.parseDateTime(tempVal.toString());
        	tempSlideshow.setCreatedDate(date);
        } else if (qName.equalsIgnoreCase(NAME_TAG)) {
            tempName = tempVal.toString();
        } else if (qName.equalsIgnoreCase(COUNT_TAG)) {
            tempCount = secureParseInt(tempVal.toString(), -1);
        } else if (qName.equalsIgnoreCase(SLIDESHOW_ID_TAG)) {
            tempSlideShowId = tempVal.toString();
        } else if (qName.equals(ID_TAG)) {
            tempSlideshowInfo.setId(tempVal.toString());
        } else if (qName.equals(PLAYER_DOC_TAG)) {
            tempSlideshowInfo.setPlayerDoc(tempVal.toString());
        } else if (qName.equalsIgnoreCase(TOTAL_SLIDES_TAG)) {
            tempSlideshowInfo.setTotalSlides(secureParseInt(tempVal.toString(),
                    0));
        } else if (qName.equals(URL_DOC_TAG)) {
            tempSlideshowInfo.setUrlDoc(tempVal.toString());
        } else if (qName.equals(LANGUAGE_TAG)) {
            tempSlideshowInfo.setLanguage(tempVal.toString());
        } else if (qName.equals(URL_TAG)) {
            tempSlideshowInfo.setUrl(tempVal.toString());
        } else if (qName.equals(USER_ID_TAG)) {
            tempSlideshowInfo.setUserId(tempVal.toString());
        } else if (qName.equals(USER_LOGIN_TAG)) {
            tempSlideshowInfo.setUserLogin(tempVal.toString());
        } else if (qName.equals(RELATED_SLIDESHOWS_TAG)) {
            tempSlideshowInfo.setRelatedSlideshows(tempVal.toString());
        } else if (qName.equals(THUMBNAIL_URL_TAG)) {
            tempSlideshowInfo.setThumbnailUrl(tempVal.toString());
        } else if (qName.equals(THUMBNAIL_SMALL_URL_TAG)) {
            tempSlideshowInfo.setThumbnailSmallUrl(tempVal.toString());
        }
    }

    private int secureParseInt(String value, int def) {
        int result = def;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException numberFormatException) {
            // using the default...
        }
        return result;
    }

    private void createResult() {
        if (this.parserResult == null) {
            this.parserResult = new DocumentParserResult();
        }
    }
}
