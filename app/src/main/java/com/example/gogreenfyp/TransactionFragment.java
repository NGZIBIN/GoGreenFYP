package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

ExpandableListView expandableListView;
TransactionExpandableListAdpater expandableListAdpater;
private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
private CollectionReference collection = fireStore.collection("Transactions");

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        expandableListView = view.findViewById(R.id.expandable_transactions);
        HashMap<String, TransactionDetails> transactionHashMap = getTransactionDetails();

       List<TransactionHeader> transactions = getTransactionHeaders();


       if(getContext() != null) {
           expandableListAdpater = new TransactionExpandableListAdpater(getContext(), transactions, transactionHashMap);
       }
       expandableListView.setAdapter(expandableListAdpater);

       expandableListView.setGroupIndicator(null);
       expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
       expandableListAdpater.notifyDataSetChanged();
      // Toast.makeText(getContext(), ""+transactionHashMap.size(), Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return view;
    }

    private HashMap<String, TransactionDetails> getTransactionDetails(){

        final HashMap<String, TransactionDetails> transactionDetails = new HashMap<String, TransactionDetails>();
        //final ArrayList<String> transactionTitles = getTransactionItemTitles();

        collection.document().collection("TransactionDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String id = documentSnapshot.getId();
                    String lastFourChars = id.substring(id.length()-3);
                    String transactionTitle = "TX*"+lastFourChars;
                    TransactionDetails transaction = documentSnapshot.toObject(TransactionDetails.class);
                    transactionDetails.put(transactionTitle, transaction);
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return transactionDetails;
    }

//    private ArrayList<String> getTransactionItemTitles(){
//
//        ArrayList<String> transactionTitles = new ArrayList<String>();
//        ArrayList<TransactionHeader> transactionHeaders = getTransactionHeaders();
//
//        for (int i = 0; i < transactionHeaders.size(); i++){
//            transactionTitles.add(transactionHeaders.get(i).getItem());
//        }
//
//        return transactionTitles;
//    }

    private ArrayList<TransactionHeader> getTransactionHeaders(){

        final ArrayList<TransactionHeader> transactionHeaders = new ArrayList<TransactionHeader>();

        collection.document().collection("TransactionHeader").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    TransactionHeader transactionHeader = documentSnapshot.toObject(TransactionHeader.class);
                    transactionHeaders.add(transactionHeader);
                    Toast.makeText(getContext(), transactionHeader.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return transactionHeaders;
    }


}
