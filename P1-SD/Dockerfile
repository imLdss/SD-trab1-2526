FROM nunopreguica/sd2526tpbase

# working directory inside docker image
WORKDIR /home/sd

ADD hibernate.cfg.xml .
ADD messages.props .

# copy the shaded project jar to the docker image
COPY target/sd2526-tp1-1.jar sd2526.jar
COPY target/classes/ target-classes/

RUN /opt/java/openjdk/bin/jar uf sd2526.jar -C target-classes . && \
	rm -rf target-classes
