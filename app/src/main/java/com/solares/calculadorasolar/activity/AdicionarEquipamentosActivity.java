package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Equipamentos;
import com.solares.calculadorasolar.classes.Global;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class AdicionarEquipamentosActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    Equipamentos meuEquipamento;
    String nome;
    int posicao=0;
    boolean correnteContinua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_equipamentos);

        System.out.println("Entrou na activity AdicionarEquipamentos");

        try{
            final Intent intent = getIntent();
            final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);
            final Global variavelGlobal = (Global)getApplicationContext();

            meuEquipamento = new Equipamentos();

            //Identificando os componentes do layout
            this.mViewHolder.buttonContinuar = findViewById(R.id.button_continuar);
            this.mViewHolder.spinnerCategoria = findViewById(R.id.spinner_categoria_equipamento);
            this.mViewHolder.spinnerEquipamento = findViewById(R.id.spinner_escolher_equipamento);
            this.mViewHolder.editTextQuantidade = findViewById(R.id.editText_quantidade);
            this.mViewHolder.editTextPotencia = findViewById(R.id.editText_potencia);
            this.mViewHolder.editTextPeriodoUso = findViewById(R.id.editText_periodoUso);
            this.mViewHolder.editTextWhdia = findViewById(R.id.editText_Whdia);

            //Aqui, coloca o vetor de strings que será exibido no spinner
            ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.Categorias, R.layout.spinner_item);
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
            });

            this.mViewHolder.spinnerEquipamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    InputStream is = SelectContext(posicao);
                    System.out.println("Position: " + position);
                    String[] vetorEquipamento = CSVRead.getEquipamento(is, position);
                    System.out.println("Vetor WEquipamento: "+vetorEquipamento);

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
            });

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
                        if(getIntent().hasExtra("posVariavelGlobal")){
                            Bundle bundle = getIntent().getExtras();
                            int posVariavelGlobal = bundle.getInt("posVariavelGlobal");
                            variavelGlobal.alterarEquipamento(posVariavelGlobal, meuEquipamento);
                            variavelGlobal.setRemoverTodasViews(true);
                            finish();
                        }else
                            variavelGlobal.adicionarElemento(meuEquipamento);



                        System.out.println("Equipamento possui corrente contrinua?(1=sim): "+correnteContinua);

                        finish();
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

    //Muda o spinner de equipamentos para os equipamentos da categoria selecionado
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
    }

    public static class ViewHolder{
        Button buttonContinuar;
        Spinner spinnerCategoria;
        Spinner spinnerEquipamento;
        EditText editTextQuantidade;
        EditText editTextPotencia;
        EditText editTextPeriodoUso;
        EditText editTextWhdia;
    }
}