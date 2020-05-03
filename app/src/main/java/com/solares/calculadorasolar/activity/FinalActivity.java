package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
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

        //Identificando os componentes do layout e ajustando seu tamanho
        TextView textSimulacao = findViewById(R.id.text_simulacao_final);
        AutoSizeText.AutoSizeTextView(textSimulacao, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        Button buttonConfirmar = findViewById(R.id.button_confirmar);
        AutoSizeText.AutoSizeButton(buttonConfirmar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        editEmail = findViewById(R.id.edit_email);
        AutoSizeText.AutoSizeEditText(editEmail, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        editNome = findViewById(R.id.edit_name);
        AutoSizeText.AutoSizeEditText(editNome, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        ConstraintLayout layout = findViewById(R.id.layout_final);


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

            //Verifica se o nome foi informado
            if(nome.equals("")){
                nome = "Nome não informado";
            }
            //Verifica se o email foi informado, se não foi, não salva nada
            if(email.equals("")){
                Toast.makeText(this, "O e-mail não foi informado!", Toast.LENGTH_LONG).show();
            } else {
                //Imprime os valores em uma linha do csv
                //a ordem é:
                //email, nome, nomeCidade, custoReais, consumokWh, potenciaNecessária, qtdPlacas, potenciaPlacas, area, qtdInversores, potenciaInversores,
                // custoTotal, geração anual, lucro25anos, taxaRetornoInvestimento, índiceLucratividade, tempo de retorno
                try {
                    fileWriter.Save(MainActivity.file, String.format(Locale.ENGLISH, "%s,%s,%s,%.2f,%.2f,%.2f,%s,%s,%.2f,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%d\n",
                            email, nome, NomeCidade, custoReais, consumokWh, potenciaNecessaria, placaEscolhida[Constants.iPANEL_QTD],
                            placaEscolhida[Constants.iPANEL_POTENCIA], area, inversor[Constants.iINV_QTD], inversor[Constants.iINV_POTENCIA],
                            custoTotal, geracaoAnual, lucro, taxaRetornoInvestimento, indiceLucratividade, tempoRetorno));
                    Toast.makeText(this, "O e-mail será enviado assim que possível", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Limpa tarifa inserida
        MainActivity.PtarifaPassada = 0.0;
        startActivity(intent);
    }

    public boolean isExternalStorageWritable () {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("MainActivity", "Error while hiding keyboard");
        }
    }



}