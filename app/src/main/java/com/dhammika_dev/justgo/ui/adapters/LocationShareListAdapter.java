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
import com.dhammika_dev.justgo.dto.lankagate.TrainsList;
import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;
import com.dhammika_dev.justgo.ui.activity.StaffHomeActivity;
import com.dhammika_dev.justgo.ui.fragments.StaffShareLocationFragment;

import java.util.List;

public class LocationShareListAdapter extends RecyclerView.Adapter<LocationShareListAdapter.ViewHolder> {

    TrainScheduleResponse trainScheduleResponse;
    private List<ScheduleList> listItems;
    private OnNoteListener onNoteListener;
    private Context context;

    public LocationShareListAdapter(Context context, List<ScheduleList> listItems, OnNoteListener onNoteListener, TrainScheduleResponse trainScheduleResponse) {
        this.context = context;
        this.listItems = listItems;
        this.onNoteListener = onNoteListener;
        this.trainScheduleResponse = trainScheduleResponse;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_location_list_item, parent, false);
        return new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(final LocationShareListAdapter.ViewHolder viewHolder, int i) {
        ScheduleList listItem = listItems.get(i);
        viewHolder.origin.setText(listItem.getOrigin());
        viewHolder.departure.setText(listItem.getDeparture());
        viewHolder.destination.setText(listItem.getDestination());
        viewHolder.train_name.setText(listItem.getTrain());
        viewHolder.share_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                TrainsList trainsList = trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList().get(i);
                args.putSerializable("trainsList", trainsList);
                args.putString("fromStation", trainScheduleResponse.getQUERY().getStartStaionName());
                args.putString("toStation", trainScheduleResponse.getQUERY().getEndStaionName());
                args.putString("trainName", listItem.getTrain());
                Fragment fragment = new StaffShareLocationFragment();
                ((StaffHomeActivity) context).loadFragment(fragment, args, "ShareLocationTrainListFragment");
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
        public TextView departure;
        public TextView destination;
        public TextView train_name;
        public Button share_location_btn;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            origin = itemView.findViewById(R.id.start_station);
            departure = itemView.findViewById(R.id.departure_at);
            destination = itemView.findViewById(R.id.end_station);
            train_name = itemView.findViewById(R.id.train_name);
            share_location_btn = itemView.findViewById(R.id.share_location_btn);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

}
