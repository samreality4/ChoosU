package com.example.sam.choosu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sam.choosu.Library.UrlPreviewView;
import com.example.sam.choosu.Library.ViewListener;
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
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_cardview, parent, false);
        final YelpCursorAdapter.MyHolder myHolder = new YelpCursorAdapter.MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final YelpCursorAdapter.MyHolder myHolder = (YelpCursorAdapter.MyHolder) holder;
        YelpModel current = mData.get(position);
        //Log.e("error", current.getName());
            myHolder.name.setText(current.getName());



        myHolder.url.setLink(current.getUrl(), new ViewListener() {

            @Override
            public void onSuccess(boolean status) {



            }

            @Override
            public void onError(Exception e) {

            }
        });

    }






    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name;
        UrlPreviewView url;

        public MyHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            url = itemView.findViewById(R.id.url);




        }
    }







}
