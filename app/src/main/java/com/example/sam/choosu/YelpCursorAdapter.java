package com.example.sam.choosu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sam.choosu.Model.YelpModel;

import org.w3c.dom.Text;

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
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_new, parent, false);
        final YelpCursorAdapter.MyHolder myHolder = new YelpCursorAdapter.MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YelpCursorAdapter.MyHolder myHolder = (YelpCursorAdapter.MyHolder) holder;
        YelpModel current = mData.get(position);
        //Log.e("error", current.getName());
            myHolder.name.setText(current.getName());
            myHolder.url.setText(current.getUrl());



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView url;

        public MyHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            url = itemView.findViewById(R.id.url);




        }
    }






}
