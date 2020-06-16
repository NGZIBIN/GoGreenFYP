package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment {
ListView listviewLeaderboard;
    ArrayList<LeaderBoard> al;
    ArrayAdapter aa;
    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        listviewLeaderboard = view.findViewById(R.id.listViewLeaderBoard);
        al = new ArrayList<LeaderBoard>();
        al.add(new LeaderBoard("Mary", 40));
        al.add(new LeaderBoard("Tom", 30));
        al.add(new LeaderBoard("Robin", 20));

        aa = new LeaderBoardAdapter(getContext(),R.layout.row_leaderboard, al);
        listviewLeaderboard.setAdapter(aa);

        return view;
    }
}
