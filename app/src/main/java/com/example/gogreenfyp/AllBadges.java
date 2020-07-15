package com.example.gogreenfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.gogreenfyp.pojo.Badge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllBadges extends AppCompatActivity {
    List<Badge> badgeList =  new ArrayList<>();
    ImageView backBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference badgesCollectionRef = db.collection("Badges");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_badges);
        backBtn = findViewById(R.id.backBtn);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleViewAllBadges);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        badgesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        Badge badge = document.toObject(Badge.class);
                        badgeList.add(new Badge(badge.getName(), badge.getImageURL(), badge.getUsagePoints(), badge.getBonusPoints()));
                    }
                    BadgesRecyclerViewAdapter myAdapter = new BadgesRecyclerViewAdapter(getApplicationContext(), badgeList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });
    }
}
