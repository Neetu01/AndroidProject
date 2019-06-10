package com.gardens.need.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gardens.need.R;

/**
 * Created by Manish on 3/12/2017.
 */

public class ContactUs extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        TextView tv_link = (TextView) view.findViewById(R.id.tv_link);
        tv_link.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
