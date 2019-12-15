package com.xnx.sample_instagram;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        getImagesPathAndRecyclerInit(rootView);
        Log.d("이미지 가져오기 및 리사이클러뷰 초기화", "완료");

        return rootView;
    }

    public ArrayList<PostingDTO> getImagesPathAndRecyclerInit(final ViewGroup rootView) {
        final ArrayList<PostingDTO> postList = new ArrayList<>();

        db.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("사진 가져오기 성공", document.getId() + " => " + document.getData());
                                //postList.add(document.getData().get("imageUrl").toString());
                                postList.add(new PostingDTO(
                                        document.getData().get("imageUrl").toString(),
                                        document.getData().get("description").toString(),
                                        document.getData().get("userId").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        Integer.parseInt(document.getData().get("good").toString())
                                ));
                            }

                            recyclerInit(rootView, postList);
                        } else {
                            Log.d("사진 가져오기 실패", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return postList;
    }

    private void recyclerInit(ViewGroup rootView, ArrayList<PostingDTO> mDataset) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new HomeAdapter(this, mDataset);
        recyclerView.setAdapter(mAdapter);
    }
}
