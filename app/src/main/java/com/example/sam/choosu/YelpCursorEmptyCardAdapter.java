package com.example.sam.choosu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sam.choosu.Model.YelpModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YelpCursorEmptyCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private List<YelpModel> mData;
    public YelpCursorEmptyCardAdapter.yelpEmptyClickListener listener;
    String url;

    public interface yelpEmptyClickListener{
        void onYelpEmptyClickListener(View v, int position);
    }



    public YelpCursorEmptyCardAdapter(Context context, List<YelpModel> list) {
        this.mContext = context;
        this.mData = list;
        this.listener = (YelpCursorEmptyCardAdapter.yelpEmptyClickListener) context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.empty_cardview, parent, false);
        final YelpCursorEmptyCardAdapter.MyHolder myHolder = new YelpCursorEmptyCardAdapter.MyHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.INVISIBLE);


                }else{*/
                if(listener !=null) {

                    listener.onYelpEmptyClickListener(v, myHolder.getAdapterPosition());
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
        //Log.e("error", current.getName());
        myHolder.name.setText(current.getName());
        if(!current.getYelpImageurl().isEmpty()) {
            Picasso.get()
                    .load(current.getYelpImageurl())
                    .resize(350, 300)
                    .into(((YelpCursorAdapter.MyHolder) holder).image);

            //notifyDataSetChanged();

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
