#!/usr/bin/env bash

#================== Variable Begin ==================

jarName=registry.jar

#================== Variable End ==================


#================== Method Begin ==================

getRunningJarCount(){
        runningJarCount=$(ps -ef | grep java | grep -v grep | grep -w $jarName | wc -l)
        echo $runningJarCount
}

startJar(){
        if [ $(getRunningJarCount) -eq 0 ];then
                nohup java -jar $jarName &
                echo "Starting $jarName"
        else
                echo "$jarName is running now"
        fi
}

stopJar(){
        if [ $(getRunningJarCount) -eq 0 ];then
                echo "$jarName is not running"
        else
                ps -ef | grep java | grep -v grep | grep -w $jarName | awk '{print $2}' | xargs kill -9
                while [ 1 -eq 1 ]
                do
                        if [ $(getRunningJarCount) -eq 0 ];then
                                break
                        fi
                done
                echo "$jarName has been shutdown"
        fi

}

#================== Method End ==================

#================== Entrance Begin ==================

case "$1" in
        start)
                startJar
                ;;
        stop)
                stopJar
                ;;
        restart)
                stopJar
                startJar
                ;;
        *)
        exit 2
esac

#================== Entrance End ==================
