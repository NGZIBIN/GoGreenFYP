package com.example.gogreenfyp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class        YourRewardFragment extends Fragment {
    List<Rewards> listReward;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rewardsCollectionRef = db.collection("Rewards");
    private CollectionReference usersCollectionRef = db.collection("Users");

    // Firebase Auth
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    SearchView searchView;
    String USER_ID;
    ArrayList<String> USER_REWARDS;
    int requestCode = 123;
    int notificationID = 888;

    public YourRewardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_reward, container, false);

        // Get current authenticated userid
        fAuth = FirebaseAuth.getInstance();
        USER_ID = fAuth.getCurrentUser().getUid();
        searchView = view.findViewById(R.id.searchViewYourReward);

        USER_REWARDS = new ArrayList<String>();

        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        User user = documentSnapshot.toObject(User.class);
                        String USER_ID_AUTH = user.getUserID();

                        // Get all rewards for this user
                        if(USER_ID.equals(USER_ID_AUTH)){
                            USER_REWARDS = (ArrayList<String>) documentSnapshot.get("userRewards");
                            Log.d("REWARDS", USER_REWARDS.toString());
                        }
                    }
                }
            }
        });

        // Search all rewards and retrieve rewards that match reward id
        listReward = new ArrayList<>();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewYourReward);

        rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        if(USER_REWARDS.size() != 1){
                            for (int i = 0; i < USER_REWARDS.size(); i++){
                                if(document.getId().equals(USER_REWARDS.get(i))){
                                    //Log.d("PRINT_REWARDS", USER_REWARDS.get(i));

                                    Rewards rewards = document.toObject(Rewards.class);
                                    String title = rewards.getName();
                                    Date date = rewards.getUseByDate();
                                    Date currDate = Calendar.getInstance().getTime();
                                    Log.d("CURRENT DATE", String.valueOf(currDate));
                                    SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String rewardDate = spf.format(date);
                                    String newCurrDate = spf.format(currDate);
                                    Log.d("REWARD DATE", String.valueOf(date));
                                    Log.d("CURRENT DATE", String.valueOf(newCurrDate));



                                    if(currDate.after(date)){
                                        Log.d("CORRECT", "YAY");
                                        NotificationManager notificationManager = (NotificationManager)
                                                getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            NotificationChannel channel = new
                                                    NotificationChannel("default", "Default Channel",
                                                    NotificationManager.IMPORTANCE_DEFAULT);

                                            channel.setDescription("This is for default notification");
                                            notificationManager.createNotificationChannel(channel);
                                        }
                                        Intent intent = new Intent(getContext(), YourRewardFragment.class);
                                        PendingIntent pIntent = PendingIntent.getActivity(getContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "default");
                                        builder.setContentTitle("Alert!");
                                        builder.setContentText("Your reward have expired!");
                                        builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
                                        builder.setContentIntent(pIntent);
                                        builder.setAutoCancel(true);
                                        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        builder.setSound(uri);

                                        builder.setPriority(Notification.PRIORITY_HIGH);
                                        Notification n = builder.build();
                                        notificationManager.notify(notificationID, n);

                                        //Current date is after Reward expiry date
                                        //DO delete
                                        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    String userIDAuth = "";
                                                    for(final DocumentSnapshot documentSnapshots:task.getResult()){
                                                        User user = documentSnapshots.toObject(User.class);
                                                        userIDAuth = user.getUserID();
                                                        if(userIDAuth.equals(fAuth.getCurrentUser().getUid())){
                                                            final String currentUser = documentSnapshots.getId();
                                                            db.collection("Rewards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if(task.isSuccessful()){
                                                                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                                                                            Rewards rewards = documentSnapshot.toObject(Rewards.class);
                                                                            String titleCurrent = rewards.getName();
                                                                            if(title.equals(titleCurrent)){
                                                                                String currentReward = documentSnapshot.getId();
                                                                                DocumentReference rewardArray = db.collection("Users").document(currentUser);
//
                                                                                rewardArray.update("userRewards", FieldValue.arrayRemove(currentReward));

                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }else{


                                    }

                                    Log.d("Tag date", date + "");
                                    Toast.makeText(getContext(), date + "", Toast.LENGTH_SHORT).show();
                                    listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
                                            rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL(), rewards.getUseByDate()));
                                    //Log.d("PRINT_REWARDS2", rewards.getName());
                                }
                            }
                        }

                    }
                    // Display the rewards that have been redeem by user
                    YourRewardRecyclerViewAdapter myAdapter = new YourRewardRecyclerViewAdapter(getContext(),listReward);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            myAdapter.getFilter().filter(s);
                            return false;
                        }
                    });
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });
        return view;
    }


}
