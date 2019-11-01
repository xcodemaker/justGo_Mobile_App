package com.dhammika_dev.justgo.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.dto.ScheduleList;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.ui.fragments.ViewCurrentLocationFragment;

import java.util.List;

public class LiveTrainListAdapter extends RecyclerView.Adapter<LiveTrainListAdapter.ViewHolder> {

    private List<ScheduleList> listItems;
    private OnNoteListener onNoteListener;
    private Context context;

    public LiveTrainListAdapter(Context context, List<ScheduleList> listItems, OnNoteListener onNoteListener) {
        this.context = context;
        this.listItems = listItems;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_train_list_item, parent, false);
        return new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(final LiveTrainListAdapter.ViewHolder viewHolder, int i) {
        ScheduleList listItem = listItems.get(i);
        viewHolder.origin.setText(listItem.getOrigin());
        viewHolder.train_name.setText(listItem.getTrain());
        viewHolder.destination.setText(listItem.getDestination());
        viewHolder.map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("trainId", listItem.getFrequency());
                Fragment fragment = new ViewCurrentLocationFragment();
                ((PassengerHomeActivity) context).loadFragment(fragment, args, "TicketBookingFragment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView origin;
        public TextView train_name;
        public TextView destination;
        public Button map_btn;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            origin = itemView.findViewById(R.id.start_station);
            train_name = itemView.findViewById(R.id.train_name);
            destination = itemView.findViewById(R.id.end_station);
            map_btn = itemView.findViewById(R.id.map_btn);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

}
