package com.keykeep.app.interfaces;

import android.Manifest;

/**
 * Created by vishalchhodwani on 18/8/17.
 */
public interface ConstantsLib {


    int SELECT_IMAGE_CAMERA = 1000;
    int SELECT_IMAGE_GALLERY = 1100;
    int STORAGE_CAMERA_PERMISSIONS_REQUEST_CODE = 1300;
    int STORAGE_PERMISSIONS_REQUEST_CODE = 1400;
    int DIALOG_LOGOUT_REQUEST = 1500;
    int DIALOG_SERVER_ERROR_REQUEST = 1501;
    int DIALOG_EXIT_REQUEST = 1600;
    int REQ_CODE_SAVE_MENU = 1700;
    int DIALOG_SHOW_BEFORE_CANCEL = 1502;
    int DIALOG_SERVICEFAILURE = 1504;
    int DIALOG_SERVICESUCCESS = 1605;
    int DIALOG_PAYMENT = 1505;
    int DIALOG_PAYMENT_ERROR = 1506;
    int DIALOG_FORCE_UPDATE = 1507;
    String API_KEY = "lditjfrwts5g88u4khg5f5W5d9J5w17";
    String APP_TYPE = "2"; // 1= Host App , 2 = Customer App
    String API_VERSION = "1";
    String GENDER_MALE = "M";
    String GENDER_FEMALE = "F";
    String GENDER_OTHER = "OTHER";
    int DIALOG_DELETENOTIFICATION_REQUEST = 1900;
    String CALLED_FROM = "calledFrom";
    String ISHOST = "ishost";
    int BREAKFAST = 1;
    int LUNCH = 2;
    int DINNER = 3;

    int CALLING_TYPE_LOAD_MORE = 2300;
    int CALLING_TYPE_REFRESH = 2400;
    int CALLING_TYPE_NORMAL = 2500;
    int DIALOG_REVIWSUCCESS = 2560;
    int DIALOG_REVIEWFAILURE = 2570;

    String TICKET_CLOSED = "0";
    String TICKET_OPEN = "1";

    public static String Event_ID = "event_id";
    public static String Event_TYPE = "event_type";
    public static String navigateFromDetails = "navigate_from_details";
    public static String navigateFromNotification = "navigate_from_notification";

    public static final int STATUS_FRESH = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_REQUESTED = 2;
    public static final int STATUS_REJECTED = 3;
    public static final int STATUS_BOOKED = 4;
    public static final int STATUS_CANCELLED = 5;
    public static final int STATUS_COMPLETED = 6;
    public static final int STATUS_PAYMENT_IN_PROCESS = 7;
    public static final int STATUS_FAILED = 8;

    // '1' =>'Accepted', '2' =>'Requested', '3' =>'Rejected', '4' =>'Booked',
    // '5' =>'Cancelled', '6' =>'Completed', '7' =>'Booking', '8' =>'Failed'


    String SUCCESS = "1";
    String FAILED = "0";
    String TOKEN_EXPIRED = "403";

    String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] STORAGE_CAMERA_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    String KEY_PARAMS = "key_social_params";
    String KEY_DINING_LIST = "key_dining_data";
    String KEY_SOCIAL_TOTAL = "social_total";

    String KEY_EVERYDAY_TOTAL = "everyday_total";
    String KEY_TAKEAWAY_TOTAL = "takeaway_total";

    String INPUT_FORMATE_DATE_TIME = "yyyy-MM-dd hh:mm:ss";
    String OUTPUT_FORMATE_DATE_TIME = "dd MMM yy hh:mm";
    String INPUT_FORMATE_DATE = "yyyy-MM-dd";
    String OUTPUT_FORMATE_DATE = "dd MMM yy";
    String DD_FORMATE = "dd";
    String INPUT_TIME_FORMATE = "hh:mm:ss";
    String OUTPUT_TIME_FORMATE = "hh:mm a";

    long TIME_INTERVAL = 1000 * 60 * 60 * 2;
    String KEY_EVENT_ID = "key_event_id";
    String KEY_HOST_MENU_LIST = "key_host_menu";
    String LEGALS = "legals";
    String SAFETYANFTRUST = "safetyandtrust";
    String HOWITWORKS = "howitworkes";
    String ABOUT = "about";
    String TERMSANDCONDITIONS = "termsandconsitions";
    String PRIVACYPOLICY = "privacypolicy";
    int VIEW_RECOMANDED = 12001;
    int VIEW_LASTVIEWED = 12002;
    String INSTANT_REVIEW_DATA = "instant_review_data";
    int NOTIFICATION_DIALOG = 13005;

    interface BookingStatus {
        int ACCETED = 1;
        int REQUESTED = 2;
        int REJECTED = 3;
        int BOOKED = 4;
        int CANCELLED = 5;
        int COMPLETED = 6;
        int PAYMENT_IN_PROCESS = 7;
    }

    String REVIEWEVENTTYPE = "revieweventtype";
    String REVIEWEVENTID = "revieweventid";
    String REVIEWHOSTID = "reviewhostid";
    String REVIEWGUESTID = "reviewguestid";
    String HOST_NAME = "host_name";
    String HOST_IMG = "host_img";
    String REVIEW_CALL_FROM = "review_call_from";
    String ISEVENTBOOKED = "is_event_booked";
    String HOST_DATA = "host_data";
    String CALLED_FROM_PUSH = "calledFromPush";
    String NOTIFICATION_DATA = "notification_data";


    //host mix pannel
//    String MIXPANEL_PROJECT_ID_TOKEN = "1be33e072ccc5ec66d5ec3a94b1aa2d3";
//    String ANALYTIC_KEY ="zUw8h2sL1WTqK8uGfoekTD21x9hmpGoi";
//    String PROJECT_NUMBER = "565465272311";


    //guest mix pannel
    String MIXPANEL_PROJECT_ID_TOKEN = "5d62afe13c0d7deb38c2f2cff20d6c99";
    String ANALYTIC_KEY = "juDgwbcc9nUPVIDlriEUte13SFPeNXNi";
    String PROJECT_NUMBER = "565465272311";


}
