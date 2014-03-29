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
public enum OriginType {

    SERVER("server", 0) {

        @Override
        public int setBit(int data) {
            return data &= ~(1 << BIT);
        }

    }, CLIENT("client", 1) {

        @Override
        public int setBit(int data) {
            return data |= 1 << BIT;
        }

    };

    private final String name;
    private final int value;
    private static final int BIT = 31;

    private OriginType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public abstract int setBit(int data);

    public static int resetBit(int data) {
        return data &= ~(1 << BIT);
    }

    // TODO Refactor
    public static OriginType get(int data) {
        int value = (data >> BIT) & 1;

        for (OriginType type : values()) {

            if (type.value == value) {
                return type;
            }

        }

        throw new IllegalArgumentException("Unable to determine OriginType from given header data");
    }

}
