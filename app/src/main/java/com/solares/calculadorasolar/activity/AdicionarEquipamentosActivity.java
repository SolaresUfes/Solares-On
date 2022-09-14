package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Inversor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AdicionarEquipamentosActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    Equipamentos_OffGrid meuEquipamento;
    String nome;
    int posicao=0;
    boolean correnteContinua;

    public float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_equipamentos);
        Log.d("Activity", "Entrou na AdicionarEquipamentos");



        try{
            //pegar os intents
            Intent intent = getIntent();
            final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);
            meuEquipamento = new Equipamentos_OffGrid();

            //Identificando os componentes do layout
            this.mViewHolder.buttonContinuar = findViewById(R.id.button_continuar);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonContinuar, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

            Spinner spinnerEquipamentos = findViewById(R.id.spinner_escolher_equipamento);
            Spinner spinnerCategorias = findViewById(R.id.spinner_categoria_equipamento);

            this.mViewHolder.editTextQuantidade = findViewById(R.id.editText_quantidade);
            AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextQuantidade, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.textQuantidade = findViewById(R.id.textQnt);
            AutoSizeText.AutoSizeTextView(this.mViewHolder.textQuantidade, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.editTextPotencia = findViewById(R.id.editText_potencia);
            AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextPotencia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.textPotencia = findViewById(R.id.textPot);
            AutoSizeText.AutoSizeTextView(this.mViewHolder.textPotencia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.editTextPeriodoUso = findViewById(R.id.editText_periodoUso);
            AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextPeriodoUso, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.textPeriodoUso = findViewById(R.id.textPerUso);
            AutoSizeText.AutoSizeTextView(this.mViewHolder.textPeriodoUso, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.editTextWhdia = findViewById(R.id.editText_Whdia);
            AutoSizeText.AutoSizeEditText(this.mViewHolder.editTextWhdia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.textWhdia = findViewById(R.id.textWhDia);
            AutoSizeText.AutoSizeTextView(this.mViewHolder.textWhdia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            //Pega o layout para poder colocar um listener nele (esconder o teclado)
            ConstraintLayout layout = findViewById(R.id.layout_add_equipamento);

            //Listener do fundo do layout, se o usuário clicar nele, esconde o teclado
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(mViewHolder.editTextWhdia);
                }
            });

            // Aqui, coloca o vetor de strings que será exibido no spinner de categorias
            ArrayAdapter<String> adapterC =new ArrayAdapter<String>(this, R.layout.spinner_item, calculadora.pegaNomesEquipamentosOffGrid());
            spinnerCategorias.setAdapter(adapterC);

            // Aqui, coloca o vetor de strings que será exibido no spinner de equipamentos
            ArrayAdapter<String> adapterS =new ArrayAdapter<String>(this, R.layout.spinner_item, calculadora.pegaNomesEquipamentosOffGrid());
            spinnerEquipamentos.setAdapter(adapterS);
            spinnerEquipamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Equipamentos_OffGrid Equipamento = calculadora.pegaVetorEquipamentos().get(position);

                    // Caso o spinner selecionado é algum diferente do settado
                    if(Equipamento!=null){
                        mViewHolder.editTextQuantidade.setText("1");
                        mViewHolder.editTextPotencia.setText(String.valueOf(Equipamento.getPotencia()));
                        mViewHolder.editTextPeriodoUso.setText("1");
                        mViewHolder.editTextWhdia.setText(String.valueOf(Equipamento.getDiasUtilizados()));
                        nome = spinnerEquipamentos.getSelectedItem().toString();

                        //Saber se o equipamento eh de Corrente Contínua ou Não
                        if(Equipamento.getCC())
                            correnteContinua = true;
                        else
                            correnteContinua = false;

                    }else{
                        // Limpar o text que está settado
                        mViewHolder.editTextQuantidade.getText().clear();
                        mViewHolder.editTextPotencia.getText().clear();
                        mViewHolder.editTextPeriodoUso.getText().clear();
                        mViewHolder.editTextWhdia.getText().clear();

                        mViewHolder.editTextQuantidade.setHint("-");
                        mViewHolder.editTextPotencia.setHint("-");
                        mViewHolder.editTextPeriodoUso.setHint("-");
                        mViewHolder.editTextWhdia.setHint("-");
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Equipamentos_OffGrid Equipamento = calculadora.pegaVetorEquipamentos().get(0);

                    // Caso o spinner selecionado é algum diferente do settado
                    if(Equipamento!=null){
                        mViewHolder.editTextQuantidade.setText("1");
                        mViewHolder.editTextPotencia.setText(String.valueOf(Equipamento.getPotencia()));
                        mViewHolder.editTextPeriodoUso.setText("1");
                        mViewHolder.editTextWhdia.setText(String.valueOf(Equipamento.getDiasUtilizados()));
                        nome = spinnerEquipamentos.getSelectedItem().toString();

                        //Saber se o equipamento eh de Corrente Contínua ou Não
                        if(Equipamento.getCC())
                            correnteContinua = true;
                        else
                            correnteContinua = false;

                    }else{
                        // Limpar o text que está settado
                        mViewHolder.editTextQuantidade.getText().clear();
                        mViewHolder.editTextPotencia.getText().clear();
                        mViewHolder.editTextPeriodoUso.getText().clear();
                        mViewHolder.editTextWhdia.getText().clear();

                        mViewHolder.editTextQuantidade.setHint("-");
                        mViewHolder.editTextPotencia.setHint("-");
                        mViewHolder.editTextPeriodoUso.setHint("-");
                        mViewHolder.editTextWhdia.setHint("-");
                    }
                }
            });

            this.mViewHolder.buttonContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        double quantidade = Double.parseDouble( mViewHolder.editTextQuantidade.getText().toString() );
                        double potencia = Double.parseDouble( mViewHolder.editTextPotencia.getText().toString() );
                        double periodoUso = Double.parseDouble( mViewHolder.editTextPeriodoUso.getText().toString() );
                        double diasUtilizado = Math.round(Double.parseDouble(mViewHolder.editTextWhdia.getText().toString()));//potencia / Integer.parseInt( mViewHolder.editTextWhdia.getText().toString()

                        // Adicionando as características para o equipamento
                        meuEquipamento.setNome(nome);
                        meuEquipamento.setQuantidade(quantidade);
                        meuEquipamento.setPotencia(potencia);
                        meuEquipamento.setHorasPorDia(periodoUso);
                        meuEquipamento.setDiasUtilizados(diasUtilizado);
                        meuEquipamento.setCC(correnteContinua);

                        //calculadora.setEquipamentosSelecionados(meuEquipamento);
                        Intent intent = new Intent(getApplicationContext(), VizualizarEquipamentosActivity.class);
                        //intent.putExtra("equipamento", calculadora);
                        intent.putExtra(Constants.EXTRA_EQUIPAMENTO, meuEquipamento);
                        setResult(Activity.RESULT_OK, intent);
                        finish(); // encerrar activity
                    }catch (Exception e){
                        try {
                            Toast.makeText(AdicionarEquipamentosActivity.this, "Selecione um equipamento", Toast.LENGTH_LONG).show();
                        } catch (Exception ee){
                            ee.printStackTrace();
                        }
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*//Muda o spinner de equipamentos para os equipamentos da categoria selecionado
    public void ChangeSpinner(int categoriaPos){
        int[] posicoesCategorias = {R.array.CategoriaNull, R.array.Categoria1, R.array.Categoria2, R.array.Categoria3};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, posicoesCategorias[categoriaPos], R.layout.spinner_item);
        this.mViewHolder.spinnerEquipamento.setAdapter(adapter);
    }

    // caso cada categoria seja um arquivo CSV diferente
    public InputStream SelectContext(int posicao){
        if(posicao == 1){
            return getResources().openRawResource(R.raw.banco_equipamentos);
        }
        return getResources().openRawResource(R.raw.banco_equipamentos);
    }*/

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("TarifaActivity", "Error while hiding keyboard");
        }
    }

    public static class ViewHolder{
        Button buttonContinuar;
        EditText editTextQuantidade;
        EditText editTextPotencia;
        EditText editTextPeriodoUso;
        EditText editTextWhdia;
        TextView textQuantidade;
        TextView textPotencia;
        TextView textPeriodoUso;
        TextView textWhdia;
        Spinner spinnerEquipamentos;
    }
}