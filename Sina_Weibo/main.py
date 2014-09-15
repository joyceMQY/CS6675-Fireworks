#encoding=utf-8
from weibo import APIClient
import sys
import  time

reload(sys)
sys.setdefaultencoding( "utf-8" )

type = sys.getfilesystemencoding()
APP_KEY = '3845757430' # app key
APP_SECRET = 'ed5c45f589b2d8983c51039a18531845' # app secret
CALLBACK_URL = 'http://zheng-lin.appspot.com/unit2/signup' # callback url

client = APIClient(app_key=APP_KEY, app_secret=APP_SECRET, redirect_uri=CALLBACK_URL)
access_token='2.006XLslBeL6QMEcc11107404TTNRrD'; expires='1553385052'
client.set_access_token(access_token, expires)

myfile = open('/Users/zhenglinyu/Documents/weibo_coordinates.dat','a')
while True:
    temp = client.statuses.public_timeline.get(count = 200)

    statuses = temp['statuses']  # a list of dics

    for i in statuses:
        #print i
        if i['geo']:
            print i
            geo = i['geo']
            p = i['text']
            len_pad = p.find('http:')
            #print geo
            myfile.write(( str(geo["coordinates"][0]) + '|' +  str(geo["coordinates"][1]) + '|' + str(i['user']['location']) + '|' +  str(i['text']) + '|' +  str(i['id'])  + '|' + str(p[len_pad:]) + '\n'))
    time.sleep(25)


myfile.close()



    #print len(temp)
    #for key in temp:
    #    print key
    #print temp['total_number']
