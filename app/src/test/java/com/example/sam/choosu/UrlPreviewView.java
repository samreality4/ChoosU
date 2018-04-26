package com.example.sam.choosu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SXG.sam.choosu.Model.MetaData;
import com.squareup.picasso.Picasso;

/**
 * Created by sam on 4/1/18.
 */

public class UrlPreviewView extends LinearLayout{


        private View view;
        Context context;
        private MetaData meta;

        LinearLayout linearLayout;
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewDesp;
        TextView textViewUrl;

        private String main_url;

        private boolean isDefaultClick = true;

        private LinkListener linkListener;


        public UrlPreviewView(Context context) {
            super(context);
            this.context = context;
        }

        public UrlPreviewView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
        }

        public UrlPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public UrlPreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            this.context = context;
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
        }


        public void initView() {
            if(findLinearLayoutChild() != null) {
                this.view = findLinearLayoutChild();
            } else  {
                this.view = this;
                inflate(context, R.layout.link_preview,this);
            }

            linearLayout = findViewById(R.id.link_preview_linear);
            imageView = findViewById(R.id.link_preview_image);



            if(meta.getImageUrl().equals("") || meta.getImageUrl().isEmpty()) {
                imageView.setVisibility(GONE);
            } else {
                imageView.setVisibility(VISIBLE);
                Picasso.get()
                        .load(meta.getImageUrl())
                        .fit()
                        .into(imageView);
            }



            linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isDefaultClick) {
                        linkClicked();
                    } else {
                        if(linkListener != null) {
                            linkListener.onClicked(view, meta);
                        } else {
                            linkClicked();
                        }
                    }
                }
            });

        }


        private void linkClicked() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
            context.startActivity(intent);
        }


        public void setDefaultClickListener(boolean isDefault) {
            isDefaultClick = isDefault;
        }

        public void setClickListener(LinkListener linkListener1) {
            linkListener = linkListener1;
        }

        protected LinearLayout findLinearLayoutChild() {
            if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
                return (LinearLayout) getChildAt(0);
            }
            return null;
        }

        public void setLinkFromMeta(MetaData metaData) {
            meta = metaData;
            initView();
        }

        public MetaData getMetaData() {
            return meta;
        }

       /* public void setLink(String url, final ViewListener viewListener) {
            main_url = url;
            UrlPreview urlPreview = new UrlPreview(new ResponseListener() {
                @Override
                public void onData(MetaData metaData) {
                    meta = metaData;
                    if(!meta.getTitle().isEmpty() || !meta.getTitle().equals("")) {
                        viewListener.onSuccess(true);
                    }

                    initView();
                }

                @Override
                public void onError(Exception e) {
                    viewListener.onError(e);
                }
            });
            urlPreview.getPreview(url);
        }*/

    }
