package com.SXG.sam.choosu.Model;

/**
 * Created by sam on 4/1/18.
 */

public class MetaData {
    private String url ="";
    private String imageurl="";
    private String mediatype;
    private String sitename;
    private String title = "";

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageurl){
        this.imageurl = imageurl;
    }


    public String getImageUrl(){
        return imageurl;
    }


    public void setSitename(String sitename){
        this.sitename = sitename;
    }

    public String getMediatype(){
        return mediatype;
    }
    public void setMediatype(String mediatype){
        this.mediatype = mediatype;
    }

}
