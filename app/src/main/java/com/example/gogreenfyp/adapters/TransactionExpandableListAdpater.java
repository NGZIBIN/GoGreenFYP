package com.example.gogreenfyp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.example.gogreenfyp.LoginActivity;
import com.example.gogreenfyp.R;
import com.example.gogreenfyp.pojo.Transaction;
import com.example.gogreenfyp.wallet.TransactionHeader;
import com.example.gogreenfyp.pojo.TransactionDetails;
import java.util.HashMap;
import java.util.List;

public class TransactionExpandableListAdpater extends BaseExpandableListAdapter {

    private Context context;
    private List<TransactionHeader> listTitle;
    private HashMap<String, TransactionDetails> expandableListData;
    private List<Transaction> transactions;
    private String fromAddress;


    public TransactionExpandableListAdpater(Context context, List<TransactionHeader> listTitle, HashMap<String, TransactionDetails> expandableListData, List<Transaction> transactions, String fromAddress) {
        this.context = context;
        this.listTitle = listTitle;
        this.expandableListData = expandableListData;
        this.transactions = transactions;
        this.fromAddress = fromAddress;
    }
    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }

    public void setListTitle(List<TransactionHeader> transactionHeaders){
        this.listTitle = transactionHeaders;
    }

    public void setExpandableListData(HashMap<String, TransactionDetails> transactionDetailsHashMap){
        this.expandableListData = transactionDetailsHashMap;
    }

    @Override
    public int getGroupCount() {
        return listTitle.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListData.get(listTitle.get(groupPosition).getItem());
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        TransactionHeader transactionHeader =  (TransactionHeader) getGroup(groupPosition);
        Transaction transaction = transactions.get(groupPosition);

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_transaction_group, null);
        }
        TextView tvFoodName = (TextView) view.findViewById(R.id.tvFoodName);
        TextView tvPrice = (TextView)view.findViewById(R.id.tvPrice);
        TextView tvLocation = (TextView) view.findViewById(R.id.tvLocationName);

        String roundUpTo2Decimals = "ETH "+String.format("%.4f", transactionHeader.getAmount());
        String[] receiverSenderName = transaction.getPlace().split(",");

        if(transaction.getTo().equals(fromAddress)){
            tvPrice.setTextColor(Color.parseColor("#00FF00"));
            tvLocation.setText(receiverSenderName[1]);
        }
        else {
            tvLocation.setText(receiverSenderName[0]);
        }


        tvFoodName.setText(transactionHeader.getItem());
        tvPrice.setText(roundUpTo2Decimals);

        return view;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final TransactionDetails trans = (TransactionDetails) getChild(groupPosition, childPosition);

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_transaction_item, null);

        }
        TextView tvTransNum = view.findViewById(R.id.tvTransNum);
        TextView tvPoints = view.findViewById(R.id.tvPointsEarn);

        String points = (trans.getPoints()/2)+"";
        String transNo = trans.getTransactionNo();

        tvTransNum.setText(transNo);
        tvPoints.setText(points);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
