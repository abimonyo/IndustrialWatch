package com.app.industrialwatch.app.business;


/***
 * Callback after success-full/error network operation
 */
public interface OnNetworkTaskListener {

    /**
     * HTTP response call back from {@link AppNetworkTask}
     *
     * @param response {@link HttpResponseItem}
     *//*
    void onNetworkResponse(HttpResponseItem response);

    *//**
     * HTTP network operation is successfully completed
     *
     * @param response {@link HttpResponseItem}
     *//*
    void onNetworkSuccess(HttpResponseItem response);

    *//**
     * For some reasons there is/are some error(s) in network operation
     *
     * @param response {@link HttpResponseItem}
     *//*
    void onNetworkError(HttpResponseItem response);

    *//**
     * For some reasons network operation has been cancelled
     *
     * @param response {@link HttpResponseItem}
     *//*
    void onNetworkCanceled(HttpResponseItem response);

    *//**
     * @return true is connected else not
     *//*
    boolean isNetworkConnected();*/
}
