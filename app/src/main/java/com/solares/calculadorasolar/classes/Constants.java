package com.solares.calculadorasolar.classes;

import android.Manifest;

public class Constants {

    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Constantes para cálculo
    public static double ICMS = 0.25;
    public static double PIS = 0.0165;
    public static double COFINS = 0.0434;
    public static double CIP = 50;
    public static double TARIFF = 0.56228;
    public static double TD = 0.8;
    public static double COST_DISP = 50.0;
    public static double PERCENTUAL_COST_INSTALATION = 0.35;
    public static double LOSS_DIRT = 0.01;
    public static double DEPREC_PANELS = 0.007;
    public static double INVERTOR_TIME = 10;
    public static double IPCA = 0.03;
    public static double MAINTENANCE_COST = 0.01;
    public static double COST_OF_CAPITAL = 0.0715;
    public static double SELIC = 0.065;
    public static double TARIFF_CHANGE = 6;
    public static double PANEL_LIFE = 25;

    // Strings para passagem de parâmetros para outras activities
    public static final String EXTRA_CUSTO_REAIS = "com.solares.com.solares.calculadorasolar.EXTRA_CUSTO_REAIS";
    public static final String EXTRA_CONSUMO = "com.solares.com.solares.calculadorasolar.EXTRA_CONSUMO";
    public static final String EXTRA_POTENCIA = "com.solares.com.solares.calculadorasolar.EXTRA_POTENCIA";
    public static final String EXTRA_PLACAS = "com.solares.com.solares.calculadorasolar.EXTRA_PLACAS";
    public static final String EXTRA_AREA = "com.solares.com.solares.calculadorasolar.EXTRA_AREA";
    public static final String EXTRA_INVERSORES = "com.solares.com.solares.calculadorasolar.EXTRA_INVERSORES";
    public static final String EXTRA_CUSTO_PARCIAL = "com.solares.com.solares.calculadorasolar.EXTRA_CUSTO_PARCIAL";
    public static final String EXTRA_CUSTO_TOTAL = "com.solares.com.solares.calculadorasolar.EXTRA_CUSTO_TOTAL";
    public static final String EXTRA_GERACAO = "com.solares.com.solares.calculadorasolar.EXTRA_GERACAO";
    public static final String EXTRA_LUCRO = "com.solares.com.solares.calculadorasolar.EXTRA_LUCRO";
    public static final String EXTRA_TAXA_DE_RETORNO = "com.solares.com.solares.calculadorasolar.EXTRA_TAXA_DE_RETORNO";
    public static final String EXTRA_INDICE_LUCRATICVIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_INDICE_LUCRATICVIDADE";
    public static final String EXTRA_LCOE = "com.solares.com.solares.calculadorasolar.EXTRA_LCOE";
    public static final String EXTRA_TEMPO_RETORNO = "com.solares.com.solares.calculadorasolar.EXTRA_TEMPO_RETORNO";
    public static final String EXTRA_HORA_SOLAR = "com.solares.com.solares.calculadorasolar.EXTRA_HORA_SOLAR";
    public static final String EXTRA_ID_CIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_ID_CIDADE";
    public static final String EXTRA_CIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_CIDADE";

    //Indices Paineis
    public static final int iPANEL_NOME = 0;
    public static final int iPANEL_POTENCIA = 1;
    public static final int iPANEL_PRECO = 2;
    public static final int iPANEL_AREA = 3;
    public static final int iPANEL_QTD = 4;
    public static final int iPANEL_CUSTO_TOTAL = 5;
    public static final int iPANEL_COEFTEMP = 6;
    public static final int PANEL_NOCT = 7;


    //Indices Inversores
    public static final int iINV_INDICE = 0;
    public static final int iINV_PRECO = 1;
    public static final int iINV_POTENCIA = 2;
    public static final int iINV_QTD = 3;
    public static final int iINV_PRECO_TOTAL = 4;
    public static final int iINV_RENDIMENTO_MAXIMO = 5;

    //Indices costs[]
    public static final int iCOSTS_PARCIAL = 0;
    public static final int iCOSTS_TOTAL = 1;

}
