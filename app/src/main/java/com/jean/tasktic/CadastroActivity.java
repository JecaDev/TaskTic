package com.jean.tasktic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.jean.tasktic.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnCriarConta.setOnClickListener(v -> validaDados());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

        private void validaDados() {
        String email = binding.editEmail.getText().toString().trim();
        String senha = binding.editSenha.getText().toString().trim();
        String confirmarSenha = binding.editConfirmarSenha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty() && !confirmarSenha.isEmpty()){
                if (senha.equals(confirmarSenha)){
                    binding.progressBar.setVisibility(View.VISIBLE);

                    criarContaFirebase(email, senha);
                }else {
                    Toast.makeText(this, "As senhas não coicidem", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Digite uma senha", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Digite o seu E-mail", Toast.LENGTH_SHORT).show();
        }
    }

    private void criarContaFirebase(String email, String senha){
        mAuth.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

            } else {

                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}