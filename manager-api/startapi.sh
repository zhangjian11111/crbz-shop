#!/bin/sh



java -Xmx512m -Xms256m -Xss256k -jar -Duser.timezone=GMT+08 /data/crbz/manager-api-4.2.5.jar> logs/manager.out  & \

# 死循环，保持docker前台运行
while [[ true ]]; do
    sleep 1
done
