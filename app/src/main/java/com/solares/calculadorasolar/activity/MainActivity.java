package com.solares.calculadorasolar.activity;

import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowPopUpInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.CSVManager;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.FirebaseManager;
import com.solares.calculadorasolar.components.CustomSpinner;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    public static int larguraTela;
    public static int alturaTela;
    public float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Get phone dimensions
        GetPhoneDimensions(this);

        // Initialize ViewHolder
        this.mViewHolder = new ViewHolder();

        // Identify layout components
        this.mViewHolder.textSimulacao = findViewById(R.id.text_simulacao);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textSimulacao, alturaTela, larguraTela, 3f);
        this.mViewHolder.buttonOnGrid = findViewById(R.id.button_on_grid);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonOnGrid, alturaTela, larguraTela, porcent);
        this.mViewHolder.buttonOffGrid = findViewById(R.id.button_off_grid);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonOffGrid, alturaTela, larguraTela, porcent);
        Button buttonEntendaSistema = findViewById(R.id.button_show_sistem_information);
        AutoSizeText.AutoSizeButton(buttonEntendaSistema, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        // Initialize custom spinners
        initializeCustomSpinners();

        this.mViewHolder.layout = findViewById(R.id.layout_calculo);

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        // Initialize On-Grid Calculator
        CalculadoraOnGrid calculadora = new CalculadoraOnGrid();
        calculadora.setListaPaineis(FirebaseManager.fbBuscaListaPaineis(MainActivity.this, sharedPref));
        calculadora.setListaInversores(FirebaseManager.fbBuscaListaInversores(MainActivity.this, sharedPref));

        // Initialize Off-Grid Calculator
        CalculadoraOffGrid calculadoraOffGrid = new CalculadoraOffGrid();
        calculadoraOffGrid.setListaPaineisOffGrid(FirebaseManager.fbBuscaListaPaineisOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaInversoresOffGrid(FirebaseManager.fbBuscaListaInversoresOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaControladoresOffGrid(FirebaseManager.fbBuscaListaControladorOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaBateriasOffGrid(FirebaseManager.fbBuscaListaBateriaOffGrid(MainActivity.this, sharedPref));
        calculadoraOffGrid.setListaEquipamentosOffGrid(FirebaseManager.fbBuscaListaEquipamentoOffGrid(MainActivity.this, sharedPref));

        // Set button click listeners
        setupButtonListeners(calculadora, calculadoraOffGrid);

        buttonEntendaSistema.setOnClickListener(view ->
                ShowPopUpInfo(MainActivity.this, findViewById(R.id.blackener), "Sistemas",
                        "Off Grid: ”Fora da rede”. Sistema fotovoltaico que trabalha de forma autônoma..."));
    }

    private void initializeCustomSpinners() {
        // Initialize states spinner
        FrameLayout statesContainer = findViewById(R.id.states_spinner_container);
        this.mViewHolder.spinnerStates = new CustomSpinner(this, statesContainer);

        // Initialize cities spinner
        FrameLayout citiesContainer = findViewById(R.id.cities_spinner_container);
        this.mViewHolder.spinnerCities = new CustomSpinner(this, citiesContainer);

        // Load data
        loadSpinnerData();
    }

    private void loadSpinnerData() {
        String[] statesArray = getResources().getStringArray(R.array.Estados);
        List<String> statesList = Arrays.asList(statesArray);
        this.mViewHolder.spinnerStates.setItems(statesList);
        this.mViewHolder.spinnerStates.setSelectedItem(statesList.get(7));

        this.mViewHolder.spinnerStates.setOnItemSelectedListener(item -> {
            int position = statesList.indexOf(item);
            changeSpinnerCities(position);
        });

        changeSpinnerCities(7);
    }

    private void setupButtonListeners(CalculadoraOnGrid calculadora, CalculadoraOffGrid calculadoraOffGrid) {
        // On-Grid button listener
        this.mViewHolder.buttonOnGrid.setOnClickListener(v -> {
            try {
                final String stateName = mViewHolder.spinnerStates.getSelectedItem();
                final String cityName = mViewHolder.spinnerCities.getSelectedItem();
                final int idCity = getCityPosition(stateName, cityName);

                calculadora.setNomeCidade(cityName);
                calculadora.setVetorCidade(CreateVetorCidade(idCity, stateName));
                calculadora.setVetorEstado(CreateVetorEstado(calculadora.pegaVetorCidade()));
                calculadora.setTarifaMensal(Double.parseDouble(calculadora.pegaVetorEstado()[Constants.iEST_TARIFA]));

                calculadora.setListaEmpresas(FirebaseManager.GetEmpresasFirebase(calculadora, MainActivity.this));
                AbrirActivityDetalhes(calculadora);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        // Off-Grid button listener
        this.mViewHolder.buttonOffGrid.setOnClickListener(v -> {
            try {
                final String stateName = mViewHolder.spinnerStates.getSelectedItem();
                final String cityName = mViewHolder.spinnerCities.getSelectedItem();
                final int idCity = getCityPosition(stateName, cityName);

                calculadoraOffGrid.setNomeCidade(cityName);
                calculadoraOffGrid.setVetorCidade(CreateVetorCidade(idCity, stateName));
                calculadoraOffGrid.setVetorEstado(CreateVetorEstado(calculadoraOffGrid.pegaVetorCidade()));

                Intent intent = new Intent(MainActivity.this, VizualizarEquipamentosActivity.class);
                intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadoraOffGrid);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    private int getCityPosition(String stateName, String cityName) {
        int statePos = Arrays.asList(getResources().getStringArray(R.array.Estados)).indexOf(stateName);
        int stateArrayRes = getStateArrayResource(statePos);
        return Arrays.asList(getResources().getStringArray(stateArrayRes)).indexOf(cityName);
    }

    public void changeSpinnerCities(int statePos) {
        int stateArrayRes = getStateArrayResource(statePos);
        String[] citiesArray = getResources().getStringArray(stateArrayRes);
        List<String> citiesList = Arrays.asList(citiesArray);
        this.mViewHolder.spinnerCities.setItems(citiesList);
        this.mViewHolder.spinnerCities.setSelectedItem(citiesList.get(0));
    }

    private int getStateArrayResource(int statePos) {
        int[] statesPositions = {R.array.AC, R.array.AL, R.array.AP, R.array.AM, R.array.BA, R.array.CE, R.array.DF,
                R.array.ES, R.array.GO, R.array.MA, R.array.MT, R.array.MS, R.array.MG, R.array.PA, R.array.PB,
                R.array.PR, R.array.PE, R.array.PI, R.array.RJ, R.array.RN, R.array.RS, R.array.RO, R.array.RR,
                R.array.SC, R.array.SP, R.array.SE, R.array.TO};
        return statesPositions[statePos];
    }

    /* Descrição: Pega informações do banco de dados e retorna o vetor da cidade do usuário */
    public String[] CreateVetorCidade(int idCity, String nomeEstado) {
        InputStream is = this.getResources().openRawResource(R.raw.banco_irradiancia);
        return CSVManager.getCity(idCity, nomeEstado, is);
    }

    /* Descrição: Pega informações do banco de dados e retorna o vetor do estado do usuário */
    public String[] CreateVetorEstado(String[] vetorCidade) {
        if (vetorCidade != null) {
            InputStream is = this.getResources().openRawResource(R.raw.banco_estados);
            return CSVManager.getState(vetorCidade, is);
        }
        return null;
    }

    public void AbrirActivityDetalhes(CalculadoraOnGrid calculadora) {
        Intent intent = new Intent(this, DetalhesActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public static void GetPhoneDimensions(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        larguraTela = size.x;
        alturaTela = size.y;
    }

    public static class ViewHolder {
        TextView textSimulacao;
        Button buttonOnGrid;
        Button buttonOffGrid;
        CustomSpinner spinnerCities;
        CustomSpinner spinnerStates;
        ConstraintLayout layout;
    }
}