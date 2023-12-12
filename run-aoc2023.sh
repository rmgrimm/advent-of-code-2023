#!/bin/bash -e

#
# A convenience script for building/running Advent of Code challenges

run_aoc_day() {
  DAY="$1"
  shift

  if [ ! -d day"$DAY" ]; then
    if [ -d day0"$DAY" ]; then
      DAY=0"$1"
    else
      echo "Day $DAY doesn't exist"
      exit 1
    fi
  fi

  if [ ! -r day"$DAY"/target/day"$DAY"-1.0.0-SNAPSHOT.jar ]; then
    echo Building day "$DAY" solutions...
    ./mvnw --quiet package --projects day"$DAY" --also-make
  fi

  echo Day "$DAY"
  echo -n "    "Challenge 1 Result:" "
  java -cp day"$DAY"/target/day"$DAY"-1.0.0-SNAPSHOT.jar com.github.rmgrimm.adventofcode2023.day"$DAY".Day"$DAY"Challenge1Kt "$@"
  echo -n "    "Challenge 2 Result:" "
  java -cp day"$DAY"/target/day"$DAY"-1.0.0-SNAPSHOT.jar com.github.rmgrimm.adventofcode2023.day"$DAY".Day"$DAY"Challenge2Kt "$@"
}

CHALLENGE_ARGS=()
DAYS_TO_RUN=()
while [ $# -gt 0 ]; do
  if [[ $1 == --* ]]; then
    CHALLENGE_ARGS+=( "$1" )
  else
    DAYS_TO_RUN+=( "$1" )
  fi
  shift
done

if [ "${#DAYS_TO_RUN[@]}" = "0" ]; then
  echo Building all day solutions first...
  ./mvnw --quiet package
  for DAY in day*; do
    run_aoc_day "${DAY##day}" "${CHALLENGE_ARGS[@]}"
  done
else
  for DAY in "${DAYS_TO_RUN[@]}"; do
    run_aoc_day "${DAY##day}" "${CHALLENGE_ARGS[@]}"
  done
fi
