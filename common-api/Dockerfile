FROM openjdk:8u342-oraclelinux8
MAINTAINER ZHANGJIAN <1097951193@qq.com>
ENV APP_VERSION=4.2.5
ENV APP_HOME=/data/crbz
ENV APP_NAME=CRBZ-shop
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir -p ${APP_HOME}/logs
RUN mkdir -p ${APP_HOME}/cert/wechatcert/xbm
WORKDIR ${APP_HOME}
COPY *.jar ${APP_HOME}/
COPY startapi.sh ${APP_HOME}/startapi.sh
RUN chmod 777 ${APP_HOME}/*
RUN chmod 777 /data/crbz/logs
EXPOSE 8891 9113
ENTRYPOINT ["sh","startapi.sh"]

