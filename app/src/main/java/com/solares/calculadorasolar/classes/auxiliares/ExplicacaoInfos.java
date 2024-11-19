package com.solares.calculadorasolar.classes.auxiliares;

import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.activity.MainActivity;

public class ExplicacaoInfos {

    ////////Pop ups das informações:
    public static void ShowPopUpInfo(Context MyContext, LinearLayout blackener, String titulo, String textoExplicacao){
        blackener.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.popup_mais_informacoes, blackener , false);
        PopupWindow pw;
        try{
            pw = new PopupWindow(rootView,(int)(MainActivity.larguraTela),(int)(MainActivity.alturaTela), true);
            pw.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
            pw.showAtLocation(blackener, Gravity.BOTTOM, 0, 0);

            pw.setOnDismissListener(new PopupWindow.OnDismissListener(){
                @Override
                public void onDismiss() {
                    blackener.setVisibility(View.GONE);
                }
            });


            //Mudar texto do Título
            TextView tituloPopup = rootView.findViewById(R.id.pInfo_titulo_info);
            AutoSizeText.AutoSizeTextView(tituloPopup, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
            tituloPopup.setText(titulo);

            //Mudar texto da explicacao
            TextView textExplicacao = rootView.findViewById(R.id.pInfo_texto_explicacao);
            AutoSizeText.AutoSizeTextView(textExplicacao, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
            textExplicacao.setText(textoExplicacao);

            //Criar maneiras de fechar o popup
            ImageView botaoFechar = rootView.findViewById(R.id.pInfo_button_xclose);
            botaoFechar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });
            rootView.findViewById(R.id.pInfo_background).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void ShowHint(LinearLayout blackener, ImageView imageInfo){
        //Mostra a dica
        imageInfo.setVisibility(View.VISIBLE);
        blackener.setVisibility(View.VISIBLE);

        //Prepara pra esconder a dica
        blackener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo.setVisibility(View.GONE);
                blackener.setVisibility(View.GONE);
            }
        });
    }

}
