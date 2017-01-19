#!/usr/bin/env bash

. /etc/profile

echo $JAVA_HOME > /data/leo.txt

#=============== Variable Begin ==================

logSysPath=/data/log-sys

groupNameArr=(
"serviceCenter"
"serviceCenter"
"service"
"service"
"service"
)

serviceNameArr=(
"registry"
"gateway"
"api"
"searcherGateway"
"searcherShensanTest"
)

serviceNameArrLength=${#serviceNameArr[*]}

index=0

#=============== Variable End ==================


#=============== Method Begin ==================

getRunningJarCount(){
	runningJarCount=$(ps -ef | grep java | grep -v grep | grep -w $serviceName | wc -l)
	echo $runningJarCount
}

#=============== Method End ==================



while [ $index -lt $serviceNameArrLength ]
do
	serviceName=${serviceNameArr[$index]}
	if [ $(getRunningJarCount) -eq 0 ];then
		cd $logSysPath/${groupNameArr[$index]}/$serviceName
		nohup $JAVA_HOME/bin/java -jar $serviceName.jar & 
	fi
	let index++
done
 
