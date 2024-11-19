package com.solares.calculadorasolar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class AreaActivity extends AppCompatActivity {

    public static float porcent = 3f;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        context = getApplicationContext();

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        TextView textTituloArea = findViewById(R.id.text_titulo_area);
        AutoSizeText.AutoSizeTextView(textTituloArea, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        Button buttonRecalcArea = findViewById(R.id.button_recalcular_area);
        AutoSizeText.AutoSizeButton(buttonRecalcArea, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        TextView textAreaAtual = findViewById(R.id.text_area_atual);
        AutoSizeText.AutoSizeTextView(textAreaAtual, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textNovaArea = findViewById(R.id.text_nova_area);
        AutoSizeText.AutoSizeTextView(textNovaArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        final EditText editArea = findViewById(R.id.editText_area);
        AutoSizeText.AutoSizeEditText(editArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textUnidadeArea = findViewById(R.id.text_unidade);
        AutoSizeText.AutoSizeTextView(textUnidadeArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        ConstraintLayout layout = findViewById(R.id.layout_area);

        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

        textAreaAtual.setText(String.format(Locale.ITALY, "Área Atual: %.2f m²", calculadora.pegaArea()));


        //Tutorial sobre as informações
        findViewById(R.id.inst_button_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExplicacaoInfos.ShowPopUpInfo(AreaActivity.this, findViewById(R.id.blackener), "Dúvidas",
                        "Selecione uma nova área, em m2, de telhado ou de solo sem sombreamento conforme o espaço que deseja que o sistema ocupe. Pode inserir uma área menor que a recomendada caso não tenha esse espaço disponível ou queira um sistema menor. Ou também pode aumentar a área ocupada pelo sistema e ver os impactos nos aspectos financeiros.");
            }
        });

        buttonRecalcArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 try {
                     //Pega a área digitada no edit text
                     final float AreaAlvo = Float.parseFloat(editArea.getText().toString());

                     //Se a área for menor ou igual a zero, pede pro usuário inserir novamente
                     if (AreaAlvo <= 0f) {
                         try {
                             Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                         } catch (Exception etoast1){
                             etoast1.printStackTrace();
                         }
                     } else {
                         //Refaz o cálculo com a nova área e inicia a ResultadoActivity
                         //Criar uma thread para fazer o cálculo pois é um processamento demorado
                         Thread thread = new Thread(){
                             public void run(){
                                 calculadora.setAreaAlvo(AreaAlvo);
                                 calculadora.Calcular(AreaActivity.this);
                             }
                         };
                         thread.start();
                     }
                 } catch (Exception e){
                     try {
                         Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                     } catch (Exception etoast2){
                         etoast2.printStackTrace();
                     }
                     e.printStackTrace();
                 }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(editArea);
            }
        });

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("AreaActivity", "Error while hiding keyboard");
        }
    }
}
