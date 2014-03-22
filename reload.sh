#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"


ant war -Dmy_source=$DIR -Dtomcat_home=/usr/local/Cellar/tomcat6/6.0.39/libexec
RETVAL=$?

if [ $RETVAL -eq 0 ]; then
  /usr/local/Cellar/tomcat6/6.0.39/libexec/bin/shutdown.sh
  sleep 3
  /usr/local/Cellar/tomcat6/6.0.39/libexec/bin/startup.sh
else
  echo "Project compilation failed. Not proceeding with tomcat restart"
fi
