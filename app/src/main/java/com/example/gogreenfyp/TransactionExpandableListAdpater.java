package com.example.gogreenfyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class TransactionExpandableListAdpater extends BaseExpandableListAdapter {

    private Context context;
    private List<TransactionHeader> listTitle;
    private HashMap<String, TransactionDetails> expandableListData;

    public TransactionExpandableListAdpater(Context context, List<TransactionHeader> listTitle, HashMap<String, TransactionDetails> expandableListData) {
        this.context = context;
        this.listTitle = listTitle;
        this.expandableListData = expandableListData;
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

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_transaction_group, null);
        }
        TextView tvFoodName = (TextView) view.findViewById(R.id.tvFoodName);
        TextView tvPrice = (TextView)view.findViewById(R.id.tvPrice);
        TextView tvLocation = (TextView) view.findViewById(R.id.tvLocationName);

        String roundUpTo2Decimals = "$"+String.format("%.2f", transactionHeader.getAmount());

        tvFoodName.setText(transactionHeader.getItem());
        tvPrice.setText(roundUpTo2Decimals);
        tvLocation.setText(transactionHeader.getPlace());

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

        String points = trans.getPoints()+"";
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
