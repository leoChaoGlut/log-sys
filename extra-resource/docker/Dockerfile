FROM index.tenxcloud.com/tenxcloud/ubuntu

RUN mkdir -p /usr/env/jdk/1.7

COPY jdk1.7/ /usr/env/jdk/1.7/
COPY run.sh /
COPY program.jar /

ENV JAVA_HOME /usr/env/jdk/1.7
ENV PATH $PATH:$JAVA_HOME/bin
ENV CLASSPATH .:$CLASSPATH:$JAVA_HOME/lib:$JAVA_HOME/jre/lib
ENV ROOT_PASS 123

ENTRYPOINT /bin/bash /run.sh