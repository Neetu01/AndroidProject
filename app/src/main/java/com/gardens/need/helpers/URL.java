package com.gardens.need.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gardens.need.R;

/**
 * Created by tarun on 3/13/2017.
 */

public class URL {

//        private static final String base = "http://192.168.0.102:4444/";
    private static final String base = "https://malhotra-plastics.herokuapp.com/";
    private static final String GetCategory = base + "category/get/";
    private static final String GetProduct = base + "product/get/";
    public static final String SendEnquiry = base + "enquiry/send";
    public static final String GetProductCount = base + "product/get/count";

    /*
    * Get category api call
    * */
    public static void getCategories(final Activity activity, String parentId, String pageNo, final CategoryApiResponse result) {

        final ProgressDialog dialog = ProgressDialog.show(activity, null, "Please wait..");
        dialog.setCancelable(false);
        dialog.show();
        String url = URL.GetCategory + parentId + "/" + pageNo;
        Log.d("log", "GET PARENT CATEGORY URL : " + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("log", "GET PARENT CATEGORY RESPONSE : " + response);
                dialog.dismiss();
                result.gotResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                Log.d("log", "GET PARENT CATEGORY API FAILED");

                if (error instanceof NetworkError) {
                    Constants.showToast(activity, activity.getString(R.string.msg_failed_to_load));
                } else {
                    Constants.showToast(activity, activity.getString(R.string.msg_failed_to_load));
                }
            }
        });

        Volley.newRequestQueue(activity).add(request);

    }

    /*
    * Get category api call
    * */
    public static void getProducts(final Activity activity, String parentId, String pageNo, final CategoryApiResponse result) {

        String url = URL.GetProduct + parentId + "/" + pageNo;
        Log.d("log", "GET PARENT CATEGORY URL : " + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("log", "GET PARENT CATEGORY RESPONSE : " + response);
                result.gotResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("log", "GET PARENT CATEGORY API FAILED");

                if (error instanceof NetworkError) {
                    Constants.showToast(activity, activity.getString(R.string.msg_failed_to_load));
                } else {
                    Constants.showToast(activity, activity.getString(R.string.msg_failed_to_load));
                }
            }
        });

        Volley.newRequestQueue(activity).add(request);

    }

    /*
    * Abstract method to return result to the calling class
    * */
    public static abstract class CategoryApiResponse {
        public abstract void gotResponse(String response);
    }
}
