#!/bin/bash -e

#
# A convenience script for building/running Advent of Code challenges

run_aoc_day() {
  DAY="$1"

  if [ ! -d day"$DAY" ]; then
    if [ -d day0"$DAY" ]; then
      DAY=0"$1"
    else
      echo "Day $DAY doesn't exist"
      exit 1
    fi
  fi

  if [ ! -r day"$DAY"/target/day"$DAY"-1.0.0-SNAPSHOT.jar ]; then
    echo Building Day "$DAY"...
    ./mvnw --quiet package --projects day"$DAY" --also-make
  fi

  echo Day "$DAY"
  echo -n "    "Challenge 1 Result:" "
  java -cp day"$DAY"/target/day"$DAY"-1.0.0-SNAPSHOT.jar com.github.rmgrimm.adventofcode2023.day"$DAY".Day"$DAY"Challenge1Kt
  echo -n "    "Challenge 2 Result:" "
  java -cp day"$DAY"/target/day"$DAY"-1.0.0-SNAPSHOT.jar com.github.rmgrimm.adventofcode2023.day"$DAY".Day"$DAY"Challenge2Kt
}

if [ "$#" = "0" ]; then
  echo Building all days first...
  ./mvnw --quiet package
  for DAY in day*; do
    run_aoc_day "${DAY##day}"
  done
else
  for DAY in "$@"; do
    run_aoc_day "${DAY##day}"
  done
fi
