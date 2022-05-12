package com.example.timeflies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.ConfigData;
import com.example.timeflies.utils.ToastCustom;

import java.util.List;

public class TableNameAdapter extends RecyclerView.Adapter<TableNameAdapter.TableNameHolder>{
    private static final String TAG = "xch";
    private List<ConfigData> list;
    private Context context;

    public TableNameAdapter(List<ConfigData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TableNameAdapter.TableNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_table_name, null);
        return new TableNameAdapter.TableNameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableNameAdapter.TableNameHolder holder, int position) {
        setContView(holder, position);
    }

    private void setContView(TableNameHolder holder, int position) {
        ConfigData data = list.get(position);
        holder.table_name.setText(data.getClassName());

        holder.radio_group_table.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int k  = 0;
                while ( k == data.getId()) {
                    k++;
                    Log.d(TAG, "onCheckedChanged: "+k);
                }
                if(k == position){
                    Log.d(TAG, "onCheckedChanged: "+position);
                    holder.radio_table.setChecked(true);
                    ToastCustom.showMsgWarning(context, data.getClassName());
                }else{
                    holder.radio_table.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TableNameHolder extends RecyclerView.ViewHolder {

        private TextView  table_name;
        private RadioGroup radio_group_table;
        private RadioButton radio_table;

        public TableNameHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            table_name = itemView.findViewById(R.id.table_name);
            radio_group_table = itemView.findViewById(R.id.radio_group_table);
            radio_table = itemView.findViewById(R.id.radio_table);
        }
    }
}
