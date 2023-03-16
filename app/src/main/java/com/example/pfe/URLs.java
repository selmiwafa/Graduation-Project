package com.example.pfe;

public class URLs {

    private static final String ROOT_URL = "https://192.168.1.4/Android/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";


    public static String getRootUrl() {
        return ROOT_URL;
    }
}