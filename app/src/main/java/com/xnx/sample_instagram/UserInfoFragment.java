package com.xnx.sample_instagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfoFragment extends Fragment {

    FirebaseAuth user = FirebaseAuth.getInstance();

    Button logoutButton;
    Button withdrawButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_user_info, container, false);

        logoutButton = (Button)rootView.findViewById(R.id.logoutButton_userInfo);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.signOut();
                startActivity(MainActivity.class, false);
                getActivity().finish();
            }
        });

        withdrawButton = (Button)rootView.findViewById(R.id.withdrawButton_userInfo);

        return rootView;
    }

    public void startActivity(Class c, Boolean noHistory) {
        Intent intent = new Intent(getActivity(), c);
        if(noHistory)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
