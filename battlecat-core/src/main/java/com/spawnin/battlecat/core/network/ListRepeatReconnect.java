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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.net.Reconnect;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Reconnects to the same address based on intervals provided in {@link #intervals} in ms. E.g. (10, 3000, 60000) -> 1.
 * reconnect after 10ms, 2. reconnect after 3sec, 3. reconnect after 1min. Infinitely tries to reconnect with last
 * interval.
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class ListRepeatReconnect implements Reconnect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListRepeatReconnect.class);

    private final List<Long> intervals;

    public ListRepeatReconnect(List<Long> intervals) {
        this.intervals = intervals;
    }

    @Override
    public Tuple2<InetSocketAddress, Long> reconnect(InetSocketAddress currentAddress, int attempt) {

        if (intervals == null || intervals.isEmpty()) {
            LOGGER.error("No intervals defined. Not trying to reconnect");

            return null;
        }

        Tuple2<InetSocketAddress, Long> retry = null;

        if (attempt > intervals.size()) {

            retry = Tuple.of(currentAddress, intervals.get(intervals.size() - 1));

        } else {

            retry = Tuple.of(currentAddress, intervals.get(attempt - 1));

        }

        LOGGER.warn("Retrying to reconnect to server after {} ms.", retry.getT2());

        return retry;
    }
}
