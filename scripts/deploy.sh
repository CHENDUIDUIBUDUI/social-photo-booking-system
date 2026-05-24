#!/bin/bash

# 部署脚本

echo "开始部署约拍平台项目..."

# 进入项目根目录
cd "$(dirname "$0")/.."

# 构建后端服务
echo "构建后端服务..."
cd social-photo-booking-backend
mvn clean package -DskipTests

# 检查构建是否成功
if [ $? -ne 0 ]; then
    echo "后端服务构建失败！"
    exit 1
fi

# 回到项目根目录
cd ..

# 启动服务
echo "启动服务..."
docker-compose up -d

# 检查服务是否启动成功
if [ $? -ne 0 ]; then
    echo "服务启动失败！"
    exit 1
fi

echo "部署成功！服务已启动。"
echo "访问地址: http://localhost"
echo "后端API地址: http://localhost/api"
