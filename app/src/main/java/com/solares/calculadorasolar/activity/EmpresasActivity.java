package com.solares.calculadorasolar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.entidades.Empresa;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.LinkedList;

public class EmpresasActivity extends AppCompatActivity {

    TextView textFacaOrcamento;
    private LinearLayout linearLayout;

    public static int larguraTela;
    public static int alturaTela;
    public float porcent = 4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        // Pegar as intent
        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

        LinkedList<Empresa> listaEmpresas = new LinkedList<>();
        //listaEmpresas = FirebaseManager.fbBuscaListaEmpresasPorEstado(EmpresasActivity.this, "ES");
        //Log.d("firebase", "Empresas Calculardoras: "+listaEmpresas.size());

        listaEmpresas = calculadora.pegaListaEmpresa();
        Log.d("firebase", "Empresas Calculardorwes: "+calculadora.pegaListaEmpresa().size());
        //listaEmpresas = FirebaseManager.fbBuscaListaEmpresasPorEstado(EmpresasActivity.this, calculadora.pegaVetorEstado()[Constants.iEST_SIGLA]);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        TextView textTituloEmpresas = findViewById(R.id.text_titulo_empresas);
        AutoSizeText.AutoSizeTextView(textTituloEmpresas, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        textFacaOrcamento = findViewById(R.id.text_faca_orcamento);
        AutoSizeText.AutoSizeTextView(textFacaOrcamento, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        Button buttonFinalizar = findViewById(R.id.button_finalizar);
        AutoSizeText.AutoSizeButton(buttonFinalizar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        linearLayout = findViewById(R.id.LinearLayoutMostrarEmpresas);

        try {
            AdicionarEmpresa(listaEmpresas, calculadora);
        }catch (Exception e){
            e.printStackTrace();
        }

        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalizarCalculo(calculadora);
            }
        });
    }

    public void AdicionarEmpresa(LinkedList<Empresa> empresas, CalculadoraOnGrid calculadora){
       // System.out.println("Empresas: "+ empresas.size());
       // if(empresas.size()==0){
       //     textFacaOrcamento.setText("Não há empresas parceiras");
        //    Toast.makeText(EmpresasActivity.this, "Não há empresas parceiras próximas", Toast.LENGTH_SHORT).show();
        //    return;
        //}
        //for (Empresa empresa: empresas){
        //    addView(empresa);
        //}

        // gambiarra
        GetEmpresasFirebase(calculadora, EmpresasActivity.this);
        //
    }

    public void addView(Empresa empresa){
        final View EquipamentosView = getLayoutInflater().inflate(R.layout.linha_mostrar_empresas, null, false);
        final TextView nomeEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewNome);
        final TextView siteEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewSite);
        final TextView telefoneEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewTelefone);
        final ImageView logoEmpresa = (ImageView)EquipamentosView.findViewById(R.id.ImageViewLogo);

        // gambiarra adicionada
        downloadLogo(empresa.nome, logoEmpresa);
        //

        nomeEmpresa.setText(empresa.getNome());
        siteEmpresa.setText(empresa.getSite());
        telefoneEmpresa.setText(empresa.getTelefone());

        AutoSizeText.AutoSizeTextView(nomeEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        AutoSizeText.AutoSizeTextView(siteEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        AutoSizeText.AutoSizeTextView(telefoneEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        linearLayout.addView(EquipamentosView);
    }

    //
    //
    // Isso é apenas uma gambiarra para adicionar as empresas
    //
    //

    public void downloadLogo(String nomeEmpresa, ImageView logoEmpresa){
        // Indicar que irá ter um tempo de espara para carregar as empresas
        Toast.makeText(EmpresasActivity.this, "Aguarde um momento!", Toast.LENGTH_SHORT).show();

        String pathLogo = "LogoEmpresas/".concat(nomeEmpresa).concat(".jpg");
        //Download file in Memory
        //StorageReference logo = storageReference.child(pathLogo);

        StorageReference logo = FirebaseStorage.getInstance().getReference(pathLogo);
        
        long MAXBYTES = 1024*1024;

        logo.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() { // Settar imagem baixada no ImageView
            @Override
            public void onSuccess(byte[] bytes) {
                // convert byte to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable image = new BitmapDrawable(getResources(), bitmap);
                //logoEmpresa.setImageBitmap(bitmap);
                logoEmpresa.setImageDrawable(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmpresasActivity.this, "Erro ao carregar logo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GetEmpresasFirebase(CalculadoraOnGrid calculadora, Context context){
        Log.d("firebase", "Firebase Database inicializado");
        // Get the reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Empresa> empresas = new LinkedList<>();

        //Verify Connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected){
            //Tell the user that this function only works with internet connection
            Log.d("firebase", "Sem internet!");
        }

        // Read the companies from the city
        String estado = calculadora.pegaVetorEstado()[Constants.iEST_SIGLA];
        DatabaseReference dbReference = database.getReference("estados").child(estado);
        dbReference.addValueEventListener(new ValueEventListener() {
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
                                addView(empresa);
                            }
                        }
                        else{
                            textFacaOrcamento.setText("Não há empresas parceiras em "+estado);
                            return;
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
    }
    //
    //
    //
    //
    //

    public static void GetPhoneDimensions(Activity activity){
        //Pegar dimensões
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        larguraTela = size.x;
        alturaTela = size.y;
    }

    public void FinalizarCalculo(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, CreditoActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }
}