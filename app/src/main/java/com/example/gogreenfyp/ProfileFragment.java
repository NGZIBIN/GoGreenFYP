package com.example.gogreenfyp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import  android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import com.example.gogreenfyp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    Button btnLogout;
    TextView totalTimeUsed, nextUnlock, tvUsername, allBadges;
    ImageView badgeImage, infoImg, profileImg;
    CardView badgesCardView;
    ProgressBar pb;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageRef;
    String userID;
    int _PROGRESS = 0;
    private static final String KEY_POINTS = "pointsBalance";
    private Boolean rookie;
    private Boolean elite;
    private Boolean prestige;
    private Uri imageUri;
    private Bitmap compressor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

//        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_info, null);
//        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        btnLogout = view.findViewById(R.id.btnLogout);
        totalTimeUsed = view.findViewById(R.id.totalTimeUsed);
        nextUnlock = view.findViewById(R.id.tvNextCheckpoint);
        profileImg = view.findViewById(R.id.profileImg);
        tvUsername = view.findViewById(R.id.tvUsername);
        badgeImage = view.findViewById(R.id.badgeImage);
        allBadges = view.findViewById(R.id.allBadges);
        infoImg = view.findViewById(R.id.infoImg);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                    int points = 0;
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        userIDAuth = user.getUserID();
                        progressCount = user.getBadgeProgress();
                        username = user.getUsername();
                        points = user.getPointsBalance();
                        if (userIDAuth.equals(userID)) {
                            final String currentUser = documentSnapshot.getId();
                            tvUsername.setText(username);
                            if (progressCount == 0) {
                                _PROGRESS = 0;
                            } else {
                                _PROGRESS = progressCount;
                            }
                            pb.setProgress(_PROGRESS);
                            totalTimeUsed.setText(_PROGRESS + "");

                            //Badges Progress
                            if(progressCount >= 50){
                                badgeImage.setImageResource(R.drawable.smallprestigebadge);
                                nextUnlock.setText("100");
                                DocumentReference badgeArray = db.collection("Users").document(currentUser);
                                badgeArray.update("userBadges", FieldValue.arrayUnion("wi6cFBpA8RE8pAg7uhZN"));
                                pb.setMax(100);
                            }
                            else if(progressCount >= 25){
                                badgeImage.setImageResource(R.drawable.smallelitebadge);
                                nextUnlock.setText("50");
                                DocumentReference badgeArray = db.collection("Users").document(currentUser);
                                badgeArray.update("userBadges", FieldValue.arrayUnion("aDnbDhHpLv3cynDIaxre"));
                                pb.setMax(50);

                            }
                            else if(progressCount >= 10){
                                badgeImage.setImageResource(R.drawable.smallrookiebadge);
                                nextUnlock.setText("25");
                                DocumentReference badgeArray = db.collection("Users").document(currentUser);
                                badgeArray.update("userBadges", FieldValue.arrayUnion("VPluIGTSFoPK3OU5K7bh"));
                                pb.setMax(25);
                            }
                            else {
                                nextUnlock.setText("10");
                            }

                            SharedPreferences settings = getActivity().getSharedPreferences("prefs", 0);
                            rookie = settings.getBoolean("rookieFirst", true);
                            elite = settings.getBoolean("eliteFirst", true);
                            prestige = settings.getBoolean("prestigeFirst", true);

//                            //Badges Points
                            if(progressCount == 10 && rookie){
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("rookieFirst", false);
                                editor.commit();
                                int newPoints = points + 200;
                                DocumentReference badgeArray = db.collection("Users").document(currentUser);
                                Map<String, Object> pointsNew = new HashMap<>();
                                pointsNew.put(KEY_POINTS, newPoints);
                                badgeArray.set(pointsNew, SetOptions.merge());
                                Log.d("Rookie", rookie+"");
                            }
                            else if(progressCount == 25 && elite ){
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("eliteFirst", false);
                                editor.commit();
                                int newPoints = points + 350;
                                DocumentReference badgeArray = db.collection("Users").document(currentUser);
                                Map<String, Object> pointsNew = new HashMap<>();
                                pointsNew.put(KEY_POINTS, newPoints);
                                badgeArray.set(pointsNew, SetOptions.merge());
                            }
                            else if(progressCount == 50 && prestige){
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("prestigeFirst", false);
                                editor.commit();
                                int newPoints = points + 550;
                                DocumentReference badgeArray = db.collection("Users").document(currentUser);
                                Map<String, Object> pointsNew = new HashMap<>();
                                pointsNew.put(KEY_POINTS, newPoints);
                                badgeArray.set(pointsNew, SetOptions.merge());
                            }
                        }
                    }
                }
            }
        });
        //Going all Badges activity
        allBadges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AllBadgesActivity.class);
                startActivity(i);
            }
        });
        //Setting Fragment into FrameLayout
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment badgeFrag = new UserBadgesFragment();
        ft.replace(R.id.frameBadges, badgeFrag);

        ft.commit();
        //Set profile Image
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(getContext(), "Permission not Granted", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else {
                        choseImage();
                    }
                }
                else{
                    choseImage();
                }
            }
        });
        //Getting info of how to gain points
        infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(getContext());
                myBuilder.setTitle("GoGreen");
                myBuilder.setMessage("You can earn points by using GoGreen wallet and earn bonus points by using reusable cointainers!");
                myBuilder.setCancelable(true);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        // Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Confirm Logout Dialog
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_logout);
                dialog.setCancelable(false);
                dialog.show();

                Button btnYes, btnNo;

                btnYes = dialog.findViewById(R.id.btnConfirmLogout);
                btnNo = dialog.findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        // Sign out user from Firebase
                        FirebaseAuth.getInstance().signOut();

                        // Set intent to login
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        // Prevent back button access
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        return view;
    }

    private void choseImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(getActivity() != null && intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, 550);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 550 && data != null) {
            Uri selectedImage =  data.getData();
            if (selectedImage != null && getActivity() != null) {
                try {
                    InputStream stream = getActivity().getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    profileImg.setImageBitmap(bitmap);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void saveImage(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        if(fAuth.getCurrentUser() != null) {
            String userId = fAuth.getCurrentUser().getUid();
        }
    }
}

