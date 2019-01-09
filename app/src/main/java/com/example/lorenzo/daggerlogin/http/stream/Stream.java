
package com.example.lorenzo.daggerlogin.http.stream;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stream {

    @SerializedName("data")
    @Expose
    private List<Datum> streams = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public List<Datum> getStreams() {
        return streams;
    }

    public void setStreams(List<Datum> streams) {
        this.streams = streams;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
