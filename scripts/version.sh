#!/usr/bin/env bash

echo '修改项目版本号'
cd ../codes/
mvn versions:set -DnewVersion=1.0.1
cd ../scripts
