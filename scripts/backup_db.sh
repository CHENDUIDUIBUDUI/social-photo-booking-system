#!/bin/bash

# 数据库备份脚本

# 备份目录
BACKUP_DIR="$(dirname "$0")/../database/backup"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 备份文件名
BACKUP_FILE="$BACKUP_DIR/photo_booking_$(date +%Y%m%d_%H%M%S).sql"

# 执行备份
docker-compose exec mysql mysqldump -u root -proot photo_booking > "$BACKUP_FILE"

# 检查备份是否成功
if [ $? -ne 0 ]; then
    echo "数据库备份失败！"
    exit 1
fi

echo "数据库备份成功！备份文件: $BACKUP_FILE"

# 删除7天前的备份文件
find "$BACKUP_DIR" -name "photo_booking_*.sql" -mtime +7 -delete

echo "已删除7天前的备份文件。"
