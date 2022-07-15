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

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class PedirConsumoEnergeticoActivity extends AppCompatActivity {


    ArrayList<Equipamentos> todosMeusEquipamentos;
    public ViewHolder mViewHolder = new ViewHolder();
    private LinearLayout linearLayout;
    int i = 0;
    double potenciaUtilizadaCC=0;
    double potenciaUtilizadaCA=0;

    TextView textSemEquipamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_consumo_energetico);

        System.out.println("Entrou na activity PedirConsumoEnergetico");

        try {
            final Global variavelGlobal = (Global)getApplicationContext();

            //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
            //Essa função pega as dimensões e as coloca em váriaveis globais
            GetPhoneDimensions(this);

            textSemEquipamentos = findViewById(R.id.text_sem_equipamentos);
            AutoSizeText.AutoSizeTextView(textSemEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
            TextView textListaEquipamentos = findViewById(R.id.text_titulo_lista_equipamentos);
            AutoSizeText.AutoSizeTextView(textListaEquipamentos, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
            this.mViewHolder.buttonAdicionar = findViewById(R.id.button_adicionar);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonAdicionar, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
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

                        variavelGlobal.setMeusEquipamentos(todosMeusEquipamentos);

                        potenciaUtilizadaCC = demandaEnergiaAtivaDiaria(todosMeusEquipamentos, true);
                        potenciaUtilizadaCA = demandaEnergiaAtivaDiaria(todosMeusEquipamentos, false);

                        System.out.println("\n------ Potencia CC: "+potenciaUtilizadaCC+" ------------------");
                        System.out.println("------ Potencia CA: "+potenciaUtilizadaCA+" ------------------");

                        CalculadoraOffGrid calculadoraOffGrid = new CalculadoraOffGrid();
                        // Insere as informações que já temos no objeto
                        calculadoraOffGrid.setNomeCidade(variavelGlobal.getNomeCidade());
                        // Insere a potencia
                        calculadoraOffGrid.setPotenciaUtilizadaDiariaCC(potenciaUtilizadaCC);
                        calculadoraOffGrid.setPotenciaUtilizadaDiariaCA(potenciaUtilizadaCA);
                        // Cria os vetores de Cidade e Estado
                        calculadoraOffGrid.setVetorCidade(CreateVetorCidade(variavelGlobal.getIdCity(), variavelGlobal.getNomeEstado()));
                        calculadoraOffGrid.setVetorEstado(CreateVetorEstado(calculadoraOffGrid.getVetorCidade()));
                        // Calcular
                        calculadoraOffGrid.Calcular(PedirConsumoEnergeticoActivity.this);
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

    // Ao voltar da Activity AdicionarEquipametos, aqui irá criar dinâmicamente objetos na tela
    @Override
    protected void onStart() {
        super.onStart();
        final Global variavelGlobal = (Global) getApplicationContext();

        Equipamentos meuEquipamento = new Equipamentos();
        Equipamentos meuEquipamento2 = new Equipamentos();
        Equipamentos meuEquipamento3 = new Equipamentos();
        // Adicionando as características para o equipamento
        meuEquipamento.setNome("Equipamento 1 - Alternada");
        meuEquipamento.setQuantidade(1);
        meuEquipamento.setPotencia(15);
        meuEquipamento.setHorasPorDia(3);
        meuEquipamento.setDiasUtilizados(4);
        meuEquipamento.setCC(false);
        variavelGlobal.adicionarElemento(meuEquipamento);
        meuEquipamento2.setNome("Equipamento 2 - Contínua");
        meuEquipamento2.setQuantidade(1);
        meuEquipamento2.setPotencia(60);
        meuEquipamento2.setHorasPorDia(2);
        meuEquipamento2.setDiasUtilizados(2);
        meuEquipamento2.setCC(true);
        variavelGlobal.adicionarElemento(meuEquipamento2);
        meuEquipamento3.setNome("Equipamento 3 - Contínua");
        meuEquipamento3.setQuantidade(1);
        meuEquipamento3.setPotencia(100);
        meuEquipamento3.setHorasPorDia(1.5);
        meuEquipamento3.setDiasUtilizados(7);
        meuEquipamento3.setCC(true);
        variavelGlobal.adicionarElemento(meuEquipamento3);

        // Esconder o texto de não ter adicionado nenhum equipamento ainda - Pensar melhor no que fazer
        TextoVisibilidade(variavelGlobal);

        if(variavelGlobal.getRemoverTodasViews()){ // quando o usuário editar um equipamento
            linearLayout.removeAllViews();
            i=0;
        }

        todosMeusEquipamentos = variavelGlobal.getEquipamentos();
        while (i < todosMeusEquipamentos.size()) {
            addView();
            i++;
        }
    }

    public void addView(){
        final View EquipamentosView = getLayoutInflater().inflate(R.layout.linha_add_elementos, null, false);
        final TextView textViewNomeE = (TextView)EquipamentosView.findViewById(R.id.nome_equipamento);
        final TextView textViewQntE = (TextView)EquipamentosView.findViewById(R.id.quantidade_equipamento);
        ImageView imageApagar = (ImageView)EquipamentosView.findViewById(R.id.apagar_equipamento);

        textViewNomeE.setText(todosMeusEquipamentos.get(i).getNome());
        textViewQntE.setText("Quantidade: "+todosMeusEquipamentos.get(i).getQuantidade());

        EquipamentosView.setOnClickListener(new View.OnClickListener() {
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
                // ----
                Intent intent = new Intent(getApplicationContext(), AdicionarEquipamentosActivity.class);
                intent.putExtra("posVariavelGlobal", j);
                startActivity(intent);
            }
        });

        imageApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextoVisibilidade(variavelGlobal);

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
            }
        });

        linearLayout.addView(EquipamentosView);
    }

    public void removerView(View view){ linearLayout.removeView(view); }

    public double demandaEnergiaAtivaDiaria(ArrayList<Equipamentos> vetorEquipamentos, boolean Icontinua){
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

    void TextoVisibilidade(Global variavelGlobal){
        if(variavelGlobal.getEquipamentos().size() != 0){
            textSemEquipamentos.setVisibility(View.GONE);
        }
        else{
            textSemEquipamentos.setVisibility(View.VISIBLE);
        }

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
