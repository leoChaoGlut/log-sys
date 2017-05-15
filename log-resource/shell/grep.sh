#!/bin/sh

keyword=$1
logs=$2

for log in $logs
do
        result="${result},`grep -q -i $keyword $log && echo 1 || echo 0`"
done

echo $result
