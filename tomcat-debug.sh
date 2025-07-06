#!/bin/bash
export CATALINA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
~/tomcat/bin/catalina.sh run