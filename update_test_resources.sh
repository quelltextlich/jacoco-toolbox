#!/bin/bash

set -e
set -o pipefail

cd "$(dirname "$0" )"

SCRIPT_DIR_ABS="$PWD"

TEST_RESOURCES_DIR_ABS="$SCRIPT_DIR_ABS/toolbox/src/test/resources"

copy_for_aspect() {
    local ASPECT="$1"
    ASPECT_TARGET_DIR_ABS="$SCRIPT_DIR_ABS/test-data-$ASPECT/target"
    cp -v "$ASPECT_TARGET_DIR_ABS/jacoco.exec" "$TEST_RESOURCES_DIR_ABS/jacoco-$ASPECT.exec"
    cp -v "$ASPECT_TARGET_DIR_ABS/TestDataGroup${ASPECT^}-"*".jar" "$TEST_RESOURCES_DIR_ABS/TestDataGroup${ASPECT^}.jar"
}

for ASPECT in merged foo bar
do
    pushd "test-data-$ASPECT" >/dev/null

    mvn clean package

    copy_for_aspect "$ASPECT"

    popd >/dev/null
done