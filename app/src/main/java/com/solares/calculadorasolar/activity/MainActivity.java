package com.solares.calculadorasolar.activity;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.CSVManager;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos;
import com.solares.calculadorasolar.classes.auxiliares.FirebaseManager;
import com.solares.calculadorasolar.classes.entidades.Empresa;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();

    /////
    public static int larguraTela;
    public static int alturaTela;
    public float porcent = 3f;

    //
    //private boolean calcByMoney = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        //Identificando os componentes do layout
        this.mViewHolder.textSimulacao = findViewById(R.id.text_simulacao);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textSimulacao, alturaTela, larguraTela, 3f);
        this.mViewHolder.buttonOnGrid = findViewById(R.id.button_on_grid);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonOnGrid, alturaTela, larguraTela, porcent);
        this.mViewHolder.buttonOffGrid = findViewById(R.id.button_off_grid);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonOffGrid, alturaTela, larguraTela, porcent);

        //Criando spinners (dos estados e das cidades)
        this.mViewHolder.spinnerStates = findViewById(R.id.spinner_states);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.Estados, R.layout.spinner_item);
        this.mViewHolder.spinnerStates.setAdapter(adapterS);
        this.mViewHolder.spinnerStates.setSelection(7);

        this.mViewHolder.spinnerCities = findViewById(R.id.spinner_cities);
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.ES, R.layout.spinner_item);
        this.mViewHolder.spinnerCities.setAdapter(adapterC);
        this.mViewHolder.spinnerCities.setSelection(77);

        this.mViewHolder.layout = findViewById(R.id.layout_calculo);

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        //////////// Inicia Calculadora On-Grid
        CalculadoraOnGrid calculadora = new CalculadoraOnGrid();
        // Cria os vetores de Paineis e de Inversores - Sistema On Grid
        calculadora.setListaPaineis(FirebaseManager.fbBuscaListaPaineis(MainActivity.this, sharedPref));
        calculadora.setListaInversores(FirebaseManager.fbBuscaListaInversores(MainActivity.this, sharedPref));
        //////////// Inicia Calculadora Off-Grid
        CalculadoraOffGrid calculadoraOffGrid = new CalculadoraOffGrid();
        // Cria os vetores de Paineis, Inversores, Baterias, Controladores e Equipamentos - Sistema Off Grid
        calculadoraOffGrid.setListaPaineisOffGrid(FirebaseManager.fbBuscaListaPaineisOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaInversoresOffGrid(FirebaseManager.fbBuscaListaInversoresOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaControladoresOffGrid(FirebaseManager.fbBuscaListaControladorOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaBateriasOffGrid(FirebaseManager.fbBuscaListaBateriaOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaEquipamentosOffGrid(FirebaseManager.fbBuscaListaEquipamentoOffGrid(MainActivity.this, sharedPref));

        //Se o spinner de estado for selecionado, muda o spinner de cidades de acordo
        this.mViewHolder.spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Muda o spinner de cidades para o estado correspondente
                ChangeSpinner(position);
            }

            //O ide reclama se eu tirar esse metodo, então deixei ele aí, mas sem nada
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //
        //Se o usuário clicar no botão On-Grid, é salvo o estado e cidade e muda-se para a próxima activity
        this.mViewHolder.buttonOnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica se o valor inserido é válido
                try{
                    /////Pega as informações inseridas pelo usuário
                    //Identifica a cidade escolhida e pega suas informações
                    final String stateName = mViewHolder.spinnerStates.getSelectedItem().toString();
                    final int idCity = mViewHolder.spinnerCities.getSelectedItemPosition();
                    final String cityName = mViewHolder.spinnerCities.getItemAtPosition(idCity).toString();
                    //Guarda o custo mensal inserido pelo usuário

                    // Insere as informações que já temos no objeto
                    calculadora.setNomeCidade(cityName);
                    // Cria os vetores de Cidade e Estado
                    calculadora.setVetorCidade(CreateVetorCidade(idCity, stateName));
                    calculadora.setVetorEstado(CreateVetorEstado(calculadora.pegaVetorCidade()));
                    // Colocar Tarifa inicial
                    calculadora.setTarifaMensal(Double.parseDouble(calculadora.pegaVetorEstado()[Constants.iEST_TARIFA]));

                    // Criar Lista de Empresas
                    calculadora.setListaEmpresas(FirebaseManager.GetEmpresasFirebase(calculadora, MainActivity.this));

                    AbrirActivityDetalhes(calculadora);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        //Se o usuário clicar no botão Off-Grid, é salvo o estado e cidade e muda-se para a próxima activity
        this.mViewHolder.buttonOffGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica se o valor inserido é válido
                try{
                    /////Pega as informações inseridas pelo usuário
                    //Identifica a cidade escolhida e pega suas informações
                    final String stateName = mViewHolder.spinnerStates.getSelectedItem().toString();
                    final int idCity = mViewHolder.spinnerCities.getSelectedItemPosition();
                    final String cityName = mViewHolder.spinnerCities.getItemAtPosition(idCity).toString();
                    //Guarda o custo mensal inserido pelo usuário

                    // Insere as informações que já temos no objeto
                    calculadoraOffGrid.setNomeCidade(cityName);
                    // Cria os vetores de Cidade e Estado
                    calculadoraOffGrid.setVetorCidade(CreateVetorCidade(idCity, stateName));
                    calculadoraOffGrid.setVetorEstado(CreateVetorEstado(calculadoraOffGrid.pegaVetorCidade()));


                    Intent intent = new Intent(MainActivity.this, VizualizarEquipamentosActivity.class);
                    intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadoraOffGrid);
                    startActivity(intent);

                    //AbrirActivityVizualizarEquipamentos(calculadoraOffGrid);
                } catch (Exception e){
                    try {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_LONG).show();
                    } catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        });
    }

    /* Descrição: Pega informações do banco de dados e retorna o vetor da cidade do usuário
     * Parâmetros de Entrada: idCity - Inteiro que representa a cidade na lista do estado // nomeEstado - String com a sigla do estado
     * Saída: Vetor de Strings com as informações da cidade;
     * Pré Condições: idCity e nomeEstado devem ser válidos. O banco de dados deve estar funcionando;
     * Pós Condições: É retornado o vetor com as informações da cidade;
     */
    public String[] CreateVetorCidade(int idCity, String nomeEstado){
        InputStream is=null;
        String[] vetorCidade;

        //Pega as informações da cidade escolhida
        is = this.getResources().openRawResource(R.raw.banco_irradiancia);
        vetorCidade = CSVManager.getCity(idCity, nomeEstado, is);

        //Pegando as informações do estado
        is = this.getResources().openRawResource(R.raw.banco_estados);
        String[] stateVec;

        return vetorCidade;
    }


    /* Descrição: Pega informações do banco de dados e retorna o vetor do estado do usuário
     * Parâmetros de Entrada: vetorCidade - Vetor de Strings com as informações da cidade;
     * Saída: Vetor de Strings com as informações do estado;
     * Pré Condições: vetorCidade deve ser válido. O banco de dados deve estar funcionando;
     * Pós Condições: É retornado o vetor com as informações do estado;
     */
    public String[] CreateVetorEstado(String[] vetorCidade) {
        //Pegando as informações do estado
        InputStream is = this.getResources().openRawResource(R.raw.banco_estados);
        String[] VetorEstado;
        if (vetorCidade != null) {
            VetorEstado = CSVManager.getState(vetorCidade, is);
            return VetorEstado;
        }
        return null;
    }


    /* Descrição: Vai para uma página para selecionar a tarifa e número de fases, sendo sugerida a tarifa do estado
     * Parâmetros de Entrada: calculadora - O Objeto da calculadoraOnGrid;
     * Saída: void;
     * Pré Condições: Calculadora já foi criada e inicializada com o nome da cidade, a conta de luz, os vetores da cidade e estado e a tarifa do estado;
     * Pós Condições: Se o usuário confirmar, realiza o cálculo e mostra os resultados;
     */
    public void AbrirActivityDetalhes(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, DetalhesActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void AbrirActivityVizualizarEquipamentos(CalculadoraOffGrid calculadora){
        Intent intent = new Intent(this, VizualizarEquipamentosActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
        startActivity(intent);
    }



    ///////////////////// Funções de utilidade para a Main Activity

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("MainActivity", "Error while hiding keyboard");
        }
    }

    //Muda o spinner de cidades para as cidades do estado selecionado
    public void ChangeSpinner(int statePos){
        int[] statesPositions = {R.array.AC, R.array.AL, R.array.AP, R.array.AM, R.array.BA, R.array.CE, R.array.DF,
                R.array.ES, R.array.GO, R.array.MA, R.array.MT, R.array.MS, R.array.MG, R.array.PA, R.array.PB,
                R.array.PR, R.array.PE, R.array.PI, R.array.RJ, R.array.RN, R.array.RS, R.array.RO, R.array.RR,
                R.array.SC, R.array.SP, R.array.SE, R.array.TO};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, statesPositions[statePos], R.layout.spinner_item);
        this.mViewHolder.spinnerCities.setAdapter(adapter);
        //Se for ES, começa com vitória (vou puxar pro meu lado mesmo :P)
        if(statePos == 7){
            this.mViewHolder.spinnerCities.setSelection(77);
        }
    }

    /*Essa função pega a largura e altura da tela do celular em números inteiros e coloca essa informação nas váriaveis públicas larguraTela e alturaTela
     *Ela também define o PtarifaPassada (uma variável pública que condiz com a tarifa escolhida pelo usuário) como um valor passado
     */
    public static void GetPhoneDimensions(Activity activity){
        //Pegar dimensões
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        larguraTela = size.x;
        alturaTela = size.y;
    }

    public static class ViewHolder{
        TextView textSimulacao;
        Button buttonOnGrid;
        Button buttonOffGrid;
        Spinner spinnerCities;
        Spinner spinnerStates;
        ConstraintLayout layout;
    }
}
