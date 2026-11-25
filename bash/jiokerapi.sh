#!/bin/bash

read -r -p "Tú: " mensaje
curl -sS https://v2.jokeapi.dev/jokes/random\
    -H "Accept: application/json" \
    -H "User-Agent: Bash Script" \
    -d "{
        \"type\": \"single\",
        \"blacklistFlags\": [\"nsfw\", \"religious\", \"political\", \"racist\", \"sexist\", \"explicit\"]
    }"
echo
