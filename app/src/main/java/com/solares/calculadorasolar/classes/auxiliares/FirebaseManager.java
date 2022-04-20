package com.solares.calculadorasolar.classes.auxiliares;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.entidades.Empresa;
import com.solares.calculadorasolar.classes.entidades.Inversor;
import com.solares.calculadorasolar.classes.entidades.Painel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

            pegaPaineisOffline(context, sharedPref, paineis);
        }


        // Read the companies from the city
        DatabaseReference dbReference = database.getReference("paineis");
        //Cria um listener que vai chamar o onDataChange sempre que os dados mudarem no banco de dados e quando ele for criado
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paineis.clear();
                //Pega os paineis e os coloca em uma lista
                Log.d("firebase", "Buscando painéis no firebase...");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Painel painel = snapshot.getValue(Painel.class);
                    if(painel != null && !painel.getCodigo().equals("")){
                        Log.d("firebase", "Painel: " + painel.getCodigo());
                        paineis.add(painel);
                    }
                }

                //Salva os inversores no banco de dados na shared pref
                if(paineis.size() > 0){
                    SharedPreferences.Editor prefsEditor = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(paineis.toArray());
                    prefsEditor.putString("paineis", json);
                    prefsEditor.apply();
                }
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

            pegaInversoresOffline(context, sharedPref, inversores);
        }


        // Read the companies from the city
        DatabaseReference dbReference = database.getReference("inversores");
        //Cria um listener que vai chamar o onDataChange sempre que os dados mudarem no banco de dados e quando ele for criado
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inversores.clear();
                //Pega os paineis e os coloca em uma lista
                Log.d("firebase", "Buscando inversores no firebase...");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Inversor inversor = snapshot.getValue(Inversor.class);
                    if(inversor != null && !inversor.getMarca().equals("")){
                        Log.d("firebase", "Inversor: " + inversor.getMarca() + " " + inversor.getPotencia());
                        inversores.add(inversor);
                    }
                }

                //Salva os inversores no banco de dados na shared pref
                if(inversores.size() > 0) {
                    SharedPreferences.Editor prefsEditor = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(inversores.toArray());
                    prefsEditor.putString("inversores", json);
                    prefsEditor.apply();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });
        return inversores;
    }




    public static LinkedList<Empresa> EncontrarEmpresas(LinkedList<Empresa> empresas){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Read the companies from the city
        DatabaseReference dbReference = database.getReference("empresas");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Pega os paineis e os coloca em uma lista
                Log.d("firebase", "Buscando empresas no firebase...");
                //for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //String nome = snapshot.getValue(String.class);
                    //if(nome != null){
                    //    Log.d("firebase", "Empresa: " +nome);
                   // }
               // }
                for(Empresa empresa:empresas) {
                    Empresa empresaAtual = dataSnapshot.child(empresa.getNome()).getValue(Empresa.class);
                    if (empresaAtual != null) {
                        Log.d("firebase", "Telefone empresa: " + empresaAtual.getTelefone());
                        empresa.CopyFrom(empresaAtual);
                    }
                }
                Log.d("firebase", "Telefone empresa: " + empresas.get(0).getTelefone());
                Log.d("firebase", "NOme empresa: " + empresas.get(0).getNome());
                Log.d("firebase", "Site empresa: " + empresas.get(0).getSite());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });

        return empresas;
    }

    public static LinkedList<Empresa> fbBuscaListaEmpresasPorEstado(Context context, String sigla){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Empresa> empresas = new LinkedList<>();
        final LinkedList<Empresa>[] empresasFinal = new LinkedList[]{new LinkedList<>()};

        //Verify Connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected){
            Log.d("firebase", "Sem internet!");
        }
        // Read the companies from the state
        DatabaseReference dbReference = database.getReference("estados").child(sigla);
        //Cria um listener que vai chamar o onDataChange sempre que os dados mudarem no banco de dados e quando ele for criado
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empresas.clear();
                //Pega as empresas e as coloca em uma lista
                Log.d("firebase", "Buscando empresas por estado no firebase...");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Empresa empresa = snapshot.getValue(Empresa.class);
                    String nome = snapshot.getValue(String.class);
                    if(nome != null){
                        Log.d("firebase", "Empresas: " + nome);
                        empresas.add(new Empresa(nome));
                    }
                }
                System.out.println("Empresas size: "+ empresas.size());

                if(empresas.size()!=0){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });

        return empresas;
    }






    public static LinkedList<Empresa> fbBuscaTodasEmpresas(Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Empresa> empresas = new LinkedList<>();
        final LinkedList<Empresa>[] empresasFinal = new LinkedList[]{new LinkedList<>()};

        //Verify Connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected){
            Log.d("firebase", "Sem internet!");
        }
        // Read the companies from the state
        DatabaseReference dbReference = database.getReference("estados");
        //Cria um listener que vai chamar o onDataChange sempre que os dados mudarem no banco de dados e quando ele for criado
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("firebase", dataSnapshot.getValue().toString());
                /*empresas.clear();
                //Pega as empresas e as coloca em uma lista
                Log.d("firebase", "Buscando empresas por estado no firebase...");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Empresa empresa = snapshot.getValue(Empresa.class);
                    String nome = snapshot.getValue(String.class);
                    if(nome != null){
                        Log.d("firebase", "Empresas: " + nome);
                        empresas.add(new Empresa(nome));
                    }
                }
                System.out.println("Empresas size: "+ empresas.size());

                if(empresas.size()!=0){

                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });

        return empresas;
    }






    public static LinkedList<Empresa> GetEmpresasFirebase(CalculadoraOnGrid calculadora, Context context){
        Log.d("firebase", "Firebase Database inicializado");
        // Get the reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Empresa> empresas = new LinkedList<>();

        // Read the companies from the city
        DatabaseReference dbReferenceCidade = database.getReference("estados")
                .child(calculadora.pegaVetorEstado()[Constants.iEST_SIGLA]);
        dbReferenceCidade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                empresas.clear();

                //Get the names of the companies that work in the city
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String nome = snapshot.getValue(String.class);
                    if(nome != null){
                        empresas.add(new Empresa(nome));
                    }
                }

                ////////////////////////
                //Get the companies info
                DatabaseReference dbReferenceEmpresas = database.getReference("empresas");
                // Read from the empresas database
                dbReferenceEmpresas.addValueEventListener(new ValueEventListener() {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(Empresa empresa : empresas) {
                            Empresa empresaAtual = dataSnapshot.child(empresa.getNome()).getValue(Empresa.class);
                            if(empresaAtual != null){
                                Log.d("firebase", "Teste empresa" + empresaAtual.getTelefone());
                                empresa.CopyFrom(empresaAtual);
                            }
                        }

                        Log.d("firebase", "Empresas disponíveis em " + calculadora.pegaNomeCidade() + ", " + calculadora.pegaVetorEstado()[Constants.iEST_SIGLA]);
                        if(empresas.size() != 0){
                            for(Empresa empresa : empresas){
                                Log.d("firebase", empresa.getNome()+" - Telefone: " + empresa.getTelefone() + " Site: "+empresa.getSite());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.d("firebase", "Failed to read value." + error.toException());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });

        return empresas;
    }








    ////// Funções para busca offline //////////////////////////////////////

    public static void pegaPaineisOffline(Context context, SharedPreferences sharedPref, LinkedList<Painel> paineis){
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

    public static void pegaInversoresOffline(Context context, SharedPreferences sharedPref, LinkedList<Inversor> inversores){
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
}
