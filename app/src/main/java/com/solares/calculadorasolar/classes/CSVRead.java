package com.solares.calculadorasolar.classes;

import android.graphics.drawable.Icon;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Struct;


public class CSVRead {

    private static final String divider = ",";

    public static String[] getCity(int idCity, String stateName, InputStream is) {
        String[] values = new String[0];
        BufferedReader bufferedReader = null;
        int currentLine = 0;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            bufferedReader.readLine(); //Ignora a primeira linha (cabeçário)
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(divider);
                if (stateName.equals(values[0])) { //Se essa linha for a primeira linha do estado correto
                    currentLine = 0;
                    if (idCity == currentLine) { //Se for igual a zero (primeira cidade)
                        //Fechar o bufferedReader
                        try {
                            bufferedReader.close();
                        } catch (IOException ioex) {
                            ioex.printStackTrace();
                        }

                        return values;
                    }
                    currentLine++;
                    while ((line = bufferedReader.readLine()) != null) {
                        values = line.split(divider);
                        if (idCity == currentLine) {
                            //Fechar o bufferedReader
                            try {
                                bufferedReader.close();
                            } catch (IOException ioex) {
                                ioex.printStackTrace();
                            }

                            return values;
                        }
                        currentLine++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }

    public static String[] DefineSolarPanel(InputStream is, double WpNeeded, float AreaAlvo, int idModuloEscolhido) {
        String[] modulo;
        String line = "";
        int cont = 0;
        double precoTotal;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            bufferedReader.readLine();

            while (cont <= idModuloEscolhido) {
                cont++; //contador para escolher um modelo específico de módulo, se preciso
                if((line = bufferedReader.readLine()) == null) return null;
            }

            modulo = line.split(divider);
            //O custo aqui é definido por preçoTotal/potênciaTotal (R$/Wp)
            if (AreaAlvo == -1f) {
                modulo[Constants.iPANEL_QTD] = String.valueOf((int) Math.floor(WpNeeded / Double.parseDouble(modulo[Constants.iPANEL_POTENCIA])));
                if (modulo[Constants.iPANEL_QTD].equals("0")) {
                    modulo[Constants.iPANEL_QTD] = "1";
                }
            } else {
                modulo[Constants.iPANEL_QTD] = String.valueOf((int) Math.floor(AreaAlvo / Double.parseDouble(modulo[Constants.iPANEL_AREA])));
            }

            //O custo aqui é em dinheiro mesmo (quantidade de dinheiro "bruta")
            precoTotal = Double.parseDouble(modulo[Constants.iPANEL_QTD]) * Double.parseDouble(modulo[Constants.iPANEL_PRECO]);
            modulo[Constants.iPANEL_CUSTO_TOTAL] = String.valueOf(precoTotal);
            
            
            //Fechar o bufferedReader
            try {
                bufferedReader.close();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }

            return modulo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader se ocorreu algum erro
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        //Se der algum erro, retorna uma string vazia
        String[] error = {""};
        return error;

    }

    private static double FindPanelCost(double WpNeeded, @NotNull String[] dataPanel, float AreaAlvo) {
        int qtd;
        double precoTotal, potenciaTotal;

        if (AreaAlvo == -1f) { //Se não precisa se preocupar com a área:
            qtd = (int) Math.floor(WpNeeded / Double.parseDouble(dataPanel[Constants.iPANEL_POTENCIA]));
        } else { //Se a área máxima for definida
            qtd = (int) Math.floor(AreaAlvo / Double.parseDouble(dataPanel[Constants.iPANEL_AREA]));
        }
        precoTotal = qtd * Double.parseDouble(dataPanel[Constants.iPANEL_PRECO]);
        potenciaTotal = qtd * Double.parseDouble(dataPanel[Constants.iPANEL_POTENCIA]);
        return precoTotal / potenciaTotal;
    }

    public static String[] getState(String[] cityVec, InputStream is) {
        String stateName = cityVec[Constants.iCID_ESTADO];
        String[] values;
        String line;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            //Passar por todas as linhas do arquivo de estados
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(divider);
                if (stateName.equals(values[0])) { //Se essa linha for a do estado correto

                    //Fechar o bufferedReader
                    try {
                        bufferedReader.close();
                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                    }

                    //Retorna as informações do estado em um array de Strings
                    return values;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }

    public static String[] DefineInvertor(InputStream is, String[] solarPanel, int idInversorEscolhido) {
        String[] cheaperInvertor, currentInvertor;
        String line;
        double currentCost, cheaperCost, WpGenerated;
        int numberInvertors, cont = 0;
        BufferedReader bufferedReader = null;
        WpGenerated = Double.parseDouble(solarPanel[Constants.iPANEL_QTD]) * Double.parseDouble(solarPanel[Constants.iPANEL_POTENCIA]);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            bufferedReader.readLine(); //Joga fora a primeira
            line = bufferedReader.readLine();
            cheaperInvertor = line.split(divider);
            numberInvertors = (int) Math.ceil((0.8 * WpGenerated) / Double.parseDouble(cheaperInvertor[Constants.iINV_POTENCIA]));//Qtd de inversores
            cheaperCost = numberInvertors * Double.parseDouble(cheaperInvertor[Constants.iINV_PRECO]);
            cheaperInvertor[Constants.iINV_QTD] = String.valueOf(numberInvertors);
            cheaperInvertor[Constants.iINV_PRECO_TOTAL] = String.valueOf(cheaperCost);

            if (idInversorEscolhido == cont) { //Se o usuário escolheu o primeiro inversor
                //Fechar o bufferedReader
                try {
                    bufferedReader.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }

                return cheaperInvertor;
            }

            while ((line = bufferedReader.readLine()) != null) {
                cont++; //Contador para escolher um inversor específico
                currentInvertor = line.split(divider);
                numberInvertors = (int) Math.ceil((0.8 * WpGenerated) / Double.parseDouble(currentInvertor[Constants.iINV_POTENCIA]));//Qtd de inversores
                currentCost = numberInvertors * Double.parseDouble(currentInvertor[Constants.iINV_PRECO]);
                currentInvertor[Constants.iINV_QTD] = String.valueOf(numberInvertors);
                currentInvertor[Constants.iINV_PRECO_TOTAL] = String.valueOf(currentCost);

                if (idInversorEscolhido == cont) { //Se o usuário escolheu o cont° inversor
                    cheaperInvertor = currentInvertor;
                    break;
                }

                if (currentCost < cheaperCost) {
                    cheaperCost = currentCost;
                    cheaperInvertor = currentInvertor;
                }
            }

            //Fechar o bufferedReader
            try {
                bufferedReader.close();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }

            return cheaperInvertor;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        String[] error = {""};
        return error;
    }

    public static String[] getEquipamento(InputStream is, int idEquipamento) {
        String[] equipament = new String[0];
        BufferedReader bufferedReader = null;
        int currentLine = 0;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            bufferedReader.readLine(); //Ignora a primeira linha (cabeçário)

            while ((line = bufferedReader.readLine()) != null) {
                equipament = line.split(divider);
                if(idEquipamento == Integer.parseInt(equipament[Constants.iEQUI_ID])){
                    System.out.println(equipament);
                    return equipament;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }

    public static String[] DefineBattery(InputStream is, double CBI_C20, int Vsist, int idBateriaEscolhida){
        String[] cheaperBattery;
        String[] currentBattery;
        double currentCost = 0, cheaperCost;
        int nBatSerie=0, nBatParalelo=0,qntBat=0, cont = 0;
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new InputStreamReader(is));
            br.readLine(); //Joga fora a primeira
            line = br.readLine();

            cheaperBattery = line.split(divider);
            nBatSerie = (int)Math.round(Vsist/Double.parseDouble(cheaperBattery[Constants.iBAT_V_NOMINAL]));
            nBatParalelo = (int)Math.round(CBI_C20/Integer.parseInt(cheaperBattery[Constants.iBAT_CBI_BAT]));
            qntBat = nBatParalelo * nBatSerie;
            cheaperCost = Double.parseDouble(cheaperBattery[Constants.iBAT_PRECO_INDIVITUDAL]) * qntBat;

            if (idBateriaEscolhida == cont) { //Se o usuário escolheu o primeiro inversor
                //Fechar o bufferedReader
                try {
                    br.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
                return cheaperBattery;
            }

            while ((line = br.readLine()) != null) {
                cont++; //Contador para escolher um inversor específico
                currentBattery = line.split(divider);

                nBatSerie = (int)Math.round(Vsist/Double.parseDouble(currentBattery[Constants.iBAT_V_NOMINAL]));
                nBatParalelo = (int)Math.round(CBI_C20/Integer.parseInt(currentBattery[Constants.iBAT_CBI_BAT]));
                qntBat = nBatParalelo * nBatSerie;

                currentCost = Double.parseDouble(cheaperBattery[Constants.iBAT_PRECO_INDIVITUDAL]) * qntBat;
                currentBattery[Constants.iINV_QTD] = String.valueOf(qntBat);
                currentBattery[Constants.iINV_PRECO_TOTAL] = String.valueOf(currentCost);

                if (idBateriaEscolhida == cont) { //Se o usuário escolheu o cont° inversor
                    cheaperBattery = currentBattery;
                    break;
                }

                if (currentCost < cheaperCost) {
                    cheaperCost = currentCost;
                    cheaperBattery = currentBattery;
                }
            }
            return cheaperBattery;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
        String[] error = {""};
        return error;
    }

    public static String[] DefineInvertorOffGrid(InputStream is, String[] painelSolar, double S, double Vcc, int idInversorEscolhido) {
        String[] cheaperInvertor = new String[8];
        String[] invertor_i;
        double FDI = 0;
        double currentCost = 0, cheaperCost, potPico = 0;
        int numberInvertors, cont = 0, VinInvertor;
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new InputStreamReader(is));
            br.readLine(); //Joga fora a primeira
            line = br.readLine();

            do{
                potPico = Double.parseDouble(painelSolar[Constants.iPANEL_QTD]) * Double.parseDouble(painelSolar[Constants.iPANEL_POTENCIA]);
                FDI = Double.parseDouble(cheaperInvertor[Constants.iINV_POTENCIA]) * potPico;
                cheaperInvertor = line.split(divider);
                numberInvertors = (int) Math.ceil((0.8 * potPico) / Double.parseDouble(cheaperInvertor[Constants.iINV_POTENCIA]));//Qtd de inversores
                cheaperCost = numberInvertors * Double.parseDouble(cheaperInvertor[Constants.iINV_PRECO]);
                cheaperInvertor[Constants.iINV_QTD] = String.valueOf(numberInvertors);
                cheaperInvertor[Constants.iINV_PRECO_TOTAL] = String.valueOf(cheaperCost);
                VinInvertor = Integer.parseInt(cheaperInvertor[Constants.iINV_PRECO]);// trocar esse iINT_PRECO pelo local em que o Vin vai ficar
            }while(Vcc != VinInvertor);


            if (idInversorEscolhido == cont) { //Se o usuário escolheu o primeiro inversor
                //Fechar o bufferedReader
                try {
                    br.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }

                if (0.75 <= FDI && FDI >= 0.85) {
                    /* --- COLOCAR UM WORNING DE BAIXO DESEMPENHO --- */
                }
                return cheaperInvertor;
            }

            while ((line = br.readLine()) != null)  {
                invertor_i = line.split(divider);
                numberInvertors = (int)Math.ceil((0.8*potPico)/Double.parseDouble(invertor_i[Constants.iINV_POTENCIA]));//Qtd de inversores
                currentCost = numberInvertors * Double.parseDouble(invertor_i[Constants.iINV_PRECO]);
                invertor_i[Constants.iINV_QTD] = String.valueOf(numberInvertors);
                invertor_i[Constants.iINV_PRECO_TOTAL] = String.valueOf(currentCost);
                FDI = Double.parseDouble(cheaperInvertor[Constants.iINV_POTENCIA]) * potPico;

                if(idInversorEscolhido == cont){ //Se o usuário escolheu o cont° inversor
                    cheaperInvertor = invertor_i;
                    break;
                }

                if(currentCost<cheaperCost && (0.75 <= FDI && FDI >= 0.85)){
                    cheaperCost = currentCost;
                    cheaperInvertor = invertor_i;
                }

                cont++; //Contador para escolher um inversor específico
            }

            if(0.75 <= FDI && FDI >= 0.85){
                /* --- COLOCAR UM WORNING DE BAIXO DESEMPENHO --- */
            }

            return cheaperInvertor;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        String[] error = {""};
        return error;
    }

    public static String[] DefineChargeController(InputStream is,int Vbat, double Isc, int qntPanel, double P_pv, int potenciaPainel, int idControladorEscolhido){
        String[] simplerController=null;
        String[] controller_i;
        double currentCost = 0, Ic=1; // achar esse Ic
        int numberController, cont = 0;
        BufferedReader br = null;
        String line = "";

        try{
            br = new BufferedReader(new InputStreamReader(is));
            br.readLine();

            while((line = br.readLine()) != null){
                controller_i = line.split(divider);
                numberController = (int)Math.ceil(Ic/Double.parseDouble(controller_i[Constants.iCON_I_CARGA])); // descobrir essa Ic
                currentCost = numberController * Double.parseDouble(controller_i[Constants.iCON_PRECO_INDIVITUDAL]);

                if(idControladorEscolhido == cont){ //Se o usuário escolheu o cont° controlador
                    controller_i[Constants.iCON_QTD] = String.valueOf(numberController);
                    controller_i[Constants.iCON_PRECO_TOTAL] = String.valueOf(currentCost);
                    simplerController = controller_i;
                    break;
                }
                if(idControladorEscolhido == -1){
                    simplerController = simplestController(line, Vbat, qntPanel, 24, P_pv, potenciaPainel);
                    if(simplerController!=null) break;
                }
                cont++;
            }

            return simplerController;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        String[] error = {""};
        return error;
    }

    static String[] simplestController(String line, double Vbat, int qntPanel, double Voc_corrigida, double P_pv, int potenciaPainel){ //calcular ela previamente com algumas informacoes que ainda nao encontrei
        CalculadoraOffGrid calculadora = new CalculadoraOffGrid();
        int modSerie=0, modParal=0;
        int numberController=0, currentPowerBatController=0;
        double currentCost=0, Isc=0;//descobrir o que eh esse Isc
        int Vcontroller, Icontroller;
        double Ic=1; // descobrir o que eh esse Ic
        String[] currentController;

        try{
            currentController = line.split(divider);
            Icontroller = Integer.parseInt(currentController[Constants.iCON_I_CARGA]);
            Vcontroller = Integer.parseInt(currentController[Constants.iCON_V_MAX_SISTEMA]);

            // Calcular a quantidade de Móduloes em Série e Paralelo
            // TALVEZ NAO PRECISE -- modSerie = calculadora.numModulosSerie(Vcontroller, Voc_corrigida); // encontrar a tensao Voc_corrigida
            modParal = calculadora.numModulosParalelo(P_pv, modSerie, potenciaPainel);

            numberController = (int)Math.ceil(Ic/Icontroller); // descobrir o que eh essa Ic
            currentCost = numberController * Double.parseDouble(currentController[Constants.iCON_PRECO_INDIVITUDAL]);
            currentPowerBatController = Integer.parseInt(currentController[Constants.iCON_V_BATERIA]);

            if(currentPowerBatController >= Vbat && (Icontroller > (1.25 * modParal * Isc) )){// && (Vcontroller >= (Voc_corrigida * modSerie)) --> isso eh a mesma funcao para calcular a quantidade dos modulos em serie
                currentController[Constants.iCON_QTD] = String.valueOf(numberController);
                currentController[Constants.iCON_PRECO_TOTAL] = String.valueOf(currentCost);
                return currentController;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}