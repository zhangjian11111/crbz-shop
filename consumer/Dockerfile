FROM adoptopenjdk/openjdk8-openj9:alpine-slim
MAINTAINER ZHANGJIAN <1097951193@qq.com>
ENV APP_VERSION=4.2.5
ENV APP_HOME=/data/crbz
ENV APP_NAME=CRBZ-shop
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir -p ${APP_HOME}/logs
RUN mkdir -p ${APP_HOME}/cert/wechatcert/xbm
WORKDIR ${APP_HOME}
COPY ./docker-consumer/consumer-4.2.5.jar ${APP_HOME}/
COPY ./docker-consumer/startapi.sh ${APP_HOME}/startapi.sh
RUN chmod -R 777 ${APP_HOME}
EXPOSE 8891 9114
ENTRYPOINT ["sh","startapi.sh"]

