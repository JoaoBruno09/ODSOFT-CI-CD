FROM tomcat:9-jre17

EXPOSE 8080

COPY build/libs/ROOT.war /usr/local/tomcat/webapps/

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
