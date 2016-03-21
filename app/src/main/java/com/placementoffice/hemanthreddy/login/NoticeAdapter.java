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
 * Created by hemanthreddy on 3/13/2016.
 */
public class  NoticeAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Notice> notices;

    public NoticeAdapter(Activity activity, List<Notice> notices) {
        this.activity = activity;
        this.notices = notices;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Object getItem(int position) {
        return notices.get(position);
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
            convertView = inflater.inflate(R.layout.notice_list_view, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView don = (TextView) convertView.findViewById(R.id.date_of_notice);

        Notice n = notices.get(position);

        title.setText(n.getTitle());
        don.setText(n.getDate());

        return convertView;
    }
}
