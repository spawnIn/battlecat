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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation using an {@link java.util.concurrent.atomic.AtomicInteger}
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class AtomicIntegerMessageIdFactory implements MessageIdFactory {

    private final AtomicInteger idCounter;

    /**
     *
     * @param seed seed to start the counter off
     */
    public AtomicIntegerMessageIdFactory(int seed) {
        this.idCounter = new AtomicInteger(seed);
    }

    @Override
    public int nextId() {
        // TODO trigger roll over / reconnect
        return idCounter.incrementAndGet();
    }
}
