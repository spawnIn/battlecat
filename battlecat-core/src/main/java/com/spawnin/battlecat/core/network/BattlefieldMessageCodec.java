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

import com.spawnin.battlecat.translator.BattlefieldMessage;
import com.spawnin.battlecat.translator.BattlefieldMessageTranslator;
import reactor.function.Consumer;
import reactor.function.Function;
import reactor.io.Buffer;
import reactor.tcp.encoding.Codec;

import java.util.Arrays;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class BattlefieldMessageCodec implements Codec<Buffer, BattlefieldMessage, BattlefieldMessage> {

    private final BattlefieldMessageTranslator translator;


    public BattlefieldMessageCodec(BattlefieldMessageTranslator translator) {

        this.translator = translator;

    }

    @Override
    public Function<Buffer, BattlefieldMessage> decoder(final Consumer<BattlefieldMessage> next) {
        return (buffer) -> {

            if (next == null) {
                throw new IllegalArgumentException("next cannot be null due to handling multiple frames");
            }

            int numberOfMessages = 0;

            while(true) {

                byte[] data = buffer.asBytes();

                if (data.length < BattlefieldMessageTranslator.MIN_MESSAGE_SIZE) {
                    break;
                }

                int size = translator.decodeMessageSize(data);

                byte[] dataFrame = Arrays.copyOf(data, size);

                BattlefieldMessage message = translator.decode(dataFrame);

                // advance buffer position
                buffer.position(buffer.position() + size);

                next.accept(message);

            }

            // why?!
            return null;
        };
    }

    @Override
    public Function<BattlefieldMessage, Buffer> encoder() {
        return msg -> Buffer.wrap(translator.encode(msg));
    }
}
