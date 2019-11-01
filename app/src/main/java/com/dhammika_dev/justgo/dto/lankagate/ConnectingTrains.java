package com.dhammika_dev.justgo.dto.lankagate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConnectingTrains {
    @SerializedName("trainsList")
    @Expose
    public List<Object> trainsList = null;
}
