package com.solares.calculadorasolar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.StorageReference;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.entidades.Empresa;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.LinkedList;

public class EmpresasActivity extends AppCompatActivity {

    TextView textFacaOrcamento;
    private LinearLayout linearLayout;

    public static int larguraTela;
    public static int alturaTela;
    public float porcent = 4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        // Pegar as intent
        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

        LinkedList<Empresa> listaEmpresas = new LinkedList<>();
        //listaEmpresas = FirebaseManager.fbBuscaListaEmpresasPorEstado(EmpresasActivity.this, "ES");
        //Log.d("firebase", "Empresas Calculardoras: "+listaEmpresas.size());

        listaEmpresas = calculadora.pegaListaEmpresa();
        Log.d("firebase", "Empresas Calculardorwes: "+calculadora.pegaListaEmpresa().size());
        //listaEmpresas = FirebaseManager.fbBuscaListaEmpresasPorEstado(EmpresasActivity.this, calculadora.pegaVetorEstado()[Constants.iEST_SIGLA]);

        // Indicar que irá ter um tempo de espara para carregar as empresas
        Toast.makeText(EmpresasActivity.this, "Aguarde um momento!", Toast.LENGTH_SHORT).show();

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        TextView textTituloEmpresas = findViewById(R.id.text_titulo_empresas);
        AutoSizeText.AutoSizeTextView(textTituloEmpresas, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        textFacaOrcamento = findViewById(R.id.text_faca_orcamento);
        AutoSizeText.AutoSizeTextView(textFacaOrcamento, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        Button buttonFinalizar = findViewById(R.id.button_finalizar);
        AutoSizeText.AutoSizeButton(buttonFinalizar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        linearLayout = findViewById(R.id.LinearLayoutMostrarEmpresas);

        try {
            AdicionarEmpresa(listaEmpresas, calculadora);
        }catch (Exception e){
            e.printStackTrace();
        }

        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalizarCalculo(calculadora);
            }
        });
    }

    public void AdicionarEmpresa(LinkedList<Empresa> empresas, CalculadoraOnGrid calculadora){
       // System.out.println("Empresas: "+ empresas.size());
       // if(empresas.size()==0){
       //     textFacaOrcamento.setText("Não há empresas parceiras");
        //    Toast.makeText(EmpresasActivity.this, "Não há empresas parceiras próximas", Toast.LENGTH_SHORT).show();
        //    return;
        //}
        //for (Empresa empresa: empresas){
        //    addView(empresa);
        //}
        GetEmpresasFirebase(calculadora, EmpresasActivity.this);
    }

    public void addView(Empresa empresa){
        final View EquipamentosView = getLayoutInflater().inflate(R.layout.linha_mostrar_empresas, null, false);
        final TextView nomeEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewNome);
        final TextView siteEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewSite);
        final TextView telefoneEmpresa = (TextView)EquipamentosView.findViewById(R.id.TextViewTelefone);
        final ImageView logoEmpresa = (ImageView)EquipamentosView.findViewById(R.id.ImageViewLogo);

        // gambiarra adicionada
        //downloadLogo(empresa.nome, logoEmpresa);
        getBitmapFromURL("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBYWFRgVFhYYGRUZGBkcHBwcGBkaHBwcGhocGhoeGRwcIS4lHB4rIRoaJjgmKy8xNTU1GiQ7QDs0Py40NTEBDAwMBgYGEAYGEDEdFh0xMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMf/AABEIALcBEwMBIgACEQEDEQH/xAAcAAABBQEBAQAAAAAAAAAAAAAAAQIDBAUGBwj/xAA9EAACAQIEBAMFBgYBAwUAAAABAgADEQQSITEFQVFhInGBBhMykbEHQqHB0fAUUmJy4fEjM4LSFkOSssL/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAwDAQACEQMRAD8A8hhCEAhCEBREhCAQhHol4DIRSIkBYt42KIC3gDGwgPzQzRkeiEwFvJb5R3jA1uXikRMBxaNhCAkUGJCAQhNrg3sti8S2WlQci9izAogtvd2sL9hcwMWS0nA3+k6ev9nePVkQUQzOCfCwISxt420Vet7m/K83j9lLogarWZqjfDTo08+vO7MVFh1NoHmzH5RJ1WI9hMTTUvXajh0ubGtWRSQNrKmYk9hOaxNJVYqrq6j7yhgp8s4B+YgR3hmgIkB2aJeJCARREhAIQhAIQgIBCEIBCEkpKCdYDUQmPdwNBEc2uBt+7yOAQhCAQhCAQhCARyORGwgKxvrEhCARyqTsCbdATa5sNu5A9Ze4Nwitiay0KKXqMedwFA3Zj91Rz+W5AnuPA/YvD4dadPRzTb3rsRbPVGiOw5KozZF2FydSLwPPaP2b1hhPesGbFVWRaVFbAKGYFnqsdPhDabDmTsL9D7H65Xx4mmH8OiqzKLnx3JsTYXtYam2w1nsPvBHZ9LwOe4N7GYXDrTX3a1Gp5sruoJzMQWa22bQa7i06JVAFgLCMLiRYnFKgJJsBAnziLeZ1HG575dv5uXp184oxGtrwLb4ZGNyik9SoJ+ZE4T2w9hMEUfEMKiMSCzoXckkgCyKrDXQbACdgvEUJtfna0vo1xeB8rY3DGm7IdweutuV+htK8+m+KezOFrhi+HpZ3BBf3VMuL8wzKde5vPJvbn2FoYCh71alZ2ZlVL+7Crr4s53a42yga76QPPY+ml4Kl9eXWOdxsIEbCJCEAhCEAhCEAhCAgEIGEAhCEAEIRbQEhCKq3gJCKy23iQCEIQATufs89l/fF8XWRjRoAsi6D3tVfhXX4gDbTmSBrqJD7EezFSurV1GoVlpEgWV2ZE94b8kDu3W6C3Ke14DDJRpJQpgBKaqoHZRz/AKiTc+d4FL2Z4IuFpElVFepZqrLrdt8gJ1KrewJ1JuxuWJl2pcHTc6nvJHxI20J+krVySCAQDb1gRPimDbiw7ynjOMNcBLsewNhIsTh1GhJPZe/cy1QxORcqIAfwHrzMBtLiFS4XIwYi920AH8xG4Hpry5yv7s1mKu1rfd526uORI5b9YuPSq3gSoqFtWIW5bTTxHvb5SHD4GnQQ5qzkkanQ69dtfpA2aFJUGVdT5yx7tiLbX3MwsFXJIKvpt4rXPos6ClUAFrgmBUp8Ppo2Yav1J+nQS7SrE9JC5U/dJPYSZaltDYducC2rRMRRR1KOishFirAMD5g6GVs/cD1Empt3vA8v9uPs8diauEUNrrTVKFMAc8uULc+dz3nlWKwz03ZKiMjqbMrAqwO+oPafUmJo51KnYixuMwI6EHlPIfar7PGu9TC2dhqaavmY9bI/iWw5Znv0EDzOEfUpsrFHUqwNirAgg9CDqDGQCEIQCEI+mtz2gMhHOBfSNgEIQgEURDFEBwEeEggk9NLm0CL3d4pQL5yy/h06ys5gVyYkcwjYBCE1OB8AxGLfLQpl7WzN8KL/AHOdB1tueQMD2L2DxCJgaAuApUE9btob/wDcGnS0kzC4Nt29Lk79Npy+G9i6iUMjVQSFGVEuFQ3zGxJGbxX3A0J23lunxOnTVXdxmRHRtSR4TfYb67QNJ0cPbYW369ABJFATUkH1larxdCqP4mDrcWBNh5DWVqvtBhgcpdV6k+EfNuW+0DR9+CbAC/Tn5npHvp8TKo6AXM5ce3eGRnRnVsvwlLFTrtma3Y3tbpeS4j23wpCAKajvYqB8Ie+UqW0seYuNbj0DpBQBFySE6W1b+7n6Tn+N8BqVCGQZhb+Zs1/La3pOUxP2hYgPkNJKWwvmYnub3IC+QMsVfahsgzu4ZtQbn8GXlA0sJjEot7p0YVNiD8X+vwnSYd84F7heg39SNp5ynGHZ8xOboedv7j+s63hPHFYBVtm53YAfMmB1a1FRb8+ml/rKGJoVqh8IKD+YsPoOcdhsfTU3Z0+d9el7m8uHjlLYMLwMlOAMDc1WP785qYekyaZvneV6+KDahrnppK5xj7aCBvU6rdL+sz+NcQwyKP4ooinY1ApF/wCkkb+Ugw4cm+ceUvV8KtRClVFdGFirAMD6GB4Z7cUganvKdY16BJCuGSoE6IXWzLpqEYADW195yk9N9rPYRUL1MCxuQQ+HvdiLXZad9W0F8jXOlxewE8zIgJCEID0S8cz20WJUcaW6eUjgEIQgEIQgEURICBOklUyBDJgYCsZE5is0jZoDTEMDOy9hPZYYhv4isP8AgRtFP/uMP/yDv1OnWAnsZ7C1cWRUqXp4bfMdGfsg6f1bdL8vYcOlPDItHDoiqo0XOEHmTYliSNTuecgXGbKugGgA6DkOVphe0KPVY0jSJolf+ql89Nt8wI1YX3XmIGxiuI4vMVyUchGpFVgbdgVnnnFi6kqSMhJDC172tYaEfyjlNrhvC8dQYAv73Dn4XzXHbUG/odNJX4jhHNU2ve99b2v2H77wObr8Sqmn7kEimSCRc3Nr2Fxa4/MTKrIWGoJI5kn8J1pwd73A8ukqVcBpcgADS/pp562gcnicMRYhb2INtr25TUx3BKtd/e4ez03C7MBlIABDAnSx5b7y22HOoZRbcEXBFtrc9jt3jKPDyxOQG7EWAzJfkSxDWfbkPlyCrx5M1dEBDNTRVdhtfuf3vH4bAvkzDMVN8qqDdzci7dhOo4NwNS4UKrKB8IRlWzXHid1BI7DfmNrdOnAwqKhcDRQoAGbw99bi+vKBwOH4a6AFlYnqb2HqfrJk4cS2ZQOoG99tp3VLBA7qcw3uLfIkG47RWo5jpTsOx39bgAQOew2KzAIRlI01t+E08BgaeoVlRtySCNfpLuI4Ffx2At3v6DkPlMqpURWC5ivK1r6+n6QNj3CLozAnlYi3pYyGtTP3Fc9wY/D4VGsQ2um97dt/0mjkK7Eg/wBO3ygYYaspub2+Wk6LhWKLD4j85FSxQJysde/OT/wqE3UZW/AwKntbhEei7kAMF1flZTmAfUZbEXDXXKdQy7zxPj7q9QOHzlr+PTMwGhFYAD/lUi2e3jBVtDv7picd7uyO2TOciPa65jsGB+h3niXtdhKaVnKqtKoHK1KIN1DG7LUoHnSYa5TqpNtiIGN7gd4SvCAkIQgEDCEAhCKogJCPqJbnGQCODmNhAeWjYERIFjBYc1HRAbZja/Qbk+gBM9YoY1adNKaaKigAdhtPNOAmzs/QWHr/AKm8cWTzgdKvGGzWGpvtN/A8aRNWGvnofSebrXyDNe5Ol+hiHiZAvm7wPTn4gjHMgy3+JRse9uv763z8U92zZVOmxOmnW2087/8AUNT7hAHU/pIqnFajWDPcfy6BP/iND63gds2KdjlRad+gGY+gvr6SGvha5FnpPvuqOvp48ot+s5ynxiqUyiq4RTqEY0157Klhawj6YDtYs5bXVyxBsVte5NvCT+ltg1K/DH3CYjTXVUIHlapeVsHXSi12puTc3JVh4SPhtc212IPOaXDuD03zoiKXFgLkgWYspPn8Nhy35a3uGezFHILZScgdC2hBIyur+RGl9rwJ8Hxqk4IR2G3hylTbXcKd/LebyY0MilbhxoBqT0uL6el5Vw+ARGVkVLiwItrbtLuMfqluh2+fpAibiD2s7XboyWsf+2UamIzMGupPQOF/A2+ssPQUg+Fm7c/TtMqtQS97FfPa/nuPWBqHHMF2c3Bvdb6HobWt6+ko4bBszHKFZTr4gGZb/p2Al7BUwy+B2BG6ixPmCCNO+oknuWU3XNn6k6noeYPzgQPg3RsptY6hrEemt/rNTh1W4yOo0/ekz8RxMkFXVlba+XwnzG4Pl+MTDYtbi5Gb+76cx5GBp4/AAqSg210/SVuFcTA8D6i9jLtKvmG9x15+szsTgwX2FyL6bGBp8WwSVaTU3Geg4seq9CDyIOo8p4x7TcMZCKFVgatAhFfY1cO4LUm7lSGXyYD7s9f4dVZDke+RtPIzjPtWVMtMgAupGRwd0bMHX+oBlGm4PmRA80/gu8SL76JApwimJAIQgICqt5Loo6mDkAab/l3kJMBWN4kIQCEIQCEIQNHhh0bz/KXGq6amwG5/TvM7ANqR1H0/3JeJ6BV7k/LQfUwExONubKdOVwAfkDpKj1iecjgBAnoKWO+k3OEcONUhEGZvET0AW17zDptPYvsz4OtPDNiH+KsCAOiDb57wOO4fhFD5WGniU/L/AMST6HpNrB8LzE2+JNWXups1v7l18wRzW9z2g4ZkfOosrkn5ak+hlNOLlWRiLOoysRsyjRT520PkIGg+F924emcpdRqOtNswPobehMPfGnULoqkFr5TscwHvEboTa4PPfeGIxV1D6Z0YOttA6HRxbkcpvaJiXRWVhqjhdT0PwE/Iqe4gXGx9JxmQm4HjRvC6jrbZraai8uPxF8lwFcDTv5HvOexeFGYi2tro3MEcj++UqZnpnOjEbA229R0vA6mjiVdb0zkbcodV9QeUmp4ixs6WPz06rfcdj85ivUuq1ctnB1I+o6eUtpjdAr/AT4X/AJD0/t12ga4ooFL08tgddLWv16fhKWNxT5CyANl1t94DmL8xJErEam21mHIg/veUBiLXR/CdQri9j0/feBUX2lGgZA2trElXX8mEuPi6dQAOmVeRBF17g8xKlLBLUB+flLmEw+XwtqID8JWKMAr5l5bHbqJ0r0hUS40cajz/AMzkcRhMj3UEqe23abvB8Zm8GazLsevY/rA08PZlsw1/GeW/afQdK6At/wARUuo6NUIWp6XRT/3mer00Ofz3nn32u4ascjimGw6pq4Uko+bUORspGU7bjeB5j7sHZoS4tAWHiOw6dIQMSEIQCEWJAIQhAIpESEAhCEBYR7U7C8ZAfTcqQRylzidVXCFTrla45jUWv33+UowgJARwsN40mBcwKZnVNixAJ6DmflPY6XFVREpqPAiqFQaXttc8hznjfDT/AMi+f4bmdxh3IUuSb3vA6jiGNLjxWLcgNhpsPnOQxIux7C34zTwtQlcx0zDTsv8AN6yhxdbZVUb/AL1gQVeKZUyHkbX7G30uZLi8QTTC9Df0J8Q+djMf+HLtkGwOp8pcVSWAGwBH5fpA36GKLIt9SLWPUG1vrJ3YNdbaEEDz+IfSY2HqWsv9I/8AtaXazEOLb3B84Gzw5xkysPCfCZGtO2ZL6cvIbHziUnA1GxsR6x+Jpn408x6/v8YFzCErYHUWt6f6iY3C5QTbNTbccweoPIxnD8SH8J0+oPT5/WW69Swsx307X5QMrD1WpONcyMbg/UE8jL+L4gEIfLdDflpfcjTaV6yhqeZPunUdDt6dI+kgdChGhGxPOA4Y1WYZTvy5ayzhqYSoDyJIYdL2tMyngyhDDUXH3XJ+YFprcTw+dEqJ8Vht2gbtBWRwCSUOx6D/ABpLPtAG/hMRkUuxo1LKBcsSh0A6zPo4omkj28SMAw7HQ/USb2iqVv4Wo+Hv70ISq6b27wPntMxF9YR1MGwv+O/rCBlwkrqAO/8AuRQCEIQCEWJAIQhAJKiW1MdksLjeQs14Dne8EUnQC5kmGwzObAevKdNw7hYRQ4IZvKBk4fgdVhcjKO8nfg6KhZn+U6kU3bSzA257ekzsZhGyFtCt7Ecx6QONdbGwNxGvvJsXTyuR0kKoTqATA0uAIC5Y7KPxM3cVjBa1/wDM5rD4oohAHiY6+QjHxLOw8xA9A4fUzLc7W+m30v8AKNqtmLEDxWsD85l08VkQKN7TdwCZgPx5wH8M4XkQsdzIcBhB711tspI+Ym+z+AnZQLDvMLB4pVLuzbKdfIXgY+Ne1UhddQPxv+U08axDo/Ii3ruPrMbhJzvc87k+c6PiVD/iWBXxeKyNlvYMLr5HU/IqfnNThmOV6Yb7ux7TCOGaqig7pex8xqP33kXBaho1Cjf9NtCDy/xA65MIHa6EZxy6nkQZFxYs6MtvEAt+qsDrfpznJ4fjD4bE/F7xB4QL/EgJsL/zAG3pOlq8XV396lmUi50s1ujDYkfS2pgYWAds7BnN769+56zrcBRJA5/W35zj+MFCVxFIgrmyMt7Oji+45j9PKafBvalEASqrgqdGXXTuOkDvsPhha37+co16LIWX7jG46A8/KNwHtKjkhRnXqNPwIFj+H5OxPtLTVwjo2Vtj4SPreBuYVBYFrZSB8/3aJxGlZSVdk0PiFjboSGBBA7zB9oOMJSoqbnIxyG29mBsRbmOs53hX2iUnT3eK0OXKzW8LfdJFtrjX1gU+JfZxVqVXqfxSHOcxJQgknU3CtYa32hOT4rTxKVXWnUerTBGR1qizKQCp33sQD3BhA5om8SEIBCEIBCEIBCLEgE0eGcOzm7aJ9ZnibVPE5UFzp+XSBs4PBr9xbKNyZvYPIwyhgGHbSctgeJNayeE697joZNg+I5GuWAPlA6GvXW5RnAIGlt78r9pW41w41E98v/EyqCVBuGtz9ZdevQemSlg+W5IGpPSc/jeOn3ZDHxWIt2gcfWcsxY7kx6VWC2Bt5c/OR5Ta9o28AZrx9E+IecjhA1cHWzv27zsMNjAian8vlOI4Y9iSdgLy1xLH7Kp5CB23E+LhaCgHVhecolUv4b+EEn5/6mbjsdnCLfQKL/pLuGcKnn+/35wNTg1QLnJ+7r+NptcXxwZAF1t/5ETha2KYMcp0IAbyuJawOKGRxe5C/wCflA7ngddSttL2lbieHBF+htft0M5v2f4iQxBOv7/zN3+MzK+sDJxVME25AD0jqNY0CDuh3lXENlNxt06dQO0s4yoGoE9h+n5QKOJxC5yyEEFgdP5Wvoeh5SzRdG2nPgm+nlJ1xGU2O2mv1gdhwuuabhl2O47GaPG3IZD528t/1nMcExysArNra06oYhHREe2cEgd9Lgg8wRAx+L8YvSZHFyhVhY3BA5emvznLcVp0yEq0yMrXDLzVt9uh1k3FsZkruuX4WI33HK49ZjPa9wLD6doDs3YQjc0IDYsSEAhCEAhJRTFrnSRQCEIQFWTioT5dJBHK0DY4XTDOGdrAchpIeKqq1WAvaVcLXsbmabolZ2cmxK7X5gaWgXeDh8jPsqqbdzKnFxmRGuL28RkGGqMiAXPPTylPHYktt8JgV3fkNpFCEAhCEB6uQCBsd4yEIBLD4okWleECUVdLRtOoVvbmCD6xkIE2GrFGDDl9JtYPHbm+jAzn45XIFoGy1XMhI5SqmMORl87esr4fE5dDsZXc6wNDh6E3t++sZjFGpG/MbEeYMdwquFfXnt5xOKkFhb08oFOm5U3G4l9eMPkAuQ6MGRhy11U9RrM2EC1xDF+9qNUIsWsSBtcAA27G1/WVYRTASEIQCEIQCTKltT+7QhAjdrxsIQCEIQCOBHrFhAQNJqFQ3HbWEICV67EnWQQhAIQhAIQhAIQhALwhCAQhCAQhCAQhCBKqC1zGMxO5iwgMhCEBRACEIBeEIQP/2Q==");
        //

        nomeEmpresa.setText(empresa.getNome());
        siteEmpresa.setText(empresa.getSite());
        telefoneEmpresa.setText(empresa.getTelefone());

        AutoSizeText.AutoSizeTextView(nomeEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        AutoSizeText.AutoSizeTextView(siteEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);
        AutoSizeText.AutoSizeTextView(telefoneEmpresa, MainActivity.alturaTela, MainActivity.larguraTela, 2f);

        linearLayout.addView(EquipamentosView);
    }

    //
    //
    // Isso é apenas uma gambiarra para adicionar as empresas
    //
    //

    /*public void downloadLogo(String nomeEmpresa, ImageView logoEmpresa){
        String pathLogo = "LogoEmpresas/".concat(nomeEmpresa).concat(".png");
        //Download file in Memory
        StorageReference logo = storageReference.child(pathLogo);

        long MAXBYTES = 1024*1024;

        logo.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() { // Settar imagem baixada no ImageView
            @Override
            public void onSuccess(byte[] bytes) {
                // convert byte to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable image = new BitmapDrawable(getResources(), bitmap);
                logoEmpresa.setImageBitmap(bitmap);
                //logoEmpresa.setImageDrawable(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmpresasActivity.this, "Erro ao carregar logo", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void GetEmpresasFirebase(CalculadoraOnGrid calculadora, Context context){
        Log.d("firebase", "Firebase Database inicializado");
        // Get the reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinkedList<Empresa> empresas = new LinkedList<>();

        //Verify Connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if(!isConnected){
            //Tell the user that this function only works with internet connection
            Log.d("firebase", "Sem internet!");
        }

        // Read the companies from the city
        String estado = calculadora.pegaVetorEstado()[Constants.iEST_SIGLA];
        DatabaseReference dbReference = database.getReference("estados").child(estado);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                empresas.clear();

                //Get the names of the companies that work in the city
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String nome = snapshot.getValue(String.class);
                    if(nome != null){
                        empresas.add(new Empresa(nome));
                    }
                }

                ////////////////////////
                //Get the companies info
                DatabaseReference dbReferenceEmpresas = database.getReference("empresas");
                // Read from the empresas database
                dbReferenceEmpresas.addValueEventListener(new ValueEventListener() {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(Empresa empresa : empresas) {
                            Empresa empresaAtual = dataSnapshot.child(empresa.getNome()).getValue(Empresa.class);
                            if(empresaAtual != null){
                                Log.d("firebase", "Teste empresa" + empresaAtual.getTelefone());
                                empresa.CopyFrom(empresaAtual);
                            }
                        }

                        Log.d("firebase", "Empresas disponíveis em " + calculadora.pegaNomeCidade() + ", " + calculadora.pegaVetorEstado()[Constants.iEST_SIGLA]);
                        if(empresas.size() != 0){
                            for(Empresa empresa : empresas){
                                Log.d("firebase", empresa.getNome()+" - Telefone: " + empresa.getTelefone() + " Site: "+empresa.getSite());
                                addView(empresa);
                            }
                        }
                        else{
                            textFacaOrcamento.setText("Não há empresas parceiras em "+estado);
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.d("firebase", "Failed to read value." + error.toException());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("firebase", "Failed to read value." + error.toException());
            }
        });
    }
    //
    //
    //
    //
    //

    public static void GetPhoneDimensions(Activity activity){
        //Pegar dimensões
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        larguraTela = size.x;
        alturaTela = size.y;
    }

    public void FinalizarCalculo(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, CreditoActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }
}