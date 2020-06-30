package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private CollectionReference collection = fireStore.collection("Users");

    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        listviewLeaderboard = view.findViewById(R.id.listViewLeaderBoard);
        al = new ArrayList<LeaderBoard>();

        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int currentCount = 0;
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    LeaderBoard leaderBoard = new LeaderBoard(user.getUsername(), user.getBadgeProgress());
                    al.add(leaderBoard);
                }

                if(getContext() != null) {
                    ArrayList<LeaderBoard> sortedRankings = sortLeaderBoardRanking(al);
                    aa = new LeaderBoardAdapter(getContext(), R.layout.row_leaderboard, sortedRankings);
                    listviewLeaderboard.setAdapter(aa);
                }
                //Toast.makeText(getContext(), "Success! "+al.size(), Toast.LENGTH_SHORT).show();
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
    private ArrayList<LeaderBoard> sortLeaderBoardRanking(ArrayList<LeaderBoard> leaderBoards){
        int currentCount = 0;
        ArrayList<LeaderBoard> sortedRankings = new ArrayList<LeaderBoard>();
        for(int i = 0; i < leaderBoards.size(); i++){
            if(leaderBoards.get(i).getCount() > currentCount) {
                currentCount = leaderBoards.get(i).getCount();
                sortedRankings.add(0, leaderBoards.get(i));
            }
            else{
                sortedRankings.add(leaderBoards.get(i));
            }
        }
        return sortedRankings;
    }
}
