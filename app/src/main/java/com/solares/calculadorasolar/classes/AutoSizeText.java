package com.solares.calculadorasolar.classes;

import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AutoSizeText {
    public static void AutoSizeTextView(TextView tv, int alturaTela, int larguraTela, float porcentagemDaTela){
        porcentagemDaTela /= 100.0f;
        float tamFonte = alturaTela*porcentagemDaTela;
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamFonte);
    }

    public static void AutoSizeButton(Button bt, int alturaTela, int larguraTela, float porcentagemDaTela){
        porcentagemDaTela /= 100.0f;
        float tamFonte = alturaTela*porcentagemDaTela;
        bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamFonte);
    }

    public static void AutoSizeEditText(EditText et, int alturaTela, int larguraTela, float porcentagemDaTela){
        porcentagemDaTela /= 100.0f;
        float tamFonte = alturaTela*porcentagemDaTela;
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamFonte);
    }
}
