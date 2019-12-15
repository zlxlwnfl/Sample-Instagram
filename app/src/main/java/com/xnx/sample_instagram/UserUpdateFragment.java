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
import android.widget.EditText;
import android.widget.TextView;

public class UserUpdateFragment extends Fragment {
    private static final int USERINFO_USERUPDATE_CLEAR_CODE = 13;

    private EditText description;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_user_update, container, false);

        description = (EditText)rootView.findViewById(R.id.editText_userInfo);

        button = (Button)rootView.findViewById(R.id.userInfoUpdateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description.getText() != null) {
                    TextView textView = rootView.findViewById(R.id.userDescriptionTextView);
                    textView.setText(description.getText());
                }

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivityForResult(intent, USERINFO_USERUPDATE_CLEAR_CODE);
            }
        });

        return rootView;
    }

}
