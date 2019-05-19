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

import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;
import redis.clients.jedis.Jedis;
import wiremock.com.github.jknack.handlebars.Helper;
import wiremock.com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.Optional;

/**
 * RedisHelper
 *
 * <p>
 * You can use the redis helper to set or get values between any requests. Useful to create complex scenarios. Examples:
 *
 * Retrieve:
 * <pre>{{redis key='mykey'}}</pre>
 *
 * Simple set:
 * <pre>{{redis key='mykey' value='my data'}}</pre>
 *
 * More powerful set:
 * <pre>{{#redis key='mykey'}}Created at {{now}}{{/redis}}</pre>
 *
 * Remove value:
 * <pre>{{#redis del='mykey'}}</pre>
 *
 * @author gustavojotz
 */
public class RedisHelper extends HandlebarsHelper {

    static final Helper<?> INSTANCE = new RedisHelper();

    static final String NAME = "redis";

    private static final String KEY = "wiremock";

    @Override
    public Object apply(final Object o, final Options options) throws IOException {

        String apply = options.apply(options.fn).toString().trim();

        Optional<String> field = Optional.ofNullable(options.hash("key"));

        Optional<String> value = Optional.ofNullable(options.hash("value"));

        Optional<String> del = Optional.ofNullable(options.hash("del"));

        try (final Jedis jedis = JedisFactory.getInstance()) {

            if (del.isPresent()) {
                jedis.hdel(KEY, del.get());

            } else if (value.isPresent()) {
                jedis.hset(KEY, field.get(), value.get());

            } else if (apply.length() > 0) {
                jedis.hset(KEY, field.get(), apply);

            } else {
                return jedis.hget(KEY, field.orElse(""));
            }
        }

        return null;
    }

}
