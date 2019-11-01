package com.dhammika_dev.justgo.dto.lankagate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RESULTS {
    @SerializedName("directTrains")
    @Expose
    public DirectTrains directTrains;
    @SerializedName("connectingTrains")
    @Expose
    public ConnectingTrains connectingTrains;
}
