#!/bin/sh

logSysProgramRootDir=""

serviceCenterProgramDir="$logSysProgramRootDir/log-service-center"

registryProgramDir="$serviceCenterProgramDir/log-service-center-registry"
configProgramDir="$serviceCenterProgramDir/log-service-center-config"
gatewayProgramDir="$serviceCenterProgramDir/log-service-center-gateway"

serviceProgramDir="$logSysProgramRootDir/log-service"

collectorProgramDir="$serviceProgramDir/log-service-collector"
collectorServiceProgramDir="$serviceProgramDir/log-service-collector-service"
tracerProgramDir="$serviceProgramDir/log-service-tracer"

mvn clean install

mkdir $logSysProgramRootDir #创建日志系统根目录

mkdir $serviceCenterProgramDir #创建基础服务目录(基础服务有:[注册中心,配置中心,网关])
mkdir $registryProgramDir
mkdir $configProgramDir
mkdir $gatewayProgramDir

mkdir $serviceProgramDir #创建日志系统服务目录(目前的服务有:[tracer,collector,collector-service])
mkdir $collectorProgramDir
mkdir $collectorServiceProgramDir
mkdir $tracerProgramDir

cp -r $registryProgramDir/application.yml $registryProgramDir/log-service-center-registry.jar $registryProgramDir/lib/ $logSysProgramRootDir/log-service-center