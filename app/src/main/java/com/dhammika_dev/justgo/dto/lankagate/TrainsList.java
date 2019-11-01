package com.dhammika_dev.justgo.dto.lankagate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainsList implements Serializable {
    @SerializedName("trainFrequncy")
    @Expose
    public String trainFrequncy;
    @SerializedName("finalStationName")
    @Expose
    public String finalStationName;
    @SerializedName("classList")
    @Expose
    public List<ClassList> classList = null;
    @SerializedName("trainID")
    @Expose
    public int trainID;
    @SerializedName("trainNo")
    @Expose
    public int trainNo;
    @SerializedName("arrivalTime")
    @Expose
    public String arrivalTime;
    @SerializedName("endStationName")
    @Expose
    public String endStationName;
    @SerializedName("arrivalTimeEndStation")
    @Expose
    public String arrivalTimeEndStation;
    @SerializedName("depatureTime")
    @Expose
    public String depatureTime;
    @SerializedName("trainType")
    @Expose
    public String trainType;
    @SerializedName("trainName")
    @Expose
    public String trainName;
    @SerializedName("startStationName")
    @Expose
    public String startStationName;
    @SerializedName("arrivalTimeFinalStation")
    @Expose
    public String arrivalTimeFinalStation;

    @Override
    public String toString() {
        return "TrainsList{" +
                "trainFrequncy='" + trainFrequncy + '\'' +
                ", finalStationName='" + finalStationName + '\'' +
                ", classList=" + classList +
                ", trainID=" + trainID +
                ", trainNo=" + trainNo +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", endStationName='" + endStationName + '\'' +
                ", arrivalTimeEndStation='" + arrivalTimeEndStation + '\'' +
                ", depatureTime='" + depatureTime + '\'' +
                ", trainType='" + trainType + '\'' +
                ", trainName='" + trainName + '\'' +
                ", startStationName='" + startStationName + '\'' +
                ", arrivalTimeFinalStation='" + arrivalTimeFinalStation + '\'' +
                '}';
    }
}
