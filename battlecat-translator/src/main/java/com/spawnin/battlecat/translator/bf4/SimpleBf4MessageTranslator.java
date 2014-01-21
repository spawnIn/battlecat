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
package com.spawnin.battlecat.translator.bf4;

import com.spawnin.battlecat.translator.*;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class SimpleBf4MessageTranslator implements BattlefieldMessageTranslator {


    @Override
    public BattlefieldMessage decode(byte[] data) {
        MutableBattlefieldMessage msg = new MutableBattlefieldMessage();

        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        // validate header
        int header = byteBuffer.getInt();

        msg.setOrigin(OriginType.get(header));
        msg.setType(MessageType.get(header));

        int id = OriginType.resetBit(header);
        id = MessageType.resetBit(id);

        msg.setId(id);

        // packet size
        int packetSize = byteBuffer.getInt();

        // number of words
        int numberOfWords = byteBuffer.getInt();

        // decode words


        byte[] words = new byte[data.length - 12];
        byteBuffer.get(words, 0, words.length);

        msg.setWords(decodeWords(numberOfWords, words));

        return msg;
    }

    @Override
    public byte[] encode(BattlefieldMessage msg) {

        int header = encodeHeader(msg.getId(), msg.getOrigin(), msg.getType());
        int numberOfWords = msg.getWords().size();

        int packetSize = 12;

        List<byte[]> words = new ArrayList<>();

        for (String word : msg.getWords()) {
            words.add(encodeWord(word));

            packetSize += word.length() + 5;
        }

        ByteBuffer buffer = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN).putInt(header).putInt(packetSize).putInt(numberOfWords);

        for (byte[] word : words) {
            buffer.put(word);
        }

        return buffer.array();

    }

    @Override
    public int decodeMessageSize(byte[] data) {
        if (data.length < BattlefieldMessageTranslator.MIN_MESSAGE_SIZE) {
            throw new IllegalArgumentException("Packet is too small to be analyzed");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        int header = byteBuffer.getInt();
        // discard

        int packetSize = byteBuffer.getInt();

        return packetSize;
    }

    private List<String> decodeWords(int numberOfWords, byte[] data) {

        List<String> words = new ArrayList<>();
        int wordsRead = 0;

        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        while(byteBuffer.hasRemaining() && wordsRead < numberOfWords) {

            int wordLength = byteBuffer.getInt();

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < wordLength; i++) {

                stringBuilder.append((char) byteBuffer.get());

            }

            if (byteBuffer.get() == (byte) 0) {
                wordsRead++;
                words.add(stringBuilder.toString());

            } else {
                throw new IllegalArgumentException("Word did not end properly");
            }
        }

        return words;
    }

    private byte[] encodeWord(String word) {

        try {
            byte[] chars = word.getBytes("US-ASCII");
            int size = chars.length;

            return ByteBuffer.allocate(4 + size  + 1).order(ByteOrder.LITTLE_ENDIAN).putInt(size).put(chars).put((byte) 0).array();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not convert", e);
        }

    }

    private int encodeHeader(int sequence, OriginType origin, MessageType messageType) {

        int data = sequence;

        data = origin.setBit(data);
        data = messageType.setBit(data);

        return data;
    }

}
