package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.FirebaseManager;
import com.solares.calculadorasolar.classes.entidades.Empresa;
import com.solares.calculadorasolar.classes.entidades.Inversor;

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
        listaEmpresas = calculadora.pegaListaEmpresa();
        //listaEmpresas = FirebaseManager.fbBuscaListaEmpresasPorEstado(EmpresasActivity.this, calculadora.pegaVetorEstado()[Constants.iEST_SIGLA]);

        // Indicar que irá ter um tempo de espara para carregar as empresas
        Toast.makeText(EmpresasActivity.this, "Aguarde um momento!", Toast.LENGTH_SHORT).show();

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
            AdicionarEmpresa(listaEmpresas);
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

    public void AdicionarEmpresa(LinkedList<Empresa> empresas){
        System.out.println("Empresas: "+ empresas.size());
        if(empresas.size()==0){
            textFacaOrcamento.setText("Não há empresas parceiras");
            Toast.makeText(EmpresasActivity.this, "Não há empresas parceiras próximas", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Empresa empresa: empresas){
            addView(empresa);
        }
    }

    public void addView(Empresa empresa){
        final View EquipamentosView = getLayoutInflater().inflate(R.layout.linha_mostrar_empresas, null, false);
        final TextView nomeEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewNome);
        final TextView siteEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewSite);
        final TextView telefoneEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewTelefone);

        nomeEmpresa.setText(empresa.getNome());
        siteEmpresa.setText(empresa.getSite());
        telefoneEmpresa.setText(empresa.getTelefone());

        AutoSizeText.AutoSizeTextView(nomeEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        AutoSizeText.AutoSizeTextView(siteEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        AutoSizeText.AutoSizeTextView(telefoneEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        linearLayout.addView(EquipamentosView);
    }

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