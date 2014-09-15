PushNotificationsExample
========================

To use the push notification example you must :

1. Put "project_number" in PushNotificationApp/res/values/strings from < Project Number >.

2. Make request to see if it works : 
```
ENDPOINT : https://android.googleapis.com/gcm/send
METHOD   : POST
HEADER1  : Authorization : key=< Api Key >
HEADER2  : Content-Type : application/json
BODY     : 
{
  "registration_ids" : ["< Registration Id >"],
  "data" : {
     "message": "Test message."
  },
}
```
Legend :
==========
< Project Number > = The number of the project in the Google Console.
Example : http://developer.android.com/google/gcm/gs.html#create-proj .

< Api Key > = The api key of the project in the Google Console.
Example : http://developer.android.com/google/gcm/gs.html#access-key .

< Registration Id > = It is generated based on your < Project Number>.It will be shown in the logs of the app.
You must copy it to make requests.
