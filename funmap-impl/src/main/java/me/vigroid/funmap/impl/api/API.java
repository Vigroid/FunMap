package me.vigroid.funmap.impl.api;

/**
 * Created by yangv on 2/5/2018.
 * API calls
 */

public final class API {
    /**
     * Account related
     */
    public static final String LOGIN = "login.php";
    public static final String REGISTER = "register.php";
    public static final String FORGET_PSW="forgetPsw";
    public static final String CHECK_USER_EXIST ="isUserExist";

    /**
     * Marker related
     */
    public static final String FETCH_MARKERS = "fetchMarkers.php";
    public static final String CREATE_MARKER = "createMarker";
    public static final String UPLOAD ="upload";
    public static final String FETCH_USER_EVENTS = "fetchUserEvents";
    public static final String FETCH_USER_PICS = "fetchUserPics";
    public static final String FETCH_SAVED_EVENTS = "fetchSavedEvents";
    public static final String FETCH_SAVED_PICS = "fetchSavedPics";
}
