package com.dhammika_dev.justgo.dto.lankagate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QUERY {
    @SerializedName("startStaionName")
    @Expose
    public String startStaionName;
    @SerializedName("endStaion")
    @Expose
    public int endStaion;
    @SerializedName("searchTimeEnd")
    @Expose
    public String searchTimeEnd;
    @SerializedName("searchDate")
    @Expose
    public String searchDate;
    @SerializedName("endStaionName")
    @Expose
    public String endStaionName;
    @SerializedName("startStaion")
    @Expose
    public int startStaion;
    @SerializedName("searchTimeStart")
    @Expose
    public String searchTimeStart;
}
