# STW-P6

## Execution environment

- Java JDK 8
- Tomcat 4.1.40
- Axis webapp running in Tomcat
- Axis libs in CLASSPATH

## Building

1. Use `gradle build` to compile java client and SOAP services
2. Copy service classes from `build/classes/main`, package `org`
3. Paste `org` at `%CATALINA_HOME%/webapps/axis/WEB-INF/classes`
4. Use `gradle war` to create the war artifact
5. Copy libs from `build/libs/<app>.war`
6. Paste them at `%CATALINA_HOME%/webapps/axis/WEB-INF/classes/libs`
7. Reload Axis application

## Deploying

1. Add AXIS libs to CLASSPATH variable
2. Run `java org.apache.axis.client.AdminClient deploy.wsdd`
3. Check that service is running with AXIS list

## Execution

[OPT] 
0. Check if service is alive by running [Alive app](src/main/java/client/Alive.java)
[/OPT]

1. Run [ServiceConsumer](src/main/java/client/ServiceConsumer.java), the GUI will appear
2. Ignore Lorem Ipsum
3. Select town and hit HTML or JSON
4. It will appear on the editor pane

## Why tomcat 4?

Because older tomcat versions have conflicts with tools.jar library, working with the same environment AXIS was prepare for (2K  initial years) solved it.
Also Java JDK 4 and Windows XP were used as develop environment. Later changed to Java JDK 8 as it didn't cause any trouble.
