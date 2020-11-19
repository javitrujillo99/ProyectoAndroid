package com.example.proyectoandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.TransformacionesAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.dialogs.DialogCrearTransformacionFragment;
import com.example.proyectoandroid.dialogs.DialogEditarTransformacionFragment;
import com.example.proyectoandroid.model.Personaje;
import com.example.proyectoandroid.model.Transformacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ActivityPersonaje extends AppCompatActivity {

    //Variables
    private ImageView imageViewFoto;
    private TextView textViewNombre;
    private TextView textViewDescripcion;
    private TextView textViewRaza;
    private TextView textViewAtaqueEspecial;
    private TextView textViewTransformaciones;
    private FloatingActionButton btnCrearTransformacion;
    private Personaje personaje;
    private List<Transformacion> transformaciones;
    private GridView gridView;
    private TransformacionesAdapter adapter;
    private static final int REQUEST_CODE = 100;
    private DragonBallSQL dragonBallSQL;
    private Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaje);

        //Inicializamos base de datos
        this.dragonBallSQL = MainActivity.dragonBallSQL;

        //Insertamos los datos del personaje
        rellenarCaracteristicas();

        //Insertamos las transformaciones
        actualizarTransformaciones();

        //Cuando pulse el botón flotante, que muestre el dialogo de crear personaje
        pulsarBotonFlotante();

        //Hacemos referencia al context menu para que lo muestre en pantalla
        registerForContextMenu(gridView);


    }

    /**
     * Metodo para rellenar las caracteristicas del personaje
     */

    private void rellenarCaracteristicas() {
        //Personaje obtenido por el intent
        Personaje p = (Personaje) getIntent().getSerializableExtra("personaje");

        //Insertamos el personaje pasado por el intent pero conectado a la base de datos
        this.personaje = dragonBallSQL.getPersonaje(p.getId());

        //Almacenamos las variables del layout
        imageViewFoto = findViewById(R.id.fotoCaracteristicas);
        textViewNombre = findViewById(R.id.nombreCaracteristicas);
        textViewRaza = findViewById(R.id.razaCaracteristicas);
        textViewAtaqueEspecial = findViewById(R.id.ataqueEspecialCaracteristicas);
        textViewDescripcion = findViewById(R.id.descripcionCaracteristicas);

        //Código para que se ponga la imagen ya sea integer o Uri
        if (personaje.getFotoCompleta().getClass().getSimpleName().equals("Integer")) {
            imageViewFoto.setImageResource((Integer) personaje.getFotoCompleta());
        } else {
            imageViewFoto.setImageURI((Uri) personaje.getFotoCompleta());
        }

        textViewNombre.setText(personaje.getNombre());
        textViewRaza.setText("Raza: " + personaje.getRaza());
        textViewAtaqueEspecial.setText("Ataque especial: " + personaje.getAtaqueEspecial());
        textViewDescripcion.setText(personaje.getDescripcion());
    }

    /**
     * Método para cuando se pulse atrás. Le paso los nuevos datos a la otra activity
     */

    public void pulsarAtras(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Método para insertar las transformaciones al gridview
     */

    public void actualizarTransformaciones() {
        //Insertamos el textView
        textViewTransformaciones = (TextView) findViewById(R.id.textViewTransformaciones);

        //Insertamos el gridview
        gridView = (GridView) findViewById(R.id.gridViewTransformaciones);

        //Insertamos la lista de transformaciones
        this.transformaciones = dragonBallSQL.getListadoTransformaciones(this.personaje);

        //Si este personaje no tienes transformaciones, que aparezca que no tiene en el textView
        if (this.transformaciones.size() == 0) {
            textViewTransformaciones.setText("ESTE PERSONAJE NO TIENE TRANSFORMACIONES");
        } else {
            textViewTransformaciones.setText("TRANSFORMACIONES");
        }

        //Inyectamos el adapter
        adapter = new TransformacionesAdapter(getApplicationContext(), R.layout.transformaciones, this.transformaciones);
        gridView.setAdapter(adapter);
    }


    /**
     * Cuando pulse el boton flotante, que me cree un dialogo para crear transformacion
     */

    private void pulsarBotonFlotante() {
        //Si pulsamos el boton de crear personaje, que muestre el dialogo
        btnCrearTransformacion = (FloatingActionButton) findViewById(R.id.floatingActionButtonCrearTransformacion);
        btnCrearTransformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTransformacion();
            }
        });
    }

    /**
     * Creamos la transformacion con el Dialog
     */

    private void crearTransformacion() {
        //Le paso al fragment el adapter, el personaje al que le vamos a insertar la transformacion,
        // la base de datos y la activity para acceder al metodo actualizarTransformaciones()
        DialogCrearTransformacionFragment dialog = new DialogCrearTransformacionFragment(this.adapter, this.personaje,
                this.dragonBallSQL, this);
        dialog.show(getSupportFragmentManager(), "DialogoCrearTransformacion");
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
        inflater.inflate(R.menu.context_menu_transformaciones, menu);
    }

    /**
     * Cuando pulsemos items del context menu
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        //Recogemos la información con el adapter. Sin esto no sale bien el menú.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //Este es el personaje actual que estamos pulsando
        Transformacion currentTransformacion = transformaciones.get(info.position);

        //Creamos el switch con todas las opciones del context menu
        switch (item.getItemId()) {
            case R.id.editarTransformacion:
                editarTransformacion(currentTransformacion);
                return true;
            case R.id.borrarTransformacion:
                confirmarBorrarTransformacion(currentTransformacion);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Editamos la transformacion
     */
    private void editarTransformacion(Transformacion currentTransformacion) {
        //Le paso la transformacion que vamos a editar y los parametros necesarios
        DialogEditarTransformacionFragment dialog = new DialogEditarTransformacionFragment(currentTransformacion, this.adapter, this, this.dragonBallSQL);
        dialog.show(getFragmentManager(), "DialogoEditarTransformacion");
    }

    /**
     * Confirmacion de borrar transformacion
     */
    private void confirmarBorrarTransformacion(Transformacion currentTransformacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar transformación");
        builder.setMessage("¿Estás seguro de que desea eliminar la transformación?");
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dragonBallSQL.borrarTransformacion(currentTransformacion);
                actualizarTransformaciones();

                //Creo un Toast para avisar de que se ha creado
                Toast.makeText(ActivityPersonaje.this, "Transformación " +
                        currentTransformacion.getNombre() + " borrada con éxito", Toast.LENGTH_SHORT).show();

                //Notificamos al adapter
                adapter.notifyDataSetChanged();
            }
        });

        //Creamos y mostramos el dialogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método para cuando se pulse la imagen completa, poder cambiarla desde la galería
     */
    public void pulsarImagenCompleta(View view) {
        //Lanzamos la galería para editar la foto
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Le asigno el tipo
        intent.setType("image/");

        //Lanzo la orden
        startActivityForResult(intent.createChooser(intent, "Selecciona aplicación"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //Si está bien
            //Creamos una URI con los datos recogidos de la galería
            this.path = data.getData();

            //Asignamos la foto al imageView con esa URI
            imageViewFoto.setImageURI(path);

            //Editamos la foto completa en base de datos
            this.dragonBallSQL.editarFotoCompleta(path, this.personaje);
        }

    }

}