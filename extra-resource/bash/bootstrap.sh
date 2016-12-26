#!/usr/bin/env bash

#Author: Leo
#Blog: http://blog.csdn.net/lc0817
#CreateTime: 2016/12/10 15:07
#Description:

source /etc/profile

#================Var Begin====================
serviceGroup=$1
serviceName=$2
serviceRootDir="/data/logSystem"
serviceDir="$serviceRootDir/$serviceGroup/$serviceName/"
jarName="$serviceName.jar"
mainClass="cn.yunyichina.log.$serviceGroup.$serviceName.Application"
tag="log-$serviceGroup-$serviceName"
commonLibDir="$serviceRootDir/commonLib"
consoleOutput="$serviceDir/console.out"
logOutputFormat=$(date +%b" "%d" "%H:%M:%S" "`hostname`)
jvmParam="-Xmx2048M -XX:PermSize=512M -XX:MaxPermSize=512M"
#================Var End====================


#================Function Begin====================
start(){
    cd $serviceDir
    runningJarCount=$(ps -ef | grep java | grep -w $tag | wc -l)
    if [ $runningJarCount -gt 0 ]; then
        echo "$logOutputFormat $tag is running."
    else
        cp /dev/null $consoleOutput
        (nohup $JAVA_HOME/bin/java $jvmParam -Dir=$tag $mainClass &) &>$consoleOutput
        echo "$logOutputFormat Ready to start $tag, if u wanna see the bootstrap process of $tag, please tail the console.out."
    fi
}

stop(){
    echo "$logOutputFormat Ready to stop $tag."
    runningJarCount=$(ps -ef | grep java | grep -w $tag | wc -l)
    if [ $runningJarCount -gt 0 ]; then
        ps -ef|grep java|grep -w $tag|grep -v grep|awk '{print $2}' |xargs -n1 kill -9
    fi
    echo "$logOutputFormat $tag was stopped."
}
#================Function End====================


if [ ! -f $serviceDir/$jarName ]; then
    echo "Cannot find $serviceDir/$jarName ."
    exit
fi

for lib in $commonLibDir/*.jar
do
    libs=$lib:$libs
done
CLASSPATH=$libs$serviceDir/$jarName
export CLASSPATH

#==================Entrance Begin===============
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        sleep 5
        start
        ;;
    *)
    echo "Usage: $0 {start|stop|restart}"
    exit 2
esac
#==================Entrance End===============