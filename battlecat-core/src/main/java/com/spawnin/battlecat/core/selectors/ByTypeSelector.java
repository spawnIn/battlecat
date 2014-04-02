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
package com.spawnin.battlecat.core.selectors;

import reactor.event.selector.HeaderResolver;
import reactor.event.selector.Selector;
import reactor.util.UUIDUtils;

import java.util.UUID;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class ByTypeSelector implements Selector {

    private final UUID uuid;

    private final Class<?> type;

    public ByTypeSelector(Class type) {
        this.uuid = UUIDUtils.create();
        this.type = type;
    }


    @Override
    public Object getObject() {
        return type;
    }

    @Override
    public boolean matches(Object key) {
        return key != null && type.isAssignableFrom(key.getClass());
    }

    @Override
    public HeaderResolver getHeaderResolver() {
        return null;
    }
}
