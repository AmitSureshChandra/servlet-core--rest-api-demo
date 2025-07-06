rm -rf ~/tomcat/webapps/webapp* || "prev webapp.war not exists"
echo "previous war removed"
cp ~/IdeaProjects/webapp/build/libs/webapp.war ~/tomcat/webapps
echo "war deployed"