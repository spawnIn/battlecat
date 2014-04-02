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
import reactor.event.Event;
import reactor.spring.context.annotation.Selector;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class IncomingConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingConsumer.class);

    @Selector(value="incoming", reactor = "@incomingMessageReactor")
    public void handleMessage(Event<BattlefieldMessage> event) {
        LOGGER.info("Received incoming message {}", event);
    }

}
