package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gogreenfyp.pojo.Badge;
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
public class BadgesFragment extends Fragment {

    List<Badge> badgeList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference badgesCollectionRef = db.collection("Badges");
    private CollectionReference usersCollectionRef = db.collection("Users");

    // Firebase Auth
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String USER_ID;
    ArrayList<String> USER_BADGES = new ArrayList<String>();
    public BadgesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badges, container, false);

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
                            USER_BADGES = (ArrayList<String>) documentSnapshot.get("userBadges");
//                            Log.d("REWARDS", USER_BADGES.toString());
                        }
                    }
                    badgeList = new ArrayList<>();

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewAllBadges);

                    badgesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document:task.getResult()){

                                    for(int i = 0; i <USER_BADGES.size(); i ++){

                                        if(document.getId().equals(USER_BADGES.get(i))){
                                            Badge badge = document.toObject(Badge.class);
                                            badgeList.add(new Badge(badge.getName(), badge.getImageURL(), badge.getUsagePoints(), badge.getBonusPoints()));
                                        }
                                    }

                                }
                                BadgesRecyclerViewAdapter myAdapter = new BadgesRecyclerViewAdapter(getContext(), badgeList);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
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
