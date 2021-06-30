# DeskTrack

## Separation of concerns

The most important principle to follow is separation of concerns. It's a common mistake to write all your code in an **Activity** or a **Fragment**. These UI-based classes should only contain logic that handles UI and operating system interactions. By keeping these classes as lean as possible, you can avoid many lifecycle-related problems.

## Drive UI from a model

Another important principle is that you should drive your UI from a model, preferably a persistent model. Models are components that are responsible for handling the data for an app. They're independent from the **View** objects and app components in your app, so they're unaffected by the app's lifecycle and the associated concerns.

## Recommended app architecture by GOOGLE

![Link to architecture example](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

Each component depends only on the component one level below it. For example, activities and fragments depend only on a view model. The repository is the only class that depends on multiple other classes; in this example, the repository depends on a persistent data model and a remote backend data source

### ViewModel

A ViewModel object provides the data for a specific UI component, such as a fragment or activity, and contains data-handling business logic to communicate with the model. For example, the ViewModel can call other components to load the data, and it can forward user requests to modify the data. The ViewModel doesn't know about UI components, so it isn't affected by configuration changes, such as recreating an activity when rotating the device.

### Repository

Our ViewModel delegates the data-fetching process to a new module, a *repository*. **Repository** modules handle data operations. They provide a clean API so that the rest of the app can retrieve this data easily. They know where to get the data from and what API calls to make when data is updated. You can consider repositories to be mediators between different data sources, such as persistent models, web services, and caches.

> //TODO improve this page explanations

### @[Link to Google jetpack guide](https://developer.android.com/jetpack/guide)


## Schema (data model)

An abstract design that represents the storage of our data in the database. 
![database schema](https://user-images.githubusercontent.com/26594010/123917937-a3fad500-d983-11eb-8dc2-0b6fd78726e0.png)

