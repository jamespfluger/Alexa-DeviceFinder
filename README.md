![Alexa - Device Finder](https://i.imgur.com/hhtczpR.png)

### What is this?  
Alexa – Device Finder is a combination of an Alexa skill with an Android companion app.  
It is a skill used to locate any Android device by asking Alexa where it is. The Android app will receive a notification to play a sound at maximum volume

### How is the application organized?

There are two parts to the application:
1. The Android application, written in Java (may be converted to Kotlin in the future)
2. The .NET Core solution, which is broken down into two additional parts
    1. The authentication API between the Android device and the DynamoDB database, which stores the user's Amazon ID + Device ID for notifications 
    2. The Alexa skill itself that the user will interact with

### How do I set up the project(s) to develop for after cloning the repository?

#### Android
1. Install Android Studio
2. Follow the [instructions to create a new security profile](https://developer.amazon.com/docs/login-with-amazon/register-android.html) to use with Login With Amazon
    1. 
3. More

#### .NET Core solution
1. 

### How does the application flow, from a business side?

1. The user signs into the Android app with their phone.
2. This sends a request to an AWS or Azure serverless function with their Amazon ID, device ID, and phone name.
3. This function inserts the values into an AWS DynamoDB or Azure CosmosDB. The key will always be the user's amazon ID.
4. The user enables the Amazon skill, which will ensure they have the Android app installed by. This skill (currently) has to be hosted on AWS, but Azure options are being explored.
5. The user asks Alexa to find a phone (e.g., "Alexa where is James's phone?").
6. This triggers the AWS Lambda function which queries the database and sends a Firebase Cloud Message (FCM) to the user's phone
7. The Android app receives the notification and plays a sound at max volume.


### How do I contribute?

Send an email to [me@jamespfluger.com](mailto:me@jamespfluger.com) to get more details, but  the process will be to simply open a PR for any changes. I am trying to follow Microsoft's standards for the C# projects and Google's standards for the Android project.

### Where are the rest of the projects?

You caught me. They are currently in a private repo until they have been cleaned up and ready for public development. I am working on setting those up ASARP (as soon as reasonably possible), but I want them to be as clean as possible before they go public.

### FAQ

**Q: Why are all the projects in one repo? Doesn't it make sense to split them into multiple with a meta-repo?**  
A: This is primarily due to being able to see when bugs were introduced by having a single "source of truth." It helps with tracking when features are implemented. Additionally, the amount of code in each project is not significant enough to justify splitting them all out. However, I recognize the desire for multiple repos because the projects are intended to be loosely-coupled. This *can* be changed in the future if the project becomes too big. 

(I haven't received any other questions so I'll add something here when I discover something that should be here)
