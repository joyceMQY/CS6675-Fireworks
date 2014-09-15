Prerequisite:
The codes are written using python 2.7. 
So python 2.7 need to be installed first to use the code.


First step: microblogging application settings 

1 Management Center applications = > Applications = > Advanced Information => OAuth2.0 authorization settings :

Authorization callback page :

Deauthorize callback page :

These two values ​need to be ​changed to a URL. For example: http://zheng-lin.appspot.com/unit2/signup

 

2 Go to the application management center = > Applications = > test account = >

Add a test account : 

If  anyone else want to run the test, the account nickname need to be added. Otherwise only application creator will be able to run the app.

 

Second Step: try to get the code and token

1 Modify the file: test.py

APP_KEY = '3845757430 '# app key

APP_SECRET = 'ed5c45f589b2d8983c51039a18531845' # app secret

These two values should be modified to the corresponding application key and secret. After registering a application , the application itself will provide these two values..

Keep Callback_url without modification.

2 Run test.py. It will print out a url.

Copy this url, and then open it in the browser .

This is a browser's address bar will be something like this:

http://zheng-lin.appspot.com/unit2/signup?code=0edbd33cfc81ef5acf071fcdee46f2c8

 

So we got the needed code value

In the above example , code = 0edbd33cfc81ef5acf071fcdee46f2c8

 

3 At this time , the program is still waiting for you to enter a value , we copy the value of the code to the console , and after pressing enter, the program continues to run, and it will print out the token and expire time.

It will be similar to: 2.006XLslBZSUU9Df002c89cf6t_6LtC        1396205999

The first value is the token. The second value is the expire time. Both are important parameters.

 

Test.py 's role is to get token and expire time.

 

Third Step:

    Modify and run main.py file

 

The APP_KEY = '3845757430 '# app key

APP_SECRET = 'ed5c45f589b2d8983c51039a18531845' # app secret

CALLBACK_URL = 'http://zheng-lin.appspot.com/unit2/signup' # callback url

 

client = APIClient (app_key = APP_KEY, app_secret = APP_SECRET, redirect_uri = CALLBACK_URL)

access_token = '2 .006 XLslBeL6QMEcc11107404TTNRrD ';

expires = '1553385052 '

 

This six lines, we will keep callback_url as it is, and the others should be replaced by the corresponding application key, secret, token and expires.

And we just got token and expires in second step.



Fourth Step:


Then we can just run the code..

Can be directly run main.py, or use the command:
python main.py

 

myfile = open ('/ Users / zhenglinyu / Documents / weibo_coordinates.dat', 'w')

myfile.write ((str (geo ["coordinates"] [0]) + '|' + str (geo ["coordinates"] [1]) + '\ n'))

 

This two are written to the file . You can modify the path accordingly .

Here is the longitude and latitude by | After separated into the dat file .


time.sleep (2) means it is suspended for two seconds .
