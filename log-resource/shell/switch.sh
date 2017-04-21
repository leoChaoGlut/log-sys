#!/usr/bin/env bash

#================== Variable Begin ==================

jar=$(pwd)/$(ls | grep ".jar$")

#================== Variable End ==================


#================== Method Begin ==================

getRunningJarCount(){
        pnameList=$(ps -ef | grep -w $jar | grep -v grep | awk '{print $NF}')
        runningJarCount=0
        for pname in ${pnameList[*]}
        do
                if [ $pname == $jar ];then
                        let runningJarCount++
                fi
        done
        echo $runningJarCount
}

getPid(){
        pidList=$(ps -ef | grep -w java | grep -v grep | awk '{print $2}')
        pnameList=$(ps -ef | grep -w java | grep -v grep | awk '{print $NF}')
        index=0
        for pname in ${pnameList[*]}
        do
                let index++;
                if [ $pname == $jar ];then
                        echo $pidList | awk '{print $'$index'}'
                fi
        done
}

startJar(){
        if [ $(getRunningJarCount) -eq 0 ];then
#                nohup java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=12345 -jar $jar &
                nohup java -XX:+HeapDumpOnOutOfMemoryError -jar $jar &
                echo "Starting $jar"
        else
                echo "$jar is running now"
        fi
}

stopJar(){
        if [ $(getRunningJarCount) -eq 0 ];then
                echo "$jar is not running"
        else
                kill -9 $(getPid)
                while [ 1 -eq 1 ]
                do
                        if [ $(getRunningJarCount) -eq 0 ];then
                                break
                        fi
                done
                echo "$jar has been shutdown"
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
