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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SimpleBf4MessageTranslatorTest {

  private SimpleBf4MessageTranslator translator;

  private BattlefieldMessageBuilderFactory messageBuilderFactory;

  @Before
  public void setUp() throws Exception {
    messageBuilderFactory = new SimpleBattleFieldMessageBuilderFactory();

    translator = new SimpleBf4MessageTranslator(messageBuilderFactory);
  }

  private BattlefieldMessage createMessage(final int id, final List<String> words, final MessageType type, final OriginType origin) {

    return new BattlefieldMessage() {
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
    };

  }

  @Test
  public void testDecode() throws Exception {

    byte[] bytes = new byte[]{16,39,0,-128,32,0,0,0,2,0,0,0,5,0,0,0,104,101,108,108,111,0,5,0,0,0,119,111,114,108,100,0};

    BattlefieldMessage message = translator.decode(bytes);

    List<String> words = new ArrayList<String>(){{
      add("hello");
      add("world");
    }};

    assertThat(message, is(notNullValue()));
    assertThat(message.getId(), is(equalTo(10000)));
    assertThat(message.getWords(), is(equalTo(words)));
    assertThat(message.getOrigin(), is(equalTo(OriginType.CLIENT)));
    assertThat(message.getType(), is(equalTo(MessageType.REQUEST)));

  }

  @Test
  public void testEncode() throws Exception {

    byte[] byteMsg = new byte[]{16,39,0,-128,32,0,0,0,2,0,0,0,5,0,0,0,104,101,108,108,111,0,5,0,0,0,119,111,114,108,100,0};

    BattlefieldMessage msg = createMessage(10000, new ArrayList<String>(){{
      add("hello");
      add("world");
    }}, MessageType.REQUEST, OriginType.CLIENT);

    byte[] bytes = translator.encode(msg);

    assertThat(bytes, is(notNullValue()));
    assertThat(bytes, is(equalTo(byteMsg)));

  }

  @Test
  public void testDecodeMessageSize() throws Exception {

    byte[] bytes = new byte[]{16,39,0,-128,32,0,0,0,2,0,0,0,5,0,0,0,104,101,108,108,111,0,5,0,0,0,119,111,114,108,100,0};

    int messageSize = translator.decodeMessageSize(bytes);

    assertThat(messageSize, is(equalTo(32)));
  }
}