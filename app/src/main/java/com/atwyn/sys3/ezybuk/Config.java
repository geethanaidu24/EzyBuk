package com.atwyn.sys3.ezybuk;

import java.net.URL;

/**
 * Created by SYS3 on 7/17/2017.
 */

public class Config {
    // For admin login
    Config(){}

    public static final String API_KEY =
            "AIzaSyBltkcv28mWW6PnlpNMZyzOab4dOhNRKAU";

    public final static String mainUrlAddress = "http://192.168.0.28/ezybuk/";


    // For admin login

    // For Getting all movies data from database

    public final static String moviesUrlAddress = mainUrlAddress+"home/get_nowshowingmovies";
    public final static String moviesUpcomingUrlAddress = mainUrlAddress+"home/get_upcomingmovies";
    public final static String videosUrlAddress = mainUrlAddress+"home/get_latestTrailers";

    public final static String loginUrlAddress = mainUrlAddress+"login/loginApp";

    public final static String Facebook_Gmail = mainUrlAddress+"register/registerFB";
    public final static String EzyBuk_SignUp = mainUrlAddress+"register/registerMe";
    public final static String Gmail = mainUrlAddress+"register/registerGmail";
    public final static String castUrlAddress = mainUrlAddress+"home/getCast/";
    public final static String crewUrlAddress = mainUrlAddress+"home/getCrew/";

    public final static String Movies_Search = mainUrlAddress+"home/searchMovies";
    //public final static String TheaterNameUrlAddress = mainUrlAddress+"home/getTheatre/";
    public final static String DateNameUrlAddress = mainUrlAddress+"home/getCinemaDate/";
    public final static String TimeNameUrlAddress = mainUrlAddress+"home/showTheatreTime/";

    public final static String EmailSubmitUrlAddress = mainUrlAddress+"login/userData/";
   // public final static String loginUrlAddress = mainphpUrl+"login.php";

    public final static String SeatLayout = mainUrlAddress+"home/generateLayout/";

    public final static String ScrollingImages = mainUrlAddress+"home/showScrollingMovies";

    public final static String MovieTimeUrlAddress = mainUrlAddress+"home/showMovieTheatre/";

    public final static String KEY_USER = "username";
    public final static String KEY_PASS = "password";
    public final static String LOGIN_SUCCESS = "success";
    public final static String SHARED_PREF_NAME = "loginapp";
    public final static String SHARED_PREF_GMAIL = "logingmail";
    public final static String USER_SHARED_PREF = "";
    public final static String LOGGEDIN_SHARED_PREF = "loggedIn";
    public final static String LOGGEDIN_SHARED_GMAIL = "gmail";
    public final static String LOGIN_CHECK ="fail";
    public final static String MovieID_PARAM = "MovieId";

}
