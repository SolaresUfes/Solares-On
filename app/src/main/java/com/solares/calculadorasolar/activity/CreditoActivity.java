package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;
import static com.solares.calculadorasolar.activity.MainActivity.PtarifaPassada;

public class CreditoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensionsAndSetTariff(this, PtarifaPassada);

        Button buttonInstagram = findViewById(R.id.button_instagram);
        AutoSizeText.AutoSizeButton(buttonInstagram, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        Button buttonRecalcular = findViewById(R.id.button_recalcular);
        AutoSizeText.AutoSizeButton(buttonRecalcular, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        TextView textConheca = findViewById(R.id.text_conheca);
        AutoSizeText.AutoSizeTextView(textConheca, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        ImageView imageInstagram = findViewById(R.id.imageViewInstagram);

        //Se o usuário clicar no ícone do instagram ou no botão com o @, ele é redirecionado para o instagram do solares
        imageInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/projeto.solares/"));
                startActivity(intent);
            }
        });
        buttonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/projeto.solares/"));
                startActivity(intent);
            }
        });

        //Se o usuário clicar no botão recalcular, limpa-se todas as activities e volta à activity MainActivity
        buttonRecalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalizarActivity();
            }
        });
    }

    public void FinalizarActivity(){
        //Limpa tarifa inserida
        MainActivity.PtarifaPassada = 0.0;

        Intent intent = new Intent(this, MainActivity.class);
        //Isso limpa as activities já abertas
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
