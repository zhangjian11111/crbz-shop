#!/bin/sh



java -Xmx1024m -Xms1024m -Xss256k -jar -Duser.timezone=GMT+08 /data/crbz/buyer-api-4.2.5.jar> logs/buyer.out  & \

# 死循环，保持docker前台运行
while [[ true ]]; do
    sleep 1
done
