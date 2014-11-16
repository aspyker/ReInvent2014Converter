/*
 *  Copyright 2008 The JSlideShare Team
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
package com.benfante.jslideshare;

/**
 *
 * Factory for creating a SlideShareAPI implementation.
 *
 * @author Lucio Benfante (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class SlideShareAPIFactory {

    private SlideShareAPIFactory() {
    }

    /**
     * Provide a SlideShareAPI object.
     * 
     * @param api_key Your API key
     * @param shared_secret Your Shared Secret
     * @return The SlideShareAPI object
     */
    public static SlideShareAPI getSlideShareAPI(String api_key,
            String shared_secret) {
        SlideShareConnector connector = new SlideShareConnectorImpl(api_key,
                shared_secret, 10000);
        return new SlideShareAPIImpl(connector);
    }

    /**
     * Provide a SlideShareAPI object.
     * 
     * @param api_key Your API key
     * @param shared_secret Your Shared Secret
     * @param timeout A connection timeout. 0 for no timeouts.
     * @return The SlideShareAPI object
     */
    public static SlideShareAPI getSlideShareAPI(String api_key,
            String shared_secret, int timeout) {
        SlideShareConnector connector = new SlideShareConnectorImpl(api_key,
                shared_secret, timeout);
        return new SlideShareAPIImpl(connector);
    }

    /**
     * Provide a SlideShareAPI object.
     * 
     * @param api_key Your API key
     * @param shared_secret Your Shared Secret
     * @param timeout A connection timeout. 0 for no timeouts.
     * @param proxyHost The proxy host (DNS or IP). Can be null.
     * @param proxyPort The proxy TCP port. -1 for using the default port.
     * @param proxyUsername The proxy username. Can be null, for no authentication.
     * @param proxyPassword The proxy password. Can be null, for no authentication.
     * @return The SlideShareAPI object
     */
    public static SlideShareAPI getSlideShareAPI(String api_key,
            String shared_secret, int timeout, String proxyHost, int proxyPort,
            String proxyUsername, String proxyPassword) {
        SlideShareConnector connector = new SlideShareConnectorImpl(api_key,
                shared_secret, timeout, proxyHost, proxyPort, proxyUsername, proxyPassword);
        return new SlideShareAPIImpl(connector);
    }
}
