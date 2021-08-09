package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Equipamentos;
import com.solares.calculadorasolar.classes.Global;

import java.io.InputStream;
import java.util.ArrayList;

public class PedirConsumoEnergeticoActivity extends AppCompatActivity {

    ArrayList<Equipamentos> equipamentos;
    public ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_consumo_energetico);

        System.out.println("Entrou na activity PedirConsumoEnergetico");



        try {
            final Global variavelGlobal = (Global)getApplicationContext();

            this.mViewHolder.buttonAdicionar = findViewById(R.id.button_adicionar);
            this.mViewHolder.buttonResultado = findViewById(R.id.button_resultados);

            this.mViewHolder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AdicionarEquipamentosActivity.class);
                    startActivity(intent);
                }
            });

            this.mViewHolder.buttonResultado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CalculadoraOffGrid calculadoraOffGrid = new CalculadoraOffGrid();
                    // Insere as informações que já temos no objeto
                    calculadoraOffGrid.setNomeCidade(variavelGlobal.getNomeCidade());
                    // Cria os vetores de Cidade e Estado
                    calculadoraOffGrid.setVetorCidade(CreateVetorCidade(variavelGlobal.getIdCity(), variavelGlobal.getNomeEstado()));
                    calculadoraOffGrid.setVetorEstado(CreateVetorEstado(calculadoraOffGrid.getVetorCidade()));
                    // Calcular
                    calculadoraOffGrid.Calcular(PedirConsumoEnergeticoActivity.this);

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
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
        vetorCidade = CSVRead.getCity(idCity, nomeEstado, is);

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
            VetorEstado = CSVRead.getState(vetorCidade, is);
            return VetorEstado;
        }
        return null;
    }

    public static class ViewHolder{
        Button buttonAdicionar;
        Button buttonResultado;
    }
}