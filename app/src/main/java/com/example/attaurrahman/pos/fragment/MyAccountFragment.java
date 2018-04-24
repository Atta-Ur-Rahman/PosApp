package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attaurrahman.pos.R;


public class MyAccountFragment extends Fragment {
    View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_my_account, container, false);


        return parentView;
    }


}
