#!/usr/bin/env bash

#================== Variable Begin ==================

jarName=$(ls | grep ".jar")

#================== Variable End ==================


#================== Method Begin ==================

getRunningJarCount(){
        pnameList=$(ps -ef | grep -w $jarName | grep -v grep | awk '{print $10}')
        runningJarCount=0
        for pname in ${pnameList[*]}
        do
                if [ $pname == $jarName ];then
                        let runningJarCount++
                fi
        done
        echo $runningJarCount
}

getPid(){
        pidList=$(ps -ef | grep -w java | grep -v grep | awk '{print $2}')
        pnameList=$(ps -ef | grep -w java | grep -v grep | awk '{print $10}')
        index=0
        for pname in ${pnameList[*]}
        do
                let index++;
                if [ $pname == $jarName ];then
                        echo $pidList | awk '{print $'$index'}'
                fi
        done
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
                kill -9 $(getPid)
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
