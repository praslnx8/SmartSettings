# SmartSettings 
![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/prasilabs/smartsettings)  ![GitHub issues](https://img.shields.io/github/issues/praslnx8/smartsettings) ![GitHub](https://img.shields.io/github/license/praslnx8/smartsettings)   ![GitHub last commit](https://img.shields.io/github/last-commit/praslnx8/smartsettings)

We are following Model Driven Development here.

SmartSettings Android app code and Ktor API Code.

An app that can smartly set your phone setting based on contextual data such as location, time, activity etc.

The app is decided to build with Kotlin, Android JetPack Components, Dagger2 etc.

On the architectural side, We are splitting the logic as
1. Core Business Logic -> The model layer and
2. Presentation logic -> UI and presentation layer.

# Core/Main business logic
Core/Main business logic will be handled by models(Not just POJO or Data classes :p) itself. Please have a look at UML Diagrams expecially the class diagram to understand how the model is structured and what resposibility is provided to models.

Sequence Diagram:
![Sequence Diagram](https://raw.githubusercontent.com/praslnx8/SmartSettings/master/MDD/sequence_diagram.png)

Class Diagram:
![Class Diagram](https://raw.githubusercontent.com/praslnx8/SmartSettings/master/MDD/class_diagram.png)

# Presentation logic
we are going with MVP + Cool feature of View Model and Live Data. :)
We use liveData to listen for data changes on model side. We use presenter because, here the view should be as dumb as possible and presenter should have the business logic of user flow.

This project also uses REST API for few features such as FindMyDevice, Change PIN remotely etc. For this the REST API we are using Ktor.
REST API Document: https://documenter.getpostman.com/view/132932/SW18waXa?version=latest


To Run Rest API Server from command line
./gradlew restApi:run 

To Create Rest API Server with Docker
./gradlew build
docker build -t prasilabs/smartsettings .
docker run -m512M --cpus 2 -it -p 5000:5000 --rm my-application

docker tag prasilabs/smartsettings:latest prasilabs/smartsettings:latest 
docker push prasilabs/smartsettings:latest 
