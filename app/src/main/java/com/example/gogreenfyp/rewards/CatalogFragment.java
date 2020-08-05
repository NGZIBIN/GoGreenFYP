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
    String USER_ID;
    FirebaseAuth fAuth;
    ArrayList<String> USER_ALLREWARDS = new ArrayList<String>();
    ArrayList<String> USER_YOURREWARDS = new ArrayList<String>();
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
        USER_ID = fAuth.getCurrentUser().getUid();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        User user = documentSnapshot.toObject(User.class);
                        String USER_ID_AUTH = user.getUserID();

                        // Get all rewards for this user
                        if(USER_ID.equals(USER_ID_AUTH)){
                            currentUser = documentSnapshot.getId();
                            Log.d("Current user", currentUser);
                            USER_ALLREWARDS = (ArrayList<String>) documentSnapshot.get("allRewards");
                            USER_YOURREWARDS = (ArrayList<String>) documentSnapshot.get("userRewards");
                            if(USER_ALLREWARDS != null) {
                                Log.d("ALL REWARDS", USER_ALLREWARDS.toString());
                            }
                            if(USER_YOURREWARDS != null){
                                Log.d("USER REWARDS", USER_YOURREWARDS.toString());
                            }
                        }
                    }
                    listReward = new ArrayList<>();


                    rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document: task.getResult()){
//                                    for(int i = 0; i < USER_ALLREWARDS.size(); i ++){
//                                        for(int x = 0; x < USER_YOURREWARDS.size(); x ++){
//                                            Log.d("All rewards " , String.valueOf(USER_ALLREWARDS));
//                                            Log.d("Your rewards " , String.valueOf(USER_YOURREWARDS));
//                                            if(!document.getId().equals(USER_ALLREWARDS.get(i))){
//                                                if(!document.getId().equals(USER_YOURREWARDS.get(x))){
//                                                    String rewardNew = document.getId();
//                                                    Log.d("NEW REWARD", rewardNew);
////
//                                                    DocumentReference rewardArray = db.collection("Users").document(currentUser);
//                                                    rewardArray.update("allRewards", FieldValue.arrayUnion(rewardNew));
//                                                }
//                                            }
//
//                                        }
//
//                                        if(document.getId().equals(USER_ALLREWARDS.get(i))){
//                                            Rewards rewards = document.toObject(Rewards.class);
//                                            Date date = rewards.getUseByDate();
//                                            Date currDate = Calendar.getInstance().getTime();
//                                            if(currDate.after(date)){
//                                                String currentReward = document.getId();
//                                                Log.d("Current Expired Reward" , currentReward);
//                                                Log.d("Current User", currentUser);
//                                                DocumentReference rewardArray = db.collection("Users").document(currentUser);
//                                                rewardArray.update("allRewards", FieldValue.arrayRemove(currentReward));
//                                            }
//                                            int quantity1 = rewards.getQuantityLeft();
//                                            if(quantity1 > 0){
//                                                listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
//                                                        rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL(), rewards.getUseByDate(), rewards.getExpired()));
//                                            }
//                                            Log.d("New ALL REWARD", listReward.toString());
//                                        }
//
//                                    }

                                    RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
                                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                                    recyclerView.setAdapter(myAdapter);
//                        Log.d("IMAGE", rewards.getImageURL());
                                }
                                RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
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


                }



            }
        });


        return view;
    }
}
