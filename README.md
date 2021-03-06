# Restrulz JVM libraries

[![Build Status](https://travis-ci.org/gantsign/restrulz-jvm.svg?branch=master)](https://travis-ci.org/gantsign/restrulz-jvm)
[![codecov](https://codecov.io/gh/gantsign/restrulz-jvm/branch/master/graph/badge.svg)](https://codecov.io/gh/gantsign/restrulz-jvm)

A set of libraries required by code generated by the
[Restrulz generator](https://github.com/gantsign/restrulz-gen) for applications
running on the Java Virtual Machine.

The code is written in [Kotlin](https://kotlinlang.org) but can easily be used
by Java or other JVM languages.

**Warning: Restrulz is far from feature complete and is not ready for general use.**

# Libraries

* `com.gantsign.restrulz.validation`
    * APIs to support validating data types

* `com.gantsign.restrulz.json`
    * Implementation independent APIs for reading and writing objects from/to
      JSON

* `com.gantsign.restrulz.jackson`
    * Support classes for implementing JSON Reading and writing using the
      [Jackson](http://wiki.fasterxml.com/JacksonHome) stream API

* `com.gantsign.restrulz.spring.mvc`
    * Spring MVC support for Restrulz responses and Restrulz JSON APIs

# Related projects

* [Restrulz](https://github.com/gantsign/restrulz) the language grammar, DSL
  parser and IDE plugins

* [Restrulz Generator](https://github.com/gantsign/restrulz-gen) this project
  will transform a Restrulz specification into various output formats / code

* [Restrulz Demo](https://github.com/gantsign/restrulz-demo) see Restrulz and
  Restrulz generator in action

# License

This project is under the
[Apache 2 Licence](https://raw.githubusercontent.com/gantsign/restrulz-jvm/master/LICENSE).

# Author information

John Freeman

GantSign Ltd.
Company No. 06109112 (registered in England)
