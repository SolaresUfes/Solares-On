package com.solares.calculadorasolar.activity;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;
import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowHint;
import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowPopUpInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintSet;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import java.util.Locale;

public class BateriaOffGridActivity extends AppCompatActivity {

    float porcent = 3f;

    private LinearLayout blackener;
    private View rootView;

    private CalculadoraOffGrid calculadora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateria_off_grid);

        GetPhoneDimensions(this);

        TextView textBaterias = findViewById(R.id.text_titulo_bateria);
        AutoSizeText.AutoSizeTextView(textBaterias, MainActivity.alturaTela, MainActivity.larguraTela, 3.6f);

        TextView textSerieEstatic = findViewById(R.id.text_qtd_serie);
        AutoSizeText.AutoSizeTextView(textSerieEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textParaleloEstatic = findViewById(R.id.text_qtd_paralelo);
        AutoSizeText.AutoSizeTextView(textParaleloEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textQtdTotalEstatic = findViewById(R.id.text_quantidade_baterias);
        AutoSizeText.AutoSizeTextView(textQtdTotalEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textPrecoEstatic = findViewById(R.id.text_preco_baterias);
        AutoSizeText.AutoSizeTextView(textPrecoEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView text1BatEstatic = findViewById(R.id.text_quantidade_baterias_qtd1);
        AutoSizeText.AutoSizeTextView(text1BatEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textPreco1BatEstatic = findViewById(R.id.text_preco_baterias_qtd1);
        AutoSizeText.AutoSizeTextView(textPreco1BatEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView valorSeie = findViewById(R.id.valor_qtd_serie);
        AutoSizeText.AutoSizeTextView(valorSeie, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView valorParalelo = findViewById(R.id.valor_qtd_paralelo);
        AutoSizeText.AutoSizeTextView(valorParalelo, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView valorQdtTotal = findViewById(R.id.valor_quantidade_baterias);
        AutoSizeText.AutoSizeTextView(valorQdtTotal, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView valorPreco = findViewById(R.id.valor_preco_baterias);
        AutoSizeText.AutoSizeTextView(valorPreco, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView valor1BatTotal = findViewById(R.id.valor_quantidade_baterias_qtd1);
        AutoSizeText.AutoSizeTextView(valor1BatTotal, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView valorPreco1Bat = findViewById(R.id.valor_preco_baterias_qtd1);
        AutoSizeText.AutoSizeTextView(valorPreco1Bat, MainActivity.alturaTela, MainActivity.larguraTela, porcent);


        try {
            //Pegar informações da última activity (informações que serão exibidas)
            Intent intent = getIntent();
            calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

            //Inicializando os nomes no array
            calculadora.iniciarNomesDasBaterias();

            if (calculadora.pegaBateriaEscolhidaOffGrid().getQtd()!=1){
                try{
                    valorSeie.setText(String.format(Locale.ITALY, "%d em série",
                            calculadora.pegaBateriaEscolhidaOffGrid().getnSerie()));

                    valorParalelo.setText(String.format(Locale.ITALY, "%d em paralelo",
                            calculadora.pegaBateriaEscolhidaOffGrid().getnParalel()));

                    valorQdtTotal.setText(String.format(Locale.ITALY, "%d de %.0f A",
                            calculadora.pegaBateriaEscolhidaOffGrid().getQtd(),calculadora.pegaBateriaEscolhidaOffGrid().getC20()));

                    valorPreco.setText(String.format(Locale.ITALY, "R$ %.2f",
                            calculadora.pegaBateriaEscolhidaOffGrid().getPrecoTotal()));

                    //Definição do layout para escurecer a tela
                    blackener = findViewById(R.id.blackener);

                    //Botões para alterar a bateria
                    Button buttonBateria = findViewById(R.id.button_trocar_bateria);
                    buttonBateria.setVisibility(View.VISIBLE);

                    buttonBateria.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowPopUpBateria();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                try{
                    textSerieEstatic.setVisibility(View.GONE);
                    textParaleloEstatic.setVisibility(View.GONE);
                    textQtdTotalEstatic.setVisibility(View.GONE);
                    textPrecoEstatic.setVisibility(View.GONE);
                    valorSeie.setVisibility(View.GONE);
                    valorParalelo.setVisibility(View.GONE);
                    valorQdtTotal.setVisibility(View.GONE);
                    valorPreco.setVisibility(View.GONE);


                    text1BatEstatic.setVisibility(View.VISIBLE);
                    textPreco1BatEstatic.setVisibility(View.VISIBLE);
                    valor1BatTotal.setVisibility(View.VISIBLE);
                    valorPreco1Bat.setVisibility(View.VISIBLE);

                    valor1BatTotal.setText(String.format(Locale.ITALY, "%d de %.0f A",
                            calculadora.pegaBateriaEscolhidaOffGrid().getQtd(),calculadora.pegaBateriaEscolhidaOffGrid().getC20()));

                    valorPreco1Bat.setText(String.format(Locale.ITALY, "R$ %.2f",
                            calculadora.pegaBateriaEscolhidaOffGrid().getPrecoTotal()));

                    //Definição do layout para escurecer a tela
                    blackener = findViewById(R.id.blackener);

                    //Botões para alterar a bateria
                    Button buttonBateria = findViewById(R.id.button_trocar_bateria_1qtd);
                    buttonBateria.setVisibility(View.VISIBLE);

                    buttonBateria.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowPopUpBateria();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            //Botão voltar
            Button buttonVoltar = findViewById(R.id.button_voltar);
            AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            buttonVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            //Tutorial sobre as informações extras
            findViewById(R.id.B_inst_button_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPopUpInfo(BateriaOffGridActivity.this, findViewById(R.id.blackener), "Bateria",
                            "A bateria é responsável por todo o armazenamento das cargas elétricas que serão posteriormente consumidas nos períodos noturnos ou em dias cujo sol esteja encoberto.");

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ShowPopUpBateria() {
        blackener.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popup_modulos_inversores, blackener , false);
        PopupWindow pw;
        try{
            pw = new PopupWindow(rootView,(int)(MainActivity.larguraTela*0.7),(MainActivity.alturaTela), true);
            pw.setAnimationStyle(16973827); //R.style.Animation_Translucent -> Não sei porque tive que botar a constante diretamente e não usando o nome dela
            pw.showAtLocation(blackener, Gravity.END, 0, 0);

            pw.setOnDismissListener(new PopupWindow.OnDismissListener(){
                @Override
                public void onDismiss() {
                    blackener.setVisibility(View.GONE);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        //Mudar texto do Título
        TextView tituloPopup = rootView.findViewById(R.id.titulo_escolher_modulo_inversor);
        AutoSizeText.AutoSizeTextView(tituloPopup, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        tituloPopup.setText(R.string.titulo_escolher_bateria);

        //Criação do Spinner
        AppCompatSpinner spinnerBateria = rootView.findViewById(R.id.spinner_modulos_inversores);

        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<String> adapterS =new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, calculadora.pegaNomesBateriasOffGrid());
        spinnerBateria.setAdapter(adapterS);
        spinnerBateria.setSelection(calculadora.pegaListaBateriasOffGrid().indexOf(calculadora.pegaBateriaEscolhidaOffGrid()));

        //Criação do botão
        Button buttonRecalc = rootView.findViewById(R.id.button_recalc_modulos_inversores);
        AutoSizeText.AutoSizeButton(buttonRecalc, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        buttonRecalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acha o spinner
                AppCompatSpinner spinnerModulos = rootView.findViewById(R.id.spinner_modulos_inversores);
                calculadora.setIdBateriaEscolhido(spinnerModulos.getSelectedItemPosition());
                //Refaz o cálculo
                calculadora.Calcular(BateriaOffGridActivity.this);
            }
        });
    }
}