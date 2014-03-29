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

import com.spawnin.battlecat.core.selectors.ByTypeSelector;
import com.spawnin.battlecat.translator.BattlefieldMessage;
import com.spawnin.battlecat.translator.BattlefieldMessageBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Reactor;
import reactor.core.composable.Stream;
import reactor.event.Event;
import reactor.tcp.Reconnect;
import reactor.tcp.TcpClient;
import reactor.tcp.TcpConnection;
import reactor.tuple.Tuple;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class ServerConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionManager.class);

    private final TcpClient<BattlefieldMessage, BattlefieldMessage> tcpClient;

    private final Reconnect reconnectStrategy;

    @Autowired
    private BattlefieldMessageBuilderFactory factory;

    private final Reactor outReactor;
    private final Reactor inReactor;

    public ServerConnectionManager(TcpClient<BattlefieldMessage, BattlefieldMessage> tcpClient, Reconnect reconnectStrategy, Reactor outReactor, Reactor inReactor) {
        this.tcpClient = tcpClient;
        this.reconnectStrategy = reconnectStrategy;
        this.outReactor = outReactor;
        this.inReactor = inReactor;
    }

    public void connect() {

        Stream<TcpConnection<BattlefieldMessage, BattlefieldMessage>> connections = tcpClient.open((address, attempt) -> {
            return Tuple.of(address, 8080L);
        });

        connections.consume(connection -> {
            connection.in().consume(message -> {
                LOGGER.debug("Passing incoming message {}", message);
                inReactor.notify("incoming", Event.wrap(message));
            });

            outReactor.getConsumerRegistry().unregister(new Object());

            outReactor.on(new ByTypeSelector(Object.class), (Event<BattlefieldMessage> event) -> {
                LOGGER.debug("Passing outgoing message {}", event.getData());
                connection.out().accept(event.getData());
            });

        });



    }
}
