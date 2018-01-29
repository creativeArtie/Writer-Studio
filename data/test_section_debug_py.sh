#!/bin/bash

file="tests/sectionDebugOutcomes.txt"

python section_debug.py > ${file}
paste -d' ' ${file} sectionDebug_note.txt | column -ts"|" | less -NR
