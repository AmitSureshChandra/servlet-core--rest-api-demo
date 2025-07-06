rm -rf ~/tomcat/webapps/webapp*
echo "previous war removed"
cp ~/IdeaProjects/webapp/build/libs/webapp.war ~/tomcat/webapps
echo "war deployed"