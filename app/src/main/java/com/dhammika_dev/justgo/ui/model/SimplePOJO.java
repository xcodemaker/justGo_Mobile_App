package com.dhammika_dev.justgo.ui.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class SimplePOJO implements AsymmetricItem {
    /* Parcelable interface implementation */
    public static final Parcelable.Creator<SimplePOJO> CREATOR = new Parcelable.Creator<SimplePOJO>() {
        @Override
        public SimplePOJO createFromParcel(@NonNull Parcel in) {
            return new SimplePOJO(in);
        }

        @Override
        @NonNull
        public SimplePOJO[] newArray(int size) {
            return new SimplePOJO[size];
        }
    };
    private int columnSpan;
    private int rowSpan;
    private int position;

    public SimplePOJO() {
        this(1, 1, 0);
    }

    public SimplePOJO(int columnSpan, int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
    }

    public SimplePOJO(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int getColumnSpan() {
        return columnSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }
}