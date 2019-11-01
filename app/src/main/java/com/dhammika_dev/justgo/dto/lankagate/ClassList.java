package com.dhammika_dev.justgo.dto.lankagate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassList implements Serializable {
    @SerializedName("classID")
    @Expose
    public int classID;
    @SerializedName("className")
    @Expose
    public String className;
}
