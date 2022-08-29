
package com.solares.calculadorasolar.activity;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

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

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import java.util.Locale;

public class EquipamentosOffGridActivity extends AppCompatActivity {

    float porcent = 3f;

    private LinearLayout blackener;
    private View rootView;

    private CalculadoraOffGrid calculadora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipamentos_off_grid);

        GetPhoneDimensions(this);

        TextView textEquipamentos = findViewById(R.id.text_titulo_lista_equipamentos);
        AutoSizeText.AutoSizeTextView(textEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 3.6f);

        TextView textPlacaEstatic = findViewById(R.id.text_placa);
        AutoSizeText.AutoSizeTextView(textPlacaEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textControladorEstatic = findViewById(R.id.text_controlador);
        AutoSizeText.AutoSizeTextView(textControladorEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textInversorEstatic = findViewById(R.id.text_inversor);
        AutoSizeText.AutoSizeTextView(textInversorEstatic, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textPlaca = findViewById(R.id.text_placa1);
        AutoSizeText.AutoSizeTextView(textPlaca, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textControlador = findViewById(R.id.text_controlador1);
        AutoSizeText.AutoSizeTextView(textControlador, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textInversor = findViewById(R.id.text_inversor1);
        AutoSizeText.AutoSizeTextView(textInversor, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        try{
            //Pegar informações da última activity (informações que serão exibidas)
            Intent intent = getIntent();
            calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

            //Inicializando os nomes no array
            calculadora.iniciarNomesDosControladores();
            calculadora.iniciarNomesDosInversores();


            textPlaca.setText(String.format(Locale.ITALY, "%d %s de %.0f Wp",
                    calculadora.pegaPlacaEscolhidaOffGrid().getQtd(), "Placa", calculadora.pegaPlacaEscolhidaOffGrid().getPotencia()));

            textControlador.setText(String.format(Locale.ITALY, "%d de I máx do sistema %.0f A",
                    calculadora.pegaControladorEscolhidaOffGrid().getQtd(), calculadora.pegaControladorEscolhidaOffGrid().getCorrente_carga()));

            textInversor.setText(String.format(Locale.ITALY, "%d %s de %.2f kW",
                        calculadora.pegaInversorEscolhidaOffGrid().getQtd(), "Inversor", (calculadora.pegaInversorEscolhidaOffGrid().getPotencia()/ 1000) * 0.8));

            //Definição do layout para escurecer a tela
            blackener = findViewById(R.id.blackener);

            //Botões para alterar os módulos e inversores e o controlador
            Button buttonInvertors = findViewById(R.id.button_trocar_inversor);
            Button buttonController = findViewById(R.id.button_trocar_controlador);
            Button buttonModules = findViewById(R.id.button_trocar_modulo);

            buttonInvertors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPopUpInversores();
                }
            });
            buttonController.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPopUpControladores();
                }
            });
            buttonModules.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPopUpModulos();
                }
            });

            //Botão voltar
            Button buttonVoltar = findViewById(R.id.button_voltar);
            AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
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
        ArrayAdapter<String> adapterS =new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, calculadora.pegaNomesPaineisOffGrid());
        spinnerModulos.setAdapter(adapterS);
        spinnerModulos.setSelection(calculadora.pegaListaPaineisOffGrid().indexOf(calculadora.pegaPlacaEscolhidaOffGrid()));

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
                calculadora.Calcular(EquipamentosOffGridActivity.this);
            }
        });
    }

    public void ShowPopUpControladores() {
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
        tituloPopup.setText(R.string.titulo_escolher_controlador);

        //Criação do Spinner
        AppCompatSpinner spinnerControlador = rootView.findViewById(R.id.spinner_modulos_inversores);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, calculadora.pegaNomesControladoresOffGrid());
        spinnerControlador.setAdapter(adapterS);
        spinnerControlador.setSelection(calculadora.pegaListaControladoresOffGrid().indexOf(calculadora.pegaControladorEscolhidaOffGrid()));

        //Criação do botão
        Button buttonRecalc = rootView.findViewById(R.id.button_recalc_modulos_inversores);
        AutoSizeText.AutoSizeButton(buttonRecalc, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        buttonRecalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acha o spinner
                AppCompatSpinner spinnerControlador = rootView.findViewById(R.id.spinner_modulos_inversores);
                calculadora.setIdControladorEscolhido(spinnerControlador.getSelectedItemPosition());
                //Refaz o cálculo
                calculadora.Calcular(EquipamentosOffGridActivity.this);
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
        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, calculadora.pegaNomesInversoresOffGrid());
        spinnerInversores.setAdapter(adapterS);
        spinnerInversores.setSelection(calculadora.pegaListaInversoresOffGrid().indexOf(calculadora.pegaInversorEscolhidaOffGrid()));

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
                calculadora.Calcular(EquipamentosOffGridActivity.this);
            }
        });
    }

}