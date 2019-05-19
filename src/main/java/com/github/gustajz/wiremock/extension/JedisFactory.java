/*
 * Copyright (c) 2019 Gustavo Jotz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.gustajz.wiremock.extension;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisFactory
 *
 * <p><b>REDIS_HOST</b>: default localhost</p>
 * <p><b>REDIS_PORT</b>: default 6379</p>
 * <p><b>REDIS_PASSWORD</b>: default null</p>
 *
 * @author gustavojotz
 */
public class JedisFactory {

    private static JedisPool instance;

    private JedisFactory() {
    }

    /**
     * Return a borrowed Jedis Resource from JedisPool.
     *
     * @return jedis
     */
    public static synchronized Jedis getInstance() {
        if (instance == null) {
            instance = initialize();
        }
        return instance.getResource();
    }

    /**
     * Initialize JedisPool with environments variables.
     *
     * @return jedisPool
     */
    private static JedisPool initialize() {

        String host = System.getenv("REDIS_HOST");

        String port = System.getenv("REDIS_PORT");

        String password = System.getenv("REDIS_PASSWORD");

        return new JedisPool(new JedisPoolConfig(), host != null ? host : "localhost", port != null ? Integer.valueOf(port) : 6379, 2000, password);
    }
}
