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

import com.spawnin.battlecat.core.network.AtomicIntegerMessageIdFactory;
import com.spawnin.battlecat.core.network.EasyRequestBuilderFactory;
import com.spawnin.battlecat.core.network.MessageIdFactory;
import com.spawnin.battlecat.core.plugin.PluginConfiguration;
import com.spawnin.battlecat.translator.BattlefieldMessageBuilderFactory;
import com.spawnin.battlecat.translator.SimpleBattleFieldMessageBuilderFactory;
import org.springframework.context.annotation.*;
import reactor.spring.context.config.EnableReactor;

/**
 * Main Spring config
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
@Configuration
@EnableReactor
@Import({ReactorConfig.class, NetworkConfig.class, PluginConfig.class})
@PropertySource("classpath:/default-application.properties")
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = true)
@ComponentScan( basePackages = "com.spawnin.battlecat.plugins",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {PluginConfiguration.class})})
public class MainConfig {

    @Bean(name = "battlefieldMessageBuilderFactory")
    public BattlefieldMessageBuilderFactory battlefieldMessageBuilderFactory() {
        return new SimpleBattleFieldMessageBuilderFactory();
    }

    @Bean
    public MessageIdFactory messageIdFactory() {
        return new AtomicIntegerMessageIdFactory(100000);
    }

    @Bean
    public EasyRequestBuilderFactory easyMessageBuilderFactory() {
        return new EasyRequestBuilderFactory(battlefieldMessageBuilderFactory(), messageIdFactory());
    }

}
