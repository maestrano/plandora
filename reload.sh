#!/bin/bash
ant war -Dmy_source=/Users/arnaudlachaume/Sites/apps-dev/2_MNO_INTEGRATING/plandora/ -Dtomcat_home=/usr/local/Cellar/tomcat6/6.0.39/libexec

cp -p context.xml /usr/local/Cellar/tomcat6/6.0.39/libexec/webapps/pandora/META-INF/

/usr/local/Cellar/tomcat6/6.0.39/libexec/bin/shutdown.sh
sleep 3
/usr/local/Cellar/tomcat6/6.0.39/libexec/bin/startup.sh
