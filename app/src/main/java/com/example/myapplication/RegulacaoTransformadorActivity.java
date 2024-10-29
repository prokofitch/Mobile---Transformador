package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegulacaoTransformadorActivity extends AppCompatActivity {

    private EditText inputVNL, inputVFL;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regulacao_transformador);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputVNL = findViewById(R.id.input_vnl);
        inputVFL = findViewById(R.id.input_vfl);
        tvResult = findViewById(R.id.tv_result);
        Button btnCalculate = findViewById(R.id.btn_calculate);

        btnCalculate.setOnClickListener(v -> calculateRegulation());
    }

    private void calculateRegulation() {
        if (inputVNL.getText().toString().isEmpty() || inputVFL.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira ambos os valores de tensão.", Toast.LENGTH_SHORT).show();
            return;
        }

        double vnl = Double.parseDouble(inputVNL.getText().toString());
        double vfl = Double.parseDouble(inputVFL.getText().toString());

        if (vnl < vfl) {
            Toast.makeText(this, "A tensão a vazio deve ser maior ou igual à tensão com carga.", Toast.LENGTH_SHORT).show();
            return;
        }

        double regulation = ((vnl - vfl) / vfl) * 100;

        tvResult.setText(String.format("Regulação: %.2f%%", regulation));
    }
}
