package com.example.sam.choosu;

import com.example.sam.choosu.Model.MetaData;

/**
 * Created by sam on 4/1/18.
 */

public interface ResponseListener {
    void onData(MetaData metaData);

    void onError(Exception e);
}
