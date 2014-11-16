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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

/**
 * A class for managing requests to SlideShare.
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class SlideShareConnectorImpl implements SlideShareConnector {

    private static final Logger logger = Logger.getLogger(
            SlideShareConnectorImpl.class);
    protected String apiKey;
    protected String sharedSecret;
    protected int soTimeout;
    protected String proxyHost;
    protected int proxyPort = -1;
    protected String proxyUsername;
    protected String proxyPassword;

    public SlideShareConnectorImpl() {
    }

    public SlideShareConnectorImpl(String apiKey, String sharedSecret) {
        this();
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;                
    }

    public SlideShareConnectorImpl(String apiKey, String sharedSecret,
            int soTimeout) {
        this(apiKey, sharedSecret);
        this.soTimeout = soTimeout;
    }

    public SlideShareConnectorImpl(String apiKey, String sharedSecret,
            int soTimeout, String proxyHost, int proxyPort,
            String proxyUsername, String proxyPassword) {
        this(apiKey, sharedSecret, soTimeout);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public InputStream sendMessage(String url, Map<String, String> parameters)
            throws IOException, SlideShareErrorException {
        HttpClient client = createHttpClient();
        PostMethod method = new PostMethod(url);
        method.addParameter("api_key", this.apiKey);
        Iterator<Map.Entry<String, String>> entryIt =
                parameters.entrySet().iterator();
        while (entryIt.hasNext()) {
            Map.Entry<String, String> entry = entryIt.next();
            method.addParameter(entry.getKey(), entry.getValue());
        }
        Date now = new Date();
        String ts = Long.toString(now.getTime() / 1000);
        String hash = DigestUtils.shaHex(this.sharedSecret + ts).toLowerCase();
        method.addParameter("ts", ts);
        method.addParameter("hash", hash);
        logger.debug("Sending POST message to " + method.getURI().getURI() +
                " with parameters " + Arrays.toString(method.getParameters()));
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            logger.debug("Server replied with a " + statusCode +
                    " HTTP status code (" + HttpStatus.getStatusText(statusCode) +
                    ")");
            throw new SlideShareErrorException(statusCode,
                    HttpStatus.getStatusText(statusCode));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(method.getResponseBodyAsString());
        }
        InputStream result = new ByteArrayInputStream(method.getResponseBody());
        method.releaseConnection();
        return result;
    }

    public InputStream sendMultiPartMessage(String url,
            Map<String, String> parameters, Map<String, File> files)
            throws IOException, SlideShareErrorException {
        HttpClient client = createHttpClient();
        PostMethod method = new PostMethod(url);
        List<Part> partList = new ArrayList();
        partList.add(createStringPart("api_key", this.apiKey));
        Date now = new Date();
        String ts = Long.toString(now.getTime() / 1000);
        String hash = DigestUtils.shaHex(this.sharedSecret + ts).toLowerCase();
        partList.add(createStringPart("ts", ts));
        partList.add(createStringPart("hash", hash));
        Iterator<Map.Entry<String, String>> entryIt =
                parameters.entrySet().iterator();
        while (entryIt.hasNext()) {
            Map.Entry<String, String> entry = entryIt.next();
            partList.add(createStringPart(entry.getKey(), entry.getValue()));
        }
        Iterator<Map.Entry<String, File>> entryFileIt =
                files.entrySet().iterator();
        while (entryFileIt.hasNext()) {
            Map.Entry<String, File> entry = entryFileIt.next();
            partList.add(createFilePart(entry.getKey(), entry.getValue()));
        }
        MultipartRequestEntity requestEntity = new MultipartRequestEntity(
                partList.toArray(new Part[partList.size()]),
                method.getParams());
        method.setRequestEntity(requestEntity);
        logger.debug("Sending multipart POST message to " +
                method.getURI().getURI() +
                " with parts " + partList);
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            logger.debug("Server replied with a " + statusCode +
                    " HTTP status code (" + HttpStatus.getStatusText(statusCode) +
                    ")");
            throw new SlideShareErrorException(statusCode,
                    HttpStatus.getStatusText(statusCode));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(method.getResponseBodyAsString());
        }
        InputStream result = new ByteArrayInputStream(method.getResponseBody());
        method.releaseConnection();
        return result;
    }

    public InputStream sendGetMessage(String url, Map<String, String> parameters)
            throws IOException, SlideShareErrorException {
        HttpClient client = createHttpClient();
        GetMethod method = new GetMethod(url);
        NameValuePair[] params = new NameValuePair[parameters.size() + 3];
        int i = 0;
        params[i++] = new NameValuePair("api_key", this.apiKey);
        Iterator<Map.Entry<String, String>> entryIt =
                parameters.entrySet().iterator();
        while (entryIt.hasNext()) {
            Map.Entry<String, String> entry = entryIt.next();
            params[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }
        Date now = new Date();
        String ts = Long.toString(now.getTime() / 1000);
        String hash = DigestUtils.shaHex(this.sharedSecret + ts).toLowerCase();
        params[i++] = new NameValuePair("ts", ts);
        params[i++] = new NameValuePair("hash", hash);
        method.setQueryString(params);
        logger.debug("Sending GET message to " + method.getURI().getURI() +
                " with parameters " + Arrays.toString(params));
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            logger.debug("Server replied with a " + statusCode +
                    " HTTP status code (" + HttpStatus.getStatusText(statusCode) +
                    ")");
            throw new SlideShareErrorException(statusCode,
                    HttpStatus.getStatusText(statusCode));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(method.getResponseBodyAsString());
        }
        InputStream result = new ByteArrayInputStream(method.getResponseBody());
        method.releaseConnection();
        return result;
    }

    private StringPart createStringPart(String name, String value) {
        StringPart stringPart = new StringPart(name, value);
        stringPart.setContentType(null);
        stringPart.setTransferEncoding(null);
        stringPart.setCharSet("UTF-8");
        return stringPart;
    }

    private FilePart createFilePart(String name, File value) throws
            FileNotFoundException {
        FilePart filePart = new FilePart(name, value);
        filePart.setTransferEncoding(null);
        filePart.setCharSet(null);
        return filePart;
    }

    private HttpClient createHttpClient() {
        HttpClient client = new HttpClient();
        client.getParams().setSoTimeout(this.soTimeout);
        if (this.getProxyHost() != null) {
            client.getHostConfiguration().setProxy(this.proxyHost,
                    this.proxyPort);
            if (this.proxyUsername != null && this.proxyPassword != null) {
                client.getState().setProxyCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(this.proxyUsername,
                        this.proxyPassword));
            }
        }
        return client;
    }
}
