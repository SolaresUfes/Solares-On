package com.solares.calculadorasolar.activity;

import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowHint;
import static com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos.ShowPopUpInfo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid;
import com.solares.calculadorasolar.components.CustomSpinner;

import java.util.Arrays;
import java.util.List;

public class AdicionarEquipamentosActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    Equipamentos_OffGrid meuEquipamento;
    String nome;
    boolean correnteContinua;
    public float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_equipamentos);
        Log.d("Activity", "Entrou na AdicionarEquipamentos");

        try {
            // Get intents
            Intent intent = getIntent();
            final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);
            meuEquipamento = new Equipamentos_OffGrid();

            // Initialize UI components
            initializeViews(calculadora);

            // Set up listeners
            setupListeners(calculadora);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeViews(CalculadoraOffGrid calculadora) {
        // Initialize buttons and text fields
        this.mViewHolder.buttonContinuar = findViewById(R.id.button_continuar);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonContinuar, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        // Replace native spinner with custom spinner
        FrameLayout spinnerContainer = findViewById(R.id.spinner_escolher_equipamento);
        this.mViewHolder.spinnerEquipamentos = new CustomSpinner(this, spinnerContainer);

        // Initialize other views
        this.mViewHolder.editTextQuantidade = findViewById(R.id.editText_quantidade);
        this.mViewHolder.editTextPotencia = findViewById(R.id.editText_potencia);
        this.mViewHolder.editTextPeriodoUso = findViewById(R.id.editText_periodoUso);
        this.mViewHolder.editTextWhdia = findViewById(R.id.editText_Whdia);

        this.mViewHolder.textQuantidade = findViewById(R.id.textQnt);
        this.mViewHolder.textPotencia = findViewById(R.id.textPot);
        this.mViewHolder.textPeriodoUso = findViewById(R.id.textPerUso);
        this.mViewHolder.textWhdia = findViewById(R.id.textWhDia);

        // Auto-size text views
        AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextQuantidade, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextPotencia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextPeriodoUso, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextWhdia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        AutoSizeText.AutoSizeTextView(this.mViewHolder.textQuantidade, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textPotencia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textPeriodoUso, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textWhdia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        // Set up spinner
        List<String> equipamentos = Arrays.asList(calculadora.pegaNomesEquipamentosOffGrid());
        this.mViewHolder.spinnerEquipamentos.setItems(equipamentos);
        this.mViewHolder.spinnerEquipamentos.setSelectedItem(equipamentos.get(0));
    }

    private void setupListeners(CalculadoraOffGrid calculadora) {
        // Layout click listener to hide keyboard
        ConstraintLayout layout = findViewById(R.id.layout_add_equipamento);
        layout.setOnClickListener(v -> hideKeyboard(mViewHolder.editTextWhdia));

        // Spinner item selection listener
        this.mViewHolder.spinnerEquipamentos.setOnItemSelectedListener(item -> {
            // Convert array to list for indexOf operation
            List<String> equipamentosList = Arrays.asList(calculadora.pegaNomesEquipamentosOffGrid());
            int position = equipamentosList.indexOf(item);
            if (position >= 0) {
                updateEquipmentFields(calculadora.pegaVetorEquipamentos().get(position));
            }
        });

        // Continue button listener
        this.mViewHolder.buttonContinuar.setOnClickListener(v -> {
            try {
                saveEquipmentData();
            } catch (Exception e) {
                Toast.makeText(AdicionarEquipamentosActivity.this,
                        "Selecione um equipamento", Toast.LENGTH_LONG).show();
            }
        });

        // Info button listeners
        findViewById(R.id.AE_inst_button_info).setOnClickListener(v ->
                ShowHint(findViewById(R.id.blackener), findViewById(R.id.inst_image_info)));

        this.mViewHolder.textPeriodoUso.setOnClickListener(v ->
                ShowPopUpInfo(AdicionarEquipamentosActivity.this, findViewById(R.id.blackener),
                        "Período de Uso", "Número de horas por dia que os equipamentos serão utilizados em média."));

        this.mViewHolder.textWhdia.setOnClickListener(v ->
                ShowPopUpInfo(AdicionarEquipamentosActivity.this, findViewById(R.id.blackener),
                        "Wh/dia", "Valor que informa o consumo energético de um equipamento em cada dia média, normalmente fornecido pelo fabricante."));
    }

    private void updateEquipmentFields(Equipamentos_OffGrid equipamento) {
        if (equipamento != null) {
            nome = equipamento.getNome();
            correnteContinua = equipamento.getCC();

            mViewHolder.editTextQuantidade.setText("1");
            mViewHolder.editTextPotencia.setText(String.valueOf(equipamento.getPotencia()));
            mViewHolder.editTextPeriodoUso.setText("1");
            mViewHolder.editTextWhdia.setText(String.valueOf(equipamento.getDiasUtilizados()));
        } else {
            clearEquipmentFields();
        }
    }

    private void clearEquipmentFields() {
        mViewHolder.editTextQuantidade.getText().clear();
        mViewHolder.editTextPotencia.getText().clear();
        mViewHolder.editTextPeriodoUso.getText().clear();
        mViewHolder.editTextWhdia.getText().clear();

        mViewHolder.editTextQuantidade.setHint("-");
        mViewHolder.editTextPotencia.setHint("-");
        mViewHolder.editTextPeriodoUso.setHint("-");
        mViewHolder.editTextWhdia.setHint("-");
    }

    private void saveEquipmentData() {
        double quantidade = Double.parseDouble(mViewHolder.editTextQuantidade.getText().toString());
        double potencia = Double.parseDouble(mViewHolder.editTextPotencia.getText().toString());
        double periodoUso = Double.parseDouble(mViewHolder.editTextPeriodoUso.getText().toString());
        double diasUtilizado = Math.round(Double.parseDouble(mViewHolder.editTextWhdia.getText().toString()));

        meuEquipamento.setNome(nome);
        meuEquipamento.setQuantidade(quantidade);
        meuEquipamento.setPotencia(potencia);
        meuEquipamento.setHorasPorDia(periodoUso);
        meuEquipamento.setDiasUtilizados(diasUtilizado);
        meuEquipamento.setCC(correnteContinua);

        Intent intent = new Intent(getApplicationContext(), VizualizarEquipamentosActivity.class);
        intent.putExtra(Constants.EXTRA_EQUIPAMENTO, meuEquipamento);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("TarifaActivity", "Error while hiding keyboard");
        }
    }

    public static class ViewHolder {
        Button buttonContinuar;
        CustomSpinner spinnerEquipamentos;
        EditText editTextQuantidade;
        EditText editTextPotencia;
        EditText editTextPeriodoUso;
        EditText editTextWhdia;
        TextView textQuantidade;
        TextView textPotencia;
        TextView textPeriodoUso;
        TextView textWhdia;
    }
}