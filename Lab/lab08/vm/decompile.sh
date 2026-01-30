#!/bin/sh
mkdir decompiled_java

for f in *.class; do
  java -jar cfr-0.152.jar "$f" --outputdir decompiled_java
done

