package com.soprahr.moodapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soprahr.moodapp.EditProfileActivity;
import com.soprahr.moodapp.R;
import com.soprahr.moodapp.SondageActivity;
import com.soprahr.moodapp.adapters.SondageAdapter;
import com.soprahr.moodapp.entities.SondageEntity;
import com.soprahr.moodapp.entities.User;
import com.soprahr.moodapp.utils.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SondageFragment extends Fragment {

    ArrayList<SondageEntity> sondages;
    ArrayList<SondageEntity> sondagesFinal;
    ArrayList<String> sondagesTermine;
    ListView listView;
    private static SondageAdapter adapter;
    List<NameValuePair> params;
    List<NameValuePair> params2;
    ImageView img;

    SharedPreferences mPrefs;
    User user = new User();

    public SondageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sondage, container, false);

        //get user
        mPrefs = getActivity().getSharedPreferences("connexion", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mJson = mPrefs.getString("MyObject", "");
        user = gson.fromJson(mJson, User.class);

        img = view.findViewById(R.id.img_sondage_vide);
		
		//get les sondages déja faites
        params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("id_collab", user.getId()));

        ServerRequest sr2 = new ServerRequest();
        JSONObject json2 = sr2.getJSON("http://10.0.2.2:3000/liste_sondage_termine_mobile",params2);
        sondagesTermine = new ArrayList<>();

        if(json2 != null){
            try{
                String jsonstr = json2.getString("sondages_termine");

                System.out.println("liste sondages termines: "+jsonstr);
                JSONArray jsonarray = new JSONArray(jsonstr);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id_sondage_termine = jsonobject.getString("id_sondage");
                    sondagesTermine.add(id_sondage_termine);
                }

                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

		//get les sondages à faire
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", "type"));

        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.2.2:3000/liste_sondages_mobile",params);

        sondages = new ArrayList<>();
        if(json != null){
            try{
                String jsonstr = json.getString("ec_sondages");

                System.out.println("liste sondages : "+jsonstr);
                listView = view.findViewById(R.id.sondages_list);

                JSONArray jsonarray = new JSONArray(jsonstr);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id = jsonobject.getString("id_sondage");
                    String titre = jsonobject.getString("titre");
                    String categorie = jsonobject.getString("categorie");
                    String dateD = jsonobject.getString("date_d");
                    String dateF = jsonobject.getString("date_f");

                    sondages.add(new SondageEntity(id,titre,categorie,dateD, dateF,"vide"));

                    }

                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        sondagesFinal = new ArrayList<>();
        if(sondages.size()!=0){
            sondagesFinal.addAll(sondages);
        }

        int count=0;

		//Afficher les sondages à faire
        if(sondagesTermine.size() != 0){

            for(int i=0; i<sondages.size(); i++){

                for(int j=0; j<sondagesTermine.size();j++){
                    System.out.println("i : "+i);
                    System.out.println("j : "+j);
                    System.out.println("s : "+sondages.get(i).getId());
                    System.out.println("s : "+sondagesTermine.get(j));
                    if(sondages.get(i).getId().equals(sondagesTermine.get(j))){
                        System.out.println("sondage equal : "+sondages.get(i).getId());

                        sondagesFinal.remove(i-count);
                        count++;
                    }

                }
            }

        }

        if(sondagesFinal.size()==0){
            img.setVisibility(View.VISIBLE);
        }
        else{
            img.setVisibility(View.GONE);
            adapter= new SondageAdapter(getActivity(),R.layout.sondage_row_item, sondagesFinal);
            listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SondageEntity sondage = sondagesFinal.get(position);
                Intent myIntent = new Intent(getActivity(), SondageActivity.class);
                myIntent.putExtra("ID_SONDAGE", sondage.getId());
                getActivity().startActivityForResult(myIntent,1);
            }
        });
        }
        return  view;
    }


    }
