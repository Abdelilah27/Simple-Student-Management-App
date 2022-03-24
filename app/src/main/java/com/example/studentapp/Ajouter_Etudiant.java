package com.example.studentapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Ajouter_Etudiant extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextNom, editTextPrenom;
    private String spinnerVille, checkBoxSexe;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button ajouter;
    private Button afficher;
    private ProgressDialog progressDialog;
    private static final String url = "https://convincing-sciences.000webhostapp.com/uploadimage.php";

    //tous ce que concerne l'image
    private Button choisir;
    private ImageView photoDeProfil;
    private String encodeImage;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_etudiant);
        getSupportActionBar().hide();

        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("attendez...");

        radioGroup = findViewById(R.id.radioGroup);
        ajouter = findViewById(R.id.buttonAjouter);
        afficher = findViewById(R.id.buttonAfficher);

        choisir = findViewById(R.id.buttonUpload);
        photoDeProfil = findViewById(R.id.imageViewMain);


        choisir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Ajouter_Etudiant.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Choisir " +
                                        "Image"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        afficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity currentActivity = (Activity) view.getContext();
                Intent i = new Intent(currentActivity, List_etudiant.class);
                currentActivity.startActivity(i);
            }
        });

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                if (radioId == -1) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champs sexe!",
                            Toast.LENGTH_LONG).show();
                } else {
                    radioButton = findViewById(radioId);
                    checkBoxSexe = radioButton.getText().toString();
                    String nom = editTextNom.getText().toString();
                    String prenom = editTextPrenom.getText().toString();

                    if (nom.isEmpty() || prenom.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        insertData(nom, prenom, spinnerVille, checkBoxSexe);
                        closeKeyboard();

                    }
                }


            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinnerVille);
        spinner.setOnItemSelectedListener(this);
        List<String> villes = new ArrayList<String>();
        villes.add("Casablanca");
        villes.add("Rabat");
        villes.add("Marrakech");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, villes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerVille = adapterView.getItemAtPosition(i).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void insertData(String nom, String prenom, String ville,
                           String sexe
    ) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                editTextNom.setText("");
                editTextPrenom.setText("");
                photoDeProfil.setImageResource(R.drawable.placeholder);
                Toast.makeText(Ajouter_Etudiant.this, "Bien Ajouté",
                        Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),
                        Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("nom", nom);
                param.put("prenom", prenom);
                param.put("ville", ville);
                param.put("sexe", sexe);
                param.put("upload", encodeImage);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                photoDeProfil.setImageBitmap(bitmap);
                imageStore(bitmap);
            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[] imageBytes = stream.toByteArray();

        encodeImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


}