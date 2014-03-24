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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class SimpleBattlefieldMessageBuilder implements BattlefieldMessageBuilder {

    private Integer id;

    private List<String> words;

    private MessageType type;

    private OriginType origin;

    public SimpleBattlefieldMessageBuilder() {
        words = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public BattlefieldMessageBuilder setId(Integer id) {
        this.id = id;

        return this;
    }

    @Override
    public List<String> getWords() {
        return words;
    }

    @Override
    public BattlefieldMessageBuilder setWords(List<String> words) {
        this.words = words;

        return this;
    }

    @Override
    public BattlefieldMessageBuilder addWord(String word) {
        words.add(word);

        return this;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public BattlefieldMessageBuilder setType(MessageType type) {
        this.type = type;

        return this;
    }

    @Override
    public OriginType getOrigin() {
        return origin;
    }

    @Override
    public BattlefieldMessageBuilder setOrigin(OriginType origin) {
        this.origin = origin;

        return this;
    }

    @Override
    public BattlefieldMessage build() {

        if (id == null) {
            throw new IllegalArgumentException("Id not set");
        }
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Words not set");
        }
        if (type == null) {
            throw new IllegalArgumentException("Message type not set");
        }
        if (origin == null) {
            throw new IllegalArgumentException("Origin type not set");
        }

        return new ImmutableBattlefieldMessage(id, Collections.unmodifiableList(new ArrayList<String>(words)), type, origin);
    }
}
