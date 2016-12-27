#!/usr/bin/env bash
source /etc/profile
#================Var Begin====================
serviceGroup=$1
serviceName=$2
serviceRootDir="/data/logSys"
serviceDir="$serviceRootDir/$serviceGroup/$serviceName/"
jarName="$serviceName.jar"
mainClass="cn.yunyichina.log.$serviceGroup.$serviceName.Application"
tag="log-$serviceGroup-$serviceName"
libDir="$serviceDir/lib"
#================Var End====================


#================Function Begin====================
start(){
    echo $serviceDir
    cd $serviceDir
    runningJarCount=$(ps -ef | grep java | grep -w $tag | wc -l)
    if [ $runningJarCount -gt 0 ]; then
        echo "$logOutputFormat $tag is running."
    else
        java -Dir=$tag $mainClass &
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

for lib in $libDir/*.jar
do
    libs=$lib:$libs
done
CLASSPATH=.:
echo CLASSPATH
export CLASSPATH


echo $1 $2 $3
#==================Entrance Begin===============
case "$3" in
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
    echo "Usage: $0 {start|stop|restart} "
    exit 2
esac
#==================Entrance End===============