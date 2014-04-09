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

import com.spawnin.battlecat.translator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.net.NetChannel;

/**
 * Performs a plain login on connection intializing
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class PlainLoginConnectionInitializer implements ConnectionInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainLoginConnectionInitializer.class);

    private final String password;

    private final OutgoingConnectionService outgoingConnectionService;

    private final BattlefieldMessageBuilderFactory messageBuilderFactory;

    private final MessageIdFactory messageIdFactory;

    public PlainLoginConnectionInitializer(String password, OutgoingConnectionService outgoingConnectionService,
                                           BattlefieldMessageBuilderFactory messageBuilderFactory,
                                           MessageIdFactory messageIdFactory) {
        this.password = password;
        this.outgoingConnectionService = outgoingConnectionService;
        this.messageBuilderFactory = messageBuilderFactory;
        this.messageIdFactory = messageIdFactory;
    }

    @Override
    public void initialize(NetChannel<BattlefieldMessage, BattlefieldMessage> connection) {

        LOGGER.info("Sending plain login to server");

        BattlefieldMessageBuilder builder = messageBuilderFactory.getBuilder();
        BattlefieldMessage message = builder.setId(messageIdFactory.nextId()).setType(MessageType.REQUEST)
                .setOrigin(OriginType.CLIENT).addWord("login.plainText").addWord(password).build();

        outgoingConnectionService.send(message, (event) -> {
            BattlefieldMessage loginResponse = event.getData();

            if (MessageType.RESPONSE.equals(loginResponse.getType()) && "OK".equals(loginResponse.getWords().get(0))) {
                LOGGER.info("Login successful, enabling admin events");
                // enable admin events
                BattlefieldMessageBuilder adminBuilder = messageBuilderFactory.getBuilder();
                BattlefieldMessage adminEvents = adminBuilder.setId(messageIdFactory.nextId()).setType(MessageType.REQUEST)
                        .setOrigin(OriginType.CLIENT).addWord("admin.eventsEnabled").addWord("true").build();

                outgoingConnectionService.send(adminEvents);
            }
        });

    }
}
