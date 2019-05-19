# wiremock-redis-extension

The wiremock-redis-extension is a Wiremock extension to enable more complex responses and scenarios, storing values between any requests and time using Redis.



## How It Works

This extension extends the native response template to include a custom handlebar adjunct for Redis communication. The goal is to use with standalone process, but can be used with JUnit too.



## Usage

### Store value

    {{ redis key='mykey' value='Redis' }}

### Read a previous stored value

    {{ redis key='mykey' }}

### Store value in conjunction an another helper

    {{#redis key='mykey'}}Created at {{now}}{{/redis}}

### Remove stored value

    {{ redis del='mykey'}}

### Configure Redis environment variables

* REDIS_HOST: default is localhost
* REDIS_PORT: default is 6379
* REDIS_PASSWORD: default is null for no auth required

> More use instructions coming soon!


## Contribute

Feedback is welcome. The source is available on [Github](https://github.com/gustajz/wiremock-redis-extension). Please [report any issues](https://github.com/gustajz/wiremock-redis-extension/issues).
