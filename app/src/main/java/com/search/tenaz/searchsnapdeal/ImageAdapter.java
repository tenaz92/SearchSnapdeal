package com.search.tenaz.searchsnapdeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by tenaz on 12/5/17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    public ArrayList<Model> getList() {
        return list;
    }

    public void setList(ArrayList<Model> list) {
        this.list = list;
    }

    ArrayList<Model> list;
    Context context;

    public ImageAdapter(ArrayList<Model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isFooter()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = null;

        switch (viewType) {
            case 1:
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
                break;
            case 2:
                rowView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_list_row, parent, false);
        }
        return new MyViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (list.get(position).isFooter()) {

            //click listener
            holder.loadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new LoadMoreEvent());

                }
            });

        } else {
            if (list.get(position).equals("")) {

            } else {
                holder.title.setText(list.get(position).getDescription());

                Picasso.with(context)
                        .load(list.get(position).getImageUrl())
                        .into(holder.image, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                holder.progressBar.setVisibility(View.INVISIBLE);

                            }
                        });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        ProgressBar progressBar;
        Button loadMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView2);
            title = (TextView) itemView.findViewById(R.id.title);
            progressBar = itemView.findViewById(R.id.progressBar);
            loadMore = itemView.findViewById(R.id.load_more);


        }
    }
}
