package com.dhammika_dev.justgo.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.ui.fragments.StaffShareLocationFragment;

import java.util.ArrayList;

public class MultiSelectGalleryAdapter extends BaseAdapter {

    ArrayList<Uri> mArrayUri;
    StaffShareLocationFragment staffShareLocationFragment;
    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    private ImageView deleteBtn;

    public MultiSelectGalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri, StaffShareLocationFragment staffShareLocationFragment) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
        this.staffShareLocationFragment = staffShareLocationFragment;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.gv_item, parent, false);

        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);
        deleteBtn = (ImageView) itemView.findViewById(R.id.cancel_icon);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrayUri.remove(position);
//                staffShareLocationFragment.updateImageGallery();
            }
        });

        ivGallery.setImageURI(mArrayUri.get(position));

        return itemView;
    }


}