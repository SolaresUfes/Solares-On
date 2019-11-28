package com.solares.calculadorasolar.classes;

import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AutoSizeText {
    public static void AutoSizeTextView(TextView tv, int alturaTela, int larguraTela, float porcentagemDaTela){
        float tamFonte;
        porcentagemDaTela /= 100.0f;
        if(((float)alturaTela / larguraTela) > 2.0){
            tamFonte = alturaTela*porcentagemDaTela * 0.7f;
        } else {
            tamFonte = alturaTela*porcentagemDaTela;
        }

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamFonte);
    }

    public static void AutoSizeButton(Button bt, int alturaTela, int larguraTela, float porcentagemDaTela){
        float tamFonte;
        porcentagemDaTela /= 100.0f;
        if(((float)alturaTela / larguraTela) > 2.0){
            tamFonte = alturaTela*porcentagemDaTela * 0.7f;
        } else {
            tamFonte = alturaTela*porcentagemDaTela;
        }
        bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamFonte);
    }

    public static void AutoSizeEditText(EditText et, int alturaTela, int larguraTela, float porcentagemDaTela){
        float tamFonte;
        porcentagemDaTela /= 100.0f;
        if(((float)alturaTela / larguraTela) > 2.0){
            tamFonte = alturaTela*porcentagemDaTela * 0.7f;
        } else {
            tamFonte = alturaTela*porcentagemDaTela;
        }
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamFonte);
    }
}
