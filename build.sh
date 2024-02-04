#!/bin/bash
export JAVA_HOME="/Users/A-3133/.sdkman/candidates/java/17.0.7-tem"
export JAVA_OPTS="-Xms512m -Xmx512m"
exec ./gradlew "$@"
