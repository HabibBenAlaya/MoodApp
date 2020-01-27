package com.soprahr.moodapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soprahr.moodapp.MainActivity;
import com.soprahr.moodapp.R;
import com.soprahr.moodapp.SecondActivity;
import com.soprahr.moodapp.entities.User;
import com.soprahr.moodapp.utils.Mail;
import com.soprahr.moodapp.utils.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    private static MainActivity parent;

    List<NameValuePair> params= new ArrayList<NameValuePair>();
    List<NameValuePair> params2= new ArrayList<NameValuePair>();

    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    User user;

    String userMail="";
    String userPassword="";

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mPrefs = getActivity().getSharedPreferences("connexion", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();

        ButterKnife.bind(getActivity());

        _loginButton = view.findViewById(R.id.btn_login);
        _signupLink = view.findViewById(R.id.link_signup);
        _emailText = view.findViewById(R.id.input_email);
        _passwordText = view.findViewById(R.id.input_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!validateMail()) {
                    return;
                }
                System.out.println("mail : "+userMail);

                new SendMail().execute("");


            }
        });

        return view;
    }

	//Envoyer un mail si mot de passe oublié
    private class SendMail extends AsyncTask<String, Integer, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Attendez SVP", "Nous sommes en train d'envoyer votre mot de passe à votre adresse mail..", true, false);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(aVoid.equalsIgnoreCase("ok")){
                Toasty.success(getActivity(), "Mot de passe envoyé avec succés.", Toast.LENGTH_LONG).show();
            }else{
                Toasty.error(getActivity(), "Erreur d'envoie !", Toast.LENGTH_LONG).show();
            }
        }

        protected String doInBackground(String... params) {
            Mail m = new Mail("noreplay.moodup@gmail.com", "azerty1312$");

            String[] toArr = {userMail};
            m.setTo(toArr);
            m.setFrom("noreplay.moodup@gmail.com");
            m.setSubject("Mot de passe Pléiades MoodUp");
            m.setBody("Votre mot de passe est : " + userPassword);

            try {
                if(m.send()) {
                    return "ok";
                } else {
                    return "not ok";
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
                return "not ok";
            }
        }
    }


    public void login() {
        Log.d(TAG, "Connexion");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connexion...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //Redirection after login
                        Intent myIntent = new Intent(getActivity(), SecondActivity.class);
                        myIntent.putExtra("message", "home");
                        getActivity().startActivity(myIntent);

                        progressDialog.dismiss();
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                    }
                }, 3000);


    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        getActivity().finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //email matching : !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if(email.isEmpty() || password.isEmpty()){
            if (email.isEmpty()) {
                _emailText.setError("Utilisateur invalide");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty()) {
                _passwordText.setError("Mot de passe invalide");
                valid = false;
            } else {
                _passwordText.setError(null);
            }
        }else{
            params.removeAll(params);
            params.add(new BasicNameValuePair("user", email));
            params.add(new BasicNameValuePair("password", password));

            System.out.println(params);

			//Verification des données d'authentification / get user informations
            ServerRequest sr = new ServerRequest();
            JSONObject json = sr.getJSON("http://10.0.2.2:3000/login_mobile",params);

            if(json != null) {
                try {
                    String jsonstr = json.getString("user");

                    System.out.println("user : " + jsonstr);

                    if (jsonstr.equals("no user")) {
                        _emailText.setError("Utilisateur invalide");
                        valid = false;
                    } else if (jsonstr.equals("no password")) {
                        _passwordText.setError("Mot de passe invalide");
                        valid = false;
                    } else {
                        _emailText.setError(null);
                        _passwordText.setError(null);
                        JSONArray jsonarray = new JSONArray(jsonstr);
                        JSONObject jsonobject = jsonarray.getJSONObject(0);
                        String id = jsonobject.getString("id");
                        String nom = jsonobject.getString("nom");
                        String prenom = jsonobject.getString("prenom");
                        String utilisateur = jsonobject.getString("utilisateur");
                        String password2 = jsonobject.getString("password");
                        String email2 = jsonobject.getString("email");
                        String role = jsonobject.getString("role");
                        String equipe = jsonobject.getString("equipe");
                        String admin = jsonobject.getString("admin");
                        String genre = jsonobject.getString("genre");
                        byte[] photo = jsonobject.getString("photo").getBytes();
                        user = new User(id, nom, prenom, utilisateur, password2, email2, role, equipe, admin, genre, photo);

                        //set shared preferences
                        Gson gson = new Gson();
                        String mJson = gson.toJson(user);
                        System.out.println("prefs user : "+mJson);
                        prefsEditor.putString("MyObject", mJson);
                        prefsEditor.commit();

                        valid = true;
                    }


                    Log.d("Hello", jsonstr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        return valid;
    }

    public boolean validateMail() {
        boolean valid = false;

        String email = _emailText.getText().toString();


        if(email.isEmpty()){
            if (email.isEmpty()) {
                _emailText.setError("Utilisateur invalide");
                valid = false;
            } else {
                _emailText.setError(null);
            }

        }else{
            params2.removeAll(params2);
            params2.add(new BasicNameValuePair("user", email));

            System.out.println(params2);

            ServerRequest sr = new ServerRequest();
            JSONObject json = sr.getJSON("http://10.0.2.2:3000/get_mail_mobile",params2);

            if(json != null) {
                try {
                    String jsonstr = json.getString("user");
                    String jsonstr2 = json.getString("password");

                    System.out.println("user : " + jsonstr);

                    if (jsonstr.equals("no user")) {
                        _emailText.setError("Utilisateur invalide");
                        valid = false;
                    } else {
                        _emailText.setError(null);
                        _passwordText.setError(null);

                        userMail = jsonstr;
                        userPassword = jsonstr2;

                        valid = true;
                    }

                    Log.d("Hello", jsonstr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return valid;
    }

}
