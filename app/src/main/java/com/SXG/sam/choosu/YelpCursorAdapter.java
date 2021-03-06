package com.SXG.sam.choosu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SXG.sam.choosu.Model.YelpModel;
import com.SXG.sam.choosu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sam on 3/27/18.
 */

public class YelpCursorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private List<YelpModel> mData;
    public yelpClickListener listener;
    String url;


    public interface yelpClickListener{
        void onYelpClickListener(View v, int position);
    }



    public YelpCursorAdapter(Context context, List<YelpModel> list) {
        this.mContext = context;
        this.mData = list;
        this.listener = (YelpCursorAdapter.yelpClickListener) context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.content_cardview, parent, false);
        final YelpCursorAdapter.MyHolder myHolder = new YelpCursorAdapter.MyHolder(view);
       view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (listener != null) {

                    listener.onYelpClickListener(v, myHolder.getAdapterPosition());
                    YelpModel current = mData.get(myHolder.getAdapterPosition());

                    url = current.getUrl();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    mContext.startActivity(intent);


                }
            }

            });

        return myHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final YelpCursorAdapter.MyHolder myHolder = (YelpCursorAdapter.MyHolder) holder;
        YelpModel current = mData.get(position);
;
            myHolder.name.setText(current.getName());
            if(!current.getYelpImageurl().isEmpty()) {
                Picasso.get()
                        .load(current.getYelpImageurl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .fit()
                        .into(((MyHolder) holder).image);

            }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;


        public MyHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);

        }
    }

    }








