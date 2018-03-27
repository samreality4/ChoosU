package com.example.sam.choosu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sam.choosu.Model.YelpModel;

import java.util.List;

/**
 * Created by sam on 3/27/18.
 */

public class YelpCursorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<YelpModel> mData;

    public YelpCursorAdapter(Context context, List<YelpModel> list) {
        this.mContext = context;
        this.mData = list;
        inflater = LayoutInflater.from(context);
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_choose, parent, false);
        final YelpCursorAdapter.Myholder myholder = new YelpCursorAdapter.Myholder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YelpCursorAdapter.Myholder myholder = (YelpCursorAdapter.Myholder) holder;
        YelpModel current = mData.get(position);

        myholder.name.setText(current.getName());
        myholder.name.setText(current.getUrl());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView name;
        TextView url;

        public Myholder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            url = itemView.findViewById(R.id.url);




        }
    }






}
