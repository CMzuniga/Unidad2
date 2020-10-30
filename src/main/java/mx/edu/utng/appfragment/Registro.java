package mx.edu.utng.appfragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnRegistra;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnIniciar;

    private String name = "";
    private String email = "";
    private String password = "";
    private String sexo = "";

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup);
        edtNombre = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_pass);
        btnRegistra = findViewById(R.id.btn_registrar);
        btnIniciar = findViewById(R.id.btn_login);

        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtNombre.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                int selectedId=radioSexGroup.getCheckedRadioButtonId();
                radioSexButton=(RadioButton)findViewById(selectedId);
                sexo = radioSexButton.getText().toString();
                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !sexo.isEmpty() ){
                    if(password.length() < 6){
                        Toast.makeText(Registro.this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    } else {
                        registrarUsuario();
                    }
                }else{
                    Toast.makeText(Registro.this, "Debe ingresar todos los datos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });
    }

    private void registrarUsuario(){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = firebaseAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);
                    map.put("sexo", sexo);
                    databaseReference.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(Registro.this, MainActivity.class));
                            }else{
                                Toast.makeText(Registro.this, "No se pudo crear los datos correctamente", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Registro.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(Registro.this, MainActivity.class));
            finish();
        }
    }
}