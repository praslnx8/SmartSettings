# SmartSettings

We are following Model Driven Development here.

SmartSettings Android app code.

An Android app that can smartly set your phone setting based on contextual data such as location, time, activity etc.

The Android app is decided to build with Kotlin, Android JetPack Components, Dagger2 etc.

On the architectural side, We are splitting the logics as
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

This project also uses REST API for few features such as FindMyDevice, Change PIN remotely etc. For this the REST API will be done by Spring BOOT.

The Web version, will give user the GUI to locate phone and change PIN. The Web GUI part will be handled by React.

