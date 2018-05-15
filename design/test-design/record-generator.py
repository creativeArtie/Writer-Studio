#!/bin/python

import random

publish=random.randint(100,200)
note=random.randint(0, 50)
start=random.randint(0, 187)
for i in range(start, random.randint(start, 365)):
    if (random.randint(1, 7) == 1):
        continue
    print 2015, i, publish, note, "PT" + str(random.randint(15, 60)) + "M", \
        "150", "PT30M"
    publish = publish + random.randint(100,200)
    note = note + random.randint(0, 50)
