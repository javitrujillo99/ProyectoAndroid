package com.example.proyectoandroid.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.TransformacionesAdapter;
import com.example.proyectoandroid.dialogs.DialogCrearTransformacionFragment;
import com.example.proyectoandroid.model.Personaje;
import com.example.proyectoandroid.model.Transformacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaje);

        //TODO: insertar un sonido a cada personaje


        //Insertamos los datos del personaje
        rellenarCaracteristicas();

        //Insertamos las transformaciones
        insertarTransformaciones();

        //Cuando pulse el botón flotante, que muestre el dialogo de crear personaje
        pulsarBotonFlotante();



    }

    /**
     * Metodo para rellenar las caracteristicas del personaje
     */

    private void rellenarCaracteristicas() {
        //Insertamos el personaje pasado por el intent
        this.personaje = (Personaje) getIntent().getSerializableExtra("personaje");

        //Almacenamos las variables del layout
        imageViewFoto = findViewById(R.id.fotoCaracteristicas);
        textViewNombre = findViewById(R.id.nombreCaracteristicas);
        textViewRaza = findViewById(R.id.razaCaracteristicas);
        textViewAtaqueEspecial = findViewById(R.id.ataqueEspecialCaracteristicas);
        textViewDescripcion = findViewById(R.id.descripcionCaracteristicas);

        imageViewFoto.setImageResource(personaje.getFotoCompleta());
        textViewNombre.setText(personaje.getNombre());
        textViewRaza.setText("Raza: " + personaje.getRaza());
        textViewAtaqueEspecial.setText("Ataque especial: " + personaje.getAtaqueEspecial());
        textViewDescripcion.setText(personaje.getDescripcion());


    }

    /**
     * Método para cuando se pulse atrás. Le paso los nuevos datos a la otra activity
     */

    public void pulsarAtras(View view) {
        getIntent().getSerializableExtra("lista");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("personajeEditado", this.personaje);

        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Método para insertar las transformaciones al gridview
     */

    private void insertarTransformaciones() {
        //Insertamos el gridview
        gridView = (GridView) findViewById(R.id.gridViewTransformaciones);

        //Insertamos la lista de transformaciones
        this.transformaciones = personaje.getTransformaciones();
        adapter = new TransformacionesAdapter(getApplicationContext(), R.layout.transformaciones, transformaciones);
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
        DialogCrearTransformacionFragment dialog = new DialogCrearTransformacionFragment(this.adapter,
                this.transformaciones);
        dialog.show(getSupportFragmentManager(), "DialogoCrearTransformacion");
    }

}