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
        expandableListAdpater = getExpandListAdapter();
        expandableListView.setAdapter(expandableListAdpater);

        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        // Inflate the layout for this fragment
        return view;
    }

    private TransactionExpandableListAdpater getExpandListAdapter(){

        final HashMap<String, TransactionDetails> transactionHashMap = new HashMap<String, TransactionDetails>();
        final ArrayList<TransactionHeader> transactions = new ArrayList<TransactionHeader>();
        final TransactionExpandableListAdpater expandableListAdpater = new TransactionExpandableListAdpater(getContext(), transactions, transactionHashMap);

            collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Transaction transaction = documentSnapshot.toObject(Transaction.class);
                        TransactionDetails details = new TransactionDetails(transaction.getTransactionNo(), transaction.getPoints());
                        TransactionHeader transactionHeader = new TransactionHeader(transaction.getItem(), transaction.getPlace(), transaction.getAmount());

                        String transNo = details.getTransactionNo();
                        String lastFourChars = transNo.substring(transNo.length() - 4);
                        String lastEightChars = transNo.substring(transNo.length() - 12);
                        String transactionTitle = transactionHeader.getItem()+" TX*" + lastFourChars;

                        details.setTransactionNo(lastEightChars);
                        transactionHeader.setItem(transactionTitle);

                        transactions.add(transactionHeader);
                        transactionHashMap.put(transactionTitle, details);
                    }

                    expandableListAdpater.setListTitle(transactions);
                    expandableListAdpater.setExpandableListData(transactionHashMap);
                    expandableListAdpater.notifyDataSetChanged();
                    Toast.makeText(getContext(), transactionHashMap.size()+"", Toast.LENGTH_SHORT).show();
                }
            });
            return expandableListAdpater;
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


}
