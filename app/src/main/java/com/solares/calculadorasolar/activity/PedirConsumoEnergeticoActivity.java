package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Equipamentos;
import com.solares.calculadorasolar.classes.Global;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PedirConsumoEnergeticoActivity extends AppCompatActivity {


    ArrayList<Equipamentos> todosMeusEquipamentos;
    public ViewHolder mViewHolder = new ViewHolder();
    private LinearLayout linearLayout;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_consumo_energetico);

        System.out.println("Entrou na activity PedirConsumoEnergetico");

        try {
            final Global variavelGlobal = (Global)getApplicationContext();

            this.mViewHolder.buttonAdicionar = findViewById(R.id.button_adicionar);
            this.mViewHolder.buttonResultado = findViewById(R.id.button_resultados);
            linearLayout = findViewById(R.id.layoutTest);

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
                    try {

                        int a=0;
                        while (a < todosMeusEquipamentos.size()){
                            variavelGlobal.printarNomeElemento(a);
                            System.out.println("Quantidade: "+variavelGlobal.getQuantidadeElemento(a));
                            System.out.println("Potencia: "+variavelGlobal.getPotenciaElemento(a));
                            System.out.println("");
                            a++;
                        }

                        variavelGlobal.setMeusEquipamentos(todosMeusEquipamentos);
                       /* CalculadoraOffGrid calculadoraOffGrid = new CalculadoraOffGrid();
                        // Insere as informações que já temos no objeto
                        calculadoraOffGrid.setNomeCidade(variavelGlobal.getNomeCidade());
                        // Cria os vetores de Cidade e Estado
                        calculadoraOffGrid.setVetorCidade(CreateVetorCidade(variavelGlobal.getIdCity(), variavelGlobal.getNomeEstado()));
                        calculadoraOffGrid.setVetorEstado(CreateVetorEstado(calculadoraOffGrid.getVetorCidade()));
                        // Calcular
                        calculadoraOffGrid.Calcular(PedirConsumoEnergeticoActivity.this);*/
                    }catch (Exception e){
                        try {
                            Toast.makeText(PedirConsumoEnergeticoActivity.this, "Não há equipamentos", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onStart() {
        super.onStart();

        final Global variavelGlobal = (Global) getApplicationContext();

        todosMeusEquipamentos = variavelGlobal.getEquipamentos();
        while (i < todosMeusEquipamentos.size()) {
            addView();
            i++;
        }
    }

    public void addView(){
        final View EquipamentosView = getLayoutInflater().inflate(R.layout.linha_add_elementos, null, false);

        final TextView textViewNomeE = (TextView)EquipamentosView.findViewById(R.id.nome_equipamento);
        TextView textViewQntE = (TextView)EquipamentosView.findViewById(R.id.quantidade_equipamento);
        ImageView imageApagar = (ImageView)EquipamentosView.findViewById(R.id.apagar_equipamento);

        textViewNomeE.setText(todosMeusEquipamentos.get(i).getNome());
        textViewQntE.setText("Quantidade: "+todosMeusEquipamentos.get(i).getQuantidade());

        imageApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // melhorar esse modo de encontrar o equipamento no array
                String nomeEquipamento = (String) textViewNomeE.getText();
                int j=0;
                while(j < todosMeusEquipamentos.size() && nomeEquipamento != todosMeusEquipamentos.get(j).getNome()){
                    j++;
                }
                System.out.println("----------------------- J: "+j+" ------------------");
                i--;
                todosMeusEquipamentos.remove(j);
                // ----

                removerView(EquipamentosView);
            }
        });

        linearLayout.addView(EquipamentosView);
    }

    public void removerView(View view){ linearLayout.removeView(view); }


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