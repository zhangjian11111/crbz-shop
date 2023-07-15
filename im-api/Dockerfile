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
COPY ./docker-im-api/im-api-4.2.5.jar ${APP_HOME}/
COPY ./docker-im-api/startapi.sh ${APP_HOME}/startapi.sh
RUN chmod 777 ${APP_HOME}/*
RUN chmod 777 /data/crbz/logs
EXPOSE 9115
ENTRYPOINT ["sh","startapi.sh"]

