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
package com.github.gustajz.wiremock.extension.test;

import com.github.gustajz.wiremock.extension.Redis;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.testcontainers.containers.GenericContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class RedisTests {

    @ClassRule
    public static GenericContainer redis =
            new GenericContainer("redis:4").withExposedPorts(6379);

    @ClassRule
    public static WireMockRule wm =
            new WireMockRule(WireMockConfiguration.options().extensions(Redis.class));

    @ClassRule
    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void setAndGetSimpleValue() {
        environmentVariables.set("REDIS_PORT", String.valueOf(redis.getFirstMappedPort()));

        stubFor(get(urlEqualTo("/step1"))
                .willReturn(aResponse()
                        .withBody("Welcome {{ redis key='mykey' value='Redis' }}!")));

        stubFor(get(urlEqualTo("/step2"))
                .willReturn(aResponse()
                        .withBody("Welcome {{ redis key='mykey' }}!")));

        when().
            get("/step1").
        then().
            statusCode(200).
            body(equalTo("Welcome !"));

        when().
            get("/step2").
        then().
            statusCode(200).
            body(equalTo("Welcome Redis!"));

    }

    @Test
    public void setAndGetComplexValue() {
        environmentVariables.set("REDIS_PORT", String.valueOf(redis.getFirstMappedPort()));

        stubFor(get(urlEqualTo("/step1"))
                .willReturn(aResponse()
                        .withBody("Welcome {{#redis key='mykey'}} {{size 'abcde'}} {{/redis}}")));

        stubFor(get(urlEqualTo("/step2"))
                .willReturn(aResponse()
                        .withBody("{{ redis key='mykey' }}!")));

        when().
                get("/step1").
                then().
                statusCode(200).
                body(equalTo("Welcome "));

        when().
                get("/step2").
                then().
                statusCode(200).
                body(equalTo("5!"));

    }

    @Test
    public void setAndGetValueAndInTheEndRemove() {
        environmentVariables.set("REDIS_PORT", String.valueOf(redis.getFirstMappedPort()));

        stubFor(get(urlEqualTo("/step1"))
                .willReturn(aResponse()
                        .withBody("{{#redis key='mykey'}} {{size 'abcde'}} {{/redis}}")));

        stubFor(get(urlEqualTo("/step2"))
                .willReturn(aResponse()
                        .withBody("{{ redis key='mykey' }}!{{ redis del='mykey'}}")));

        stubFor(get(urlEqualTo("/step3"))
                .willReturn(aResponse()
                        .withBody("{{ redis key='mykey' }}!")));

        when().
                get("/step1").
                then().
                statusCode(200).
                body(equalTo(""));

        when().
                get("/step2").
                then().
                statusCode(200).
                body(equalTo("5!"));

        when().
                get("/step3").
                then().
                statusCode(200).
                body(equalTo("!"));

    }

}