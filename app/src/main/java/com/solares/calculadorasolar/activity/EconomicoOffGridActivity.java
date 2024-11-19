
package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import java.util.Locale;

public class EconomicoOffGridActivity extends AppCompatActivity {

    float porcent = 2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economico_off_grid);

        //Pegar informações da última activity (informações que serão exibidas)
        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

        calculadora.iniciarNomesDosModulos();

        TextView textTituloEquipamentos = findViewById(R.id.text_titulo_equipamentos);
        AutoSizeText.AutoSizeTextView(textTituloEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        TextView textPrecoEquip = findViewById(R.id.text_preco_equipamento);
        AutoSizeText.AutoSizeTextView(textPrecoEquip, MainActivity.alturaTela, MainActivity.larguraTela, 3f);


        TextView textNomePlaca = findViewById(R.id.text_nome_placa);
        AutoSizeText.AutoSizeTextView(textNomePlaca, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textNomeControlador = findViewById(R.id.text_nome_controlador);
        AutoSizeText.AutoSizeTextView(textNomeControlador, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textNomeInversor = findViewById(R.id.text_nome_inversor);
        AutoSizeText.AutoSizeTextView(textNomeInversor, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textNomeBateria = findViewById(R.id.text_nome_bateria);
        AutoSizeText.AutoSizeTextView(textNomeBateria, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textPrecoPlaca = findViewById(R.id.text_preco_placa);
        AutoSizeText.AutoSizeTextView(textPrecoPlaca, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textPrecoControlador = findViewById(R.id.text_preco_controlador);
        AutoSizeText.AutoSizeTextView(textPrecoControlador, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textPrecoInversor = findViewById(R.id.text_preco_inversor);
        AutoSizeText.AutoSizeTextView(textPrecoInversor, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textPrecoBateria = findViewById(R.id.text_preco_bateria);
        AutoSizeText.AutoSizeTextView(textPrecoBateria, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textCusto = findViewById(R.id.text_total_a_pagar);
        AutoSizeText.AutoSizeTextView(textCusto, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        TextView valorCusto = findViewById(R.id.valor_total_a_pagar);
        valorCusto.setText(String.format(Locale.ITALY,"R$ %.2f", calculadora.pegaCustoTotal()));
        AutoSizeText.AutoSizeTextView(valorCusto, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        TextView textEstaticoLucro = findViewById(R.id.text_lucro_1);
        AutoSizeText.AutoSizeTextView(textEstaticoLucro, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        TextView textLucro = findViewById(R.id.text_lucro);
        textLucro.setText(String.format(Locale.ITALY,"R$ %.2f", calculadora.pegaLucro()));
        AutoSizeText.AutoSizeTextView(textLucro, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        try {

            //Placa
            textNomePlaca.setText(String.format(Locale.ITALY, "%s",
                    calculadora.pegaPlacaEscolhidaOffGrid().getNome()));
            textPrecoPlaca.setText(String.format(Locale.ITALY, "R$ %.2f",
                    calculadora.pegaPlacaEscolhidaOffGrid().getCustoTotal()));

            //Controlador
            textNomeControlador.setText(String.format(Locale.ITALY, "%s",
                    calculadora.pegaControladorEscolhidaOffGrid().getNome()));
            textPrecoControlador.setText(String.format(Locale.ITALY, "R$ %.2f",
                    calculadora.pegaControladorEscolhidaOffGrid().getPrecoTotal()));

            //Inversor
            if(calculadora.pegaInversorEscolhidaOffGrid() != null) {
                textNomeInversor.setText(String.format(Locale.ITALY, "%s",
                        calculadora.pegaInversorEscolhidaOffGrid().getNome()));
                textPrecoInversor.setText(String.format(Locale.ITALY, "R$ %.2f",
                        calculadora.pegaInversorEscolhidaOffGrid().getPrecoTotal()));
            }

            //Controlador
            textNomeBateria.setText(String.format(Locale.ITALY, "%s",
                    calculadora.pegaBateriaEscolhidaOffGrid().getNome()));
            textPrecoBateria.setText(String.format(Locale.ITALY, "R$ %.2f",
                    calculadora.pegaBateriaEscolhidaOffGrid().getPrecoTotal()));

            //Botão voltar
            Button buttonVoltar = findViewById(R.id.button_voltar);
            AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
            buttonVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}