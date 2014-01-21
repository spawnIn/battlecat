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

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public interface BattlefieldMessageTranslator {

    /**
     * Min. message size in bytes
     */
    public static final int MIN_MESSAGE_SIZE = 12;

    BattlefieldMessage decode(byte[] data);

    byte[] encode(BattlefieldMessage msg);

    /**
     * also works on partial message
     *
     * @param data
     * @return
     * @throws IllegalArgumentException if
     */
    int decodeMessageSize(byte[] data);

}
