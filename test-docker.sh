#!/usr/bin/env bash

[ -f local.properties ] && mv local.properties local.properties.bk

docker run -t -i -v `pwd`:/workspace -w /workspace ksoichiro/android /bin/sh -c "./gradlew assemble"

[ -f local.properties.bk ] && mv local.properties.bk local.properties
