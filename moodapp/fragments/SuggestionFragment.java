package com.soprahr.moodapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.soprahr.moodapp.R;
import com.soprahr.moodapp.entities.ReponseEntity;
import com.soprahr.moodapp.utils.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SuggestionFragment extends Fragment {

    EditText etType, etSujet, etTitre, etDesc;
    Button addSugg,btnType,btnSujet;
    String type = "";
    String sujet = "";
    String titre,description;
    List<NameValuePair> params;


    public SuggestionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);

        etType = view.findViewById(R.id.et_type_sugg);
        etSujet = view.findViewById(R.id.et_sujet_sugg);
        etTitre = view.findViewById(R.id.et_titre_sugg);
        etDesc = view.findViewById(R.id.et_desc_sugg);
        addSugg = view.findViewById(R.id.btn_envoyer_sugg);


        final CharSequence typeSec[] = new CharSequence[] {"Proposition", "Plainte", "Remarque"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Type de message");
        builder.setItems(typeSec, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type = typeSec[which].toString();
                etType.setText(type);
            }
        });

        final CharSequence sujetSec[] = new CharSequence[] {"Loisir", "Espace de travail", "Bonheur au travail", "Relation avec les managers", "relation avec les collaborateurs", "Autre.."};

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        builder2.setTitle("Sujet");
        builder2.setItems(sujetSec, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sujet = sujetSec[which].toString();
                etSujet.setText(sujet);
            }
        });

        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });

        etSujet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder2.show();
            }
        });

        addSugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titre = etTitre.getText().toString();
                description = etDesc.getText().toString();

                if(type.equals("")){
                    Toasty.error(getActivity(), "Vous devez sélectionner le type du message !", Toast.LENGTH_SHORT, true).show();
                }else if(sujet.equals("")){
                    Toasty.error(getActivity(), "Vous devez sélectionner le sujet !", Toast.LENGTH_SHORT, true).show();
                }else if(titre.equals("")){
                    Toasty.error(getActivity(), "Vous devez saisir le titre !", Toast.LENGTH_SHORT, true).show();
                }else if(description.equals("")){
                    Toasty.error(getActivity(), "Vous devez saisir la description !", Toast.LENGTH_SHORT).show();
                }else{
                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("type", type));
                    params.add(new BasicNameValuePair("sujet", sujet));
                    params.add(new BasicNameValuePair("titre", titre));
                    params.add(new BasicNameValuePair("description", description));

                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON("http://10.0.2.2:3000/add_suggestion",params);

                    if(json != null){
                        try{
                            String jsonstr = json.getString("response");

                            Toasty.success(getActivity(), jsonstr, Toast.LENGTH_LONG).show();

                            type = "";
                            sujet = "";
                            etType.setText("");
                            etSujet.setText("");
                            etTitre.setText("");
                            etDesc.setText("");

                            Log.d("Hello", jsonstr);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toasty.error(getActivity(), "Connexion echoué !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

}
