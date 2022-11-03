
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