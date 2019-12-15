package com.xnx.sample_instagram;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.sql.Ref;
import java.util.*;

public class PostingFragment extends Fragment {

    private static final int POSTING_GALLERY_CODE = 10;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private ImageView imageView;
    private EditText description;
    private Button button;
    private String imagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        getActivity().startActivityForResult(intent, POSTING_GALLERY_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_writing, container, false);

        imageView = (ImageView)rootview.findViewById(R.id.imageView_writing);
        description = (EditText)rootview.findViewById(R.id.editText_writing);

        button = (Button)rootview.findViewById(R.id.button_writing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = getArguments().getString("imagePath");
                upload(imagePath);
            }
        });

        return rootview;
    }

    private void upload(String uri) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(uri));
        final StorageReference ref = storageRef.child("images/" + user.getEmail() + "_" + file.getLastPathSegment());
        final UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    PostingDTO postingDTO = new PostingDTO(
                            downloadUri.toString(),
                            description.getText().toString(),
                            user.getEmail(),
                            new Date(), 0);
                    db.collection("posts").document(user.getEmail() + "_" + new Date()).set(postingDTO);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}
