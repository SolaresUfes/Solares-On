package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Equipamentos;
import com.solares.calculadorasolar.classes.Global;

class AdicionarEquipamentos extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    Equipamentos meuEquipamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_equipamentos);

        try{
            Intent intent = getIntent();
            final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

            meuEquipamento = new Equipamentos();

            //Identificando os componentes do layout
            this.mViewHolder.buttonCont = findViewById(R.id.button_continuar);
            this.mViewHolder.spinnerCategoria = findViewById(R.id.spinner_categoria_equipamento);
            this.mViewHolder.spinnerEquipamento = findViewById(R.id.spinner_escolher_equipamento);

            //Aqui, coloca o vetor de strings que será exibido no spinner
            ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.Categorias, R.layout.spinner_item);
            this.mViewHolder.spinnerCategoria.setAdapter(adapterC);
            this.mViewHolder.spinnerCategoria.setSelection(0);

            ArrayAdapter<CharSequence> adapterE = ArrayAdapter.createFromResource(this, R.array.Equipamentos, R.layout.spinner_item);
            this.mViewHolder.spinnerEquipamento.setAdapter(adapterE);
            this.mViewHolder.spinnerEquipamento.setSelection(0);

            this.mViewHolder.buttonCont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Equipamentos meusEquipamentos = new Equipamentos();
                    ((Global) this.getApplication()).adicionarElemento(meusEquipamentos); // descobrir um  jeito de fazer uma variavel global
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class ViewHolder{
        Button buttonCont;
        Spinner spinnerCategoria;
        Spinner spinnerEquipamento;
    }
}