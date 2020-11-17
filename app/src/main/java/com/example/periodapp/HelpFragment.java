package com.example.periodapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HelpFragment extends Fragment {
    ImageView contactUs;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        contactUs= view.findViewById(R.id.contactUs);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .setPackage("com.google.android.gm")
                        .putExtra(Intent.EXTRA_EMAIL, new String[]{"puzniakmichal@gmail.com"})
                        .putExtra(Intent.EXTRA_SUBJECT, "Help")
                        .putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my demo app :-)");
                startActivity(intent);
            }
        });
    }

}