package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultadoTransformador extends AppCompatActivity {

    private ImageView imagem;

    private TextView zcc;
    private TextView req;
    private TextView xeq;
    private TextView rc;
    private TextView zphi;
    private TextView xm;
    private TextView rcp;
    private TextView xmp;
    private TextView iphip;
    private TextView ic;
    private TextView im;

    private TextView rp;
    private TextView xp;
    private TextView rs;
    private TextView xs;

    private TextView reqtotal;
    private TextView xeqtotal;
    private TextView regulacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultado_transformador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imagem = findViewById(R.id.imageView);

        zcc = findViewById(R.id.Np);
        req = findViewById(R.id.Req);
        xeq = findViewById(R.id.Xeq);
        rc = findViewById(R.id.Rc);
        zphi = findViewById(R.id.Zphi);
        xm = findViewById(R.id.Xm);
        rcp = findViewById(R.id.RcP);
        xmp = findViewById(R.id.XmP);
        iphip = findViewById(R.id.IphiP);
        ic = findViewById(R.id.Ic);
        im = findViewById(R.id.Im);

        rp = findViewById(R.id.Rp);
        xp = findViewById(R.id.Xp);
        rs = findViewById(R.id.Rs);
        xs = findViewById(R.id.Xs);

        reqtotal = findViewById(R.id.ReqTotal);
        xeqtotal = findViewById(R.id.XeqTotal);

        Bundle dados = getIntent().getExtras();
        assert dados != null;
        Double espirasPrimario = Double.parseDouble(dados.getString("espirasPrimario"));
        Double espirasSecundario = Double.parseDouble(dados.getString("espirasSecundario"));
        String tipoCircuito = dados.getString("tipoCircuito");
        String referencia = dados.getString("referencia");

        String tipoEnsaioA = dados.getString("tipoEnsaioA");
        String ladoEnsaioA = dados.getString("ladoEnsaioA");
        Double tensaoA = Double.parseDouble(dados.getString("tensaoA"));
        Double correnteA = Double.parseDouble(dados.getString("correnteA"));
        Double potenciaA = Double.parseDouble(dados.getString("potenciaA"));

        String tipoEnsaioB = dados.getString("tipoEnsaioB");
        String ladoEnsaioB = dados.getString("ladoEnsaioB");
        Double tensaoB = Double.parseDouble(dados.getString("tensaoB"));
        Double correnteB = Double.parseDouble(dados.getString("correnteB"));
        Double potenciaB = Double.parseDouble(dados.getString("potenciaB"));

        calcularParametros(espirasPrimario, espirasSecundario, tipoCircuito, referencia, tipoEnsaioA, ladoEnsaioA, tipoEnsaioB, ladoEnsaioB, tensaoA, correnteA, potenciaA, tensaoB, correnteB, potenciaB);
    }

    @SuppressLint("DefaultLocale")
    private void calcularParametros(
            Double n1, Double n2, String tipoCircuito, String referencia,
            String tipoA, String ladoA, String tipoB, String ladoB,
            Double vA, Double iA, Double pA, Double vB, Double iB, Double pB
    ) {
        Double a = n1 / n2;
        Double a2 = a * a;

        Double Zcc = 0.0, Req = 0.0, Xeq = 0.0;
        Double Rc = 0.0, Xm = 0.0, RcPrime = 0.0, XmPrime = 0.0;
        Double Ic = 0.0, Im = 0.0, IphiPrime = 0.0;
        Double Zphi = 0.0;

        if (tipoA.equals("Circuito Aberto") && ladoA.equals("Alta Tensão") && tipoB.equals("Curto-Circuito") && ladoB.equals("Baixa Tensão")) {

            Ic = pA / vA;

            Im = Math.sqrt(iA * iA - Ic * Ic);

            Rc = vA / Ic;
            Xm = vA / Im;

            IphiPrime = iA;

            Zphi = vA / iA;

            Zcc = vB / iB;

            Req = pB / (iB * iB);

            Xeq = Math.sqrt(Zcc * Zcc - Req * Req);

            Zcc *= a2;
            Req *= a2;
            Xeq *= a2;

            RcPrime = Rc;
            XmPrime = Xm;
        } else if (tipoA.equals("Curto-Circuito") && ladoA.equals("Baixa Tensão") && tipoB.equals("Circuito Aberto") && ladoB.equals("Alta Tensão")) {

            Zcc = vA / iA;

            Req = pA / (iA * iA);

            Xeq = Math.sqrt(Zcc * Zcc - Req * Req);

            Zcc *= a2;
            Req *= a2;
            Xeq *= a2;

            Ic = pB / vB;

            Im = Math.sqrt(iB * iB - Ic * Ic);

            Rc = vB / Ic;
            Xm = vB / Im;

            IphiPrime = iB;

            Zphi = vB / iB;

            RcPrime = Rc;
            XmPrime = Xm;
        } else if (tipoA.equals("Circuito Aberto") && ladoA.equals("Baixa Tensão") && tipoB.equals("Curto-Circuito") && ladoB.equals("Alta Tensão")) {
            Ic = pA / vA;

            Im = Math.sqrt(iA * iA - Ic * Ic);

            Rc = vA / Ic;
            Xm = vA / Im;

            RcPrime = Rc * a2;
            XmPrime = Xm * a2;

            IphiPrime = iA / a;

            Zphi = vA / iA;
            Zphi *= a2;

            Zcc = vB / iB;

            // Cálculo de Req e Xeq no primário
            Req = pB / (iB * iB);
            Xeq = Math.sqrt(Zcc * Zcc - Req * Req);

        } else if (tipoA.equals("Curto-Circuito") && ladoA.equals("Alta Tensão") && tipoB.equals("Circuito Aberto") && ladoB.equals("Baixa Tensão")) {

            Zcc = vA / iA;

            Req = pA / (iA * iA);

            Xeq = Math.sqrt(Zcc * Zcc - Req * Req);

            Ic = pB / vB;

            Im = Math.sqrt(iB * iB - Ic * Ic);

            Rc = vB / Ic;
            Xm = vB / Im;

            RcPrime = Rc * a2;
            XmPrime = Xm * a2;

            IphiPrime = iB / a;

            Zphi = vB / iB;
            Zphi *= a2;
        }

        zcc.setText(String.format("Zcc: %.2f Ω", Zcc));
        req.setText(String.format("Req: %.2f Ω", Req));
        xeq.setText(String.format("Xeq: %.2f Ω", Xeq));
        rc.setText(String.format("Rc: %.2f Ω", Rc));
        xm.setText(String.format("Xm: %.2f Ω", Xm));
        rcp.setText(String.format("Rc' (Referido ao Primário): %.2f Ω", RcPrime));
        xmp.setText(String.format("Xm' (Referido ao Primário): %.2f Ω", XmPrime));
        iphip.setText(String.format("Iφ' (Referido ao Primário): %.2f A", IphiPrime));
        ic.setText(String.format("Ic: %.2f A", Ic));
        im.setText(String.format("Im: %.2f A", Im));
        zphi.setText(String.format("Zφ: %.2f Ω", Zphi));

        Double Rp = 0.0, Xp = 0.0, Rs = 0.0, Xs = 0.0, ReqTotal = 0.0, XeqTotal = 0.0;

        switch (tipoCircuito) {
            case "T - Circuito em T":
                if (referencia.equals("Primário")) {
                    Rp = Req / 2;
                    Xp = Xeq / 2;
                    Rs = Rp;
                    Xs = Xp;
                } else if (referencia.equals("Secundário")) {
                    Rp = (Req / 2) / a2;
                    Xp = (Xeq / 2) / a2;
                    Rs = Rp;
                    Xs = Xp;
                }
                imagem.setImageResource(R.drawable.tipot);
                rp.setText(String.format("Rp: %.2f Ω", Rp));
                xp.setText(String.format("Xp: %.2f Ω", Xp));
                rs.setText(String.format("Rs: %.2f Ω", Rs));
                xs.setText(String.format("Xs: %.2f Ω", Xs));
                ((ViewGroup) reqtotal.getParent()).removeView(reqtotal);
                ((ViewGroup) xeqtotal.getParent()).removeView(xeqtotal);
                break;
            case "L - Circuito em L":
                if (referencia.equals("Primário")) {
                    Rp = Req;
                    Xp = Xeq;
                    ReqTotal = Rp;
                    XeqTotal = Xp + XmPrime;
                } else if (referencia.equals("Secundário")) {
                    Rp = Req / a2;
                    Xp = Xeq / a2;
                    ReqTotal = Rp;
                    XeqTotal = Xp + (XmPrime / a2);
                }
                imagem.setImageResource(R.drawable.tipol);
                reqtotal.setText(String.format("Req Total: %.2f Ω", ReqTotal));
                xeqtotal.setText(String.format("Xeq Total: %.2f Ω", XeqTotal));
                ((ViewGroup) rp.getParent()).removeView(rp);
                ((ViewGroup) xp.getParent()).removeView(xp);
                ((ViewGroup) rs.getParent()).removeView(rs);
                ((ViewGroup) xs.getParent()).removeView(xs);
                break;
            case "Série - Circuito em Série":
                if (referencia.equals("Primário")) {
                    ReqTotal = Req + RcPrime;
                    XeqTotal = Xeq + XmPrime;
                } else if (referencia.equals("Secundário")) {
                    ReqTotal = (Req / a2) + (RcPrime / a2);
                    XeqTotal = (Xeq / a2) + (XmPrime / a2);
                }
                imagem.setImageResource(R.drawable.serie);
                reqtotal.setText(String.format("Req Total: %.2f Ω", ReqTotal));
                xeqtotal.setText(String.format("Xeq Total: %.2f Ω", XeqTotal));
                ((ViewGroup) rp.getParent()).removeView(rp);
                ((ViewGroup) xp.getParent()).removeView(xp);
                ((ViewGroup) rs.getParent()).removeView(rs);
                ((ViewGroup) xs.getParent()).removeView(xs);
                break;
        }

        Double Vfl = vA;
        Double If = iA;

        calcularRegulacao(Req, Xeq, Vfl, If);
    }


    private void calcularRegulacao(Double Req, Double Xeq, Double Vfl, Double If) {
        Double Vnl = Vfl + (Req * If + Xeq * If);
        Double regulacao = ((Vnl - Vfl) / Vfl) * 100;

        TextView regulacaoTextView = findViewById(R.id.regulacao);

        regulacaoTextView.setText(String.format("Regulação: %.2f%%", regulacao));
    }

}