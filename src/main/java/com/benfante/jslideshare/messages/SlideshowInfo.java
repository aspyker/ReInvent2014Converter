/*
 *  Copyright 2008 The JSlideShare Team.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.benfante.jslideshare.messages;

import com.benfante.jslideshare.utils.Utils;

/**
 * The slideshow document with more infos.
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class SlideshowInfo {

    private String id;
    private String title;
    private String description;
    private int status;
    private String embedCode;
    private String playerDoc;
    private int totalSlides;
    private String urlDoc;
    private String language;
    private String url;
    private String userId;
    private String userLogin;
    private String relatedSlideshows;
    private String thumbnailUrl;
    private String thumbnailSmallUrl;
    private String tags;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlayerDoc() {
        return playerDoc;
    }

    public void setPlayerDoc(String playerDoc) {
        this.playerDoc = playerDoc;
    }

    public String getRelatedSlideshows() {
        return relatedSlideshows;
    }

    public void setRelatedSlideshows(String relatedSlideshows) {
        this.relatedSlideshows = relatedSlideshows;
    }

    public String[] getRelatedSlideshowsArray() {
        return Utils.splitCsvLine(this.relatedSlideshows);
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public String[] getTagArray() {
        return Utils.splitCsvLine(this.tags);
    }

    public String getThumbnailSmallUrl() {
        return thumbnailSmallUrl;
    }

    public void setThumbnailSmallUrl(String thumbnailSmallUrl) {
        this.thumbnailSmallUrl = thumbnailSmallUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalSlides() {
        return totalSlides;
    }

    public void setTotalSlides(int totalSlides) {
        this.totalSlides = totalSlides;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlDoc() {
        return urlDoc;
    }

    public void setUrlDoc(String urlDoc) {
        this.urlDoc = urlDoc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "id=" + this.id + ";status=" + this.status + ";title=" +
                this.title + ";description=" + this.description + ";tags=" +
                this.tags + ";embedCode=" + this.embedCode + ";playerDoc=" +
                this.playerDoc + ";totalSlides=" + this.totalSlides + ";urlDoc=" +
                this.urlDoc + ";language=" + this.language + ";url=" + this.url +
                ";userId=" + this.userId + ";userLogin=" + this.userLogin +
                ";relatedSlideshows=" + this.relatedSlideshows +
                ";thumbnailUrl=" + this.thumbnailUrl + ";thumbnailSmallUrl=" +
                this.thumbnailSmallUrl;
    }
}