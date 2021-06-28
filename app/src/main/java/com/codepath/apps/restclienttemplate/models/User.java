package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public String screenName;
    public String publicImageUrl;
    public static User fromJson(JSONObject data) throws JSONException {
        User user = new User();
        user.name = data.getString("name");
        user.screenName = data.getString("screen_name");
        user.publicImageUrl = data.getString("profile_image_url_https");
        return user;
    }

}
