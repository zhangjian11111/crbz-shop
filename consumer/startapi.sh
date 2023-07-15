#!/bin/sh



java -Xmx1024m -Xms1024m -Xss256k -jar -Duser.timezone=GMT+08 /data/crbz/consumer-4.2.5.jar> logs/consumer.out  & \

# 死循环，保持docker前台运行
while [[ true ]]; do
    sleep 1
done
