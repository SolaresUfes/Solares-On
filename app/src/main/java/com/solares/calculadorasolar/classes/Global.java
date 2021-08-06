package com.solares.calculadorasolar.classes;

import java.util.ArrayList;
import android.app.Application;

public class Global extends Application{
    private ArrayList<Equipamentos> meusEquipamentos = new ArrayList<>();

    /* --- Funcoes setter e getters --- */
    public void adicionarElemento(Equipamentos equipamentos){ meusEquipamentos.add(equipamentos); }

    private static Global instance;

    public static Global getInstance() {
        if (instance == null)
            instance = new Global();
        return instance;
    }
}
