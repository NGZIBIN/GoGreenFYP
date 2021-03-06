package com.example.gogreenfyp.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.gogreenfyp.R;
import com.example.gogreenfyp.wallet.TransactionHeader;
import com.example.gogreenfyp.wallet.Wallet;
import com.example.gogreenfyp.pojo.Transaction;
import com.example.gogreenfyp.pojo.TransactionDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class TransactionFragment extends Fragment {

    ExpandableListView expandableListView;
    TransactionExpandableListAdpater expandableListAdpater;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Wallet wallet = new Wallet();
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private CollectionReference collection = fireStore.collection("Transactions");

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        expandableListView = view.findViewById(R.id.expandable_transactions);

        expandableListAdpater = getExpandListAdapter();
        expandableListView.setAdapter(expandableListAdpater);
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        return view;
    }

    private TransactionExpandableListAdpater getExpandListAdapter(){

        String address = "";
        if(getActivity() != null){
            address = wallet.getWalletAddress(getActivity());
        }
        final HashMap<String, TransactionDetails> transactionHashMap = new HashMap<String, TransactionDetails>();
        final ArrayList<TransactionHeader> transactionHeaders = new ArrayList<TransactionHeader>();
        final ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        final TransactionExpandableListAdpater expandableListAdpater = new TransactionExpandableListAdpater(getContext(), transactionHeaders, transactionHashMap, transactions, address);

        Task<QuerySnapshot> receiverAddressFilter = collection.whereEqualTo("to", address).get();
        Task<QuerySnapshot> senderAddressFilter = collection.whereEqualTo("from", address).get();

                Tasks.whenAllSuccess(receiverAddressFilter, senderAddressFilter).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        for(Object object : objects) {
                            QuerySnapshot snapshot = (QuerySnapshot) object;
                            for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                                Transaction transaction = documentSnapshot.toObject(Transaction.class);
                                TransactionDetails details = new TransactionDetails(transaction.getTransactionNo(), transaction.getPoints());
                                TransactionHeader transactionHeader = new TransactionHeader(transaction.getItem(), transaction.getPlace(), transaction.getAmount());

                                String transNo = details.getTransactionNo();
                                String lastFourChars = transNo.substring(transNo.length() - 4);
                                String lastTwelveChars = transNo.substring(transNo.length() - 12);
                                String transactionTitle = transactionHeader.getItem()+" TX*" + lastFourChars;

                                details.setTransactionNo(lastTwelveChars);
                                transactionHeader.setItem(transactionTitle);

                                transactions.add(transaction);
                                transactionHeaders.add(transactionHeader);
                                transactionHashMap.put(transactionTitle, details);
                            }
                        }
                        expandableListAdpater.setTransactions(transactions);
                        expandableListAdpater.setListTitle(transactionHeaders);
                        expandableListAdpater.setExpandableListData(transactionHashMap);
                        expandableListAdpater.notifyDataSetChanged();
                    }
                });
        return expandableListAdpater;
    }

}
