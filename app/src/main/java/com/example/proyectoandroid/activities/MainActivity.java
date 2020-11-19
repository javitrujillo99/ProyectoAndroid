package com.example.proyectoandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.MainAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.dialogs.DialogCrearPersonajeFragment;
import com.example.proyectoandroid.dialogs.DialogEditarPersonajeFragment;
import com.example.proyectoandroid.model.Personaje;
import com.example.proyectoandroid.model.Transformacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    //Variables
    private ListView listView;
    private List<Personaje> personajes;
    private MainAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton btnCrearPersonaje;
    private static final int REQUEST_CODE_FUNCTONE = 100;
    private SQLiteDatabase db;

    //La base de datos la creo estática para que pueda acceder a ella desde la otra activity
    public static DragonBallSQL dragonBallSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Agregamos el metodo para cambiar de activity
        irACaracteristicas(listView);

    }

    /**
     * Método para rellenar el activity con el listview
     */

    public void rellenarActivity() {
        //Creamos conexion con la base de datos
        db = dragonBallSQL.getWritableDatabase();

        //Insertamos el ListView en la activity
        listView = (ListView) findViewById(R.id.listView);

        //Creamos la lista de personajes que aparecerá en la vista
        this.personajes = dragonBallSQL.getListadoPersonajes();

        //Inyectamos el adapter
        adapter = new MainAdapter(this, R.layout.lista, personajes);
        listView.setAdapter(adapter);

        //Cerramos la conexion
        db.close();
    }


    /**
     * Método para cuando pulse en las caracteristicas
     */

    private void irACaracteristicas(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Personaje currentPersonaje = personajes.get(position);

                //Creamos el Intent explicito, y le decimos que vaya desde aqui hasta la activity 2
                Intent intent = new Intent(getApplicationContext(), ActivityPersonaje.class);

                //Insertamos el personaje que de la lista (importante que implemente el Serializable)
                intent.putExtra("personaje", currentPersonaje);

                //Lanzamos la activity (IMPORTANTE)
                startActivityForResult(intent, REQUEST_CODE_FUNCTONE);
            }
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


    /**
     * MENU

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
        btnCrearPersonaje = (FloatingActionButton) findViewById(R.id.floatingActionButtonCrearPersonaje);
        btnCrearPersonaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPersonaje();
            }
        });
    }

    /**
     * Creamos el personaje con el Dialog
     */
    private void crearPersonaje() {
        //Le paso la mainActivity para poder acceder al metodo rellenarActivity()
        DialogCrearPersonajeFragment dialog = new DialogCrearPersonajeFragment(this.adapter, this.dragonBallSQL, this);
        dialog.show(getSupportFragmentManager(), "DialogoCrearPersonaje");
    }

    /**
     * CONTEXT MENU
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //Recogemos la información con el adapter. Sin esto no sale bien el menú.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //Inflamos el context menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * Cuando pulsemos items del context menu
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        //Recogemos la información con el adapter. Sin esto no sale bien el menú.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //Este es el personaje actual que estamos pulsando
        Personaje currentPersonaje = personajes.get(info.position);

        //Creamos el switch con todas las opciones del context menu
        switch (item.getItemId()) {
            case R.id.editar:
                editarPersonaje(currentPersonaje);
                return true;
            case R.id.borrar:
                confirmarBorrarPersonaje(currentPersonaje);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Editamos el personaje
     * @param personaje
     */
    private void editarPersonaje(Personaje personaje) {
        //Le paso el personaje que vamos a editar y los parametros necesarios
        DialogEditarPersonajeFragment dialog = new DialogEditarPersonajeFragment(personaje, this.adapter, this, this.dragonBallSQL);
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
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dragonBallSQL.borrarPersonaje(p);
                rellenarActivity();

                //Creo un Toast para avisar de que se ha creado
                Toast.makeText(MainActivity.this, "Personaje " + p.getNombre() +
                        " borrado con éxito", Toast.LENGTH_SHORT).show();

                //Notificamos al adapter
                adapter.notifyDataSetChanged();
            }
        });

        //Creamos y mostramos el dialogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void pulsarImagen(View view, ImageView imagen) {
        //Al pulsar en la imagen:
        imagen = (ImageView) view.findViewById(R.id.nuevaFoto);


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo un intent para darme la opción para acceder a la galería
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //Le asigno el tipo
                intent.setType("image/");

                //Lanzo la orden
                startActivityForResult(intent.createChooser(intent, "Selecciona aplicación"), 10);
            }
        });
    }
}