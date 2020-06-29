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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    User user = documentSnapshot.toObject(User.class);
                    LeaderBoard leaderBoard = new LeaderBoard(user.getUsername(), user.getBadgeProgress());
                    al.add(leaderBoard);
                }

                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                Log.d("User", firebaseAuth.getCurrentUser()+"");
                e.printStackTrace();
            }
        });

        if(getContext() != null) {
            aa = new LeaderBoardAdapter(getContext(), R.layout.row_leaderboard, al);
            listviewLeaderboard.setAdapter(aa);
        }
        return view;
    }
}
