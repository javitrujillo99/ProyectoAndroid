package com.example.proyectoandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.Objects;

public class ActivityPersonaje extends AppCompatActivity {

    //Variables
    private ImageView imageViewFoto;
    private Personaje personaje;
    private List<Transformacion> transformaciones;
    private GridView gridView;
    private TransformacionesAdapter adapter;
    private static final int REQUEST_CODE = 100;
    private DragonBallSQL dragonBallSQL;

    //Este boolean lo creo porque tengo un onActivityResult aquí y en el DialogFragment que también
    //pertenece a esta activity, entonces me chocaban los 2 onActivityResult, y creando estos boolean
    //se soluciona
    private boolean isEditarFotoCompletaClicked = false;

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
        //Id del personaje obtenido por el intent
        int id = (int) getIntent().getSerializableExtra("id");

        //Obtenemos el personaje de la base de datos con el id pasado
        this.personaje = dragonBallSQL.getPersonaje(id);

        //Almacenamos las variables del layout
        imageViewFoto = findViewById(R.id.fotoCaracteristicas);
        TextView textViewNombre = findViewById(R.id.nombreCaracteristicas);
        TextView textViewRaza = findViewById(R.id.razaCaracteristicas);
        TextView textViewAtaqueEspecial = findViewById(R.id.ataqueEspecialCaracteristicas);
        TextView textViewDescripcion = findViewById(R.id.descripcionCaracteristicas);

        //Código para que se ponga la imagen ya sea integer o Uri
        if (personaje.getFotoCompleta().getClass().getSimpleName().equals("Integer")) {
            imageViewFoto.setImageResource((Integer) personaje.getFotoCompleta());
        } else {
            imageViewFoto.setImageURI((Uri) personaje.getFotoCompleta());
        }

        textViewNombre.setText(personaje.getNombre());
        textViewRaza.setText(String.format("%s%s", getString(R.string.insertarRaza), personaje.getRaza()));
        textViewAtaqueEspecial.setText(String.format("%s%s", getString(R.string.ataque), personaje.getAtaqueEspecial()));
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
        TextView textViewTransformaciones = findViewById(R.id.textViewTransformaciones);

        //Insertamos el gridview
        gridView = findViewById(R.id.gridViewTransformaciones);

        //Insertamos la lista de transformaciones
        this.transformaciones = dragonBallSQL.getListadoTransformaciones(this.personaje);

        //Si este personaje no tienes transformaciones, que aparezca que no tiene en el textView
        if (this.transformaciones.size() == 0) {
            textViewTransformaciones.setText(R.string.noTransformaciones);
        } else {
            textViewTransformaciones.setText(R.string.siTransformaciones);
        }

        //Inyectamos el adapter
        adapter = new TransformacionesAdapter(getApplicationContext(), this.transformaciones);
        gridView.setAdapter(adapter);
    }


    /**
     * Cuando pulse el boton flotante, que me cree un dialogo para crear transformacion
     */

    private void pulsarBotonFlotante() {
        //Si pulsamos el boton de crear personaje, que muestre el dialogo
        FloatingActionButton btnCrearTransformacion = findViewById(R.id.floatingActionButtonCrearTransformacion);
        btnCrearTransformacion.setOnClickListener(v -> crearTransformacion());
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


        //Inflamos el context menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_transformaciones, menu);
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
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            dragonBallSQL.borrarTransformacion(currentTransformacion);
            actualizarTransformaciones();

            //Creo un Toast para avisar de que se ha creado
            Toast.makeText(ActivityPersonaje.this, "Transformación " +
                    currentTransformacion.getNombre() + " borrada con éxito", Toast.LENGTH_SHORT).show();

            //Notificamos al adapter
            adapter.notifyDataSetChanged();
        });

        //Creamos y mostramos el dialogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método para cuando se pulse la imagen completa, poder cambiarla desde la galería
     */
    @SuppressLint("IntentReset")
    public void pulsarImagenCompleta(View view) {
        //Lanzamos la galería para editar la foto
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Le asigno el tipo
        intent.setType("image/");

        //Ponemos la bandera en true en el boolean de hacer click en esa imagen
        this.isEditarFotoCompletaClicked = true;

        //Lanzo la orden
        startActivityForResult(Intent.createChooser(intent, "Selecciona aplicación"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && isEditarFotoCompletaClicked) { //Si está bien
            //Creamos una URI con los datos recogidos de la galería
            Uri path = Objects.requireNonNull(data).getData();

            //Asignamos la foto al imageView con esa URI
            imageViewFoto.setImageURI(path);

            //Editamos la foto completa en base de datos
            this.dragonBallSQL.editarFotoCompleta(path, this.personaje);

            //Volvemos a poner el boolean en falso
            this.isEditarFotoCompletaClicked = false;
        }

    }

    /**
     * Asignamos permisos
     */
    public void asignarPermisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                    , 1000);
        }
    }

}