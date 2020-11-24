package com.example.proyectoandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.MainAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.dialogs.DialogCrearPersonajeFragment;
import com.example.proyectoandroid.dialogs.DialogEditarPersonajeFragment;
import com.example.proyectoandroid.model.Personaje;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.proyectoandroid.R.*;


public class MainActivity extends AppCompatActivity {

    //Variables
    private ListView listView;
    private List<Personaje> personajes;
    private MainAdapter adapter;
    private static final int REQUEST_CODE_FUNCTONE = 100;

    //La base de datos la creo estática para que pueda acceder a ella desde la otra activity
    @SuppressLint("StaticFieldLeak")
    public static DragonBallSQL dragonBallSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        //Inicializamos la base de datos
        dragonBallSQL = new DragonBallSQL(this, "DragonBall.db", null, 1);

        //Reinicar base de datos
        dragonBallSQL.reiniciarDb("DragonBall.db");

        //Rellenamos el activity con el listview
        rellenarActivity();

        //Cuando pulse el botón flotante, que muestre el dialogo de crear personaje
        pulsarBotonFlotante();

        //Hacemos referencia al context menu para que lo muestre en pantalla
        registerForContextMenu(listView);

        //Agregamos la toolbar personalizada
        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        //Agregamos el metodo para cambiar de activity
        irACaracteristicas(listView);

    }

    /**
     * Método para rellenar el activity con el listview
     */

    public void rellenarActivity() {
        //Creamos conexion con la base de datos
        SQLiteDatabase db = dragonBallSQL.getWritableDatabase();

        //Insertamos el ListView en la activity
        listView = findViewById(id.listView);

        //Creamos la lista de personajes que aparecerá en la vista
        this.personajes = dragonBallSQL.getListadoPersonajes();

        //Inyectamos el adapter
        adapter = new MainAdapter(this, personajes);
        listView.setAdapter(adapter);

        //Cerramos la conexion
        db.close();
    }


    /**
     * Método para cuando pulse en las caracteristicas
     */

    private void irACaracteristicas(ListView listView) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Personaje currentPersonaje = personajes.get(position);

            //Creamos el Intent explicito, y le decimos que vaya desde aqui hasta la activity 2
            Intent intent = new Intent(getApplicationContext(), ActivityPersonaje.class);

            //Insertamos el id del personaje que pulsemos, luego cargaremos el personaje desde base de datos
            intent.putExtra("id", currentPersonaje.getId());

            //Lanzamos la activity (IMPORTANTE)
            startActivityForResult(intent, REQUEST_CODE_FUNCTONE);
        });
    }



    /**
     * Método para que salte error si falla algo en el cambio de Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Esto es por si hay algun error, nos dira donde esta el error
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FUNCTONE) {
            if (resultCode != RESULT_OK) {
                Exception e = new Exception();
                e.printStackTrace();
            }
        }
    }


    /*
      MENU

     Originalmente mi idea era hacer un botón de añadir personaje en el menú. Finalmente he hecho un botón flotante,
     me ha parecido una mejor opción.

     @Override public boolean onCreateOptionsMenu(Menu menu) {
     //Inflamos menu
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.menu, menu);
     return true;
     }

     /**
      * Cuando pulsemos items del menu

     @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     //Esto es para saber qué item está seleccionado en el menú. Lo mejor es hacer un switch
     switch (item.getItemId()) {
     default:
     return super.onOptionsItemSelected(item);
     }
     }

     */

    /**
     * Cuando pulse el boton flotante, que me cree un dialogo para crear personaje
     */

    private void pulsarBotonFlotante() {
        //Si pulsamos el boton de crear personaje, que muestre el dialogo
        FloatingActionButton btnCrearPersonaje = findViewById(id.floatingActionButtonCrearPersonaje);
        btnCrearPersonaje.setOnClickListener(v -> crearPersonaje());
    }

    /**
     * Creamos el personaje con el Dialog
     */
    private void crearPersonaje() {
        //Le paso la mainActivity para poder acceder al metodo rellenarActivity()
        DialogCrearPersonajeFragment dialog = new DialogCrearPersonajeFragment(this.adapter, dragonBallSQL, this);
        dialog.show(getSupportFragmentManager(), "DialogoCrearPersonaje");
    }

    /**
     * CONTEXT MENU
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //Inflamos el context menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * Cuando pulsemos items del context menu
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        //Recogemos la información con el adapter. Sin esto no sale bien el menú.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //Este es el personaje actual que estamos pulsando
        Personaje currentPersonaje = personajes.get(info.position);

        //Creamos el switch con todas las opciones del context menu
        switch (item.getItemId()) {
            case id.editar:
                editarPersonaje(currentPersonaje);
                return true;
            case id.borrar:
                confirmarBorrarPersonaje(currentPersonaje);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Editamos el personaje
     */
    private void editarPersonaje(Personaje personaje) {
        //Le paso el personaje que vamos a editar y los parametros necesarios
        DialogEditarPersonajeFragment dialog = new DialogEditarPersonajeFragment(personaje, this.adapter, this, dragonBallSQL);
        dialog.show(getSupportFragmentManager(), "DialogoEditarPersonaje");
    }


    /**
     * Método para confirmar borrar un personaje
     */
    private void confirmarBorrarPersonaje(final Personaje p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar personaje");
        builder.setMessage("¿Estás seguro de que desea eliminar el personaje?");
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            dragonBallSQL.borrarPersonaje(p);
            rellenarActivity();

            //Creo un Toast para avisar de que se ha creado
            Toast.makeText(MainActivity.this, "Personaje " + p.getNombre() +
                    " borrado con éxito", Toast.LENGTH_SHORT).show();

            //Notificamos al adapter
            adapter.notifyDataSetChanged();
        });

        //Creamos y mostramos el dialogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}