#!/bin/bash

# 启动脚本

echo "启动约拍平台项目..."

# 进入项目根目录
cd "$(dirname "$0")/.."

# 启动服务
docker-compose up -d

# 检查服务是否启动成功
if [ $? -ne 0 ]; then
    echo "服务启动失败！"
    exit 1
fi

echo "服务启动成功！"
echo "访问地址: http://localhost"
echo "后端API地址: http://localhost/api"
