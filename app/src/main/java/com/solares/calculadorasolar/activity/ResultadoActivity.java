package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class ResultadoActivity extends AppCompatActivity{

    public float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        try {

            Intent intent = getIntent();
            final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

            //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
            //Essa função pega as dimensões e as coloca em váriaveis globais
            GetPhoneDimensions(this);

            TextView textTituloResultado = findViewById(R.id.text_titulo_resultado);
            AutoSizeText.AutoSizeTextView(textTituloResultado, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
            Button buttonDados = findViewById(R.id.button_dados);
            AutoSizeText.AutoSizeButton(buttonDados, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonInstalacao = findViewById(R.id.button_instalacao);
            AutoSizeText.AutoSizeButton(buttonInstalacao, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonAnalise = findViewById(R.id.button_analise);
            AutoSizeText.AutoSizeButton(buttonAnalise, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonIndicesEconomicos = findViewById(R.id.button_indices_economicos);
            AutoSizeText.AutoSizeButton(buttonIndicesEconomicos, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonFinalizar = findViewById(R.id.button_finalizar);
            AutoSizeText.AutoSizeButton(buttonFinalizar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

            //Erro de valor pequeno
            final TextView textExplicacaoValor = findViewById(R.id.ACRtext_explicacao_popup);
            AutoSizeText.AutoSizeTextView(textExplicacaoValor, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            final ConstraintLayout layoutExplicacaoValor = findViewById(R.id.ACRlayout_pergunta);
            LinearLayout darkenerResultado = findViewById(R.id.ACRdarkener_resultado);

            //Verifica se a potencia necessária é baixa:
            if (calculadora.pegaPotenciaNecessaria() < 200) {
                // Make the explanation layout visible
                layoutExplicacaoValor.setVisibility(View.VISIBLE);

                // Apply window insets handling
                ViewCompat.setOnApplyWindowInsetsListener(layoutExplicacaoValor, (v, windowInsets) -> {
                    Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

                    // Adjust the bottom guideline to account for navigation bar
                    Guideline guideline45 = findViewById(R.id.guideline45);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline45.getLayoutParams();
                    params.guidePercent = 0.92f - (insets.bottom / (float) getResources().getDisplayMetrics().heightPixels);
                    guideline45.setLayoutParams(params);

                    return WindowInsetsCompat.CONSUMED;
                });

                // Set up click listeners
                darkenerResultado.setOnClickListener(view -> layoutExplicacaoValor.setVisibility(View.GONE));

                Button verResultados = findViewById(R.id.RESbutton_ver_resultados);
                verResultados.setOnClickListener(view -> layoutExplicacaoValor.setVisibility(View.GONE));

                Button saibaMais = findViewById(R.id.RESbutton_saiba_mais);
                saibaMais.setOnClickListener(view -> {
                    textExplicacaoValor.setText(R.string.saibaa_mais_valor);
                    saibaMais.setVisibility(View.GONE);
                });
            }

            buttonDados.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityDados(calculadora);
                }
            });

            buttonInstalacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityInstalacao(calculadora);
                }
            });

            buttonAnalise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityAnalise(calculadora);
                }
            });

            buttonIndicesEconomicos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityIndices(calculadora);
                }
            });

            buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinalizarCalculo(calculadora);
                }
            });

        } catch (Exception e){
            Log.i("ResultadoActivity", "Erro na captura de intents");
        }
    }

    public void AbrirActivityDados(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, DadosActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);

        startActivity(intent);
    }

    public void AbrirActivityInstalacao(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, InstalacaoActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void AbrirActivityAnalise(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, AnaliseActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void AbrirActivityIndices(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, IndicesEconomicosActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void FinalizarCalculo(CalculadoraOnGrid calculadora){ // aqui foi mudado o local do intent
        Intent intent = new Intent(this, EmpresasActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }
}





