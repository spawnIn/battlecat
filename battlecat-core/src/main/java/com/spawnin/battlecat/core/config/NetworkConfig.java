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
package com.spawnin.battlecat.core.config;

import com.spawnin.battlecat.core.network.*;
import com.spawnin.battlecat.translator.BattlefieldMessage;
import com.spawnin.battlecat.translator.BattlefieldMessageBuilderFactory;
import com.spawnin.battlecat.translator.BattlefieldMessageTranslator;
import com.spawnin.battlecat.translator.bf4.SimpleBf4MessageTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.net.netty.tcp.NettyTcpClient;
import reactor.net.tcp.TcpClient;
import reactor.net.tcp.spec.TcpClientSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring JavaConfig for networking related objects and components
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
@Configuration
public class NetworkConfig {

    @Autowired
    private org.springframework.core.env.Environment springEnv;

    @Autowired
    private BattlefieldMessageBuilderFactory battlefieldMessageBuilderFactory;

    @Autowired
    private MessageIdFactory messageIdFactory;

    @Autowired
    @Qualifier("incomingMessageReactor")
    private Reactor incomingMessageReactor;

    @Autowired
    @Qualifier("outgoingMessageReactor")
    private Reactor outgoingMessageReactor;

    @Autowired
    @Qualifier("responseReactor")
    private Reactor responseReactor;

    @Bean
    public TcpClient<BattlefieldMessage, BattlefieldMessage> serverClient(Environment env) {

        String host = springEnv.getRequiredProperty("battlecat.battlefield.server.host");
        int port = springEnv.getRequiredProperty("battlecat.battlefield.server.port", Integer.class);

        return new TcpClientSpec<BattlefieldMessage, BattlefieldMessage>(NettyTcpClient.class).env(env).
                codec(battlefieldMessageCodec()).connect(host, port).get();
    }

    @Bean
    public BattlefieldMessageCodec battlefieldMessageCodec() {
        return new BattlefieldMessageCodec(battlefieldMessageTranslator());
    }

    @Bean
    public BattlefieldMessageTranslator battlefieldMessageTranslator() {
        return new SimpleBf4MessageTranslator(battlefieldMessageBuilderFactory);
    }

    @Bean(initMethod = "connect")
    public ServerConnectionManager serverConnectionManager(Environment env) {

        return new ServerConnectionManager(serverClient(env), null,
                connectionInitializers());

    }

    @Bean
    public OutgoingConnectionService outgoingConnectionService() {
        return new SimpleOutgoingConnectionService(outgoingMessageReactor, responseReactor);
    }

    @Bean
    public List<ConnectionInitializer> connectionInitializers() {
        List<ConnectionInitializer> connectionInitializers = new ArrayList<>();

        connectionInitializers.add(messageIdFactoryResetConnectionInitializer());
        connectionInitializers.add(inputOutputConnectionInitializer());
        connectionInitializers.add((SimpleOutgoingConnectionService) outgoingConnectionService());
        connectionInitializers.add(plainLoginConnectionInitializer());

        return connectionInitializers;
    }

    @Bean
    public ConnectionInitializer inputOutputConnectionInitializer() {
        return new InputOutputConnectionInitializer(incomingMessageReactor, outgoingMessageReactor);
    }

    @Bean
    public MessageIdFactoryResetConnectionInitializer messageIdFactoryResetConnectionInitializer() {
        return new MessageIdFactoryResetConnectionInitializer(messageIdFactory);
    }

    @Bean
    public PlainLoginConnectionInitializer plainLoginConnectionInitializer() {
        return new PlainLoginConnectionInitializer(
                springEnv.getRequiredProperty("battlecat.battlefield.server.admin.password"),
                outgoingConnectionService(), battlefieldMessageBuilderFactory, messageIdFactory);
    }

    @Bean
    public IncomingConsumer incomingConsumer() {
        return new IncomingConsumer();
    }

    @Bean
    public SimpleResponseAcknowledger responseAcknowledger() {
        return new SimpleResponseAcknowledger(outgoingConnectionService(), battlefieldMessageBuilderFactory);
    }

    //@Bean
    public NetworkInitializer networkInitializer() {
        return new NetworkInitializer();
    }


}
