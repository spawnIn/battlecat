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
package com.spawnin.battlecat.translator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Immutable implementation of {@link com.spawnin.battlecat.translator.BattlefieldMessage}
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
class ImmutableBattlefieldMessage implements BattlefieldMessage {

    private final Integer id;

    private final List<String> words;

    private final MessageType type;

    private final OriginType origin;

    ImmutableBattlefieldMessage(Integer id, List<String> words, MessageType type, OriginType origin) {
        this.id = id;
        this.words = words;
        this.type = type;
        this.origin = origin;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public List<String> getWords() {
        return words;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public OriginType getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("words", words)
                .append("type", type)
                .append("origin", origin)
                .toString();
    }
}
