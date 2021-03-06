package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gogreenfyp.adapters.LeaderBoardAdapter;
import com.example.gogreenfyp.pojo.LeaderBoard;
import com.example.gogreenfyp.pojo.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment {

    private ListView listviewLeaderboard;
    private ArrayList<LeaderBoard> al;
    private LeaderBoardAdapter aa;
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        listviewLeaderboard = view.findViewById(R.id.listViewLeaderBoard);
        al = new ArrayList<LeaderBoard>();


        FirebaseFirestore.getInstance().collection("Users").orderBy("badgeProgress", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int currentCount = 0;
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    LeaderBoard leaderBoard = new LeaderBoard(user.getUsername(), user.getBadgeProgress());
                    al.add(leaderBoard);
                }

                if(getContext() != null) {
                    aa = new LeaderBoardAdapter(getContext(), R.layout.row_leaderboard, al);
                    listviewLeaderboard.setAdapter(aa);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                if(firebaseAuth.getCurrentUser() != null) {
                    Log.d("User", firebaseAuth.getCurrentUser().getUid());
                }
                e.printStackTrace();
            }
        });

        return view;
    }
}
