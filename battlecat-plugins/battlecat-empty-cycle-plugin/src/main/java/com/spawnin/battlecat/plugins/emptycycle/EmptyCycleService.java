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
package com.spawnin.battlecat.plugins.emptycycle;

import com.spawnin.battlecat.core.network.EasyRequestBuilderFactory;
import com.spawnin.battlecat.core.network.OutgoingConnectionService;
import com.spawnin.battlecat.translator.BattlefieldMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class EmptyCycleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyCycleService.class);

    private final OutgoingConnectionService outgoingConnectionService;

    private final EasyRequestBuilderFactory messageBuilderFactory;

    public EmptyCycleService(OutgoingConnectionService outgoingConnectionService,
                             EasyRequestBuilderFactory messageBuilderFactory) {
        this.outgoingConnectionService = outgoingConnectionService;
        this.messageBuilderFactory = messageBuilderFactory;
    }

    public void checkForPlayers() {

        BattlefieldMessage listPlayers = messageBuilderFactory.getBuilder().addWord("listPlayers").build();

        outgoingConnectionService.send(listPlayers, event -> System.out.println(event.getData()));

    }


}
