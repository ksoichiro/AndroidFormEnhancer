#!/usr/bin/env bash

if [ $# -gt 0 ]; then
  CMD=$1
  PORT=5554
  echo "Starting emulator[$PORT]..."
  emulator -avd test -no-skin -no-audio -no-window -port $PORT &
  wait-for-emulator
  $CMD
else
  echo "No command is specified"
fi
