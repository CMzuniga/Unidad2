package mx.edu.utng.appfragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Fragment f; // Para manejo de los fragments a utilizar
    boolean modoDia = true; // bandera indicadora del modo dia o noche
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.txt_name);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Gestor de fragments, por defecto aparece el dia
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contenedor, new DiaFragment()).commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        getUser();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";
                if(modoDia){
                    f = new NocheFragment();
                    msg = "Cambio a modo noche";
                } else {
                    f = new DiaFragment();
                    msg = "Cambio a modo día";
                }
                modoDia = !modoDia;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor, f).commit();
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
               /** Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // se obtiene el id de la opcion seleccionada
        String msg = "Serverless app";
        switch (id) {
            case R.id.action_que_es:
                Intent intentQueEs = new Intent(MainActivity.this, QueEsActivity.class);
                startActivity(intentQueEs);
                msg = "Qué es?";
                break;
            case R.id.action_mas_conocidos:
                Intent intentMas = new Intent(MainActivity.this, MasConocidosActivity.class);
                startActivity(intentMas);
                msg = "Más conocidos";
                break;
            case R.id.action_ventajas:
                Intent intenVent = new Intent(MainActivity.this, VentajasActivity.class);
                startActivity(intenVent);
                msg = "Ventajas";
                break;
            case R.id.action_desventajas:
                Intent intenDesv = new Intent(MainActivity.this, DesventajasActivity.class);
                startActivity(intenDesv);
                msg = "Desventajas";
                break;
            case R.id.action_diferencias:
                Intent intentDif = new Intent(MainActivity.this, DiferenciasActivity.class);
                startActivity(intentDif);
                msg = "Diferencias";
                break;
            case R.id.action_salir:
                firebaseAuth.signOut();
                Intent intentRegistro = new Intent(MainActivity.this, Registro.class);
                startActivity(intentRegistro);
                finish();
                break;
            case R.id.action_versus:
                Intent intentVersus = new Intent(MainActivity.this, VersusActivity.class);
                startActivity(intentVersus);
                msg = "Versus";
                break;
            case R.id.action_acerca:
                Intent intentAcerca = new Intent(MainActivity.this, AcercaActivity.class);
                startActivity(intentAcerca);
                msg = "Acerca de";
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private  void getUser(){
        String id = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombre = snapshot.child("name").getValue().toString();
                    name.setText("Bienvenid@ " + nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}