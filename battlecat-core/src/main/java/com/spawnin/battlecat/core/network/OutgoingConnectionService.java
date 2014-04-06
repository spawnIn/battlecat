/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spawnin.battlecat.core.network;

import com.spawnin.battlecat.translator.BattlefieldMessage;

/**
 * Sends messages to the connected server.
 * <p>
 * It is preferred to use this interface to send messages instead of using the actual tcpConnection or
 * the outgoingMessageReactor directly.
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public interface OutgoingConnectionService {

    /**
     * sends a message to the connected server
     *
     * @param message a message
     */
    void send(BattlefieldMessage message);

    /**
     * sends a message to the connected server and registers the onResponse to be executed when a response from
     * the server is received
     *
     * @param message
     * @param onResponse
     */
    void send(BattlefieldMessage message, OnResponse onResponse);

}
