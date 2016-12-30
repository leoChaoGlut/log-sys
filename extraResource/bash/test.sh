#!/usr/bin/env bash

source /etc/profile

for lib in /data/logSys/serviceCenter/registry/lib/*.jar
do
    libs=$lib:$libs
done

CLASSPATH=$libs
echo $libs
export CLASSPATH

java -jar /data/logSys/serviceCenter/registry/registry.jar
