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
package com.spawnin.battlecat.core.plugin;

import com.spawnin.battlecat.translator.BattlefieldMessage;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.spring.annotation.Selector;

/**
 * Forwards incoming events to the plugin reactor. It uses the command/first word
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class SimplePluginHub implements PluginHub {

    private final Reactor pluginHubReactor;

    public SimplePluginHub(Reactor pluginHubReactor) {
        this.pluginHubReactor = pluginHubReactor;
    }

    @Selector(value="incoming", reactor = "@incomingMessageReactor")
    @Override
    public void handleEvent(Event<BattlefieldMessage> event) {

        BattlefieldMessage message = event.getData();

        String command = message.getWords().get(0);

        // filter OK responses
        if (!"OK".equals(command)) {
            pluginHubReactor.notify(command, event);
        }

    }
}
