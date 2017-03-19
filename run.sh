#!/bin/bash

if [ ! -f .env ]; then
  echo "No .env file. Please see README. Exiting."
  exit 1
fi

source .env
lein run
