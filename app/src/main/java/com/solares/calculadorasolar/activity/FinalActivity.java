package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.fileWriter;

import java.util.Locale;

public class FinalActivity extends AppCompatActivity {

    public EditText editEmail;
    public EditText editNome;
    public static boolean foiInformadoEmail = true;


    public String NomeCidade;
    public double custoReais;
    public double consumokWh;
    public double potenciaNecessaria;
    public String[] placaEscolhida;
    public double area;
    public String[] inversor;
    public double custoParcial;
    public double custoTotal;
    public double geracaoAnual;
    public double lucro;
    public double taxaRetornoInvestimento;
    public double indiceLucratividade;
    public double LCOE;
    public int tempoRetorno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Button buttonConfirmar = findViewById(R.id.button_confirmar);
        ConstraintLayout layout = findViewById(R.id.layout_final);
        editEmail = findViewById(R.id.edit_email);
        editNome = findViewById(R.id.edit_name);


        Intent intentReceive = getIntent();
        NomeCidade = intentReceive.getStringExtra(Constants.EXTRA_CIDADE);
        custoReais = intentReceive.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
        consumokWh = intentReceive.getDoubleExtra(Constants.EXTRA_CONSUMO, 0.0);
        potenciaNecessaria = intentReceive.getDoubleExtra(Constants.EXTRA_POTENCIA, 0.0);
        placaEscolhida = intentReceive.getStringArrayExtra(Constants.EXTRA_PLACAS);
        area = intentReceive.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        inversor = intentReceive.getStringArrayExtra(Constants.EXTRA_INVERSORES);
        custoParcial = intentReceive.getDoubleExtra(Constants.EXTRA_CUSTO_PARCIAL, 0.0);
        custoTotal = intentReceive.getDoubleExtra(Constants.EXTRA_CUSTO_TOTAL, 0.0);
        geracaoAnual = intentReceive.getDoubleExtra(Constants.EXTRA_GERACAO, 0.0);
        lucro = intentReceive.getDoubleExtra(Constants.EXTRA_LUCRO, 0.0);
        taxaRetornoInvestimento = intentReceive.getDoubleExtra(Constants.EXTRA_TAXA_DE_RETORNO, 0.0);
        indiceLucratividade = intentReceive.getDoubleExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, 0.0);
        LCOE = intentReceive.getDoubleExtra(Constants.EXTRA_LCOE, 0.0);
        tempoRetorno = intentReceive.getIntExtra(Constants.EXTRA_TEMPO_RETORNO, 0);

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalvarDados();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(editNome);
                hideKeyboard(editEmail);
            }
        });

    }

    public void SalvarDados(){
        editNome.onEditorAction(EditorInfo.IME_ACTION_DONE);
        editEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Espaço de armazenamento não encontrado!", Toast.LENGTH_LONG).show();
        } else {
            String nome = editNome.getText().toString();
            String email = editEmail.getText().toString();

            if(nome.equals("")){
                nome = "Nome não informado";
            }
            if(email.equals("")){
                email = "E-mail não informado";
                foiInformadoEmail = false;
            } else {
                foiInformadoEmail = true;
            }

            String singPlur;
            if(inversor[Constants.iINV_QTD] == "1"){
                singPlur = "Inversor";
            } else {
                singPlur = "Inversores";
            }

            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "\n%s\n%s\n", nome, email));

            fileWriter.Save(MainActivity.file, "Resultado:\n");
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Cidade: %s\n", NomeCidade));
            fileWriter.Save(MainActivity.file, String.format(Locale.ITALY, "Consumo mensal: R$ %.2f\n", custoReais));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Consumo mensal: %.2f kWh\n", consumokWh));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Potência necessária: %.2f Wp\n", potenciaNecessaria));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH,
                    "%d painéis de %d W\n", Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]), Integer.parseInt(placaEscolhida[Constants.iPANEL_POTENCIA])));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Área Necessária: %.2f m²\n", area));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH,
                    "%d %s de %d W\n", Integer.parseInt(inversor[Constants.iINV_QTD]), singPlur, Integer.parseInt(inversor[Constants.iINV_POTENCIA])));
            fileWriter.Save(MainActivity.file, String.format(Locale.ITALY, "Custo parcial: R$ %.2f\n", custoParcial));
            fileWriter.Save(MainActivity.file, String.format(Locale.ITALY, "Custo total: R$ %.2f\n", custoTotal));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Geração anual: %.2f W\n", geracaoAnual));
            fileWriter.Save(MainActivity.file, String.format(Locale.ITALY, "Lucro (25 anos): R$ %.2f\n", lucro));
            fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Internal Rate of Return: %.2f%%\n", taxaRetornoInvestimento));
            fileWriter.Save(MainActivity.file, String.format(Locale.ITALY, "Índice de lucratividade: R$ %.2f\n", indiceLucratividade));
            fileWriter.Save(MainActivity.file, String.format(Locale.ITALY, "LCOE: R$ %.2f\n", LCOE));
            if(tempoRetorno == 30){
                fileWriter.Save(MainActivity.file, "Tempo para retorno do investimento: Nunca\n");
            } else {
                fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "Tempo para retorno do investimento: %d anos\n", tempoRetorno));
            }
            fileWriter.Save(MainActivity.file, "//////////////////\n\n");
        }

        Toast.makeText(this, "O e-mail será enviado assim que possível", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, CalculoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean isExternalStorageWritable () {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}
