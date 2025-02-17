package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication) {

        // AuthenticationPrincipal principal = (AuthenticationPrincipal)
        // authentication.getPrincipal();

        // check whether user is logged in using google, github or normal signin
        if (authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                // sign with google
                System.out.println("login with google");
                username = oauth2User.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("github")) {
                // sign with github
                System.out.println("login with github");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email")
                        : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }

            return username;
        } else {
            System.out.println("Getting data from database");
            return authentication.getName();
        }
    }
}
