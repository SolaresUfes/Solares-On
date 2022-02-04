package com.solares.calculadorasolar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;
import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowHint;
import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowPopUpInfo;

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
        textPotencia.setText(String.format(Locale.ITALY,"%.2f kWp", calculadora.pegaPotenciaNecessaria()/1000));
        AutoSizeText.AutoSizeTextView(textPotencia, MainActivity.alturaTela, MainActivity.larguraTela, percent);

        TextView textEstaticoPlaca = findViewById(R.id.text_placa_1);
        AutoSizeText.AutoSizeTextView(textEstaticoPlaca, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textPlaca = findViewById(R.id.text_placa);
        AutoSizeText.AutoSizeTextView(textPlaca, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        String singplur;
        if(calculadora.pegaPlacaEscolhida().getQtd() > 1){
            singplur = "Placas";
        } else {
            singplur = "Placa";
        }
        textPlaca.setText(String.format(Locale.ITALY, "%d %s de %.0f Wp",
                calculadora.pegaPlacaEscolhida().getQtd(), singplur, calculadora.pegaPlacaEscolhida().getPotencia()));

        TextView textEstaticoArea = findViewById(R.id.text_area_1);
        AutoSizeText.AutoSizeTextView(textEstaticoArea, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textArea = findViewById(R.id.text_area);
        textArea.setText(String.format(Locale.ITALY, "%.2f m²", calculadora.pegaArea()));
        AutoSizeText.AutoSizeTextView(textArea, MainActivity.alturaTela, MainActivity.larguraTela, percent);

        TextView textEstaticoInversor = findViewById(R.id.text_inversor_1);
        AutoSizeText.AutoSizeTextView(textEstaticoInversor, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        TextView textInversor = findViewById(R.id.text_inversor);
        AutoSizeText.AutoSizeTextView(textInversor, MainActivity.alturaTela, MainActivity.larguraTela, percent);
        if(calculadora.pegaInversor().getQtd() > 1){
            singplur = "Inversores";
        } else {
            singplur = "Inversor";
        }
        textInversor.setText(String.format(Locale.ITALY, "%d %s de %.2f kW",
                calculadora.pegaInversor().getQtd(), singplur, calculadora.pegaInversor().getPotencia()/1000));


        //Definição do layout para escurecer a tela
        blackener = findViewById(R.id.blackener);

        //Botões para alterar os módulos e inversores e a área
        Button buttonAlterarArea = findViewById(R.id.button_modificar_area);
        AutoSizeText.AutoSizeButton(buttonAlterarArea, MainActivity.alturaTela, MainActivity.larguraTela, percent - 0.5f);

        Button buttonInvertors = findViewById(R.id.button_trocar_inversor);

        Button buttonModules = findViewById(R.id.button_trocar_modulo);

        buttonAlterarArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirActivityArea(calculadora);
            }
        });
        buttonInvertors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpInversores();
            }
        });
        buttonModules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpModulos();
            }
        });

        //Tutorial sobre as informações extras
        findViewById(R.id.inst_button_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHint(blackener, findViewById(R.id.inst_image_info));
            }
        });


        //Clicar nas informações para explicação
        textPotencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpInfo(InstalacaoActivity.this, blackener, getString(R.string.potencia_necessaria),
                        "Capacidade de Potência ideal que o sistema fotovoltaico deve possuir para suprir o consumo energético informado. O sistema foi dimensionado almejando esse valor de potência.");
            }
        });
        textPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpInfo(InstalacaoActivity.this, blackener, getString(R.string.paineis),
                        "De acordo com a demanda solicitada, este arranjo de placas fotovoltaicas traz o maior retorno financeiro dentre as possíveis opções no nosso banco de dados.");
            }
        });
        textArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpInfo(InstalacaoActivity.this, blackener, getString(R.string.area_necessaria),
                        "A área de telhado ou de solo sem sombreamento em m² necessária para instalar as placas fotovoltaicas apresentadas. No hemisfério sul, é interessante que essa área tenha uma leve inclinação em direção ao norte, para o sistema ter maior eficiência.");
            }
        });
        textInversor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpInfo(InstalacaoActivity.this, blackener, getString(R.string.inversores),
                        "O arranjo de inversores escolhido para suportar o sistema fotovoltaico proposto. A potência total dos inversores pode ser até 20% menor do que a potência nominal do arranjo das placas fotovoltaicas.");
            }
        });






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

    public void AbrirActivityArea(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, AreaActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void ShowPopUpModulos(){
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
        tituloPopup.setText(R.string.titulo_escolher_modulo);

        //Criação do Spinner
        AppCompatSpinner spinnerModulos = rootView.findViewById(R.id.spinner_modulos_inversores);

        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<String> adapterS =new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, calculadora.pegaNomesPaineis());
        spinnerModulos.setAdapter(adapterS);
        spinnerModulos.setSelection(calculadora.pegaListaPaineis().indexOf(calculadora.pegaPlacaEscolhida()));

        //Criação do botão
        Button buttonRecalc = rootView.findViewById(R.id.button_recalc_modulos_inversores);
        AutoSizeText.AutoSizeButton(buttonRecalc, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        buttonRecalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acha o spinner
                AppCompatSpinner spinnerModulos = rootView.findViewById(R.id.spinner_modulos_inversores);
                calculadora.setIdModuloEscolhido(spinnerModulos.getSelectedItemPosition());
                //Refaz o cálculo
                calculadora.Calcular(InstalacaoActivity.this);
            }
        });
    }

    public void ShowPopUpInversores() {
        blackener.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popup_modulos_inversores, blackener, false);

        PopupWindow pw;
        try {
            pw = new PopupWindow(rootView, (int) (MainActivity.larguraTela * 0.7), (MainActivity.alturaTela), true);
            pw.setAnimationStyle(16973827); //R.style.Animation_Translucent -> Não sei porque tive que botar a constante diretamente e não usando o nome dela
            pw.showAtLocation(blackener, Gravity.END, 0, 0);

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    blackener.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mudar texto do Título
        TextView tituloPopup = rootView.findViewById(R.id.titulo_escolher_modulo_inversor);
        AutoSizeText.AutoSizeTextView(tituloPopup, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        tituloPopup.setText(R.string.titulo_escolher_inversor);

        //Criação do Spinner
        AppCompatSpinner spinnerInversores = rootView.findViewById(R.id.spinner_modulos_inversores);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, calculadora.pegaNomesInversores());
        spinnerInversores.setAdapter(adapterS);
        spinnerInversores.setSelection(calculadora.pegaListaInversores().indexOf(calculadora.pegaInversor()));

        //Criação do botão
        Button buttonRecalc = rootView.findViewById(R.id.button_recalc_modulos_inversores);
        AutoSizeText.AutoSizeButton(buttonRecalc, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        buttonRecalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acha o spinner
                AppCompatSpinner spinnerInversores = rootView.findViewById(R.id.spinner_modulos_inversores);
                calculadora.setIdInversorEscolhido(spinnerInversores.getSelectedItemPosition());
                //Refaz o cálculo
                calculadora.Calcular(InstalacaoActivity.this);
            }
        });
    }
}
