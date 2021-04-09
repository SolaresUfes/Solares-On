package com.solares.calculadorasolar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class InstalacaoActivity extends AppCompatActivity {

    public float percent = 3f;

    private LinearLayout blackener;
    private View rootView;

    private CalculadoraOnGrid calculadora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalacao);

        Intent intent = getIntent();
        calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);


        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        TextView textTituloInstal = findViewById(R.id.text_titulo_intalacao);
        AutoSizeText.AutoSizeTextView(textTituloInstal, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        TextView textEstaticoPotencia = findViewById(R.id.text_potencia_1);
        AutoSizeText.AutoSizeTextView(textEstaticoPotencia, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textPotencia = findViewById(R.id.text_potencia);
        textPotencia.setText(String.format(Locale.ITALY,"%.2f Wp", calculadora.pegaPotenciaNecessaria()));
        AutoSizeText.AutoSizeTextView(textPotencia, MainActivity.alturaTela, MainActivity.larguraTela, percent);

        TextView textEstaticoPlaca = findViewById(R.id.text_placa_1);
        AutoSizeText.AutoSizeTextView(textEstaticoPlaca, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textPlaca = findViewById(R.id.text_placa);
        AutoSizeText.AutoSizeTextView(textPlaca, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        String singplur;
        if(Integer.parseInt(calculadora.pegaPlacaEscolhida()[Constants.iPANEL_QTD]) > 1){
            singplur = "Placas";
        } else {
            singplur = "Placa";
        }
        textPlaca.setText(String.format(Locale.ITALY, "%d %s de %.0f W",
                Integer.parseInt(calculadora.pegaPlacaEscolhida()[Constants.iPANEL_QTD]), singplur, Double.parseDouble(calculadora.pegaPlacaEscolhida()[Constants.iPANEL_POTENCIA])));

        TextView textEstaticoArea = findViewById(R.id.text_area_1);
        AutoSizeText.AutoSizeTextView(textEstaticoArea, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textArea = findViewById(R.id.text_area);
        textArea.setText(String.format(Locale.ITALY, "%.2f m²", calculadora.pegaArea()));
        AutoSizeText.AutoSizeTextView(textArea, MainActivity.alturaTela, MainActivity.larguraTela, percent);

        TextView textEstaticoInversor = findViewById(R.id.text_inversor_1);
        AutoSizeText.AutoSizeTextView(textEstaticoInversor, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textInversor = findViewById(R.id.text_inversor);
        AutoSizeText.AutoSizeTextView(textInversor, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        if(Integer.parseInt(calculadora.pegaInversor()[Constants.iINV_QTD]) > 1){
            singplur = "Inversores";
        } else {
            singplur = "Inversor";
        }
        textInversor.setText(String.format(Locale.ITALY, "%d %s de %.0f W",
                Integer.parseInt(calculadora.pegaInversor()[Constants.iINV_QTD]), singplur, Double.parseDouble(calculadora.pegaInversor()[Constants.iINV_POTENCIA])));

        //Definição do layout para escurecer a tela
        blackener = findViewById(R.id.blackener);


        //Botão voltar
        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void ShowPopUpModulos(View view){
        blackener.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popup_modulos, null, false);

        PopupWindow pw = new PopupWindow(rootView,(int)(MainActivity.larguraTela*0.7),(int)(MainActivity.alturaTela), true);
        pw.setAnimationStyle(16973827); //R.style.Animation_Translucent -> Não sei porque tive que botar a constante diretamente e não usando o nome dela
        pw.showAtLocation(view, Gravity.END, 0, 0);

        pw.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                blackener.setVisibility(View.GONE);
            }
        });

        //Mudar texto do Título
        TextView tituloPopup = rootView.findViewById(R.id.titulo_escolher_modulo_inversor);
        AutoSizeText.AutoSizeTextView(tituloPopup, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        tituloPopup.setText(R.string.titulo_escolher_modulo);

        //Criação do Spinner
        Spinner spineerModulos = rootView.findViewById(R.id.spinner_modulos_inversores);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.Modulos, R.layout.spinner_item);
        spineerModulos.setAdapter(adapterS);
        spineerModulos.setSelection(Integer.parseInt(calculadora.pegaPlacaEscolhida()[Constants.iPANEL_I]));

        //Criação do botão
        Button buttonRecalc = rootView.findViewById(R.id.button_recalc_modulos_inversores);
        AutoSizeText.AutoSizeButton(buttonRecalc, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        buttonRecalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acha o spinner
                Spinner spinnerModulos = rootView.findViewById(R.id.spinner_modulos_inversores);
                calculadora.setIdModuloEscolhido(spinnerModulos.getSelectedItemPosition());
                //Refaz o cálculo
                calculadora.Calcular(InstalacaoActivity.this);
            }
        });
    }

    public void ShowPopUpInversores(View view){
        blackener.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popup_modulos, null, false);

        PopupWindow pw = new PopupWindow(rootView,(int)(MainActivity.larguraTela*0.7),(int)(MainActivity.alturaTela), true);
        pw.setAnimationStyle(16973827); //R.style.Animation_Translucent -> Não sei porque tive que botar a constante diretamente e não usando o nome dela
        pw.showAtLocation(view, Gravity.END, 0, 0);

        pw.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                blackener.setVisibility(View.GONE);
            }
        });

        //Mudar texto do Título
        TextView tituloPopup = rootView.findViewById(R.id.titulo_escolher_modulo_inversor);
        AutoSizeText.AutoSizeTextView(tituloPopup, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        tituloPopup.setText(R.string.titulo_escolher_inversor);

        //Criação do Spinner
        Spinner spineerModulos = rootView.findViewById(R.id.spinner_modulos_inversores);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.Inversores, R.layout.spinner_item);
        spineerModulos.setAdapter(adapterS);
        spineerModulos.setSelection(Integer.parseInt(calculadora.pegaInversor()[Constants.iINV_ID]));

        //Criação do botão
        Button buttonRecalc = rootView.findViewById(R.id.button_recalc_modulos_inversores);
        AutoSizeText.AutoSizeButton(buttonRecalc, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        buttonRecalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acha o spinner
                Spinner spinnerInversores = rootView.findViewById(R.id.spinner_modulos_inversores);
                calculadora.setIdInversorEscolhido(spinnerInversores.getSelectedItemPosition());
                //Refaz o cálculo
                calculadora.Calcular(InstalacaoActivity.this);
            }
        });
    }
}
