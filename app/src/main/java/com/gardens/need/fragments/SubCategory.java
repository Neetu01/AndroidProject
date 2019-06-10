package com.gardens.need.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

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

public class SubCategory extends Fragment {

    GridView gv_dashboard;
    List<ProductList> productList = new ArrayList<>();

    GridAdapter ad;

    int count = 1;

    Homescreen home;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        home = (Homescreen) getActivity();

        TextView tv_link = (TextView) view.findViewById(R.id.tv_link);
        tv_link.setMovementMethod(LinkMovementMethod.getInstance());

        gv_dashboard = (GridView) view.findViewById(R.id.gv_subCategory);
        ad = new GridAdapter(getActivity(), productList, gridItemsListener);
        gv_dashboard.setAdapter(ad);

        if (productList.isEmpty())
            getCategories();
    }

    /*
    * Get API response
    * */
    public void getCategories() {
        URL.getCategories(getActivity(), String.valueOf(home.subParentId), String.valueOf(count), new URL.CategoryApiResponse() {
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
                            getCategories();
                        }
                    } else {

                        getProducts();

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

    private void getProducts() {
        URL.getProducts(getActivity(), String.valueOf(home.subParentId), String.valueOf(count), new URL.CategoryApiResponse() {
            @Override
            public void gotResponse(String response) {
                if(isAdded()) {

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equalsIgnoreCase("success")) {
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject details = data.getJSONObject(i);

                                boolean isChecked = false;
                                for (int j = 0; j < home.selectedProducts.size(); j++) {
                                    if (home.selectedProducts.get(j).CategoryId == details.getInt("product_id")) {
                                        isChecked = true;
                                    }
                                }

                                productList.add(new ProductList(details.getInt("product_id"), 0, details.getString("name"), details.getString("description"), details.getString("image"), isChecked));
                            }

                            if (count < object.getInt("lastPage")) {
                                count++;
                                getProducts();
                            }
                        } else {
                            Constants.showToast(getActivity(), "No products found!!");
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        Log.d("log", "GET PARENT CATEGORY API JSON EXCEPTION");
                        Constants.showToast(getActivity(), getString(R.string.msg_failed_to_load));
                    }
                    ad.notifyDataSetChanged();
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

            home.detailsParentId = productList.get(position).CategoryId;
            Log.d("log", "DETAILS PARENT ID : " + home.detailsParentId);

            home.openFragmentByTag(Constants.ProductDetails, true);
        }
    };
}
