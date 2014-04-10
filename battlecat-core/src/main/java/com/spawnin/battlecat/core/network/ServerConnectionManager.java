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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.composable.Stream;
import reactor.net.NetChannel;
import reactor.net.Reconnect;
import reactor.net.tcp.TcpClient;
import reactor.tuple.Tuple;

import java.util.List;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class ServerConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionManager.class);

    private final TcpClient<BattlefieldMessage, BattlefieldMessage> tcpClient;

    private final Reconnect reconnectStrategy;

    private final List<ConnectionInitializer> connectionInitializers;


    public ServerConnectionManager(TcpClient<BattlefieldMessage, BattlefieldMessage> tcpClient, Reconnect reconnectStrategy, List<ConnectionInitializer> connectionInitializers) {
        this.tcpClient = tcpClient;
        this.reconnectStrategy = reconnectStrategy;
        this.connectionInitializers = connectionInitializers;
    }

    public void connect() {

        LOGGER.info("Connecting to server");

        Stream<NetChannel<BattlefieldMessage, BattlefieldMessage>> connections = tcpClient.open(reconnectStrategy);

        connections.consume(connection -> {
            LOGGER.debug("Initializing new connection {}", connection);

            for (ConnectionInitializer initializer : connectionInitializers) {

                LOGGER.debug("Initializing new connection {} with connection intializer {}", connection, initializer);
                initializer.initialize(connection);

            }

        });


    }

    public void disconnect() {
        LOGGER.info("Disonnecting from server");

        tcpClient.close();
    }
}
