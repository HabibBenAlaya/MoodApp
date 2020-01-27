package com.soprahr.moodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soprahr.moodapp.R;
import com.soprahr.moodapp.entities.SondageEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SondageAdapter extends ArrayAdapter<SondageEntity> {
    Context context;
    int resource;

    public SondageAdapter(Context context, int resource, ArrayList<SondageEntity> sondages) {
        super(context, resource, sondages);
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        SondageAdapter.SondageHolder holder = new SondageAdapter.SondageHolder();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource,parent,false);

            holder.tvTitre = view.findViewById(R.id.tv_titre_sondage);
            holder.tvCategorie = view.findViewById(R.id.tv_categorie_sondage);
            holder.tvDates = view.findViewById(R.id.tv_dates_sondage);
            holder.imgSondage = view.findViewById(R.id.img_sondage);
            view.setTag(holder);
        }
        else{
            holder = (SondageAdapter.SondageHolder) view.getTag();
        }

        holder.tvTitre.setText(getItem(position).getTitre());
        holder.tvCategorie.setText(getItem(position).getCategorie());
        holder.tvDates.setText("Du " + getItem(position).getDate_d() + " au " + getItem(position).getDate_f());
        if(getItem(position).getImageSondage()!=null) {
            Picasso.with(context).load(R.drawable.ic_survey_white).into(holder.imgSondage);
            // Picasso.with(context).load(getItem(position).getImageSondage()).into(holder.imgSondage);
        }
        return view;
    }

    class SondageHolder {
        TextView tvTitre;
        TextView tvCategorie;
        TextView tvDates;
        ImageView imgSondage;
    }


}
