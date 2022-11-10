# ODSOFT REPORT

This is the repository of g202, and the members are:

- Gonçalo Pinho - 1220257

- João Rocha - 1220256

- Nuno Leite - 1220271

- Tiago Lacerda - 1220285

## Pipeline Design

Initialy we started to design the pipelines (Sequential and parallel).

**Sequential pipeline**

![Alt text](pipeline-diagrams/sequential_pipeline.png?raw=true "Sequencial pipeline diagram")

Sequential pipeline, as the name says, executes the stages in a sequence.

**Parallel pipeline**

![Alt text](pipeline-diagrams/parallel_pipeline.png?raw=true "Sequencial pipeline diagram")

Parallel pipeline, can execute more than one task at the same time so that we can use hardware more efficiently and save time (or not).

## Group Tasks:

- [x] **1º Task - Repository Checkout:**

For that we used the snippet generator and picked the sample step "checkout" where we indicate the repository and branch and we can generate the code snippet that will checkout the repository.

Created the pipeline and created the stage checkout.

```groovy

node{

stage('checkout'){

checkout([$class:  'GitSCM',  branches:  [[name:  '*/master']],  extensions:  [],  userRemoteConfigs:  [[url:  'git@bitbucket.org:mei-isep/odsoft-22-23-ncf-g202.git']]])

}

}



```

This step, when executed for the first time, will fetch all the data from the repository. The next times will only fetch the updates.

- [x] **2º Task - Build and publish the deployment file**

On this task we've created the "build" stage where we built the application in **production mode** and generated a .war/.jar file.

For that we used the [vaadin documentation](https://vaadin.com/docs/latest/overview), and to build in production mode we need to enable it in the gradle.build file adding the following tag into the vaadin configuration:

```groovy

productionMode =  true

```

If you want to generate a war file you need to import the war plugin by adding it on the build.gradle plugins task.

```groovy

id 'war'

```

After that we can finally build the app with the following command:

`./gradlew clean build "-Pvaadin.productionMode" war`

_Yes, leave the quotation marks. Vaadin documentation have a little mistake that might take us some hours to figure it out._

Note: if you want a .war artifacts leave the `war` tag.If you prefer a .jar artifacts remove it.

After it builds, on the `build/libs` directory you can find the artifacts generated by the build.

- [x] **3º Task - Generate and publish javadoc**

To complete this last team goal we followed the gradle documentation [about javadoc](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.javadoc.Javadoc.html) and created a new task to generate the javadoc on the build.gradle file.

```groovy

javadoc {

source = sourceSets.main.allJava

destinationDir = reporting.file("javadoc")

}

```

After that we ran `./gradlew javadoc` it will generate the javadoc on the following directory `build/reports/javadoc`.

To publish it, we need to install [HTML Publisher](https://plugins.jenkins.io/htmlpublisher/) that is a Jenkins plugin that allow us to publish HTML files on the pipeline.

After installing it, with snippet generator we selected "publishHTML" sample step and we had to provide the directory, the index page and the report title. With that, we generated the code snippet and implemented it on our Jenkinsfile resulting on the following stage:

```groovy

stage('javadoc'){

if (isUnix()){

sh './gradlew javadoc'

}else{

bat './gradlew javadoc'

}

publishHTML([allowMissing:  false,  alwaysLinkToLastBuild:  false,  keepAll:  false,  reportDir:  'build/reports/javadoc/',  reportFiles:  'index.html',  reportName:  'Javadoc',  reportTitles:  '',  useWrapperFileDirectly:  true])

}

```

After running the stage successfully on the pipeline, on the left side tab we can see a new option with the report name.

![Side tab](https://i.imgur.com/NPkp29U.png)

If we click on it we get redirected to the report!

# Individual Goals

## Integration Tests

Integration test is a type of software testing in which the different units, modules or components of a software application are tested as a combined entity.

- [x] **Integration Tests Execution**

For this stage, we created another integration test to add to the project:

```java

@Test

public  void  formShownWhenContactDeselected()  {

Grid<Contact> grid = listView.grid;

Contact emptyContact =  new  Contact();

ContactForm form = listView.form;

form.setVisible(true);

assertTrue(form.isVisible());

grid.asSingleSelect().setValue(emptyContact);

form.setVisible(false);

assertFalse(form.isVisible());

assertEquals(emptyContact.getFirstName(), form.firstName.getValue());

}

```

This test is meant to verify if the form stays invisible after pressing the button. The test starts by initializing an empty contact, after that opens a contact form and verify if it is visible. If so, the value of the empty contact is set and the form isn't visible anymore. Last, there is a comparation between the first name of the empty contact and the first name present in the form.

- [x] **Integration Tests HTML Report Generation**

To generate the HTML test report, we need to create a task of type `Test` and we named it `integrationTest`. For this, we used the [JUnit framework](https://junit.org/junit5/), and we specify that we require a HTML report, which will be generated in the directory `/build/htmlReports/junitReports/integration`. We also applied a filter that only executes tests that contains "IT" in the name.

```java

task integrationTest(type: Test)  {

useJUnitPlatform()

reports {

reports.html.required =  true

reports.html.destination =  file("/build/htmlReports/junitReports/integration")

}

filter{

includeTestsMatching "*IT"

}

}

```

- [x] **Integration Tests HTML Report Publishing**

To publish the Integration Tests HTML Report, we only need to add this stage to the JenkinsFile:

```groovy

stage('integrationReport'){

if (isUnix()) {

sh './gradlew integrationTest'

} else {

bat './gradlew integrationTest'

}

publishHTML([allowMissing:  false,  alwaysLinkToLastBuild:  false,  keepAll:  false,  reportDir:  'build/htmlReports/junitReports/integration',  reportFiles:  'index.html',  reportName:  'IntegrationTests Report',  reportTitles:  '',  useWrapperFileDirectly:  true])

}

```

This command allows Jenkins to find the report in the mentioned directory `build/htmlReports/junitReports/integration` and look for the `index.html`. Then, Jenkins generate a report named IntegrationTests Report.

- [x] **Integration Tests HTML Coverage Report Generation**

To generate the HTML coverage report, we had to create a new task named `jacocoIntegrationReport` of type `org.gradle.testing.jacoco.tasks.JacocoReport`.

First, we set the directory to get the execution data of integration tests, generated by default by the `integrationTest` task.

```groovy

task jacocoIntegrationReport(type:  org.gradle.testing.jacoco.tasks.JacocoReport) {

sourceSets sourceSets.main

getExecutionData().setFrom("/build/jacoco/integrationTest.exec")

reports {

html.enabled =  true

xml.enabled =  false

csv.enabled =  false

}

}

```

- [x] **Integration Tests HTML Coverage Report Publishing**

To publish the Integration Tests HTML Report, we only need to add this stage to the JenkinsFile:

```groovy

stage('integrationReportCoverage'){

if (isUnix()){

sh './gradlew jacocoIntegrationReport'

} else {

bat './gradlew jacocoIntegrationReport'

}

publishHTML([allowMissing:  false,  alwaysLinkToLastBuild:  false,  keepAll:  false,  reportDir:  'build/reports/jacoco/jacocoIntegrationReport/html',  reportFiles:  'index.html',  reportName:  'IntegrationTests Coverage Report',  reportTitles:  '',  useWrapperFileDirectly:  true])

}

```

This command allows Jenkins to find the report, generated by Jacoco, in the mentioned directory `build/reports/jacoco/jacocoIntegrationReport/html` and look for the `index.html`. Then, Jenkins generate a report named IntegrationTests Coverage Report.

## Unit Tests

Unit tests are the tests of each unit or an individual component of the software application.

- [x] **Unit Tests Execution**

For this stage, we created another unit test to add to the project:

```java
@Test

public  void  loginFormTesting()  {

	LoginForm  formtest  =  new  LoginForm();
	formtest.setAction("login");
	LoginEvent  form  =  new  LoginEvent(formtest,  false,  this.username,  this.password);
	assertEquals("user",  form.getUsername());
	assertEquals("userpass",  form.getPassword());

}

```

This unit test is responsible to verify if the Login Form is working correctly. First of all, creates a LoginForm and set its login action. Furthermore, was instantiated a LoginEvent with the username and password whose should be the corrects ones. Finally, the assertEquals confirmed that the username and password, passed in LoginEvent, and those expected values are equal.

- [x] **Unit Tests HTML Report Generation**

To generate the HTML test report, we need to create a task of type `Test` and we named it as `unitTest`. For this, we used the [JUnit framework](https://junit.org/junit5/), and we specify that we require a HTML report, which will be generated in the directory `/build/htmlReports/junitReports/unit`. We also applied a filter that only executes tests that contains "Test" in the name.

```java
task unitTest(type: Test) {
    useJUnitPlatform()
    reports {
        reports.html.required = true
        reports.html.destination = file("build/htmlReports/junitReports/unit")
    }
    filter{
        includeTestsMatching "*Test"
    }
}


```

- [x] **Unit Tests HTML Report Publishing**

To publish the Unit Tests HTML Report, we only need to add this stage to the JenkinsFile:

```groovy

    stage('unitReport'){
        echo 'Running Unit Tests...'
        if (isUnix()){
            sh './gradlew unitTest'
        }else{
            bat './gradlew unitTest'
        }
        echo 'Generating Unit Test HTML Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/junitReports/unit', reportFiles: 'index.html', reportName: 'UnitTests Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Unit Test HTML Report!'
    }

```

This command allows Jenkins to find the report in the mentioned directory `build/htmlReports/junitReports/unit` and look for the `index.html`. Then, Jenkins generate a report named UnitTests Report.

- [x] **Unit Tests HTML Coverage Report Generation**

To generate the HTML coverage report, we had to create a new task named `jacocoUnitReport` of type `org.gradle.testing.jacoco.tasks.JacocoReport`.

First, we set the directory to get the execution data of unit tests, generated by default by the `unitTest` task.

```groovy

task jacocoUnitReport(type: org.gradle.testing.jacoco.tasks.JacocoReport) {
	sourceSets sourceSets.main

    getExecutionData().setFrom("build/jacoco/unitTest.exec")

    reports {
        html.enabled = true
        xml.enabled = false
        csv.enabled = false
    }
}

```

- [x] **Unit Tests HTML Coverage Report Publishing**

To publish the Unit Tests HTML Report, we only need to add this stage to the JenkinsFile:

```groovy

stage('unitReportCoverage'){
    echo 'Generating Unit Test Coverage Report...'
    if (isUnix()){
        sh './gradlew jacocoUnitReport'
    }else{
        bat './gradlew jacocoUnitReport'
    }
    echo 'Generated Unit Test Coverage Report and started Publishing...'
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/jacoco/jacocoUnitReport/html', reportFiles: 'index.html', reportName: 'UnitTests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
    echo 'Published Unit Test Coverage Report!'
    }

```

This command allows Jenkins to find the report, generated by Jacoco, in the mentioned directory `build/reports/jacoco/jacocoUnitReport/html` and look for the `index.html`. Then, Jenkins generate a report named UnitTests Coverage Report.

## Mutation Tests

Mutation testing is a form of white box testing in which testers change specific components of an application's source code to ensure a software test suite will be able to detect the changes. Changes introduced to the software are intended to cause errors in the program.

In this work it was proposed that we run mutation tests to our project, to which we would have to 'Generate the Mutation Tests HTML Coverage Report' and 'Publish the Mutation Tests
HTML Coverage Report on Jenkins'.

- [x] **Mutation Tests Execution**

For this stage, we created and executed mutation test to add to the project our path where it will be our HTML report adding the following plugin to the 'plugins': 

id 'info.solidsoft.pitest' version '1.7.4'

In our task with the name of 'pitest' we need to contain the version of 'junit plugin' and a command called 'timestampedReports' for our file don't come with datatime format.

```groovy

pitest  {
	
	junit5PluginVersion = '0.15'
	timestampedReports = false

}

- [x] **Mutation Tests HTML Coverage Report Publishing**

To publish the Mutation Tests HTML Report, we only need to add this stage to the JenkinsFile:

```groovy

stage('mutationReportCoverage'){
        echo 'Generating Mutation Test Coverage Report...'
        if (isUnix()){
            sh './gradlew pitest'
        }else{
            bat './gradlew pitest'
        }
        echo 'Generated Mutation Test Coverage Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/pitest', reportFiles: 'index.html', reportName: 'Mutation Tests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Mutation Test Coverage Report!'    
}

```

This command allows Jenkins to find the report, in the mentioned directory `build/reports/pitest` and look for the `index.html`. Then, Jenkins generate a report named Mutation Tests Coverage Report.



## Pipeline Stage(5/5)
1220257 - Gonçalo Pinho was responsible for this pipeline stages.

 - [x] **1º Staging Deployment**
For this stage we thought about two possible options:
 - Simulate the production environment on a docker container;
 - Create a instance on a cloud service and prepare it like it is the production environment.

We picked the creation of the instance on a cloud service (Amazon Web Sevices), created a EC2 Instance with Ubuntu 22.04 and Apache Tomcat version 9.

 For this stage we based ourselves on the following tutorials:
 
 - To prepare the environment we followed [this guide](https://medium.com/@hasnat.saeed/install-tomcat-9-on-ubuntu-18-04-605ca963ffcc).
 - To deploy the .war file from Jenkins to the server we followed [this video tutorial](https://www.youtube.com/watch?v=YbaPlDpV184).

Summing up both of the guides, we installed:

 - [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
 - [Apache Tomcat Version](https://tomcat.apache.org)

The key point of make things possible to deploy the project from Jenkins to the EC2 instance was configuring AWS EC2 security group and creating a tomcat user that is able to deploy by script.

By default the Tomcat port is 8080, and we are keeping it but, its important to know it to configure the EC2 Instance security group.

After configuring the security group we can try to access the tomcat with your ec2 Public IPv4 DNS:8080, for example: ```http://ec2-3-85-104-7.compute-1.amazonaws.com:8080/```

After that you should create a tomcat user with ``admin-script`` ``manager-script`` ``admin-gui`` ``manager-script`` 

Your should edit the file on this directory ``tomcat/conf/tomcat-users.xml``.
And add this and edit the username and password for something of your choice:
```xml
<role rolename="admin-gui"/>
<role rolename="admin-script"/>
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="admin" password="123admin456" roles="admin-gui,manager-gui,admin-script,manager-script"/>
```

And now you can deploy manually or via script, on this case with Jenkins.

Before the deploy we recommend you to encrease the max upload file since the .war file that is generated by the project is bigger than the default value (50MB). For that you should go to the following directory: ``/tomcat/webapps/manager/WEB-INF/web.xml``

And change the max file size and request size for this:
```xml
<max-file-size>104857600</max-file-size>
<max-request-size>104857600</max-request-size>
```
After that we can jump to the Jenkins configuration of the credentials.

In there we want to store the tomcat username and password and the most important part is defining a id for a dynamic approach when using the jenkinsfile in multiple computers.

To deploy we used the plugin [Deploy to Container].(https://plugins.jenkins.io/deploy/)

One more time we use the jenkins pipeline syntax to generate your code, we select the sample step ``deploy:Deploy war/ear to container`` and in there we indicate:

 - The war file path;
 - The context path (this is the "/path" that you will put in front of your URL)
 - The container you are using (this case Tomcat 9)
 - The credentials (that we configured before)
 - The tomcat url (```http://ec2-3-85-104-7.compute-1.amazonaws.com:8080/```) 

And generate the snippet:
```groovy
deploy adapters: [tomcat9(credentialsId: "odsoft", path: "", url: "http://ec2-3-85-104-7.compute-1.amazonaws.com:8080/")], contextPath: "crm", war: "flowcrmtutorial-0.0.1-SNAPSHOT.war"
```

On our case the .war file is on the root of the project because we moved it with the task that we've created on ``build.gradle``file.
```groovy
tasks.register('copyArtifact', Copy) {
	from layout.buildDirectory.file("libs/flowcrmtutorial-0.0.1-SNAPSHOT.war")
	into layout.buildDirectory.dir("../")
}
```

Well, at this point we had the everything ready, ran the job in jenkins and then occurred an error deploying the application to the Tomcat.

Then we reached to the conclusion that to deploy the .war file we had to do some more things, so we searched in springboot docs and found [this documentation](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/howto-traditional-deployment.html) about deploying.

 Long story short, we had to extend our Application with the ``SpringBootServletInitializer`` abstract classs and override the following method:
```java
@Override
protected  SpringApplicationBuilder  configure(SpringApplicationBuilder  application) {
	return  application.sources(Application.class);
}
```

After that, the deploy was successful and we could open our beautiful application through the ec2 tomcat link + /crm, something like this: ``http://ec2-3-85-104-7.compute-1.amazonaws.com:8080/crm ``!

 - [x] **2º - System Test**
For the system test we will perform a automatic smoke test that will be checking if the base URL of the application is responsive after staging the deployment.

For that we used the command line tool curl, that stands for client url, that is normally used for transfering data using various network protocols.

We could have just print all the headers of the server response and that contains the http reponse. But we wanted to go a bit further and only print the http code.
And depending on the operating system that the job is running, we had to make some changes.
**For Linux**
For linux we used the following command:
```curl -s -o /dev/null -w '%{http_code}' $url/crm```
Where ``$url`` is the variable that holds the url that you want to get the http code.

**For Windows**
For windows we used the following command
```curl -s -o ./response -w '%%{http_code}' $url/crm"```


Knowing the commands we just created a way to store the result and create a condition to verify is the response was positive and we could proceed or negative and we throw an error and abort the build.

```groovy
stage('systemTest'){
	echo "Initiating Smoke Test"
	if (isUnix()){
		httpCode = sh( script: "curl -s -o /dev/null -w '%{http_code}' $url/crm", returnStdout: true ).trim()
	}else{
		httpCode = bat( script: "curl -s -o ./response -w '%%{http_code}' $url/crm", returnStdout: true).trim()
		httpCode = httpCode.readLines().drop(1).join(" ")//windows returns full command plus the response, but the response is at a new line so we can drop the first line and remove spaces and we get only the http code
	}
//checking if the http code was ok(200) or found(302)
	if (httpCode == "200" || httpCode == "302"){
		echo 'The application is responding!'
	}else{
		currentBuild.result = 'ABORTED'
		error('The application is not responding...')
	}
}
```

\*\*


