# DeskTrack

*This project is for personal use*


## Summary

Since 2019, the Spanish government obliges all Spanish companies, both small and / or large, to submit a monthly report on the control of workers' schedules. Since then, many tech companies have created solutions to help companies keep track of their employees work hours.

However, many solutions lack something simple, such as an administrator changing already logged schedule by an employee.

### Solutions and approach

We plan to create a simple but stylish mobile application to suit the needs of **small** Spanish companies.

**The app will have the following components:**

- **Normal user** panel
    - A home page where the user can check-in/out (to track arrival and leaving times)
    - A calendar view page where users can check the schedules already registered previously
    - A profile editor page
- **Admin** panel
    - A home page where the admin can create companies, employees associated to those companies, export the data into an Excel file by month/year, connect to google drive to upload the data periodically.
    - A calendar view page where admin can select any of his employees(users) to see its calendar assoaciated. Admin can edit employees calendar.
    - A profile editor page

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

## Screenshots
- **Admin** panel
    - Home page
        - ![Selection_002](https://user-images.githubusercontent.com/29677743/133680101-7fc6576d-636f-4ab0-bbd6-32cf2d5c565a.png) ![image](https://user-images.githubusercontent.com/29677743/133683465-85639ff0-7807-40c5-b76f-0413a5de5025.png) ![image](https://user-images.githubusercontent.com/29677743/133683540-0814d98c-c94d-4d80-8129-e2d46157f7bb.png) ![image](https://user-images.githubusercontent.com/29677743/133683812-b3694ce5-740c-4796-9e8d-7616e4811262.png)
    - Calendar View Page
        - ![Selection_003](https://user-images.githubusercontent.com/29677743/133680415-2472b3d6-0247-4430-976e-1b647729776c.png) ![image](https://user-images.githubusercontent.com/29677743/133684116-401d9a1b-d9a5-4492-b538-eda733555893.png) ![image](https://user-images.githubusercontent.com/29677743/133684181-b040a868-6faf-446a-a735-372b49acb157.png) ![image](https://user-images.githubusercontent.com/29677743/133684261-e48bef08-9cda-4e02-9a37-5f0ce0f1dfe6.png) ![image](https://user-images.githubusercontent.com/29677743/133684322-b953a28b-2e11-4ca8-adbb-ee296cf89e82.png)
    - Profile Editor Page
        - ![image](https://user-images.githubusercontent.com/29677743/133680536-1933d64b-2bc7-4141-a543-9ec3e0a51624.png) 

- **Normal User** panel
    - Home page
        - ![image](https://user-images.githubusercontent.com/29677743/133686470-593db388-e250-4d2b-83af-c420c7e38e0a.png) ![image](https://user-images.githubusercontent.com/29677743/133686538-2968e4f5-ff14-46f0-a3b2-da523ddf1927.png) ![image](https://user-images.githubusercontent.com/29677743/133686587-ce8dc609-1e49-44c4-b950-82061daeb261.png)

    - Calendar View Page
        - ![image](https://user-images.githubusercontent.com/29677743/133686688-e9157317-6253-405b-bc2b-a28f9584dc42.png)

    - Profile Editor Page
        - ![image](https://user-images.githubusercontent.com/29677743/133686754-94265aab-9a29-4ad8-8484-e9b2c6e4e9fc.png)



