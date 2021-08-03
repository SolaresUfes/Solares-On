package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.Task;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class CreditoActivity extends AppCompatActivity {

    Button bSim;
    Button bNao;
    TextView textTituloPopup;
    TextView textExplicacaoPopup;
    ConstraintLayout popUp;
    LinearLayout black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

        PedeAvaliacao();

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        //Pegando as shared preferences para verificar se já foi mostrado o pop up da pesquisa off grid
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        boolean popUpJaFoiMostrado = sharedPref.getBoolean(getString(R.string.SP_popUpJaFoiMostrado), false);

        //Instanciação dos views
        Button buttonInstagram = findViewById(R.id.button_instagram);
        AutoSizeText.AutoSizeButton(buttonInstagram, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        Button buttonSpotify = findViewById(R.id.button_spotify);
        AutoSizeText.AutoSizeButton(buttonSpotify, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        Button buttonRecalcular = findViewById(R.id.button_recalcular);
        AutoSizeText.AutoSizeButton(buttonRecalcular, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        TextView textConheca = findViewById(R.id.text_conheca);
        AutoSizeText.AutoSizeTextView(textConheca, MainActivity.alturaTela, MainActivity.larguraTela, 3f);


        //Colocando as imagens nos image views
        //Imagem do instagram
        ImageView imageInstagram = findViewById(R.id.imageViewInstagram);
        imageInstagram.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.instagram_icon));

        //Imagem com o nome do Solares
        ImageView imageTitulo = findViewById(R.id.imageViewCredito);
        imageTitulo.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.logo_solares_horizontal_fundo_claro));

        //Imagem do Social
        ImageView imageSocial = findViewById(R.id.imageViewCirculoSocial);
        imageSocial.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.circulo_social));

        //Imagem do Barco
        ImageView imageBarco = findViewById(R.id.imageViewCirculoBarco);
        imageBarco.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.circulo_barco));

        //Imagem do Spotify
        ImageView imageSpotify = findViewById(R.id.imageViewSpotify);
        imageSpotify.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.icone_spotify));

        //Se o usuário clicar no ícone do instagram ou no botão com o @, ele é redirecionado para o instagram do solares
        imageInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/projeto.solares/?utm_source=SolaresOn&utm_medium=name&utm_campaign=app"));
                startActivity(intent);
            }
        });
        buttonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/projeto.solares/?utm_source=SolaresOn&utm_medium=icon&utm_campaign=app"));
                startActivity(intent);
            }
        });

        //Se o usuário clicar no ícone do spotfy ou no botão, ele é redirecionado para a playlist do podcast do solares
        imageSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com/show/6mJP4CnB8jMOgz66PzBXfc?si=DhYDOT9mTt-Cj8jg2LH6gA"));
                startActivity(intent);
            }
        });
        buttonSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com/show/6mJP4CnB8jMOgz66PzBXfc?si=DhYDOT9mTt-Cj8jg2LH6gA"));
                startActivity(intent);
            }
        });

        //Se o usuário clicar no botão recalcular, limpa-se todas as activities e volta à activity MainActivity
        buttonRecalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalizarActivity(calculadora);
            }
        });
    }


    private void PedeAvaliacao(){
        ReviewManager manager = ReviewManagerFactory.create(this);

        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();


                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task2 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, log and continue
                Objects.requireNonNull(task.getException()).printStackTrace();
            }
        });



    }

    public void FinalizarActivity(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, MainActivity.class);
        //Isso limpa as activities já abertas
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
