package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Equipamentos;

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
            this.mViewHolder.buttonAdicionar = findViewById(R.id.button_adicionar);

            this.mViewHolder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class ViewHolder{
        Button buttonAdicionar;
    }
}