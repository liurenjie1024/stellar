#!/usr/bin/env just --justfile


format-check:
    cd stellar && sbt scalafmtCheck

test:
    cd stellar && sbt test
