package test;

import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class Test {

    public static void main(String[] args) {
            String access_token = "2.00b3pWYFZSUU9D18cc07179bR8xoFB";
            String uid ="godmiev587";
            Users um = new Users();
            um.client.setToken(access_token);
            try {
                    User user = um.showUserById(uid);
                    Log.logInfo(user.toString());
            } catch (WeiboException e) {
                    e.printStackTrace();
            }
    }
}
