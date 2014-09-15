__author__ = 'zhenglinyu'

from weibo import APIClient

APP_KEY = '3169402215' # app key
APP_SECRET = '5472223bdf902910d12fd35d11207e0e' # app secret
CALLBACK_URL = 'http://zheng-lin.appspot.com/unit2/signup' # callback url

client = APIClient(app_key=APP_KEY, app_secret=APP_SECRET, redirect_uri=CALLBACK_URL)
url = client.get_authorize_url()
print url


code = raw_input()
print code

r = client.request_access_token(code)
access_token = r.access_token
expires_in = r.expires_in

client.set_access_token(access_token, expires_in)

print access_token, expires_in