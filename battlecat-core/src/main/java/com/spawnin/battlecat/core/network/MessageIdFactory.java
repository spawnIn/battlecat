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

/**
 * Factory to create new message ids for messages sent to the server.
 *
 * Implementations must adhere to the sequence number definition of 30 bits
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public interface MessageIdFactory {

    /**
     * creates a new id
     *
     * @return a new id
     * @throws com.spawnin.battlecat.core.network.CounterOverflowException if the counter exceeds its max value
     */
    int nextId();

    /**
     * resets the Factory for a new connection with the server
     */
    void reset();

}
