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

import com.spawnin.battlecat.core.plugin.PluginHub;
import com.spawnin.battlecat.core.plugin.SimplePluginHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;

/**
 * Reactor config
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
@Configuration
public class ReactorConfig {

    @Bean
    public Reactor pluginHubReactor(Environment env) {
        return Reactors.reactor(env);
    }


    public PluginHub pluginHub(Environment env) {
        return new SimplePluginHub(pluginHubReactor(env));
    }

}
