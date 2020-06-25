package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {
ExpandableListView expandableListView;
TransactionExpandableListAdpater expandableListAdpater;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        expandableListView = view.findViewById(R.id.expandable_transactions);
        HashMap<String, TransactionDetails> transactionHashMap = TransactionListData.getTransactionData();

       List<TransactionHeader> transactions = new ArrayList<TransactionHeader>();

       transactions.add(new TransactionHeader("Chicken Rice TX*1505","Chicken Rice Stall @ Republic Poly, North Canteen",3.50));
       transactions.add(new TransactionHeader("Wanton Mee TX*1393","Noodle Boogle @ Yishun Shopping Centre, FoodRepublic",2.50));
       transactions.add(new TransactionHeader("Chicken Rice TX*9312","Chicken Rice Stall @ Republic Poly, North Canteen",3.50));
       transactions.add(new TransactionHeader("Wanton Mee TX*3290","Noodle Boogle @ Yishun Shopping Centre, FoodRepublic",2.50));

       if(getContext() != null) {
           expandableListAdpater = new TransactionExpandableListAdpater(getContext(), transactions, transactionHashMap);
       }
       expandableListView.setAdapter(expandableListAdpater);
       expandableListAdpater.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return view;
    }


}
