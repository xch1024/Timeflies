package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.model.TimeData;
import java.util.List;

/**
 * @author:halo
 * @projectName:com.example.timeflies.adapter
 * @date:2022-05-12
 * @time:09:23
 * @description:
 */
public class MySpinnerAdapter extends BaseAdapter {
    private List<TimeData> list;
    private Context context;

    public MySpinnerAdapter(List<TimeData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }


    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.spiner_time_select, null);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(list.get(i).getTableName());
        return view;
    }
}
