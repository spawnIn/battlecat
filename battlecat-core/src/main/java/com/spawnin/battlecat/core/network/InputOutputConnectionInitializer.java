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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.net.NetChannel;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class InputOutputConnectionInitializer implements ConnectionInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputConnectionInitializer.class);

    private final Reactor inputReactor;
    private final Reactor outputReactor;

    public InputOutputConnectionInitializer(Reactor inputReactor, Reactor outputReactor) {
        this.inputReactor = inputReactor;
        this.outputReactor = outputReactor;
    }

    @Override
    public void initialize(NetChannel<BattlefieldMessage, BattlefieldMessage> connection) {

        LOGGER.debug("Registering connection with inputReactor {} and outputReactor {}", inputReactor, outputReactor);

        connection.in().consume(message -> {
            LOGGER.debug("Passing incoming message {}", message);
            inputReactor.notify("incoming", Event.wrap(message));
        });

        outputReactor.getConsumerRegistry().unregister(new Object());

        outputReactor.on(new ByTypeSelector(Object.class), (Event<BattlefieldMessage> event) -> {
            LOGGER.debug("Passing outgoing message {}", event.getData());
            connection.out().accept(event.getData());
        });

    }
}
