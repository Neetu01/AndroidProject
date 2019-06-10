package com.gardens.need.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gardens.need.R;
import com.gardens.need.activites.Homescreen;
import com.gardens.need.adapters.GridAdapter;
import com.gardens.need.adapters.ProductList;
import com.gardens.need.helpers.Constants;
import com.gardens.need.helpers.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 3/9/2017.
 */

public class Dashboard extends Fragment {

    GridView gv_dashboard;
    List<ProductList> productList = new ArrayList<>();

    GridAdapter ad;

    Homescreen home;

    int count = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        home = (Homescreen) getActivity();

        TextView tv_link = (TextView) view.findViewById(R.id.tv_link);
        tv_link.setMovementMethod(LinkMovementMethod.getInstance());

        gv_dashboard = (GridView) view.findViewById(R.id.gv_dashboard);
        ad = new GridAdapter(getActivity(), productList, gridItemsListener);
        gv_dashboard.setAdapter(ad);

        if (productList.isEmpty())
            getComplaints();

        // Gets product count
        StringRequest request = new StringRequest(URL.GetProductCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("log", "PRODUCTS FOUND");
                    JSONObject object = new JSONObject(response);
                    int count = object.getInt("count(*)");
                    SharedPreferences pref = getActivity().getSharedPreferences("gardens", Context.MODE_PRIVATE);
                    int prefCount = pref.getInt("productsCount", -1);
                    if (prefCount != -1 && prefCount < count) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setMessage("Total No. of New Products Found : " + String.valueOf(count - prefCount))
                                .setTitle("New products are found");
                        builder.show();
                    }

                    pref.edit().putInt("productsCount", count).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

    /*
    * Get API response
    * */
    public void getComplaints() {
        URL.getCategories(getActivity(), String.valueOf(home.parentId), String.valueOf(count), new URL.CategoryApiResponse() {
            @Override
            public void gotResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equalsIgnoreCase("success")) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject details = data.getJSONObject(i);

                            boolean isChecked = false;
                            for (int j = 0; j < home.selectedProducts.size(); j++) {
                                if (home.selectedProducts.get(j).CategoryId == details.getInt("category_id")) {
                                    isChecked = true;
                                }
                            }

                            productList.add(new ProductList(details.getInt("category_id"), details.getInt("parent_id"), details.getString("name"), details.getString("description"), details.getString("image"), isChecked));
                        }

                        if (count < object.getInt("lastPage")) {
                            count++;
                            getComplaints();
                        }
                    } else {
                        Constants.showToast(getActivity(), object.getString("message"));
                    }

                    ad.notifyDataSetChanged();

                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.d("log", "GET PARENT CATEGORY API JSON EXCEPTION");
                    Constants.showToast(getActivity(), getString(R.string.msg_failed_to_load));
                }
            }
        });
    }

    GridAdapter.GridItemsListener gridItemsListener = new GridAdapter.GridItemsListener() {
        @Override
        public void onCheckChanged(int position, ProductList productListData, boolean isChecked) {
            Log.d("log", "CHECK CHANGED\nGOT PRODUCT LIST DATA\nSELECTED PRODUCTS SIZE BEFORE CHANGE : " + home.selectedProducts.size());
            if (isChecked && !home.selectedProductsId.contains(productListData.CategoryId)) {
                home.selectedProducts.add(productListData);
                home.selectedProductsId.add(productListData.CategoryId);
            } else {
                home.selectedProducts.remove(productListData);
                home.selectedProductsId.remove((Integer) productListData.CategoryId);
            }

            productListData.isChecked = isChecked;
            productList.set(position, productListData);

            Log.d("log", "SELECTED PRODUCTS SIZE AFTER CHANGE : " + home.selectedProducts.size());
        }

        @Override
        public void onItemClicked(int position, View view) {
            Log.d("log", "GRID VIEW ITEM CLICKED!!");
            Log.d("log", "SUBCATEGORY PARENT ID : " + productList.get(position).CategoryId);
            home.subParentId = productList.get(position).CategoryId;
            home.openFragmentByTag(Constants.SubCategory, true);
        }
    };
}
