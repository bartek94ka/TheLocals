package com.example.bartosz.thelocals.Providers;

import com.facebook.AccessToken;

public class FacebookInfoProvider {
    public Boolean IsUserLogged(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
    }
}
