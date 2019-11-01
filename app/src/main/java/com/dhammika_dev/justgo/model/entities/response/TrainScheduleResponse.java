package com.dhammika_dev.justgo.model.entities.response;

import com.dhammika_dev.justgo.dto.lankagate.QUERY;
import com.dhammika_dev.justgo.dto.lankagate.RESULTS;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainScheduleResponse {
    @SerializedName("SUCCESS")
    @Expose
    public boolean sUCCESS;
    @SerializedName("MESSAGE")
    @Expose
    public String mESSAGE;
    @SerializedName("QUERY")
    @Expose
    public QUERY qUERY;
    @SerializedName("NOFRESULTS")
    @Expose
    public int nOFRESULTS;
    @SerializedName("RESULTS")
    @Expose
    public RESULTS rESULTS;
    @SerializedName("STATUSCODE")
    @Expose
    public String sTATUSCODE;
}
