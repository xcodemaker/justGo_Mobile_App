package com.dhammika_dev.justgo.ui.adapters;

import android.widget.ListAdapter;

import com.dhammika_dev.justgo.ui.model.DemoItem;

import java.util.List;

public interface DemoAdapter extends ListAdapter {

    void appendItems(List<DemoItem> newItems);

    void setItems(List<DemoItem> moreItems);
}
