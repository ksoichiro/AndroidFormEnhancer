#!/usr/bin/env bash

[ -f local.properties ] && mv local.properties local.properties.bk

if [ $# -ge 1 -a "$1" = "jdk6" ]; then
  echo "Execute connectedCheck task with jdk6"
  docker run -t -i -v `pwd`:/workspace ksoichiro/android-jdk6-emulator start-emulator "./gradlew :library:connectedCheck"
else
  echo "Execute assemble task"
  docker run -t -i -v `pwd`:/workspace -w /workspace ksoichiro/android /bin/sh -c "./gradlew assemble"
fi

[ -f local.properties.bk ] && mv local.properties.bk local.properties
