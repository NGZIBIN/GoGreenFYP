package com.example.gogreenfyp;

import android.content.Intent;
import android.graphics.Bitmap;
import  android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    TextView totalTimeUsed, nextUnlock, tvUsername;
    ImageView badgeImage;
    CircleImageView profileImg;
    ProgressBar pb;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String PROFILE_IMAGE_URL = null;
    int _PROGRESS = 0;

    int REQUEST_IMAGE_CAPTURE = 100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

//        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_info, null);
//        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        totalTimeUsed = view.findViewById(R.id.totalTimeUsed);
        nextUnlock = view.findViewById(R.id.tvNextCheckpoint);
        tvUsername = view.findViewById(R.id.tvUsername);
        badgeImage = view.findViewById(R.id.badgeImage);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        pb = view.findViewById(R.id.pb);
        userID = fAuth.getCurrentUser().getUid();
        fStore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String userIDAuth = "";
                    String username = "";
                    int progressCount = 0;
                    for(DocumentSnapshot documentSnapshot:task.getResult()){
                        User user = documentSnapshot.toObject(User.class);
                        userIDAuth = user.getUserID();
                        progressCount = user.getBadgeProgress();
                        username = user.getUsername();
                        if(userIDAuth.equals(userID)){
                            tvUsername.setText(username);
                            if(progressCount == 0){
                                _PROGRESS = 0;
                            }else{
                                _PROGRESS = progressCount;
                            }
                            pb.setProgress(_PROGRESS);
                            totalTimeUsed.setText(_PROGRESS + "");

                            if(progressCount < 10){
                              //  badgeImage.setImageResource(R.drawable.smallrookiebadge);
                                nextUnlock.setText("10");
                                pb.setMax(10);
                            }
                            else if(progressCount < 25){
                              //  badgeImage.setImageResource(R.drawable.smallelitebadge);
                                nextUnlock.setText("25");
                                pb.setMax(25);
                            }else{
                              //  badgeImage.setImageResource(R.drawable.smallprestigebadge);
                                nextUnlock.setText("50");
                                pb.setMax(100);
                            }
                        }
                    }
                }
            }
        });


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
//    public void onClick(View view) {
//        if(view.getId() == R.id.infoImg){
//            Intent i = new Intent(getActivity().getApplicationContext(), infoActivity.class);
//            startActivity(i);
//        }
//    }

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

