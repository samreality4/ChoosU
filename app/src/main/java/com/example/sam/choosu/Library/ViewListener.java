package com.example.sam.choosu.Library;

/**
 * Created by sam on 4/1/18.
 */

public interface ViewListener {
    void onSuccess(boolean status);

    void onError(Exception e);
}
