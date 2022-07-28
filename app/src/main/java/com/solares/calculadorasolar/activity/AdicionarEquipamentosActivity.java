package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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
        Log.d("Activity", "Entrou na VizualizarEquipamentos");

        //pegar os intents
        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

        try{
            meuEquipamento = new Equipamentos_OffGrid();

            //Identificando os componentes do layout
            this.mViewHolder.buttonContinuar = findViewById(R.id.button_continuar);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonContinuar, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

            this.mViewHolder.spinnerCategoria = findViewById(R.id.spinner_categoria_equipamento);

            this.mViewHolder.spinnerEquipamento = findViewById(R.id.spinner_escolher_equipamento);

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

            //Aqui, coloca o vetor de strings que será exibido no spinner
           /* ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.Categorias, R.layout.spinner_item);
            this.mViewHolder.spinnerCategoria.setAdapter(adapterC);

            ArrayAdapter<CharSequence> adapterE = ArrayAdapter.createFromResource(this, R.array.CategoriaNull, R.layout.spinner_item);
            adapterE.setDropDownViewResource(R.layout.spinner_item);
            this.mViewHolder.spinnerEquipamento.setAdapter(adapterE);

            this.mViewHolder.spinnerCategoria.setSelection(0);
            this.mViewHolder.spinnerEquipamento.setSelection(0);

            //Se o spinner de categorias for selecionado, muda o spinner de equipamentos de acordo
            this.mViewHolder.spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Muda o spinner de equipamenros para a categoria correspondente
                    ChangeSpinner(position);
                    posicao = position;
                }

                //O ide reclama se eu tirar esse metodo, então deixei ele aí, mas sem nada
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });*/

            /*this.mViewHolder.spinnerEquipamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    InputStream is = SelectContext(posicao);
                    System.out.println("Position: " + position);
                    String[] vetorEquipamento;

                    //Pega os nomes dos inversores
                    int cont=0;
                    vetorEquipamento = new String[calculadora.pegaE.size()];
                    for (Inversor inversorAtual : this.pegaListaInversores()) {
                        nomesInversores[cont] = inversorAtual.getMarca() + " " + inversorAtual.getCodigo() + " - " + String.format(Locale.ITALY, "%.1f", inversorAtual.getPotencia()/1000f) + "kW";
                        cont++;
                    }

                    // Caso o spinner selecionado é algum diferente do settado
                    if(vetorEquipamento!=null){
                        mViewHolder.editTextQuantidade.setText("1");
                        mViewHolder.editTextPotencia.setText(vetorEquipamento[Constants.iEQUI_POT]);
                        mViewHolder.editTextPeriodoUso.setText("1");
                        mViewHolder.editTextWhdia.setText("1");
                        nome = mViewHolder.spinnerEquipamento.getSelectedItem().toString();

                        //Saber se o equipamento eh de Corrente Contínua ou Não
                        if(vetorEquipamento[Constants.iEQUI_CC].equals("1"))
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
                public void onNothingSelected(AdapterView<?> parent) {}
            });*/

            this.mViewHolder.buttonContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        double quantidade = Integer.parseInt( mViewHolder.editTextQuantidade.getText().toString() );
                        double potencia = Integer.parseInt( mViewHolder.editTextPotencia.getText().toString() );
                        double periodoUso = Integer.parseInt( mViewHolder.editTextPeriodoUso.getText().toString() );
                        double diasUtilizado = Math.round(Integer.parseInt(mViewHolder.editTextWhdia.getText().toString()));//potencia / Integer.parseInt( mViewHolder.editTextWhdia.getText().toString()

                        // Adicionando as características para o equipamento
                        meuEquipamento.setNome(nome);
                        meuEquipamento.setQuantidade(quantidade);
                        meuEquipamento.setPotencia(potencia);
                        meuEquipamento.setHorasPorDia(periodoUso);
                        meuEquipamento.setDiasUtilizados(diasUtilizado);
                        meuEquipamento.setCC(correnteContinua);

                        //Usuário clicou para editar um equipamento posteriormente selecionado
                        /*if(getIntent().hasExtra("posVariavelGlobal")){
                            Bundle bundle = getIntent().getExtras();
                            int posVariavelGlobal = bundle.getInt("posVariavelGlobal");
                            variavelGlobal.alterarEquipamento(posVariavelGlobal, meuEquipamento);
                            variavelGlobal.setRemoverTodasViews(true);
                            finish();
                        }else
                            variavelGlobal.adicionarElemento(meuEquipamento);*/



                        System.out.println("Equipamento possui corrente contrinua?(1=sim): "+correnteContinua);

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
        Spinner spinnerCategoria;
        Spinner spinnerEquipamento;
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