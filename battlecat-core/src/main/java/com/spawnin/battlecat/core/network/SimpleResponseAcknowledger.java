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
import com.spawnin.battlecat.translator.BattlefieldMessageBuilder;
import com.spawnin.battlecat.translator.BattlefieldMessageBuilderFactory;
import com.spawnin.battlecat.translator.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.spring.context.annotation.Consumer;
import reactor.spring.context.annotation.Selector;

/**
 * Responds to events from the server with a simple OK message and the same ID
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
@Consumer
public class SimpleResponseAcknowledger {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleResponseAcknowledger.class);

    private final OutgoingConnectionService outgoingConnectionService;

    private final BattlefieldMessageBuilderFactory factory;


    public SimpleResponseAcknowledger(OutgoingConnectionService outgoingConnectionService,
                                      BattlefieldMessageBuilderFactory factory) {
        this.outgoingConnectionService = outgoingConnectionService;
        this.factory = factory;
    }

    @Selector(value = "incoming", reactor = "@incomingMessageReactor")
    public void handleMessage(Event<BattlefieldMessage> event) {
        BattlefieldMessage message = event.getData();

        if (message.getType() == MessageType.REQUEST) {

            BattlefieldMessageBuilder messageBuilder = factory.getBuilder();

            messageBuilder.setId(message.getId()).setOrigin(message.getOrigin()).setType(MessageType.RESPONSE).addWord("OK");

            BattlefieldMessage response = messageBuilder.build();

            LOGGER.debug("Responding to incoming message {} with {}", message, response);

            outgoingConnectionService.send(response);
        }
    }

}
