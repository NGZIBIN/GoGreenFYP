package com.example.gogreenfyp;

import android.content.Intent;
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


public class TransactionFragment extends Fragment {

    ExpandableListView expandableListView;
    TransactionExpandableListAdpater expandableListAdpater;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String walletAddress = "";
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

        if(getActivity() != null) {
            Intent intent = getActivity().getIntent();
            walletAddress = intent.getStringExtra("walletAddress");
        }

        expandableListAdpater = getExpandListAdapter();
        expandableListView.setAdapter(expandableListAdpater);
       // Toast.makeText(getContext(), walletAddress, Toast.LENGTH_SHORT).show();
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        // Inflate the layout for this fragment
        return view;
    }

    private TransactionExpandableListAdpater getExpandListAdapter(){

        final HashMap<String, TransactionDetails> transactionHashMap = new HashMap<String, TransactionDetails>();
        final ArrayList<TransactionHeader> transactions = new ArrayList<TransactionHeader>();
        final TransactionExpandableListAdpater expandableListAdpater = new TransactionExpandableListAdpater(getContext(), transactions, transactionHashMap);

            String address = this.walletAddress;

            collection.whereEqualTo("walletAddress", address).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Transaction transaction = documentSnapshot.toObject(Transaction.class);
                        TransactionDetails details = new TransactionDetails(transaction.getTransactionNo(), transaction.getPoints());
                        TransactionHeader transactionHeader = new TransactionHeader(transaction.getItem(), transaction.getPlace(), transaction.getAmount());

                        String transNo = details.getTransactionNo();
                        String lastFourChars = transNo.substring(transNo.length() - 4);
                        String lastTwelveChars = transNo.substring(transNo.length() - 12);
                        String transactionTitle = transactionHeader.getItem()+" TX*" + lastFourChars;

                        details.setTransactionNo(lastTwelveChars);
                        transactionHeader.setItem(transactionTitle);

                        transactions.add(transactionHeader);
                        transactionHashMap.put(transactionTitle, details);
                    }
                    //Toast.makeText(getContext(), transactionHashMap.size()+"", Toast.LENGTH_SHORT).show();
                    expandableListAdpater.setListTitle(transactions);
                    expandableListAdpater.setExpandableListData(transactionHashMap);
                    expandableListAdpater.notifyDataSetChanged();
                }
            });
            return expandableListAdpater;
    }


}
