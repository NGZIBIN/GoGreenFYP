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
import com.example.gogreenfyp.adapters.RedeemedRewardRecycleViewAdapter;
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
public class HistoryFragment extends Fragment {
    List<Rewards> listReward;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rewardsCollectionRef = db.collection("Rewards");
    private CollectionReference usersCollectionRef = db.collection("Users");


    // Firebase Auth
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    SearchView searchView;
    String USER_ID;
    ArrayList<String> USER_REWARDS = new ArrayList<String>();

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_redeemed_rewards, container, false);

        searchView = view.findViewById(R.id.searchViewRedeemedReward);

        // Get current authenticated userid
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
                            USER_REWARDS = (ArrayList<String>) documentSnapshot.get("userRedeemedRewards");
                            Log.d("REDEEMED REWARDS", USER_REWARDS.toString());
                        }
                    }
                    listReward = new ArrayList<>();

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewRedeemedReward);

                    rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document: task.getResult()){
                                    for (int i = 0; i < USER_REWARDS.size(); i++){

                                        if(document.getId().equals(USER_REWARDS.get(i))){

                                            Rewards rewards = document.toObject(Rewards.class);
                                            listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
                                                    rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL(), rewards.getUseByDate(), rewards.getExpired()));

                                        }
                                    }


                                }

                                // Display the rewards that have been redeemed by user

                                RedeemedRewardRecycleViewAdapter myAdapter = new RedeemedRewardRecycleViewAdapter(getContext(),listReward);

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
