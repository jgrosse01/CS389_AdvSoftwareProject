FROM alpine:3.14
ENV GOSU_VERSION 1.16
RUN set -eux; \
	\
	apk add --no-cache --virtual .gosu-deps \
		ca-certificates \
		dpkg \
		gnupg \
	; \
	\
	dpkgArch="$(dpkg --print-architecture | awk -F- '{ print $NF }')"; \
	wget -O /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch"; \
	wget -O /usr/local/bin/gosu.asc "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch.asc"; \
	\
# clean up fetch dependencies
	apk del --no-network .gosu-deps; \
	\
	chmod +x /usr/local/bin/gosu; \
# verify that the binary works
	gosu --version; \
	gosu nobody true; \
    \
    apk add --no-cache git; \
    \
    mkdir /usr/docker; \
    mkdir /usr/docker/application; \
    git clone https://github.com/jgrosse01/CS389_AdvSoftwareProject /usr/docker/application; \
    \
    apk add --update iputils;

COPY docker-entrypoint.sh /usr/docker/docker-entrypoint.sh
RUN chmod +x /usr/docker/docker-entrypoint.sh
ENTRYPOINT ["/usr/docker/docker-entrypoint.sh"]