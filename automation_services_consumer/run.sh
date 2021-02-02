#!/bin/sh

mvn clean
git pull https://ridwan:M1r34cl3@gitlab.teknologi-nusantara.com/bank-automation/withdraw/automation.git
mvn package -Dmaven.test.skip=true
docker build -t automation .