package com.soprahr.moodapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soprahr.moodapp.R;
import com.soprahr.moodapp.entities.QuestionEntity;
import com.soprahr.moodapp.entities.ReponseEntity;
import com.soprahr.moodapp.entities.User;
import com.soprahr.moodapp.utils.BubbleSeekBar;
import com.soprahr.moodapp.utils.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    Button btnNext,btnPrev;
    TextView tvQuestion;
    RadioGroup rgQuestion;
    RadioButton rb1,rb2,rb3,rb4,rb5;
    EditText etRep;
    BubbleSeekBar bubbleSeekBar4;
    ImageView imgTermine,imgResult1,imgResult2,imgResult3;
    int pStatus1 = 0;
    int pStatus2 = 0;
    int pStatus3 = 0;
    float res1 = 1;
    float res2 = 1;
    float res3 = 1;
    private Handler handler = new Handler();
    TextView tvPercent1,tvPercent2,tvPercent3;
    ProgressBar mProgress1,mProgress2,mProgress3;
    RelativeLayout rlResult;
    String gender="Femme";

    ArrayList<QuestionEntity> questions = new ArrayList<>();
    ArrayList<QuestionEntity> questionsFinal = new ArrayList<>();
    ArrayList<ReponseEntity> reponses = new ArrayList<>();
    ArrayList<String> questionsTermine = new ArrayList<>();
    List<NameValuePair> params;
    List<NameValuePair> paramsRep;
    List<NameValuePair> paramsTermine;

    SharedPreferences mPrefs;
    User user = new User();

    private int actualQuest = 0;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //get user
        mPrefs = getActivity().getSharedPreferences("connexion", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mJson = mPrefs.getString("MyObject", "");
        user = gson.fromJson(mJson, User.class);

        System.out.println("the user is : "+user);
        System.out.println("user : "+mJson);

        gender=user.getGenre().toString();

        bubbleSeekBar4 = view.findViewById(R.id.seek_bar);
        btnNext = view.findViewById(R.id.btn_next_quest_home);
        btnPrev = view.findViewById(R.id.btn_prev_quest_home);
        tvQuestion = view.findViewById(R.id.tv_question);
        rgQuestion = view.findViewById(R.id.radio_group_question);
        rb1 = view.findViewById(R.id.question_radioButton1);
        rb2 = view.findViewById(R.id.question_radioButton2);
        rb3 = view.findViewById(R.id.question_radioButton3);
        rb4 = view.findViewById(R.id.question_radioButton4);
        rb5 = view.findViewById(R.id.question_radioButton5);
        etRep = view.findViewById(R.id.et_replibre_question);
        imgTermine = view.findViewById(R.id.img_questions_termine);

        //Initialisation circular bar
        imgResult1 = view.findViewById(R.id.img_result_mood1);
        imgResult2 = view.findViewById(R.id.img_result_mood2);
        imgResult3 = view.findViewById(R.id.img_result_mood3);
        tvPercent1 = view.findViewById(R.id.tv_percent1);
        tvPercent2 = view.findViewById(R.id.tv_percent2);
        tvPercent3 = view.findViewById(R.id.tv_percent3);
        mProgress1 = view.findViewById(R.id.circularProgressbar1);
        mProgress2 = view.findViewById(R.id.circularProgressbar2);
        mProgress3 = view.findViewById(R.id.circularProgressbar3);
        rlResult = view.findViewById(R.id.rl_result);

        Resources res = getResources();
        Drawable drawable1 = res.getDrawable(R.drawable.circular);
        Drawable drawable2 = res.getDrawable(R.drawable.circular);
        Drawable drawable3 = res.getDrawable(R.drawable.circular);
        mProgress1.setProgress(0);   // Main Progress
        mProgress1.setSecondaryProgress(100); // Secondary Progress
        mProgress1.setMax(100); // Maximum Progress
        mProgress1.setProgressDrawable(drawable1);

        mProgress2.setProgress(0);   // Main Progress
        mProgress2.setSecondaryProgress(100); // Secondary Progress
        mProgress2.setMax(100); // Maximum Progress
        mProgress2.setProgressDrawable(drawable2);

        mProgress3.setProgress(0);   // Main Progress
        mProgress3.setSecondaryProgress(100); // Secondary Progress
        mProgress3.setMax(100); // Maximum Progress
        mProgress3.setProgressDrawable(drawable3);


		//get les questions déja faites
        paramsTermine = new ArrayList<NameValuePair>();
        paramsTermine.add(new BasicNameValuePair("id_collab", user.getId()));

        ServerRequest sr2 = new ServerRequest();
        JSONObject json2 = sr2.getJSON("http://10.0.2.2:3000/liste_questions_termine_mobile",paramsTermine);

        if(json2 != null){
            try{
                String jsonstr = json2.getString("questions_termine");

                System.out.println("liste questions termines: "+jsonstr);
                JSONArray jsonarray = new JSONArray(jsonstr);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id_question = jsonobject.getString("id_question");
                    questionsTermine.add(id_question);
                }

                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

		//get les questions à faire
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_collab", user.getId()));
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.2.2:3000/liste_questions_mobile",params);

        if(json != null) {
            try {
                String jsonstr = json.getString("questions");

                System.out.println("liste questions : " + jsonstr);

                JSONArray jsonarray = new JSONArray(jsonstr);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String id = jsonobject.getString("id");
                    String titre = jsonobject.getString("titre");
                    String categorie = jsonobject.getString("categorie");
                    String type = jsonobject.getString("type");
                    String periodicite = jsonobject.getString("periodicite");
                    String activation = jsonobject.getString("activation");
                    String rep1 = jsonobject.getString("rep1");
                    String rep2 = jsonobject.getString("rep2");
                    String rep3 = jsonobject.getString("rep3");
                    String rep4 = jsonobject.getString("rep4");
                    String rep5 = jsonobject.getString("rep5");
                    String affichage = jsonobject.getString("affichage");
                    questions.add(new QuestionEntity(id, titre, categorie, type, periodicite, activation, "", rep1, rep2, rep3, rep4, rep5, affichage));

                }

                Log.d("Hello", jsonstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(questions.size()!=0){
            questionsFinal.addAll(questions);
        }else{
            moodResult();
        }

        int count=0;

        if(questionsTermine.size() != 0){

            for(int i=0; i<questions.size(); i++){

                for(int j=0; j<questionsTermine.size();j++){
                    System.out.println("i : "+i);
                    System.out.println("j : "+j);
                    System.out.println("s : "+questions.get(i).getId());
                    System.out.println("s : "+questionsTermine.get(j));
                    if(questions.get(i).getId().equals(questionsTermine.get(j))){
                        System.out.println("sondage equal : "+questions.get(i).getId());

                        questionsFinal.remove(i-count);
                        count++;
                    }

                }
            }

        }

        if(questionsFinal.size()==0){
            moodResult();
        }else{
            //Initialisation first question
            btnPrev.setVisibility(View.INVISIBLE);
            setTheScene(questionsFinal.get(actualQuest));

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(questionsFinal.get(actualQuest).getAffichage().equals("Bouton")){
                        reponses.add(new ReponseEntity("0", questionsFinal.get(actualQuest).getId(), user.getId(), getResponseFromRadioScene(), "",questionsFinal.get(actualQuest).getPeriodicite()));
                    }else if(questionsFinal.get(actualQuest).getAffichage().equals("Barre")){
                        reponses.add(new ReponseEntity("0", questionsFinal.get(actualQuest).getId(), user.getId(), getResponseFromSeekBar(bubbleSeekBar4.getProgress(),questionsFinal.get(actualQuest)), "",questionsFinal.get(actualQuest).getPeriodicite()));
                    }

                    actualQuest++;

                    //si les questions sont terminées
                    if((actualQuest+1)>questionsFinal.size()){

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

                                    endOfQuestions();

                                    Log.d("Hello", jsonstr);
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toasty.error(getActivity(), "Connexion à la BD échoué !", Toast.LENGTH_LONG).show();
                                Log.d("Hello", "échoué");
                            }
                        //get all collabs mood after 3 seconds
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        moodResult();
                                    }
                                },
                                3000);


                    }
                    //si c'est la derniere question
                    else if((actualQuest+1)==questionsFinal.size()){
                        btnNext.setText("TERMINER");
                        btnPrev.setVisibility(View.VISIBLE);
                        setTheScene(questionsFinal.get(actualQuest));
                    }
                    else{

                        btnPrev.setVisibility(View.VISIBLE);
                        setTheScene(questionsFinal.get(actualQuest));

                    }
                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualQuest--;

                    //supprimer la derniere reponse sauvgardée
                    reponses.remove(actualQuest);

                    //si c'est la premiere question
                    if((actualQuest)==0){
                        btnNext.setText("SUIVANT");
                        btnPrev.setVisibility(View.INVISIBLE);
                        setTheScene(questionsFinal.get(actualQuest));
                    }else{
                        btnNext.setText("SUIVANT");
                        setTheScene(questionsFinal.get(actualQuest));
                    }
                }
            });

        }

        return view;
    }

    private String getResponseFromRadioScene(){
        int radioButtonID = rgQuestion.getCheckedRadioButtonId();
        View radioButton = rgQuestion.findViewById(radioButtonID);
        int idx = rgQuestion.indexOfChild(radioButton);
        RadioButton r = (RadioButton)  rgQuestion.getChildAt(idx);
        String selectedtext = r.getText().toString();
        System.out.println("rep = "+selectedtext);

        if(etRep.isShown()){
            return etRep.getText().toString();
        }else{
            return selectedtext;
        }
    }

    private String getResponseFromSeekBar(int progress, QuestionEntity question){
        String rep = "";

        if(question.getType().equals("2 Réponses")){
            if(progress<=5){
                rep=question.getRep1();
            }else {
                rep=question.getRep2();
            }
        }else if(question.getType().equals("3 Réponses")){
            if(progress<=3){
                rep=question.getRep1();
            }else if(progress>3 && progress<7){
                rep=question.getRep2();
            }else{
                rep=question.getRep3();
            }
        }else if(question.getType().equals("4 Réponses")){
            if(progress<=2){
                rep=question.getRep1();
            }else if(progress>2 && progress<=5){
                rep=question.getRep2();
            }else if(progress>5 && progress<=8){
                rep=question.getRep3();
            }else{
                rep=question.getRep4();
            }
        }else if(question.getType().equals("5 Réponses")){
            if(progress<=2){
                rep=question.getRep1();
            }else if(progress>2 && progress<=4){
                rep=question.getRep2();
            }else if(progress>4 && progress<=6){
                rep=question.getRep3();
            }else if(progress>6 && progress<=8){
                rep=question.getRep4();
            }else{
                rep=question.getRep5();
            }
        }

        return rep;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setTheScene(QuestionEntity question){

        tvQuestion.setText(question.getTitre());

        if(question.getAffichage().equals("Bouton")){

            bubbleSeekBar4.setVisibility(View.GONE);

            if(question.getTitre().length()<100){
                tvQuestion.setTextSize(24);
            }else if(question.getTitre().length()>=100 && question.getTitre().length()<200){
                tvQuestion.setTextSize(20);
            }else{
                tvQuestion.setTextSize(18);
            }
            setTheVisibility(question.getType());
            rb1.setText(question.getRep1());
            rb2.setText(question.getRep2());
            rb3.setText(question.getRep3());
            rb4.setText(question.getRep4());
            rb5.setText(question.getRep5());
            rb1.setChecked(true);
        }
        else if(question.getAffichage().equals("Barre")){

            tvQuestion.setTextSize(24);
            rb1.setVisibility(View.GONE);
            rb2.setVisibility(View.GONE);
            rb3.setVisibility(View.GONE);
            rb4.setVisibility(View.GONE);
            rb5.setVisibility(View.GONE);
            etRep.setVisibility(View.GONE);
            bubbleSeekBar4.setVisibility(View.VISIBLE);

            setTheSeekBar(question);
        }

    }

    private void setTheSeekBar(final QuestionEntity question){
        bubbleSeekBar4.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                if(question.getType().equals("2 Réponses")){
                    array.put(2, question.getRep1());
                    array.put(8, question.getRep2());
                }else if(question.getType().equals("3 Réponses")){
                    array.put(2, question.getRep1());
                    array.put(5, question.getRep2());
                    array.put(8, question.getRep3());
                }else if(question.getType().equals("4 Réponses")){
                    array.put(1, question.getRep1());
                    array.put(3, question.getRep2());
                    array.put(7, question.getRep3());
                    array.put(9, question.getRep4());
                }else if(question.getType().equals("5 Réponses")){
                    array.put(1, question.getRep1().toString());
                    array.put(3, question.getRep2().toString());
                    array.put(5, question.getRep3().toString());
                    array.put(7, question.getRep4().toString());
                    array.put(9, question.getRep5().toString());
                }

                return array;
            }
        });
        bubbleSeekBar4.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @NonNull
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                int color;
                if (progress <= 2) {
                    color = ContextCompat.getColor(getActivity(), R.color.red);
                } else if (progress <= 4) {
                    color = ContextCompat.getColor(getActivity(), R.color.sopra_orange);
                } else if (progress <= 6) {
                    color = ContextCompat.getColor(getActivity(), R.color.yellow);
                } else if (progress <= 8) {
                    color = ContextCompat.getColor(getActivity(), R.color.yellow_green);
                } else {
                    color = ContextCompat.getColor(getActivity(), R.color.green_fade);
                }

                bubbleSeekBar.setSecondTrackColor(color);
                bubbleSeekBar.setThumbColor(color);
                bubbleSeekBar.setBubbleColor(color);
                System.out.println("prog: "+bubbleSeekBar.getProgress());
            }
        });
        bubbleSeekBar4.setProgress(5);
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
        }
    }

    private void endOfQuestions(){
        tvQuestion.setVisibility(View.GONE);
        rb1.setVisibility(View.GONE);
        rb2.setVisibility(View.GONE);
        rb3.setVisibility(View.GONE);
        rb4.setVisibility(View.GONE);
        rb5.setVisibility(View.GONE);
        etRep.setVisibility(View.GONE);
        bubbleSeekBar4.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        btnPrev.setVisibility(View.GONE);
        imgTermine.setVisibility(View.VISIBLE);
    }

    private void moodResult(){
        tvQuestion.setVisibility(View.GONE);
        rb1.setVisibility(View.GONE);
        rb2.setVisibility(View.GONE);
        rb3.setVisibility(View.GONE);
        rb4.setVisibility(View.GONE);
        rb5.setVisibility(View.GONE);
        etRep.setVisibility(View.GONE);
        bubbleSeekBar4.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        btnPrev.setVisibility(View.GONE);
        imgTermine.setVisibility(View.GONE);
        rlResult.setVisibility(View.VISIBLE);

        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.2.2:3000/result_humeur_mobile",params);

        if(json != null) {
            try {
                String humCollabToday = json.getString("humCollabToday");
                res1 = Float.parseFloat(humCollabToday);
                System.out.println("humCollabToday : " + humCollabToday);

                String humEntrepToday = json.getString("humEntrepToday");
                res2 = Float.parseFloat(humEntrepToday);
                System.out.println("humEntrepToday : " + humEntrepToday);

                String humEntrepWeek = json.getString("humEntrepWeek");
                res3 = Float.parseFloat(humEntrepWeek);
                System.out.println("humEntrepWeek : " + humEntrepWeek);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getMoodPicture(res1, 1);
        getMoodPicture(res2, 2);
        getMoodPicture(res3, 3);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                while (pStatus1 < res1) {
                    pStatus1 += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress1.setProgress(pStatus1);
                            tvPercent1.setText(pStatus1 + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(16); //thread will take approx 3 seconds to finish

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (pStatus2 < res2) {
                    pStatus2 += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress2.setProgress(pStatus2);
                            tvPercent2.setText(pStatus2 + "%");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(16); //thread will take approx 3 seconds to finish

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (pStatus3 < res3) {
                    pStatus3 += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress3.setProgress(pStatus3);
                            tvPercent3.setText(pStatus3 + "%");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(16); //thread will take approx 3 seconds to finish

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private void getMoodPicture(float res, int img){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            if(gender.equals("Homme")){
                if(img==1){
                    if(res>=0 && res <20){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_tresmal_m));
                    }else if(res>=20 && res <40){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_mal_m));
                    }else if(res>=40 && res <60){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_ok_m));
                    }else if(res>=60 && res <80){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_bien_m));
                    }else if(res>=80){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_super_m));
                    }
                }
                else if(img==2){
                    if(res>=0 && res <20){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_tresmal_m));
                    }else if(res>=20 && res <40){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_mal_m));
                    }else if(res>=40 && res <60){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_ok_m));
                    }else if(res>=60 && res <80){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_bien_m));
                    }else if(res>=80){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_super_m));
                    }
                }
                else if(img==3){
                    if(res>=0 && res <20){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_tresmal_m));
                    }else if(res>=20 && res <40){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_mal_m));
                    }else if(res>=40 && res <60){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_ok_m));
                    }else if(res>=60 && res <80){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_bien_m));
                    }else if(res>=80){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_super_m));
                    }
                }
            }
            else if(gender.equals("Femme")){
                if(img==1){
                    if(res>=0 && res <20){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_tresmal_w));
                    }else if(res>=20 && res <40){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_mal_w));
                    }else if(res>=40 && res <60){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_ok_w));
                    }else if(res>=60 && res <80){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_bien_w));
                    }else if(res>=80){
                        imgResult1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_super_w));
                    }
                }
                else if(img==2){
                    if(res>=0 && res <20){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_tresmal_w));
                    }else if(res>=20 && res <40){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_mal_w));
                    }else if(res>=40 && res <60){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_ok_w));
                    }else if(res>=60 && res <80){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_bien_w));
                    }else if(res>=80){
                        imgResult2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_super_w));
                    }
                }
                else if(img==3){
                    if(res>=0 && res <20){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_tresmal_w));
                    }else if(res>=20 && res <40){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_mal_w));
                    }else if(res>=40 && res <60){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_ok_w));
                    }else if(res>=60 && res <80){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_bien_w));
                    }else if(res>=80){
                        imgResult3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_super_w));
                    }
                }
            }

        }
    }

}
