package com.solares.calculadorasolar.classes;

public class Constants {

    // Constantes para cálculo
    public static double ICMS = 0.25; //É o valor usado pela maioria dos estados
    public static double PIS = 0.0120;
    public static double COFINS = 0.0553;
    public static double CIPMAX = 50; // 50 reais
    public static double CIPMIN = 8; //8 reais
    //public static double TARIFF = 0.56228;
    public static double TD = 0.8;
    public static double COST_DISP_MONOFASICO = 30.0;
    public static double COST_DISP_BIFASICO = 50.0;
    public static double COST_DISP_TRIFASICO = 100.0;
    public static double PERCENTUAL_COST_INSTALATION = 0.35;
    public static double LOSS_DIRT = 0.01; //Perda de geração devido a sujeira
    public static double DEPREC_PANELS = 0.007; //Perda de eficiência a cada ano
    public static double INVERTOR_TIME = 10;
    public static double IPCA = 0.03;
    public static double MAINTENANCE_COST = 0.01;
    public static double COST_OF_CAPITAL = 0.065; //Taxa Selic * Retorno Esperado
    public static double SELIC = 0.065;
    public static double TARIFF_CHANGE = 6;
    public static double PANEL_LIFE = 25;

    // Strings para passagem de parâmetros para outras activities
    public static final String EXTRA_CALCULADORAON = "com.solares.com.solares.calculadorasolar.EXTRA_CALCULADORAON";
    public static final String EXTRA_EMPRESAS = "com.solares.com.solares.calculadorasolar.EXTRA_EMPRESAS";

    //Indices Paineis
    public static final int iPANEL_NOME = 0;
    public static final int iPANEL_MARCA = 1;
    public static final int iPANEL_POTENCIA = 2;
    public static final int iPANEL_PRECO = 3;
    public static final int iPANEL_AREA = 4;
    public static final int iPANEL_QTD = 5;
    public static final int iPANEL_CUSTO_TOTAL = 6;
    public static final int iPANEL_COEFTEMP = 7;
    public static final int iPANEL_NOCT = 8;
    public static final int iPANEL_I = 9;



    //Indices Inversores
    public static final int iINV_NOME = 0;
    public static final int iINV_MARCA = 1;
    public static final int iINV_POTENCIA = 2;
    public static final int iINV_PRECO = 3;
    public static final int iINV_RENDIMENTO_MAXIMO = 4;
    public static final int iINV_QTD = 5;
    public static final int iINV_PRECO_TOTAL = 6;
    public static final int iINV_ID = 7;


    //Indices Cidades
    public static final int iCID_ESTADO = 0;
    //Indices Estados
    public static final int iEST_SIGLA = 0;
    public static final int iEST_TARIFA = 13;

    //Indices costs[]
    public static final int iCOSTS_PARCIAL = 0;
    public static final int iCOSTS_TOTAL = 1;

}
