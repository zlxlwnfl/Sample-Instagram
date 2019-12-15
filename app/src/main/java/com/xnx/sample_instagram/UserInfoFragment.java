package com.xnx.sample_instagram;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserInfoFragment extends Fragment {
    private static final int USERINFO_GALLERY_CODE = 11;
    private static final int USERINFO_USERUPDATE_CODE = 12;

    FirebaseAuth user = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_user_info, container, false);

        getImagesPathAndRecyclerInit(rootView);
        Log.d("이미지 가져오기 및 리사이클러뷰 초기화", "완료");

        ImageView userImageView = (ImageView)rootView.findViewById(R.id.userImageView);
        userImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
               getActivity().startActivityForResult(intent, USERINFO_GALLERY_CODE);
           }
        });

        TextView userNameTextView = (TextView)rootView.findViewById(R.id.userNameTextView);
        userNameTextView.setText(user.getCurrentUser().getEmail());

        Button userUpdateButton = (Button)rootView.findViewById(R.id.userUpdateButton);
        userUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivityForResult(intent, USERINFO_USERUPDATE_CODE);
            }
        });

        Button logoutButton = (Button)rootView.findViewById(R.id.logoutButton_userInfo);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.signOut();

                startActivity(MainActivity.class, false);
                getActivity().finish();
            }
        });

        Button withdrawButton = (Button)rootView.findViewById(R.id.withdrawButton_userInfo);
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getCurrentUser().delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "회원 탈퇴에 성공하였습니다.", Toast.LENGTH_LONG).show();

                                    startActivity(MainActivity.class, false);
                                    getActivity().finish();
                                }
                            }
                        });
            }
        });

        return rootView;
    }

    public ArrayList<String> getImagesPathAndRecyclerInit(final ViewGroup rootView) {
        final ArrayList<String> postList = new ArrayList<>();

        db.collection("posts")
                .whereEqualTo("userId", user.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("사진 가져오기 성공", document.getId() + " => " + document.getData());
                                postList.add(document.getData().get("imageUrl").toString());
                            }

                            recyclerInit(rootView, postList);
                        } else {
                            Log.d("사진 가져오기 실패", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return postList;
    }

    private void recyclerInit(ViewGroup rootView, ArrayList<String> mDataset) {
        final int numOfColumns = 3;

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), numOfColumns);

        // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GalleryAdapter(this, mDataset);
        recyclerView.setAdapter(mAdapter);
    }

    public void startActivity(Class c, Boolean noHistory) {
        Intent intent = new Intent(getActivity(), c);
        if(noHistory)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
