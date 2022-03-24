package com.example.studentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List_etudiant extends AppCompatActivity {
    RecyclerView recyclerView;
    myAdapter myadapter;
    List<Etudiant> etudiantList;
    Etudiant etudiant;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);
        getSupportActionBar().hide();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewEtudiant);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        etudiantList = new ArrayList<>();
        myadapter = new myAdapter(etudiantList, this);
        recyclerView.setAdapter(myadapter);

        fetchImages();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final TextView id = viewHolder.itemView.findViewById(R.id.idOutput);
                int position = viewHolder.getAdapterPosition();
                int ide = Integer.parseInt((String) id.getText());
                deleteData(ide);
                Log.d("a", ide + "");
                recyclerView.setAdapter(myadapter);
                myadapter.notifyItemRemoved(ide);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void deleteData(int position) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://convincing-sciences.000webhostapp.com/delete.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(List_etudiant.this, "Bien Supprimé",
                                Toast.LENGTH_SHORT).show();
                        myadapter.notifyDataSetChanged();
                        etudiantList.remove(etudiantList.get(findPositionById(etudiantList, position)));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(List_etudiant.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id", String.valueOf(position));
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private int findPositionById(List<Etudiant> etudiantList, int position) {
        int identifiant = -1;
        for (Etudiant e : etudiantList) {
            if (e.getId().equalsIgnoreCase(String.valueOf(position))) {
                identifiant = etudiantList.indexOf(e);
            }
        }
        return identifiant;
    }


    private void fetchImages() {

        StringRequest request = new StringRequest(Request.Method.POST, "https://convincing-sciences.000webhostapp.com/fetchImages.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (succes.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String nom = object.getString("nom");
                                    String prenom = object.getString("prenom");
                                    String ville = object.getString("ville");
                                    String sexe = object.getString("sexe");
                                    String imageurl = object.getString("upload");

                                    String url = "https://convincing-sciences.000webhostapp.com/images/" + imageurl;

                                    Etudiant etudiant = new Etudiant(id, nom, prenom, ville, sexe
                                            , url);
                                    etudiantList.add(etudiant);
                                    myadapter.notifyDataSetChanged();

                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(List_etudiant.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}