#!/bin/sh

export GRAALVM_HOME=/Users/tomschindl/SDKs/java/graalvm-community-openjdk-21.0.2+13.1/Contents/Home
export JAVA_HOME=${GRAALVM_HOME}
export PATH=${GRAALVM_HOME}/bin:$PATH

./mvnw install -Dnative
