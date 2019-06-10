package com.gardens.need.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gardens.need.R;
import com.gardens.need.activites.Homescreen;
import com.gardens.need.helpers.Constants;
import com.gardens.need.helpers.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by tarun on 3/12/2017.
 */

public class Enquiry extends Fragment {

    AppCompatEditText etSubject, etName, etEmail, etPhone, etMessage;

    String subject, name, email, phone, message;

    Homescreen home;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enquiry, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        home = (Homescreen) getActivity();

        TextView tv_link = (TextView) view.findViewById(R.id.tv_link);
        tv_link.setMovementMethod(LinkMovementMethod.getInstance());

        etSubject = (AppCompatEditText) view.findViewById(R.id.et_subject);
        etName = (AppCompatEditText) view.findViewById(R.id.et_name);
        etEmail = (AppCompatEditText) view.findViewById(R.id.et_email);
        etPhone = (AppCompatEditText) view.findViewById(R.id.et_phone);
        etMessage = (AppCompatEditText) view.findViewById(R.id.et_message);

        String msg = home.getSelectedProducts();

        if(!msg.isEmpty()){
            etMessage.setText(msg);
        }

        Button bSend = (Button) view.findViewById(R.id.b_send);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInput()) {
                    sendEnquiry();
                }

            }
        });
    }

    /*
    * Checks the inputted values
    * */
    public boolean checkInput() {

        subject = etSubject.getText().toString();
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        message = etMessage.getText().toString();

        if (subject.isEmpty()) {
            Constants.showToast(getActivity(), getString(R.string.msg_blank_subject));
        } else if (name.isEmpty()) {
            Constants.showToast(getActivity(), getString(R.string.msg_blank_name));
        } else if (email.isEmpty()) {
            Constants.showToast(getActivity(), getString(R.string.msg_blank_email));
        } else if (phone.isEmpty()) {
            Constants.showToast(getActivity(), getString(R.string.msg_blank_phone));
        } else if (message.isEmpty()) {
            Constants.showToast(getActivity(), getString(R.string.msg_blank_message));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Constants.showToast(getActivity(), getString(R.string.msg_email));
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            Constants.showToast(getActivity(), getString(R.string.msg_phone));
        } else {
            return true;
        }

        return false;
    }

    /*
    * Calls enquiry api
    * */
    public void sendEnquiry() {

        final ProgressDialog dialog = ProgressDialog.show(getActivity(),null,"Please wait..");
        dialog.setCancelable(false);
        dialog.show();

        Log.d("log","SEND ENQUIRY URL : "+URL.SendEnquiry);
        StringRequest request = new StringRequest(Request.Method.POST, URL.SendEnquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Log.d("log","SEND ENQUIRY API RESPONSE : "+response);

                try{

                    JSONObject object = new JSONObject(response);

                    if(object.getString("status").equalsIgnoreCase("success")){
                        Constants.showToast(getActivity(),"Successfully sent your selected products");
                        etSubject.setText("");
                        etName.setText("");
                        etEmail.setText("");
                        etPhone.setText("");
                        etMessage.setText("");
                    }else{

                        JSONArray errors = object.getJSONArray("error");
                        JSONObject msg = errors.getJSONObject(0);
                        Constants.showToast(getActivity(),msg.getString("message"));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                Log.d("log","SEND ENQUIRY API FAILED");
                Constants.showToast(getActivity(),getString(R.string.msg_failed_to_load));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put("subject",subject);
                params.put("name",name);
                params.put("email",email);
                params.put("phone",phone);
                params.put("message",message);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);

    }

}
