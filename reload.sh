#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"


ant war -Dmy_source=$DIR -Dtomcat_home=/usr/local/Cellar/tomcat6/6.0.39/libexec


/usr/local/Cellar/tomcat6/6.0.39/libexec/bin/shutdown.sh
sleep 3
/usr/local/Cellar/tomcat6/6.0.39/libexec/bin/startup.sh
