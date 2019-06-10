package com.gardens.need.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gardens.need.R;
import com.gardens.need.activites.Homescreen;
import com.gardens.need.adapters.ProductDetailsAdapter;
import com.gardens.need.adapters.ProductList;
import com.gardens.need.helpers.Constants;
import com.gardens.need.helpers.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 3/18/2017.
 */

public class ProductDetails extends Fragment {

    Homescreen home;

    RecyclerView rv;
    RecyclerView.Adapter ad;
    RecyclerView.LayoutManager lm;

    int count = 1;

    List<ProductList> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        home = (Homescreen) getActivity();

        TextView tv_link = (TextView) view.findViewById(R.id.tv_link);
        tv_link.setMovementMethod(LinkMovementMethod.getInstance());

        rv = (RecyclerView) view.findViewById(R.id.rv_productDetails);

        lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);

        ad = new ProductDetailsAdapter(home, productList);
        rv.setAdapter(ad);

//        getDetails();
        getProducts();
    }


    public void getDetails() {
        URL.getCategories(getActivity(), String.valueOf(home.detailsParentId), String.valueOf(count), new URL.CategoryApiResponse() {
            @Override
            public void gotResponse(String response) {
                Log.d("log", "GET DETAILS API RESPONSE : " + response);
                try {

                    JSONObject object = new JSONObject(response);

                    if (object.getString("status").equalsIgnoreCase("success")) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject details = data.getJSONObject(i);

                            boolean isChecked = false;
                            for(int j=0;j<home.selectedProducts.size();j++){
                                if(home.selectedProducts.get(j).CategoryId == details.getInt("category_id")){
                                    isChecked = true;
                                }
                            }

                            productList.add(new ProductList(details.getInt("category_id"), details.getInt("parent_id"), details.getString("name"), details.getString("description"), details.getString("image"), isChecked));                        }

                        if(count < object.getInt("lastPage")){
                            count++;
                            getDetails();
                        }
                    }else{
                        getProducts();
                    }

                    ad.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    if(isAdded()){
                        Log.d("log", "GET DETAILS PARENT CATEGORY API JSON EXCEPTION");
                        Constants.showToast(getActivity(), getString(R.string.msg_failed_to_load));
                    }
                }

            }
        });
    }


    private void getProducts() {
        URL.getProducts(getActivity(), String.valueOf(home.detailsParentId), String.valueOf(count), new URL.CategoryApiResponse() {
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
                        if(isAdded()){
                            Log.d("log", "GET PARENT CATEGORY API JSON EXCEPTION");
                            Constants.showToast(getActivity(), getString(R.string.msg_failed_to_load));
                        }
                    }
                    ad.notifyDataSetChanged();
                }
            }
        });
    }

}
