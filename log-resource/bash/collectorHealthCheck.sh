#!/usr/bin/env bash

. /etc/profile

#======Usage: * * * * * bash /data/log-sys/collector/test/shensan/healthCheck.sh ===

#=============== Variable Begin ==================

jarName=xxx.jar

#=============== Variable End ==================

#=============== Method Begin ==================

getRunningJarCount(){
	runningJarCount=$(ps -ef | grep java | grep -v grep | grep -w $jarName | wc -l)
	echo $runningJarCount
}

#=============== Method End ==================


if [ $(getRunningJarCount) -eq 0 ];then
    nohup $JAVA_HOME/bin/java -jar $jarName.jar &
fi

 
