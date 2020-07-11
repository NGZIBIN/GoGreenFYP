package com.example.gogreenfyp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import  android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    TextView totalTimeUsed, nextUnlock, tvUsername;
    ImageView badgeImage, infoImg;
    ImageView profileImg;
    CardView badgesCardView;
    ProgressBar pb;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageRef;
    String userID;
    int _PROGRESS = 0;

    private Uri imageUri;
    private Bitmap compressor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

//        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_info, null);
//        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        totalTimeUsed = view.findViewById(R.id.totalTimeUsed);
        nextUnlock = view.findViewById(R.id.tvNextCheckpoint);
        profileImg = view.findViewById(R.id.profileImg);
        tvUsername = view.findViewById(R.id.tvUsername);
        badgeImage = view.findViewById(R.id.badgeImage);
        badgesCardView = view.findViewById(R.id.cardView);
        infoImg = view.findViewById(R.id.infoImg);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference("Images");
        pb = view.findViewById(R.id.pb);
        userID = fAuth.getCurrentUser().getUid();
        fStore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String userIDAuth = "";
                    String username = "";
                    int progressCount = 0;
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        userIDAuth = user.getUserID();
                        progressCount = user.getBadgeProgress();
                        username = user.getUsername();
                        if (userIDAuth.equals(userID)) {
                            tvUsername.setText(username);
                            if (progressCount == 0) {
                                _PROGRESS = 0;
                            } else {
                                _PROGRESS = progressCount;
                            }
                            pb.setProgress(_PROGRESS);
                            totalTimeUsed.setText(_PROGRESS + "");

                            if (progressCount < 10) {
                                badgeImage.setImageResource(R.drawable.smallrookiebadge);
                                nextUnlock.setText("10");
                                pb.setMax(10);
                            } else if (progressCount < 25) {
                                badgeImage.setImageResource(R.drawable.smallelitebadge);
                                nextUnlock.setText("25");
                                pb.setMax(25);
                            } else {
                                badgeImage.setImageResource(R.drawable.smallprestigebadge);
                                nextUnlock.setText("50");
                                pb.setMax(100);
                            }
                        }
                    }
                }
            }
        });


        //Setting Fragment into FrameLayout

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment badgeFrag = new BadgesFragment();
        ft.replace(R.id.frameBadges, badgeFrag);

        ft.commit();


        //Set profile Image
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else {

                        choseImage();

                    }
                }else{
                    choseImage();
                }
            }
        });

        //Getting info of how to gain points
        infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(getContext());
                myBuilder.setMessage("You can earn points by using GoGreen wallet and earn bonus points by using reusable cointainers!");
                myBuilder.setCancelable(true);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });


        return view;
    }
//
    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(requireActivity());


    }

//    private void Fileuploader(){
//        StorageReference Ref = storageRef.child(System.currentTimeMillis() + "."+getExtension(imageUri));
//        Ref.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.;
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });
//    }

//    private String getExtension(Uri uri){
//        ContentResolver cr = getContext().getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
//    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1 && requestCode == RESULT_OK && data != null && data.getData()!=null){
//            imageUri = data.getData();
//            profileImg.setImageURI(imageUri);
//        };
//    }
    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                imageUri = result.getUri();
                profileImg.setImageURI(imageUri);
                Log.d("PASS TAG", "PASSSSSSSSSSSSSSSSSP PASSSSSSSSSSSSSS PASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Log.d("TAG",  "FAIL FIAL FIAL FAIL");
            }
        }
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

