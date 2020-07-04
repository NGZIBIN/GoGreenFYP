package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gogreenfyp.pojo.Badge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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
    private CollectionReference badgesCollectionRef = db.collection("Rewards");
    public BadgesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badges, container, false);

        badgeList = new ArrayList<>();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewAllBadges);

        badgesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        Badge badge = document.toObject(Badge.class);
                        badgeList.add(new Badge(badge.getName(), badge.getBadgeImage(), badge.getUsagePoints(), badge.getBonusPoints()));
                    }
                    BadgesRecyclerViewAdapter myAdapter = new BadgesRecyclerViewAdapter(getContext(), badgeList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });

        return view;
    }
}
