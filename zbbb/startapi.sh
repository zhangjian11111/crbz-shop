#!/bin/sh



java -Xmx256m -Xms128m -Xss256k -jar -Duser.timezone=GMT+08 /data/crbz/zbbb-4.2.5.jar> logs/zbbb.out  & \

# 死循环，保持docker前台运行
while [[ true ]]; do
    sleep 1
done
