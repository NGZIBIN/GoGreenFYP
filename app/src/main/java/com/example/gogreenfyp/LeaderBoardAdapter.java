package com.example.gogreenfyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class LeaderBoardAdapter extends ArrayAdapter<LeaderBoard> {

    private ArrayList<LeaderBoard> leaderBoards;
    private Context context;
    private TextView tvName;
    private TextView tvCount;
    private ImageView ivCrown;

    public LeaderBoardAdapter(@NonNull Context context, int resource, ArrayList<LeaderBoard> objects) {
        super(context, resource, objects);
        leaderBoards = objects;
        // Store Context object as we would need to use it later
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // The usual way to get the LayoutInflater object to
        //  "inflate" the XML file into a View object
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // "Inflate" the row.xml as the layout for the View object
        View rowView = inflater.inflate(R.layout.row_leaderboard, parent, false);

        // Get the TextView object
        tvName = (TextView) rowView.findViewById(R.id.userName);
        tvCount = (TextView) rowView.findViewById(R.id.numOfUsage);
        ivCrown = (ImageView) rowView.findViewById(R.id.leaderBoardImage);
        // The parameter "position" is the index of the
        //  row ListView is requesting.
        //  We get back the food at the same index.
        LeaderBoard currentType = leaderBoards.get(position);
        if(position == 0){
            ivCrown.setImageResource(R.drawable.crownfirst);
        }
        else if(position == 1){
            ivCrown.setImageResource(R.drawable.crownsecond);
        }
        else if(position == 2){
            ivCrown.setImageResource(R.drawable.crownthird);
        }


        tvName.setText(currentType.getName());
        tvCount.setText(currentType.getCount() + "");
        return rowView;
    }
}
