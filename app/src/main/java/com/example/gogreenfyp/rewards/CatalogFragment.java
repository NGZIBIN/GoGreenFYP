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
import com.example.gogreenfyp.pojo.Rewards;
import com.example.gogreenfyp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends Fragment {
    List<Rewards> listReward;
    SearchView searchView;
    String CURR_USER_ID;
    FirebaseAuth fAuth;
    ArrayList<String> CATALOG_REWARDS_ID;
    ArrayList<String> USER_HISTORY_REWARDS_ID;
    ArrayList<String> USER_ALLREWARDS = new ArrayList<String>();
    ArrayList<String> USER_REWARDS = new ArrayList<String>();
    String currentUser = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rewardsCollectionRef = db.collection("Rewards");

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_rewards, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewAllReward);
        searchView = view.findViewById(R.id.searchViewAllReward);

        fAuth = FirebaseAuth.getInstance();
        CURR_USER_ID = fAuth.getCurrentUser().getUid();

        // Retrieve catalog of rewards
        rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    // Declare an empty arraylist to store rewards
                    listReward = new ArrayList<>();

                    // Declare empty arraylist to store rewards ID
                    CATALOG_REWARDS_ID = new ArrayList<>();

                    // Loop through all rewards documents
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // Convert documents to rewards object
                        Rewards rewards = document.toObject(Rewards.class);

                        // Add document id to array
                        CATALOG_REWARDS_ID.add(document.getId());

                        // Add rewards to list
                        listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
                            rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL(), rewards.getUseByDate(), rewards.getExpired()));


                    }

                }

                // Loop through userRewards array in user document
                db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            // Declare empty arraylist to store user rewards
                            USER_REWARDS = new ArrayList<>();
                            USER_HISTORY_REWARDS_ID = new ArrayList<>();

                            // Loop through all user documents
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                // Convert user document into User objects
                                User user = documentSnapshot.toObject(User.class);

                                // Get all user id
                                String USER_ID = user.getUserID();

                                // Check if current user id == USER_ID
                                if(CURR_USER_ID.equals(USER_ID)){
                                    // Get current user's rewards and store in list
                                    USER_REWARDS = (ArrayList<String>) documentSnapshot.get("userRewards");
                                    USER_HISTORY_REWARDS_ID = (ArrayList<String>) documentSnapshot.get("userRedeemedRewards");

                                    Log.d("USER REWARDS", USER_REWARDS.toString());
                                    Log.d("LIST REWARDS start", String.valueOf(listReward.size()));

                                    // Compare array, if id matches then remove from listRewards
                                    /*for(int i = 0; i < listReward.size(); i++)
                                    {
                                        Log.d("LIST REWARDS start", listReward.get(i).getName());
                                    }*/
                                    for(int i = 0; i < listReward.size(); i++)
                                    {
                                        for(int j = 0; j < USER_REWARDS.size(); j++)
                                        {
                                            // Log.d("LIST REWARDS", CATALOG_REWARDS_ID.toString());
                                            if((CATALOG_REWARDS_ID.get(i).equals(USER_REWARDS.get(j))))
                                            {
                                                listReward.remove(i);
//                                              Log.d("LIST REWARDS end", listReward.toString());
                                            }
                                        }
                                    }

                                    for(int i = 0; i < listReward.size(); i++)
                                    {
                                        for(int j = 0; j < USER_HISTORY_REWARDS_ID.size(); j++)
                                        {
                                            // Log.d("LIST REWARDS", CATALOG_REWARDS_ID.toString());
                                            if((CATALOG_REWARDS_ID.get(i).equals(USER_HISTORY_REWARDS_ID.get(j))))
                                            {
                                                listReward.remove(i);
//                                              Log.d("LIST REWARDS end", listReward.toString());
                                            }
                                        }
                                    }
                                }

                            }
                            Log.d("LIST REWARDS end", String.valueOf(listReward.size()));

                            // Setup card view to adapter
                            RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            recyclerView.setAdapter(myAdapter);
                        }
                    }
                });

            }
        });

        return view;
    }
}
