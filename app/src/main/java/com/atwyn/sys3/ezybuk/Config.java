package com.atwyn.sys3.ezybuk;

/**
 * Created by SYS3 on 7/17/2017.
 */

public class Config {
    // For admin login
    Config(){}

    public static final String API_KEY =
            "AIzaSyBltkcv28mWW6PnlpNMZyzOab4dOhNRKAU";

    public final static String mainUrlAddress = "http://192.168.0.8/ezybuk/";


    // For admin login

    // For Getting all movies data from database

    public final static String moviesUrlAddress = mainUrlAddress+"home/get_nowshowingmovies";
    public final static String moviesUpcomingUrlAddress = mainUrlAddress+"home/get_upcomingmovies";

    public final static String loginUrlAddress = mainUrlAddress+"login/loginApp";

    public final static String Facebook_Gmail = mainUrlAddress+"register/registerFBGmail";
    public final static String EzyBuk_SignUp = mainUrlAddress+"register/registerEzyBuk";
    public final static String Gmail = mainUrlAddress+"register/registerGmail";
    public final static String castUrlAddress = mainUrlAddress+"home/get_cast/";
    public final static String crewUrlAddress = mainUrlAddress+"home/get_cast/";

    public final static String TheaterNameUrlAddress = mainUrlAddress+"home/getTheatre/";
    public final static String DateNameUrlAddress = mainUrlAddress+"home/getCinemaDate/";
    public final static String TimeNameUrlAddress = mainUrlAddress+"home/getCinemaTime/";

    public final static String EmailSubmitUrlAddress = mainUrlAddress+"login/userData/";
   // public final static String loginUrlAddress = mainphpUrl+"login.php";

    public final static String KEY_USER = "username";
    public final static String KEY_PASS = "password";
    public final static String LOGIN_SUCCESS = "success";
    public final static String SHARED_PREF_NAME = "loginapp";
    public final static String USER_SHARED_PREF = "";
    public final static String LOGGEDIN_SHARED_PREF = "loggedIn";
    public final static String LOGIN_CHECK ="fail";
    public final static String MovieID_PARAM = "MovieId";

}
