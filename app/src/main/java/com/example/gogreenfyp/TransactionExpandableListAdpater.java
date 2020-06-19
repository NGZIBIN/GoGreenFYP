package com.example.gogreenfyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class TransactionExpandableListAdpater extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitle;
    private HashMap<String, TransactionHistory> expandableListData;

    public TransactionExpandableListAdpater(Context context, List<String> listTitle, HashMap<String,TransactionHistory> expandableListData) {
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
        return expandableListData.get(listTitle.get(groupPosition));
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
        String headerTitle = (String) getGroup(groupPosition);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_transaction_group, null);
        }
        TextView tvFoodName = (TextView) view.findViewById(R.id.tvFoodName);
        TextView tvPrice = (TextView)view.findViewById(R.id.tvPrice);
        TextView tvLocation = (TextView) view.findViewById(R.id.tvLocationName);

        return view;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final TransactionHistory trans = (TransactionHistory) getChild(groupPosition,childPosition);

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_transaction_item, null);

        }
        TextView tvTransNum = view.findViewById(R.id.tvTransNum);
        TextView tvPoints = view.findViewById(R.id.tvPointsEarn);





        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
