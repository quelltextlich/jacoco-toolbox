#!/bin/bash

set -e
set -o pipefail

TOOLBOX_DIR_ABS="$(dirname "$0" )"

JAVA="$(which java 2>/dev/null || true)"
MAVEN="$(which mvn 2>/dev/null || true)"

find_toolbox_jar() {
    TOOLBOX_JAR_FILE_ABS="$( \
        find \
            "$TOOLBOX_DIR_ABS/toolbox/target" \
            -maxdepth 1 \
            -name "jacoco-toolbox-*.jar" \
            2>/dev/null \
        | head -n 1 \
        || true
        )"
    if test -e "$TOOLBOX_JAR_FILE_ABS"
    then
        return 0
    else
        return 1
    fi
}

build_toolbox_jar() {
    if [ -x "$MAVEN" ]
    then
        BUILD_LOG_FILE_ABS="$TOOLBOX_DIR_ABS/build.log"
        log "Starting to build the jar ..."
        log "(Writing build logs to $BUILD_LOG_FILE_ABS)"
        pushd "$TOOLBOX_DIR_ABS" >/dev/null

        mvn clean package &>"$BUILD_LOG_FILE_ABS"

        popd >/dev/null
        log "Building was successful."
        log
    else
        error "Cannot find Maven to start build the toolbox \
jar. Please either build the toolbox jar on your own, or install \
Maven, and rerun this command."
    fi
}

log() {
    echo "$@" >&2
}

error() {
    log "Error :" "$@"
    exit 1
}

if [ ! -x "$JAVA" ]
then
    error "Could not find runnable Java version. Please install Java."
fi

if ! find_toolbox_jar
then
    log "Could not find toolbox jar."
    build_toolbox_jar
fi

if ! find_toolbox_jar
then
    error "Failed to build the toolbox jar file"
fi

exec "$JAVA" -jar "$TOOLBOX_JAR_FILE_ABS" "$@"