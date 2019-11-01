package com.dhammika_dev.justgo.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.dto.Ticket;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;

import java.util.List;

public class MyBookingsListAdapter extends RecyclerView.Adapter<MyBookingsListAdapter.ViewHolder> {

    TicketListResponse ticketListResponse;
    private List<Ticket> listItems;
    private OnNoteListener onNoteListener;
    private Context context;

    public MyBookingsListAdapter(Context context, List<Ticket> listItems, OnNoteListener onNoteListener, TicketListResponse ticketListResponse) {
        this.context = context;
        this.listItems = listItems;
        this.onNoteListener = onNoteListener;
        this.ticketListResponse = ticketListResponse;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list_item, parent, false);
        return new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(final MyBookingsListAdapter.ViewHolder viewHolder, int i) {
        Ticket listItem = listItems.get(i);
        viewHolder.origin.setText(listItem.getTicket_details().getSource());
        viewHolder.date.setText(listItem.getTicket_details().getDate());
        viewHolder.destination.setText(listItem.getTicket_details().getDestination());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_qr);
        requestOptions.error(R.drawable.ic_error);
        Glide.with(context)
                .load("https://storage.googleapis.com/bizhub-kp/tickets/qrcodes/" + listItem.getTicket_details().getQr_code())
                .apply(requestOptions)
                .into(viewHolder.ticket_qr);
        viewHolder.download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public TextView destination;
        public TextView date;
        public Button download_btn;
        OnNoteListener onNoteListener;
        ImageView ticket_qr;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            origin = itemView.findViewById(R.id.start_station);
            date = itemView.findViewById(R.id.date);
            destination = itemView.findViewById(R.id.end_station);
            download_btn = itemView.findViewById(R.id.download_btn);
            ticket_qr = itemView.findViewById(R.id.ticket_qr);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

}
