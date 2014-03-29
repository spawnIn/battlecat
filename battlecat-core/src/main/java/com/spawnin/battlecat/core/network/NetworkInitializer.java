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
import com.spawnin.battlecat.translator.BattlefieldMessageBuilderFactory;
import com.spawnin.battlecat.translator.MessageType;
import com.spawnin.battlecat.translator.OriginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import reactor.core.Reactor;
import reactor.core.composable.Promise;
import reactor.event.Event;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class NetworkInitializer implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private BattlefieldMessageBuilderFactory factory;

    @Autowired
    @Qualifier("outgoingMessageReactor")
    private Reactor outgoingMessageReactor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        BattlefieldMessage serverinfo = factory.getBuilder().setId(12345).setOrigin(OriginType.CLIENT).setType(MessageType.REQUEST).addWord("serverinfo").build();
        BattlefieldMessage login = factory.getBuilder().setId(12346).setOrigin(OriginType.CLIENT).setType(MessageType.REQUEST).addWord("login.plainText").addWord("password").build();
        BattlefieldMessage events = factory.getBuilder().setId(12347).setOrigin(OriginType.CLIENT).setType(MessageType.REQUEST).addWord("admin.eventsEnabled").addWord("true").build();


        outgoingMessageReactor.notify(Event.wrap(login));
        outgoingMessageReactor.notify(Event.wrap(events));
        outgoingMessageReactor.notify(Event.wrap(serverinfo));
    }
}
