package com.solares.calculadorasolar.classes.auxiliares;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.activity.MainActivity;
import com.solares.calculadorasolar.classes.entidades.Inversor;
import com.solares.calculadorasolar.classes.entidades.Painel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class FirebaseManager {
    public static LinkedList<Painel> fbBuscaListaPaineis(Context context, SharedPreferences sharedPref){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Painel> paineis = new LinkedList<>();

        //Verify Connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected){
            Log.d("firebase", "Sem internet!");

            //Pega os paineis da shared prefs
            Gson gson = new Gson();
            String json = sharedPref.getString("paineis", "");
            Painel[] arrayPaineis = gson.fromJson(json, Painel[].class);

            paineis.clear();
            //Se não tiver nada salvo, pega do csv:
            if(arrayPaineis == null) {
                Log.d("firebase", "Pegando os paineis do CSV!");
                paineis.addAll(Objects.requireNonNull(CSVManager.getPaineisCSV(context)));
            } else {
                paineis.addAll(Arrays.asList(arrayPaineis));
            }
        }


        // Read the companies from the city
        DatabaseReference dbReference = database.getReference("paineis");
        //Cria um listener que vai chamar o onDataChange sempre que os dados mudarem no banco de dados e quando ele for criado
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paineis.clear();
                //Pega os paineis e os coloca em uma lista
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Painel painel = snapshot.getValue(Painel.class);
                    if(painel != null && !painel.getCodigo().equals("")){
                        Log.d("firebase", "Painel: " + painel.getCodigo());
                        paineis.add(painel);
                    }
                }

                //Salva os inversores no banco de dados na shared pref
                SharedPreferences.Editor prefsEditor = sharedPref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(paineis.toArray());
                prefsEditor.putString("paineis", json);
                prefsEditor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });

        return paineis;
    }

    public static LinkedList<Inversor> fbBuscaListaInversores(Context context, SharedPreferences sharedPref){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Inversor> inversores = new LinkedList<>();

        //Verify Connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected){
            Log.d("firebase", "Sem internet!");

            //Pega os paineis da shared prefs
            Gson gson = new Gson();
            String json = sharedPref.getString("inversores", "");
            Inversor[] arrayInversores = gson.fromJson(json, Inversor[].class);

            inversores.clear();
            //Se não tiver nada salvo, pega do csv:
            if(arrayInversores == null){
                Log.d("firebase", "Pegando os inversores do CSV!");
                inversores.addAll(Objects.requireNonNull(CSVManager.getInversoresCSV(context)));
            } else {
                inversores.addAll(Arrays.asList(arrayInversores));
            }
        }


        // Read the companies from the city
        DatabaseReference dbReference = database.getReference("inversores");
        //Cria um listener que vai chamar o onDataChange sempre que os dados mudarem no banco de dados e quando ele for criado
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inversores.clear();
                //Pega os paineis e os coloca em uma lista
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Inversor inversor = snapshot.getValue(Inversor.class);
                    if(inversor != null && !inversor.getMarca().equals("")){
                        Log.d("firebase", "Inversor: " + inversor.getMarca() + inversor.getPotencia());
                        inversores.add(inversor);
                    }
                }

                //Salva os inversores no banco de dados na shared pref
                SharedPreferences.Editor prefsEditor = sharedPref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(inversores.toArray());
                prefsEditor.putString("inversores", json);
                prefsEditor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });

        return inversores;
    }
}
