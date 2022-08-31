package com.solares.calculadorasolar.activity;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid;

import java.util.ArrayList;

public class VizualizarEquipamentosActivity extends AppCompatActivity {

    public static float porcent = 2.5f;


    ArrayList<Equipamentos_OffGrid> todosMeusEquipamentos = new ArrayList<>();
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
        calculadora.iniciarNomesDosElementos();

        GetPhoneDimensions(this);

        //Pega o view do texo pra recalcular e ajusta o tamanho da fonte
        textSemEquipamentos = findViewById(R.id.text_sem_equipamentos);
        AutoSizeText.AutoSizeTextView(textSemEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do texto pra recalcular e ajusta o tamanho da fonte
        TextView textListaEquipamentos = findViewById(R.id.text_titulo_lista_equipamentos);
        AutoSizeText.AutoSizeTextView(textListaEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 2.6f);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        this.mViewHolder.buttonAdicionar = findViewById(R.id.button_adicionar);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonAdicionar, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        this.mViewHolder.buttonResultado = findViewById(R.id.button_resultados);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonResultado, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        linearLayout = findViewById(R.id.layoutTest);

        this.mViewHolder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdicionarEquipamentosActivity.class);
                intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
                //startActivity(intent);
                //Intent intent = new Intent(getApplicationContext(), AdicionarEquipamentosActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        try{
            this.mViewHolder.buttonResultado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    potenciaUtilizadaCC=0;
                    potenciaUtilizadaCA=0;

                    try {
                        /*if (todosMeusEquipamentos.size()==0) todosMeusEquipamentos.get(0);
                        for (Equipamentos_OffGrid equipamentos_offGrid:todosMeusEquipamentos){
                            System.out.println("Euqipamento: "+equipamentos_offGrid.getNome()+" , corrente continua: "+equipamentos_offGrid.getCC()+" , cons: "+equipamentos_offGrid.getHorasPorDia());
                            if (equipamentos_offGrid.getCC()){
                                potenciaUtilizadaCC += demandaEnergiaAtivaDiariaCC(equipamentos_offGrid);
                            }
                            else{
                                potenciaUtilizadaCA += demandaEnergiaAtivaDiariaCA(equipamentos_offGrid);
                            }
                        }*/

                        System.out.println("\n------ Potencia CC: " + potenciaUtilizadaCC + " ------------------");
                        System.out.println("------ Potencia CA: " + potenciaUtilizadaCA + " ------------------");

                        // Insere a potencia
                        calculadora.setPotenciaUtilizadaDiariaCC(potenciaUtilizadaCC);
                        calculadora.setPotenciaUtilizadaDiariaCA(800);
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

    public double demandaEnergiaAtivaDiaria(ArrayList<Equipamentos_OffGrid> todosMeusEquipamentos, boolean continua){
        double potenciaTotal=0;
        if(continua){
            for(int a=0; a<todosMeusEquipamentos.size(); a++){
                if(todosMeusEquipamentos.get(a).getCC())
                    potenciaTotal +=demandaEnergiaAtivaDiariaCC(todosMeusEquipamentos.get(a));
            }
            return potenciaTotal;
        }

        for(int a=0; a<todosMeusEquipamentos.size(); a++){
            if (!todosMeusEquipamentos.get(a).getCC())
                potenciaTotal +=demandaEnergiaAtivaDiariaCA(todosMeusEquipamentos.get(a));
        }
        return potenciaTotal;
    }

    public double demandaEnergiaAtivaDiariaCA(Equipamentos_OffGrid equipamentos_offGrid){
        double potenciaTotal=0;
        double rendimentoBat=0.9, rendimentoInv=0.9;
        potenciaTotal = equipamentos_offGrid.getPotencia() * equipamentos_offGrid.getQuantidade() * equipamentos_offGrid.getHorasPorDia() * equipamentos_offGrid.getDiasUtilizados()/7;
        return potenciaTotal / (rendimentoBat * rendimentoInv);
    }

    public double demandaEnergiaAtivaDiariaCC(Equipamentos_OffGrid equipamentos_offGrid){
        double potenciaTotal=0;
        double rendimentoBat=0.9;
        potenciaTotal = equipamentos_offGrid.getPotencia() * equipamentos_offGrid.getQuantidade() * equipamentos_offGrid.getHorasPorDia() * equipamentos_offGrid.getDiasUtilizados()/7;
        return (potenciaTotal / (rendimentoBat));
    }


    //
    //
    // Ao voltar da Activity AdicionarEquipametos, aqui irá criar dinâmicamente objetos na tela
    //
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                Bundle bundle = data.getExtras();
                if (bundle != null){
                    Equipamentos_OffGrid meuEquipamento = (Equipamentos_OffGrid) bundle.get(Constants.EXTRA_EQUIPAMENTO);
                    todosMeusEquipamentos.add(meuEquipamento);

                    while( i < todosMeusEquipamentos.size()){
                        addView();
                        i++;
                    }

                    for (Equipamentos_OffGrid equipamentos_offGrid : todosMeusEquipamentos){
                        System.out.println("Nome do equipamento: "+equipamentos_offGrid.getNome());
                    }
                }
                System.out.println("Quantidade de elementos no vetor: "+todosMeusEquipamentos.size());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (todosMeusEquipamentos.size()!=0) textSemEquipamentos.setVisibility(View.GONE);
        else textSemEquipamentos.setVisibility(View.VISIBLE);

        // Adicionando as características para o equipamento
        //Equipamentos_OffGrid meuEquipamento = new Equipamentos_OffGrid("Equipamento 1 - Alternada", 1, 3, 4, 15.0, false);
        //Equipamentos_OffGrid meuEquipamento2 = new Equipamentos_OffGrid("Equipamento 2 - Contínua", 1, 2, 2, 60.0, true);
        //todosMeusEquipamentos.add(meuEquipamento);
        //todosMeusEquipamentos.add(meuEquipamento2);

        if (todosMeusEquipamentos.size()!=0) textSemEquipamentos.setVisibility(View.GONE);
        else textSemEquipamentos.setVisibility(View.VISIBLE);

    }

    public void addView(){
        final View EquipamentosView = getLayoutInflater().inflate(R.layout.linha_add_equipamentos, null, false);
        final TextView textViewNomeE = (TextView)EquipamentosView.findViewById(R.id.nome_equipamento);
        final TextView textViewQntE = (TextView)EquipamentosView.findViewById(R.id.quantidade_equipamento);
        ImageView imageApagar = (ImageView)EquipamentosView.findViewById(R.id.apagar_equipamento);

        textViewNomeE.setText(todosMeusEquipamentos.get(i).getNome());
        textViewQntE.setText("Quantidade: "+todosMeusEquipamentos.get(i).getQuantidade());

        imageApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // melhorar esse modo de encontrar o equipamento no array
                String nomeEquipamentoSelecionado = (String) textViewNomeE.getText();
                String qntEquipamentoSelecionado = (String) textViewQntE.getText();
                String quantidade;
                int j=0;
                while(j < todosMeusEquipamentos.size()){
                    quantidade = "Quantidade: ";
                    quantidade = quantidade + Double.toString(todosMeusEquipamentos.get(j).getQuantidade());
                    if(nomeEquipamentoSelecionado.equals(todosMeusEquipamentos.get(j).getNome()) && quantidade.equals(qntEquipamentoSelecionado)) break;
                    j++;
                }
                i--;
                todosMeusEquipamentos.remove(j);
                // ----

                removerView(EquipamentosView);

                System.out.println("Quantidade de elementos no vetor: "+todosMeusEquipamentos.size());

                if (todosMeusEquipamentos.size()!=0) textSemEquipamentos.setVisibility(View.GONE);
                else textSemEquipamentos.setVisibility(View.VISIBLE);
            }
        });

        linearLayout.addView(EquipamentosView);
    }

    public void removerView(View view){ linearLayout.removeView(view); }

    public static class ViewHolder{
    Button buttonAdicionar;
    Button buttonResultado;
    }
}