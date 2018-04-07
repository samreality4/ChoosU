package com.example.sam.choosu.Library;

/**
 * Created by sam on 4/1/18.
 */

public interface ResponseListener {
    void onData(MetaData metaData);

    void onError(Exception e);
}
