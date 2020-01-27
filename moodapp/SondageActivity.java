package com.soprahr.moodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soprahr.moodapp.entities.QuestionEntity;
import com.soprahr.moodapp.entities.ReponseEntity;
import com.soprahr.moodapp.entities.User;
import com.soprahr.moodapp.utils.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SondageActivity extends AppCompatActivity {

    ImageButton btnCancel,btnValidate;
    TextView tvNbr,tvQuestion;
    RadioButton rb1,rb2,rb3,rb4,rb5;
    RadioGroup rgSondage;
    Button btnNext,btnPrev;
    EditText etRep;
    Toolbar tbSondage;
    ImageView imgTermine;
    private int actualQuest = 0;

    ArrayList<QuestionEntity> questions;
    QuestionEntity question;
    List<NameValuePair> params;
    List<NameValuePair> paramsRep;
    ArrayList<ReponseEntity> reponses = new ArrayList<ReponseEntity>();

    private String idSondage;

    SharedPreferences mPrefs;
    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sondage);

        //get user
        mPrefs = this.getSharedPreferences("connexion", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mJson = mPrefs.getString("MyObject", "");
        user = gson.fromJson(mJson, User.class);

        idSondage = getIntent().getStringExtra("ID_SONDAGE");
        System.out.println(idSondage);

        btnCancel = findViewById(R.id.cancel_sondage);
        btnValidate = findViewById(R.id.validate_sondage);
        tvNbr = findViewById(R.id.nbr_question_sondage);
        tvQuestion = findViewById(R.id.tv_question_sondage);
        rgSondage = findViewById(R.id.radio_group_sondage);
        tbSondage = findViewById(R.id.toolbar_sondage);
        imgTermine = findViewById(R.id.img_sondage_termine);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        rb5 = findViewById(R.id.radioButton5);
        btnNext = findViewById(R.id.btn_next_quest_sond);
        btnPrev = findViewById(R.id.btn_prev_quest_sond);
        etRep = findViewById(R.id.et_replibre_sondage);

        rb1.setChecked(true);
        btnValidate.setVisibility(View.INVISIBLE);
        btnPrev.setVisibility(View.INVISIBLE);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", idSondage));


        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.2.2:3000/liste_questions_sondage_mobile",params);

        if(json != null){
            try{
                String jsonstr = json.getString("questions");

                System.out.println("liste questions : "+jsonstr);
                questions = new ArrayList<>();

                JSONArray jsonarray = new JSONArray(jsonstr);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id = jsonobject.getString("id");
                    String titre = jsonobject.getString("titre");
                    String type = jsonobject.getString("type");
                    String rep1 = jsonobject.getString("rep1");
                    String rep2 = jsonobject.getString("rep2");
                    String rep3 = jsonobject.getString("rep3");
                    String rep4 = jsonobject.getString("rep4");
                    String rep5 = jsonobject.getString("rep5");
                    questions.add(new QuestionEntity(id,titre,"",type,"","",idSondage,rep1,rep2,rep3,rep4,rep5,""));

                }

                tvNbr.setText("Question 1");
                tvQuestion.setText(questions.get(0).getTitre());
                setTheVisibility(questions.get(0).getType());
                rb1.setText(questions.get(0).getRep1());
                rb2.setText(questions.get(0).getRep2());
                rb3.setText(questions.get(0).getRep3());
                rb4.setText(questions.get(0).getRep4());
                rb5.setText(questions.get(0).getRep5());

                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonID = rgSondage.getCheckedRadioButtonId();
                View radioButton = rgSondage.findViewById(radioButtonID);
                int idx = rgSondage.indexOfChild(radioButton)+1;
                System.out.println("index is : "+idx);
                String selectedtext = "rep"+idx;
                System.out.println("rep = "+selectedtext);

                //ajouter la reponse precedente
                if(etRep.isShown()){
                    reponses.add(new ReponseEntity(idSondage, questions.get(actualQuest).getId(), user.getId(), etRep.getText().toString(), "","0"));
                }else{
                    reponses.add(new ReponseEntity(idSondage, questions.get(actualQuest).getId(), user.getId(), selectedtext, "","0"));
                }

                actualQuest++;
                rb1.setChecked(true);
                //si le sondage est terminé
                if((actualQuest+1)>questions.size()){

                    //ajouter les reponses
                    paramsRep = new ArrayList<NameValuePair>();

                    for (int i = 0; i < reponses.size(); i++) {
                        paramsRep.add(new BasicNameValuePair("idSondage"+(i+1), reponses.get(i).getId_sondage()));
                        paramsRep.add(new BasicNameValuePair("idQuestion"+(i+1), reponses.get(i).getId_question()));
                        paramsRep.add(new BasicNameValuePair("idCollab"+(i+1), reponses.get(i).getId_collab()));
                        paramsRep.add(new BasicNameValuePair("reponse"+(i+1), reponses.get(i).getReponse()));
                        paramsRep.add(new BasicNameValuePair("periodicite"+(i+1), reponses.get(i).getPeriodicite()));

                        System.out.println("rep "+(i+1)+" "+reponses.get(i));
                    }
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON("http://10.0.2.2:3000/add_reponses",paramsRep);
                    if(json != null){
                        try{
                            //Toasty.success(getApplication(), "Vos réponses ont été envoyé avec succès !", Toast.LENGTH_LONG).show();
                            String jsonstr = json.getString("response");
                            setTheVisibility("terminé");
                            Log.d("Hello", jsonstr);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toasty.error(getApplication(), "Connexion à la BD échoué !", Toast.LENGTH_LONG).show();
                        Log.d("Hello", "échoué");
                    }
                    //send result to reload sondage fragment after 3 seconds
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("id","value");
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            },
                            3000);

                }
                //si c'est la derniere question
                else if((actualQuest+1)==questions.size()){
                    //btnNext.setVisibility(View.INVISIBLE);
                     btnNext.setText("TERMINER");
                    //btnValidate.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                    tvNbr.setText("Question "+ (actualQuest+1));
                    tvQuestion.setText(questions.get(actualQuest).getTitre());
                    setTheVisibility(questions.get(actualQuest).getType());
                    rb1.setText(questions.get(actualQuest).getRep1());
                    rb2.setText(questions.get(actualQuest).getRep2());
                    rb3.setText(questions.get(actualQuest).getRep3());
                    rb4.setText(questions.get(actualQuest).getRep4());
                    rb5.setText(questions.get(actualQuest).getRep5());
                }else{
                    btnPrev.setVisibility(View.VISIBLE);
                    tvNbr.setText("Question "+ (actualQuest+1));
                    tvQuestion.setText(questions.get(actualQuest).getTitre());
                    setTheVisibility(questions.get(actualQuest).getType());
                    rb1.setText(questions.get(actualQuest).getRep1());
                    rb2.setText(questions.get(actualQuest).getRep2());
                    rb3.setText(questions.get(actualQuest).getRep3());
                    rb4.setText(questions.get(actualQuest).getRep4());
                    rb5.setText(questions.get(actualQuest).getRep5());
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualQuest--;

                //supprimer la derniere reponse sauvgardée
                reponses.remove(actualQuest);

                rb1.setChecked(true);
                //si c'est la premiere question
                if((actualQuest)==0){
                    //btnNext.setVisibility(View.VISIBLE);
                    btnNext.setText("SUIVANT");
                    btnPrev.setVisibility(View.INVISIBLE);
                    btnValidate.setVisibility(View.INVISIBLE);
                    tvNbr.setText("Question "+ (actualQuest+1));
                    tvQuestion.setText(questions.get(actualQuest).getTitre());
                    setTheVisibility(questions.get(actualQuest).getType());
                    rb1.setText(questions.get(actualQuest).getRep1());
                    rb2.setText(questions.get(actualQuest).getRep2());
                    rb3.setText(questions.get(actualQuest).getRep3());
                    rb4.setText(questions.get(actualQuest).getRep4());
                    rb5.setText(questions.get(actualQuest).getRep5());
                }else{
                    //btnNext.setVisibility(View.VISIBLE);
                    btnNext.setText("SUIVANT");
                    btnValidate.setVisibility(View.INVISIBLE);
                    tvNbr.setText("Question "+ (actualQuest+1));
                    tvQuestion.setText(questions.get(actualQuest).getTitre());
                    setTheVisibility(questions.get(actualQuest).getType());
                    rb1.setText(questions.get(actualQuest).getRep1());
                    rb2.setText(questions.get(actualQuest).getRep2());
                    rb3.setText(questions.get(actualQuest).getRep3());
                    rb4.setText(questions.get(actualQuest).getRep4());
                    rb5.setText(questions.get(actualQuest).getRep5());
                }
            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void setTheVisibility(String type) {
        System.out.println(type);
        if(type.equals("2 Réponses")){
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.GONE);
            rb4.setVisibility(View.GONE);
            rb5.setVisibility(View.GONE);
            etRep.setVisibility(View.GONE);
        }else if(type.equals("3 Réponses")){
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.VISIBLE);
            rb4.setVisibility(View.GONE);
            rb5.setVisibility(View.GONE);
            etRep.setVisibility(View.GONE);
        }else if(type.equals("4 Réponses")){
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.VISIBLE);
            rb4.setVisibility(View.VISIBLE);
            rb5.setVisibility(View.GONE);
            etRep.setVisibility(View.GONE);
        }else if(type.equals("5 Réponses")){
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.VISIBLE);
            rb4.setVisibility(View.VISIBLE);
            rb5.setVisibility(View.VISIBLE);
            etRep.setVisibility(View.GONE);
        }else if(type.equals("Texte libre")){
            rb1.setVisibility(View.GONE);
            rb2.setVisibility(View.GONE);
            rb3.setVisibility(View.GONE);
            rb4.setVisibility(View.GONE);
            rb5.setVisibility(View.GONE);
            etRep.setVisibility(View.VISIBLE);
        }else {
            rb1.setVisibility(View.GONE);
            rb2.setVisibility(View.GONE);
            rb3.setVisibility(View.GONE);
            rb4.setVisibility(View.GONE);
            rb5.setVisibility(View.GONE);
            etRep.setVisibility(View.GONE);
            tbSondage.setVisibility(View.GONE);
            tvQuestion.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            btnPrev.setVisibility(View.GONE);
            imgTermine.setVisibility(View.VISIBLE);
        }
    }
}
