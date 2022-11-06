# ODSOFT REPORT

This is the repository of g202, and the members are:

 - Gonçalo Pinho - 1220257
 - João Rocha - 1220256
 - Nuno Leite - 1220271
 - Tiago Lacerda - 1220285

  ## Pipeline Design

Initialy we started to design the pipelines (Sequential and parallel).

  

**Sequential pipeline**

![Alt text](pipeline-diagrams/sequential_pipeline.png?raw=true  "Sequencial pipeline diagram")

  

Sequential pipeline, as the name says, executes the stages in a sequence.

  

**Parallel pipeline**

![Alt text](pipeline-diagrams/parallel_pipeline.png?raw=true  "Sequencial pipeline diagram")

  

Parallel pipeline, can execute more than one task at the same time so that we can use hardware more efficiently and save time (or not).

  

## Group Tasks:

 - [x] **1º Task - Repository Checkout:**

For that we used the snippet generator and picked the sample step "checkout" where we indicate the repository and branch and we can generate the code snippet that will checkout the repository.

  

Created the pipeline and created the stage checkout.

  

```groovy
node{
	stage('checkout'){
		checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'git@bitbucket.org:mei-isep/odsoft-22-23-ncf-g202.git']]])
	}
}

```

  

This step, when executed for the first time, will fetch all the data from the repository. The next times will only fetch the updates.

 - [x] **2º Task - Build and publish the deployment file**

On this task we've created the "build" stage where we built the application in **production mode** and generated a .war/.jar file.

For that we used the [vaadin documentation](https://vaadin.com/docs/latest/overview), and to build in production mode we need to enable it in the gradle.build file adding the following tag into the vaadin configuration:
```groovy
productionMode = true
```
If you want to generate a war file you need to import the war plugin by adding it on the build.gradle plugins task.
```groovy
id 'war'
```
After that we can finally build the app with the following command:
`./gradlew clean build "-Pvaadin.productionMode" war`

*Yes, leave the quotation marks. Vaadin documentation have a little mistake that might take us some hours to figure it out.*

Note: if you want a .war artifacts leave the  ```war``` tag.If you prefer a .jar artifacts remove it.

After it builds, on the ``` build/libs ``` directory you can find the artifacts generated by the build.

 - [x] **3º Task - Generate and publish javadoc**
To complete this last team goal we followed the gradle documentation [about javadoc](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.javadoc.Javadoc.html) and created a new task to generate the javadoc on the build.gradle file.

```groovy
javadoc {
	source = sourceSets.main.allJava
	destinationDir = reporting.file("javadoc")
}
```

After that we ran ```./gradlew javadoc``` it will generate the javadoc on the following directory ```build/reports/javadoc```.

To publish it, we need to install [HTML Publisher](https://plugins.jenkins.io/htmlpublisher/) that is a Jenkins plugin that allow us to publish HTML files on the pipeline.

After installing it, with snippet generator we selected "publishHTML" sample step and we had to provide the directory, the index page and the report title. With that, we generated the code snippet and implemented it on our Jenkinsfile resulting on the following stage:
```groovy
stage('javadoc'){
	if (isUnix()){
		sh './gradlew javadoc'
	}else{
		bat './gradlew javadoc'
	}
publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/javadoc/', reportFiles: 'index.html', reportName: 'Javadoc', reportTitles: '', useWrapperFileDirectly: true])
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

``` java
@Test
public  void  formShownWhenContactDeselected() {
	Grid<Contact> grid = listView.grid;
	Contact  emptyContact = new  Contact();
	ContactForm  form = listView.form;
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

To generate the HTML test report, we need to create a task of type ``Test`` and we named it ``integrationTest``. For this, we used the [JUnit framework](https://junit.org/junit5/), and we specify that we require a HTML report, which will be generated in the directory ``/build/htmlReports/junitReports/integration``. We also applied a filter that only executes tests that contains "IT" in the name.

```` java
task  integrationTest(type: Test) {
	useJUnitPlatform()
	reports {
		reports.html.required = true
		reports.html.destination = file("/build/htmlReports/junitReports/integration")
		}
	filter{
		includeTestsMatching  "*IT"
		}
}
````

- [x] **Integration Tests HTML Report Publishing**

To publish the Integration Tests HTML Report, we only need to add this stage to the JenkinsFile:
```` groovy
stage('integrationReport'){
	if (isUnix()) {
		sh './gradlew integrationTest'
	} else {
		bat './gradlew integrationTest'
	}
	publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/junitReports/integration', reportFiles: 'index.html', reportName: 'IntegrationTests Report', reportTitles: '', useWrapperFileDirectly: true])
}
````
This command allows Jenkins to find the report in the mentioned directory ``build/htmlReports/junitReports/integration`` and look for the ``index.html``. Then, Jenkins generate a report named IntegrationTests Report.