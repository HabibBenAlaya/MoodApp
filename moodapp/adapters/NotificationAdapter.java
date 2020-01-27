package com.soprahr.moodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soprahr.moodapp.R;
import com.soprahr.moodapp.entities.NotificationEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Habib on 27/02/2018.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationEntity> {

    Context context;
    int resource;

    public NotificationAdapter(Context context, int resource, ArrayList<NotificationEntity> notifications) {
        super(context, resource, notifications);
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        NotificationHolder holder = new NotificationHolder();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource,parent,false);

            holder.tvTitre = view.findViewById(R.id.tv_titre);
            holder.tvDescription = view.findViewById(R.id.tv_description);
            holder.imgNotification = view.findViewById(R.id.img_notification);
            view.setTag(holder);
        }
        else{
            holder = (NotificationHolder) view.getTag();
        }

        holder.tvTitre.setText(getItem(position).getTitre());
        holder.tvDescription.setText(getItem(position).getDescription());
        System.out.println(getItem(position).getImageNotification());
        if(getItem(position).getImageNotification()!=null) {
            Picasso.with(context).load(R.drawable.ic_edit_profile).into(holder.imgNotification);
        }
        return view;
    }

    class NotificationHolder {
        TextView tvTitre;
        TextView tvDescription;
        ImageView imgNotification;
    }
}
