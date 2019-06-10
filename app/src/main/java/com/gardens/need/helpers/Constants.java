package com.gardens.need.helpers;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Manish on 3/9/2017.
 */

public class Constants {

    public static final String Dashboard = "Dashboard";
    public static final String AboutUs = "AboutUs";
    public static final String ContactUs = "ContactUs";
    public static final String Enquiry = "Enquiry";
    public static final String SubCategory = "SubCategory";
    public static final String ProductDetails = "ProductDetails";

    /*
    * Show Toast to the user
    * */
    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
