package com.solares.calculadorasolar.activity;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid;

import java.util.ArrayList;

public class VizualizarEquipamentosActivity extends AppCompatActivity {

    public static float porcent = 3f;


    ArrayList<Equipamentos_OffGrid> todosMeusEquipamentos;
    public ViewHolder mViewHolder = new ViewHolder();
    private LinearLayout linearLayout;
    int i = 0;
    double potenciaUtilizadaCC=0;
    double potenciaUtilizadaCA=0;

    TextView textSemEquipamentos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizar_equipamentos);
        Log.d("Activity", "Entrou na VizualizarEquipamentos");

        //pegar os intents
        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

        GetPhoneDimensions(this);

        //Pega o view do texo pra recalcular e ajusta o tamanho da fonte
        textSemEquipamentos = findViewById(R.id.text_sem_equipamentos);
        AutoSizeText.AutoSizeTextView(textSemEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        //Pega o view do texto pra recalcular e ajusta o tamanho da fonte
        TextView textListaEquipamentos = findViewById(R.id.text_titulo_lista_equipamentos);
        AutoSizeText.AutoSizeTextView(textListaEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        this.mViewHolder.buttonAdicionar = findViewById(R.id.button_adicionar);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonAdicionar, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        this.mViewHolder.buttonResultado = findViewById(R.id.button_resultados);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonResultado, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        linearLayout = findViewById(R.id.layoutTest);

        this.mViewHolder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdicionarEquipamentosActivity.class);
                startActivity(intent);
            }
        });

        try{
            this.mViewHolder.buttonResultado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        /*int a=0;
                        while (a < todosMeusEquipamentos.size()){
                            variavelGlobal.printarNomeElemento(a);
                            System.out.println("Quantidade: "+variavelGlobal.getQuantidadeElemento(a));
                            System.out.println("Potencia: "+variavelGlobal.getPotenciaElemento(a));
                            System.out.println("");
                            a++;
                        }
                        System.out.println("----------------------- Quantidade de equipamentos: "+todosMeusEquipamentos.size()+" ------------------");*/

                        potenciaUtilizadaCC = demandaEnergiaAtivaDiaria(todosMeusEquipamentos, true);
                        potenciaUtilizadaCA = demandaEnergiaAtivaDiaria(todosMeusEquipamentos, false);

                        System.out.println("\n------ Potencia CC: " + potenciaUtilizadaCC + " ------------------");
                        System.out.println("------ Potencia CA: " + potenciaUtilizadaCA + " ------------------");

                        // Insere a potencia
                        calculadora.setPotenciaUtilizadaDiariaCC(potenciaUtilizadaCC);
                        calculadora.setPotenciaUtilizadaDiariaCA(potenciaUtilizadaCA);
                        // Calcular
                        calculadora.Calcular(VizualizarEquipamentosActivity.this);
                    } catch (Exception e) {
                        try {
                            Toast.makeText(VizualizarEquipamentosActivity.this, "Não há equipamentos", Toast.LENGTH_LONG).show();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double demandaEnergiaAtivaDiaria(ArrayList<Equipamentos_OffGrid> vetorEquipamentos, boolean Icontinua){
        double potenciaTotal=0;
        double rendimentoBat=0.9, rendimentoInv=0.9;

        if(Icontinua){
            for(int i=0; i<vetorEquipamentos.size(); i++){
                if(vetorEquipamentos.get(i).getCC()){
                    potenciaTotal = potenciaTotal + vetorEquipamentos.get(i).getPotencia() * vetorEquipamentos.get(i).getQuantidade() * vetorEquipamentos.get(i).getHorasPorDia() * vetorEquipamentos.get(i).getDiasUtilizados()/7;
                }
            }
            return (potenciaTotal / (rendimentoBat));
        }

        for(int i=0; i<vetorEquipamentos.size(); i++){
            if(!vetorEquipamentos.get(i).getCC()){
                potenciaTotal = potenciaTotal + vetorEquipamentos.get(i).getPotencia() * vetorEquipamentos.get(i).getQuantidade() * vetorEquipamentos.get(i).getHorasPorDia() * vetorEquipamentos.get(i).getDiasUtilizados()/7;
            }
        }
        return potenciaTotal / (rendimentoBat * rendimentoInv);
    }

    public static class ViewHolder{
    Button buttonAdicionar;
    Button buttonResultado;
    }
}