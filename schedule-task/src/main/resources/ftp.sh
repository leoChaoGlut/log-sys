#!/bin/bash

host=$1
username=$2
password=$3
srcFile=$4
destFile=$5

ftp -vn  >/tmp/ftp.log <<EOF
open $host
user $username $password
cd $destFile
put $srcFile
quit
EOF


count=`cat /tmp/ftp.log | awk '{print $1}' | grep 226 | wc -l`

if [ $count -eq 1 ];then
  echo "success"
else
  echo "failure"
fi