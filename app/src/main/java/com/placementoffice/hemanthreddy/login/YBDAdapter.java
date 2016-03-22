package com.placementoffice.hemanthreddy.login;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hemanthreddy on 3/21/2016.
 */
public class YBDAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    public List<YDBclass> ybdlist;

    public YBDAdapter(Activity activity, List<YDBclass> ybdlist) {
        this.activity = activity;
        this.ybdlist = ybdlist;
    }

    @Override
    public int getCount() {
        return ybdlist.size();
    }

    @Override
    public Object getItem(int position) {
        return ybdlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.ybd_list_view, null);
        TextView companyname = (TextView) convertView.findViewById(R.id.companyname);
        TextView lastdate = (TextView) convertView.findViewById(R.id.lastdate);
        TextView designation = (TextView) convertView.findViewById(R.id.designation);
        TextView eligible = (TextView) convertView.findViewById(R.id.eligibility);

        YDBclass obj = ybdlist.get(position);

        companyname.setText(obj.getCompanyname());
        lastdate.setText(obj.getLastdate());
        designation.setText(obj.getDesignation());
        String e;
        if(obj.isEligible())
            e = "eligible";
        else
        e = "not eligible";
        eligible.setText(e);

        return convertView;

    }
}
