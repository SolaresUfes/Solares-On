package com.solares.calculadorasolar.classes.auxiliares;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.activity.MainActivity;

public class ExplicacaoInfos {

    ////////Pop ups das informações:
    public static void ShowPopUpInfo(Context MyContext, LinearLayout blackener, String titulo, String textoExplicacao) {
        blackener.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.popup_mais_informacoes, blackener, false);

        // Create popup window with full width and height
        PopupWindow pw = new PopupWindow(
                rootView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
        );

        // Edge-to-edge behavior configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pw.setElevation(0);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setClippingEnabled(false);
        }

        pw.setOnDismissListener(() -> blackener.setVisibility(View.GONE));

        // Modern window insets handling
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            View content = v.findViewById(R.id.pInfo_background);
            if (content != null) {
                content.setPadding(
                        content.getPaddingLeft(),
                        content.getPaddingTop(),
                        content.getPaddingRight(),
                        insets.bottom > 0 ? insets.bottom : dpToPx(MyContext, 16)
                );
            }
            return WindowInsetsCompat.CONSUMED;
        });

        pw.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        pw.showAtLocation(blackener, Gravity.BOTTOM, 0, 0);

        // Title text
        TextView tituloPopup = rootView.findViewById(R.id.pInfo_titulo_info);
        AutoSizeText.AutoSizeTextView(tituloPopup, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        tituloPopup.setText(titulo);

        // Explanation text
        TextView textExplicacao = rootView.findViewById(R.id.pInfo_texto_explicacao);
        AutoSizeText.AutoSizeTextView(textExplicacao, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        textExplicacao.setText(textoExplicacao);

        // Close button with proper click handling
        ImageView botaoFechar = rootView.findViewById(R.id.pInfo_button_xclose);
        botaoFechar.setOnClickListener(v -> pw.dismiss());

        // Background click to dismiss
        View background = rootView.findViewById(R.id.pInfo_background);
        background.setOnClickListener(v -> pw.dismiss());
    }

    private static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
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
