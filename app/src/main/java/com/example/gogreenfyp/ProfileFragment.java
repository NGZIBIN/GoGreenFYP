package com.example.gogreenfyp;

import android.content.Intent;
import android.graphics.Bitmap;
import  android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    CircleImageView profileImg;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String PROFILE_IMAGE_URL = null;
    int REQUEST_IMAGE_CAPTURE = 100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImg = view.findViewById(R.id.profileImg);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureIntent();
            }
        });


    }

    private void takePictureIntent(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_IMAGE_CAPTURE){
//            switch (requestCode){
//                case RESULT_OK:
//                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                    profileImg.setImageBitmap(bitmap);
//                    handleUpload(bitmap);
//            }
//        }
//    }
//
////    private void handleUpload(Bitmap bitmap){
////        ByteArrayOutputStream boas = new ByteArrayOutputStream();
////        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);
////
////        StorageReference re
////    }
}

