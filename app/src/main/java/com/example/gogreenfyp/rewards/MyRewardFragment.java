package com.example.gogreenfyp.rewards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.gogreenfyp.R;
import com.example.gogreenfyp.adapters.RewardRecyclerViewAdapter;
import com.example.gogreenfyp.adapters.YourRewardRecyclerViewAdapter;
import com.example.gogreenfyp.pojo.Rewards;
import com.example.gogreenfyp.pojo.User;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyRewardFragment extends Fragment {
    List<Rewards> listReward;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rewardsCollectionRef = db.collection("Rewards");
    private CollectionReference usersCollectionRef = db.collection("Users");

    // Firebase Auth
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    SearchView searchView;
    String USER_ID;
    String CURR_USER_ID;
    ArrayList<String> CATALOG_REWARDS_ID;
    ArrayList<String> USER_REWARDS = new ArrayList<String>();

    Date CURR_DATE = Calendar.getInstance().getTime();
    int requestCode = 123;
    int notificationID = 888;

    public MyRewardFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_reward, container, false);

        // Get current authenticated userid
        fAuth = FirebaseAuth.getInstance();
        CURR_USER_ID = fAuth.getCurrentUser().getUid();
        searchView = view.findViewById(R.id.searchViewYourReward);

        // Loop through userRewards array in user document
        usersCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    // Declare empty arraylist to store user rewards
                    USER_REWARDS = new ArrayList<>();

                    // Loop through all user documents
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        // Convert user document into User objects
                        User user = documentSnapshot.toObject(User.class);

                        // Get all user id
                        String USER_ID = user.getUserID();

                        // Check if current user id == USER_ID
                        if (CURR_USER_ID.equals(USER_ID)) {

                            // Get current user's rewards and store in list
                            USER_REWARDS = (ArrayList<String>) documentSnapshot.get("userRewards");
                        }
                    }
                }


                // Get recyclerview layout
                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewYourReward);

                rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            // Declare empty arraylist
                            listReward = new ArrayList<>();

                            // Declare empty arraylist for rewrads ID
                            CATALOG_REWARDS_ID = new ArrayList<>();

                            // Loop through all rewards
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Convert rewards document into Rewards objects
                                Rewards rewards = document.toObject(Rewards.class);

                                //Log.d("REWARDS", listReward.toString());

                                for(int j = 0; j < USER_REWARDS.size(); j++)
                                {
                                    if (document.getId().equals(USER_REWARDS.get(j))) {
                                        // Add rewards to list
                                        listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
                                                rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL(), rewards.getUseByDate(), rewards.getExpired()));

                                    }
                                }
                                // Log.d("REWARDS end", listReward.toString());
                            }

                            // Setup card view to adapter
                            YourRewardRecyclerViewAdapter myAdapter = new YourRewardRecyclerViewAdapter(getContext(),listReward);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            recyclerView.setAdapter(myAdapter);
                        }
                    }
                });
            }
        });

        return view;
    }
    public class dateDiff{
        public long daysBetween(Date one, Date two){
            long difference = (one.getTime() - two.getTime())/ 86400000;
            return Math.abs(difference);
        }
    }

}
