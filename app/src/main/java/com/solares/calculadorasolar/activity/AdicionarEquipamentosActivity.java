package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Equipamentos;
import com.solares.calculadorasolar.classes.Global;

import java.util.Random;

public class AdicionarEquipamentosActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    Equipamentos meuEquipamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_equipamentos);

        System.out.println("Entrou na activity AdicionarEquipamentos");

        try{
            Intent intent = getIntent();
            final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);
            final Global variavelGlobal = (Global)getApplicationContext();

            meuEquipamento = new Equipamentos();

            //Identificando os componentes do layout
            this.mViewHolder.buttonContinuar = findViewById(R.id.button_continuar);
            this.mViewHolder.spinnerCategoria = findViewById(R.id.spinner_categoria_equipamento);
            this.mViewHolder.spinnerEquipamento = findViewById(R.id.spinner_escolher_equipamento);

            //Aqui, coloca o vetor de strings que será exibido no spinner
            ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.Categorias, R.layout.spinner_item);
            this.mViewHolder.spinnerCategoria.setAdapter(adapterC);
            this.mViewHolder.spinnerCategoria.setSelection(0);

            ArrayAdapter<CharSequence> adapterE = ArrayAdapter.createFromResource(this, R.array.CategoriaNull, R.layout.spinner_item);
            adapterE.setDropDownViewResource(R.layout.spinner_item);
            this.mViewHolder.spinnerEquipamento.setAdapter(adapterE);
            this.mViewHolder.spinnerEquipamento.setSelection(0);

            //Se o spinner de estado for selecionado, muda o spinner de cidades de acordo
            this.mViewHolder.spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Muda o spinner de cidades para o estado correspondente
                    ChangeSpinner(position);
                }

                //O ide reclama se eu tirar esse metodo, então deixei ele aí, mas sem nada
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            this.mViewHolder.buttonContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Entrou no botao continuar");
                    int i=0;
                    Random random = new Random();
                    i=random.nextInt();
                    String s;
                    Equipamentos meusEquipamentos = new Equipamentos();
                    s=Integer.toString(i);
                    meusEquipamentos.setNome("Equipamento "+s);
                    variavelGlobal.adicionarElemento(meusEquipamentos); // descobrir um  jeito de fazer uma variavel global

                    finish();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Muda o spinner de cidades para as cidades do estado selecionado
    public void ChangeSpinner(int categoriaPos){
        int[] posicoesCategorias = {R.array.CategoriaNull, R.array.Categoria1, R.array.Categoria2, R.array.Categoria3};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, posicoesCategorias[categoriaPos], R.layout.spinner_item);
        this.mViewHolder.spinnerEquipamento.setAdapter(adapter);

        if(categoriaPos == 1){
            this.mViewHolder.spinnerEquipamento.setSelection(1);
        }
    }

    public static class ViewHolder{
        Button buttonContinuar;
        Spinner spinnerCategoria;
        Spinner spinnerEquipamento;
    }
}