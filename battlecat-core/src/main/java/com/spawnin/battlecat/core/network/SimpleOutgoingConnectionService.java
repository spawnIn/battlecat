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
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.event.registry.Registration;
import reactor.event.selector.Selectors;
import reactor.function.Consumer;
import reactor.net.NetChannel;
import reactor.spring.context.annotation.Selector;

/**
 * Simple implementation that notifies the configured {@link #outgoingMessageReactor} with the given message using
 * the {@link #notificationKey}.
 * Uses and manages a {@link #responseReactor} to handle response callbacks.
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
@reactor.spring.context.annotation.Consumer
public class SimpleOutgoingConnectionService implements OutgoingConnectionService, ConnectionInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOutgoingConnectionService.class);

    private final Reactor outgoingMessageReactor;

    private final Reactor responseReactor;

    private final String notificationKey = "outgoingMessage";

    public SimpleOutgoingConnectionService(Reactor outgoingMessageReactor, Reactor responseReactor) {
        this.outgoingMessageReactor = outgoingMessageReactor;
        this.responseReactor = responseReactor;
    }

    @Override
    public void send(BattlefieldMessage message) {

        LOGGER.debug("Notifying outgoingMessageReactor {} of message {}", outgoingMessageReactor, message);

        outgoingMessageReactor.notify(notificationKey, Event.wrap(message));

    }

    @Override
    public void send(BattlefieldMessage message, OnResponse onResponse) {
        LOGGER.debug("Notifying outgoingMessageReactor {} of message {} and registering onResponse object {}",
                new Object[]{outgoingMessageReactor, message, onResponse});

        Registration<Consumer<Event<BattlefieldMessage>>> registration =
                responseReactor.on(Selectors.$(message.getId()), onResponse);

        // perform only once
        registration.cancelAfterUse();

        outgoingMessageReactor.notify(notificationKey, Event.wrap(message));
    }

    @Override
    public void initialize(NetChannel<BattlefieldMessage, BattlefieldMessage> connection) {

        LOGGER.debug("Clearing all callbacks on responseReactor {}", responseReactor);

        // clear all registered consumers on the responseReactor on reconnect
        responseReactor.getConsumerRegistry().clear();
    }

    @Selector(value = "incoming", reactor = "@incomingMessageReactor")
    public void handleIncomingMessage(Event<BattlefieldMessage> event) {
        BattlefieldMessage message = event.getData();

        Integer id = message.getId();

        LOGGER.debug("Forwarding incoming message {} to responseReactor {} using id {}",
                new Object[]{message, responseReactor, id});

        responseReactor.notify(id, Event.wrap(message));
    }
}
