#!/bin/bash

# 启动入口类，该脚本文件用于别的项目时要改这里
MAIN_CLASS=com.kakarote.crm9.Application

COMMAND="$1"

if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]] && [[ "$COMMAND" != "debug" ]]; then
	echo "Usage: $0 start | stop | restart | debug"
	exit 0
fi


# Java 命令行参数，根据需要开启下面的配置，改成自己需要的，注意等号前后不能有空格
# JAVA_OPTS="-Xms256m -Xmx1024m -Dundertow.port=80 -Dundertow.host=0.0.0.0"
# JAVA_OPTS="-Dundertow.port=80 -Dundertow.host=0.0.0.0"
JAVA_OPTS_DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=8240"
# 生成 class path 值

APP_BASE_PATH=$(cd `dirname $0`; pwd)

CP=${APP_BASE_PATH}/config:${APP_BASE_PATH}/lib/*

function debug()
{
    nohup java -Xverify:none ${JAVA_OPTS_DEBUG} -cp ${CP} ${MAIN_CLASS} > ./output.log &
}

function start()
{
    nohup java -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS} > ./output.log &
}

function stop()
{
    kill `pgrep -f ${APP_BASE_PATH}` 2>/dev/null
}

if [[ "$COMMAND" == "start" ]]; then
	start
elif [[ "$COMMAND" == "stop" ]]; then
    stop
elif [[ "$COMMAND" == "debug" ]]; then
    debug
else
    stop
    start
fi
