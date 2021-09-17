#!/bin/bash

VERSION="1.0-SNAPSHOT"
SERVER_NAME="冒险岛079 v"${VERSION}
info="我们将启动【${SERVER_NAME}】服务端，请稍候..."
echo ${info}

cd `dirname $0`
HOME=`pwd`

export SERVER_PID=$HOME/ms079.pid
export SERVER_LOG_PATH=$HOME/logs
export SERVER_CLASS=com.github.mrzhqiang.maplestory.MapleStoryApplication

if [[ ! -d "$SERVER_LOG_PATH" ]]
then
  sudo mkdir -p $SERVER_LOG_PATH
fi

if test -z "$SERVER_JAVA_OPTS"
then
  # -Xmx512M -XX:+UseG1GC
  export SERVER_JAVA_OPTS="-server -Dwzpath=wz -Xloggc:$SERVER_LOG_PATH/ms079-gc.log"
fi

if [[ -f "${SERVER_PID}" ]]; then
    pid=$(cat ${SERVER_PID})
    if kill -0 ${pid} >/dev/null 2>&1; then
      echo "【${SERVER_NAME}】服务端已经在运行。"
      exit 1
    fi
fi

nohup java $SERVER_JAVA_OPTS -cp $HOME/*:$HOME/lib/* $SERVER_CLASS 2>&1 > $SERVER_LOG_PATH/ms079.out &
pid=$!
if [[ -z "${pid}" ]]; then
    echo "【${SERVER_NAME}】服务端启动失败！"
    exit 1
else
    echo "【${SERVER_NAME}】服务端启动成功！"
    echo $pid > $SERVER_PID
    sleep 1
fi
