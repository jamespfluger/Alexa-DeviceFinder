![Alexa - Device Finder](https://i.imgur.com/hhtczpR.png)

### What is this?  
Alexa – Device Finder is a combination of an Alexa skill with an Android companion app.  
It is a skill used to locate any Android device by asking Alexa where it is. The Android app will receive a notification to play a sound at maximum volume

#### How is the application organized?

There are two parts to the application:
1. The Android application, written in Java (may be converted to Kotlin in the future)
2. The .NET Core solution, which is broken down into two additional parts
    1. The authentication API between the Android device and the DynamoDB database, which stores the user's Amazon ID + Device ID for notifications 
    2. The Alexa skill itself that the user will interact with

#### Ok, but give me the details on how the application works

1. The user signs into the Android app with their phone.
2. This sends a request to an AWS or Azure serverless function with their Amazon ID, device ID, and phone name.
3. This function inserts the values into an AWS DynamoDB or Azure CosmosDB. The key will always be the user's amazon ID.
4. The user enables the Amazon skill, which will ensure they have the Android app installed by. This skill (currently) has to be hosted on AWS, but Azure options are being explored.
5. The user asks Alexa to find a phone (e.g., "Alexa where is James's phone?").
6. This triggers the AWS Lambda function which queries the database and sends a Firebase Cloud Message (FCM) to the user's phone
7. The Android app receives the notification and plays a sound at max volume.

## FAQ

**Q: Why are all the projects in one repo? Doesn't it make sense to split them into multiple with a meta-repo?**  
A: This is primarily due to being able to see when bugs were introduced by having a single "source of truth." It helps with tracking when features are implemented. Additionally, the amount of code in each project is not significant enough to justify splitting them all out. However, I recognize the desire for multiple repos because the projects are intended to be loosely-coupled. This *can* be changed in the future if the project becomes too big. 

**Q: Where are the rest of the projects?**

A: You caught me. They are currently in a private repo until they have been cleaned up and ready for public development. I am working on setting those up ASARP (as soon as reasonably possible), but I want them to be as clean as possible before they go public.

*(I haven't received any other questions so I'll add something here when I discover something that should be here)*


## Development

### How do I contribute?

First, select an issue you'd like to work on and comment on it. If you have any questions or clarifications needed, you can discuss it there with the team. For extensive conversations, you may need to send an email to [me@jamespfluger.com](mailto:me@jamespfluger.com). Generally, the process will be to simply open a PR for any changes. As for coding standards, we are strictly adhering to Microsoft's standards for the C# projects and Google's standards for the Android project.

### How do I set up the project(s) to develop for after cloning the repository?

#### Android
1. Install Android Studio
2. Download the [Login With Amazon Android SDK](https://developer.amazon.com/docs/apps-and-games/sdk-downloads.html#lwa)
3. Place `login-with-amazon-sdk.jar` into the directory `\AlexaDeviceFinder-Android\app\libs`
4. Build the app
5. Sign your version of the app with the [instructions here](https://developer.android.com/studio/publish/app-signing)
6. Follow the [instructions here](https://developer.amazon.com/docs/login-with-amazon/register-android.html) to create a new security profile for use with Login With Amazon
7. Navigate to the directory `\Alexa-DeviceFinder\AlexaDeviceFinder-Android\app\src\main\assets` and create a file called `api_key.txt`. If that directory does not exist, then you must create it
8. Under the Android/Kindle settings of your security profile, copy and paste the API key value directly into `api_key.txt`. Note that there cannot be spaces before/after the key. Only copy and paste the raw text given
9. Build and run the app
10. If this does not work, please open an issue with the documentation tag so we can identify what problems may arise

#### .NET Core solution 

*(Instructions coming soon)*
