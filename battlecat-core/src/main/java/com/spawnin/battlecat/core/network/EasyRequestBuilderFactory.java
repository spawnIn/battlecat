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

import com.spawnin.battlecat.translator.BattlefieldMessageBuilder;
import com.spawnin.battlecat.translator.BattlefieldMessageBuilderFactory;
import com.spawnin.battlecat.translator.MessageType;
import com.spawnin.battlecat.translator.OriginType;

/**
 * Convenience class to create {@link com.spawnin.battlecat.translator.BattlefieldMessageBuilder} for outgoing requests
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class EasyRequestBuilderFactory {

    private final BattlefieldMessageBuilderFactory messageBuilderFactory;

    private final MessageIdFactory messageIdFactory;

    public EasyRequestBuilderFactory(BattlefieldMessageBuilderFactory messageBuilderFactory,
                                     MessageIdFactory messageIdFactory) {
        this.messageBuilderFactory = messageBuilderFactory;
        this.messageIdFactory = messageIdFactory;
    }

    public BattlefieldMessageBuilder getBuilder() {

        BattlefieldMessageBuilder builder = messageBuilderFactory.getBuilder();

        builder.setId(messageIdFactory.nextId());
        builder.setType(MessageType.REQUEST);
        builder.setOrigin(OriginType.CLIENT);

        return builder;
    }
}
