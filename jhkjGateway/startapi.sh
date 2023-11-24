#!/bin/sh


java -Dreactor.netty.pool.leasingStrategy=lifo -Xmx256m -Xms128m -Xss256k -jar -Duser.timezone=GMT+08 /data/crbz/jhkjGateway-4.2.5.jar> logs/jhkjGateway.out  & \
# 死循环，保持docker前台运行
while [[ true ]]; do
    sleep 1
done
