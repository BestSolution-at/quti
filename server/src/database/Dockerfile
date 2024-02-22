FROM docker.io/library/alpine:3

LABEL org.opencontainers.image.title="Qutime - database setup"
LABEL org.opencontainers.image.description="Utility for initializing and preparing the database for Qutime"
LABEL org.opencontainers.image.authors="BestSolution.at"
LABEL org.opencontainers.image.vendor="BestSolution.at"
LABEL org.opencontainers.image.url="https://gitlab.bestsolution.at/BestSolution.at/qutime"
LABEL org.opencontainers.image.source="https://gitlab.bestsolution.at/BestSolution.at/qutime/-/tree/main/server/src/database"

RUN adduser -u 1000 -h /opt/qutime/ -G root -D dbinit
WORKDIR /opt/qutime/

ARG POSTGRES_VERSION=16
RUN apk add --no-cache postgresql${POSTGRES_VERSION}-client
USER dbinit

ENV SETUP_SQL_DATA_DIR=/opt/qutime/sql/

COPY setup.sh .
COPY sql/ ${SETUP_SQL_DATA_DIR}
COPY sql-generated/postgresql16.sql ${SETUP_SQL_DATA_DIR}/structure.sql

CMD [ "/opt/qutime/setup.sh" ]
