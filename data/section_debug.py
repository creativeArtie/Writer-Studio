#!/bin/usr/python

import re

def get_type(line):
    if "HEAD " in line:
        return " HEADING "
    elif "SCENE " in line:
        return " OUTLINE "
    else:
        return " OTHERS  "

lines = [line.rstrip('\n') for line in open('sectionDebug_note.txt')]
class LineSetup:
    def __init__(self):
        self.fNum = 1
        self.level = 1
        self.layers = [-1]
    def nextFile(self):
        self.fNum = self.fNum + 1
        self.level = 1
        self.layers = [-1]
    def addChild(self):
        self.level = self.level + 1
        self.layers.append(0)
    def nextSibling(self):
        self.layers[-1] = self.layers[-1] + 1
    def removeChild(self):
        self.level = self.level - 1
        self.layers.pop()
    def output(self, line):
        last = ""
        for layer in self.layers:
            last = last + str(layer) + " "
        print str(self.fNum) + get_type(line) + last + "|"


data = LineSetup()
with open('sectionDebug_note.txt') as lines:
    for line in lines:
        count = line.count("\t")
        if re.search('[a-zA-Z]', line):
            if count == data.level - 1:
                data.nextSibling()
                data.output(line)
            elif count > data.level - 1:
                data.addChild()
                data.output(line)
            elif count < data.level - 1:
                while count < data.level - 1:
                    data.removeChild()
                data.nextSibling()
                data.output(line)
        elif line == "\n":
            data.nextFile()
            print "0 NEW_FILE |"
        else:
            print "0 SKIP_LINE |"
            pass