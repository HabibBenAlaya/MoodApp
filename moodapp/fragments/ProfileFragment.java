package com.soprahr.moodapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soprahr.moodapp.EditProfileActivity;
import com.soprahr.moodapp.MainActivity;
import com.soprahr.moodapp.R;
import com.soprahr.moodapp.entities.User;
import com.soprahr.moodapp.utils.PictureUtility;
import com.soprahr.moodapp.utils.RoundImage;
import com.soprahr.moodapp.utils.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String TAG = "LoginActivity";
    ImageButton imgProfile,editProfile;
    ImageView imgCover;
    TextView tvName,tvRole,tvEquipe,tvEmail,tvUtilisateur,tvGenre;
    Spinner optionSpinner;
    RoundImage roundedImage;
    String spinnerList[] = {"À propos", "Déconnexion"};

    List<NameValuePair> params= new ArrayList<NameValuePair>();

    SharedPreferences mPrefs;
    User user = new User();

    private byte[] imageData = null;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get user
        mPrefs = getActivity().getSharedPreferences("connexion", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mJson = mPrefs.getString("MyObject", "");
        user = gson.fromJson(mJson, User.class);


        imgProfile =  view.findViewById(R.id.user_profile_photo);

        editProfile = view.findViewById(R.id.edit_profile);

        imgCover =  view.findViewById(R.id.header_cover_image);

        optionSpinner = view.findViewById(R.id.option_spinner);

        tvName = view.findViewById(R.id.user_profile_name);
        tvEmail = view.findViewById(R.id.user_profile_email);
        tvEquipe = view.findViewById(R.id.user_profile_equipe);
        tvGenre = view.findViewById(R.id.user_profile_genre);
        tvRole = view.findViewById(R.id.user_profile_role);
        tvUtilisateur = view.findViewById(R.id.user_profile_utilisateur);

        tvName.setText(user.getNom()+" "+user.getPrenom());
        tvEmail.setText(user.getEmail());
        tvEquipe.setText(user.getEquipe());
        tvGenre.setText(user.getGenre());
        tvRole.setText(user.getRole());
        tvUtilisateur.setText(user.getUtilisateur());

        if(user.getGenre().equals("Homme")){
            //Rounding image
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.img_super_m);
            roundedImage = new RoundImage(bm);
            imgProfile.setImageDrawable(roundedImage);
        } else{
            //Rounding image
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.img_super_w);
            roundedImage = new RoundImage(bm);
            imgProfile.setImageDrawable(roundedImage);
        }


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
		
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,spinnerList);
        optionSpinner.setAdapter(adapter);

        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {

                switch (pos) {
                    case 0:
                        //à propos
                        break;
                    case 1:
                        logout();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        return view;
    }

	//Déconnexion
    public void logout() {
        Log.d(TAG, "Déconnexion");


        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Déconnexion...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent myIntent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(myIntent);
                        progressDialog.dismiss();
                        getActivity().finish();

                    }
                }, 3000);
    }


	//Permission android
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PictureUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                    Toast.makeText(getActivity(), "Oups ! vous n'avez pas la permission.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

	//Choisir une photot
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= PictureUtility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //resize picture
        Bitmap bmw = Bitmap.createScaledBitmap(thumbnail, 120, 120, false);

        //rounding picture
        roundedImage = new RoundImage(bmw);
        imgProfile.setImageDrawable(roundedImage);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap bmw = Bitmap.createScaledBitmap(bm, 120, 120, false);

        roundedImage = new RoundImage(bmw);
        imgProfile.setImageDrawable(roundedImage);
    }

}
