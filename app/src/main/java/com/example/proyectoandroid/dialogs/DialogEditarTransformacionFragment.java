package com.example.proyectoandroid.dialogs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.activities.ActivityPersonaje;
import com.example.proyectoandroid.adapters.TransformacionesAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Transformacion;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


@SuppressWarnings("deprecation")
@SuppressLint("ValidFragment")
public class DialogEditarTransformacionFragment extends DialogFragment implements InterfazDialogFragment {

    private ImageView imagen;
    private EditText nombre;
    private final TransformacionesAdapter adapter;
    private final Transformacion transformacion;
    private final ActivityPersonaje activityPersonaje;
    private final DragonBallSQL dragonBallSQL;
    private Uri path;


    public DialogEditarTransformacionFragment(Transformacion transformacion, TransformacionesAdapter adapter, ActivityPersonaje activityPersonaje, DragonBallSQL dragonBallSQL) {
        //Le paso al constructor el personaje que queremos editar y el adapter para editarlo
        this.transformacion = transformacion;
        this.adapter = adapter;
        this.activityPersonaje = activityPersonaje;
        this.dragonBallSQL = dragonBallSQL;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_editar_transformacion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagen = view.findViewById(R.id.editarFotoTransformacion);
        nombre = view.findViewById(R.id.editarNombreTransformacion);

        cargarCaracteristicas();

        pulsarAceptar(view);

        pulsarCancelar(view);

        pulsarImagen(view);
    }

    /**
     * Cargo las características de la transformacion
     */
    private void cargarCaracteristicas() {
        //Depende del tipo que sea, ya que si insertamos desde galeria será tipo Uri
        if (transformacion.getFoto().getClass().getSimpleName().equals("Integer")) {
            imagen.setImageResource((Integer) transformacion.getFoto());
        } else {
            imagen.setImageURI((Uri) transformacion.getFoto());
        }
        nombre.setText(transformacion.getNombre());
    }

    /**
     *
     */
    @Override
    public void pulsarAceptar(View view) {
        //Cuando pulse aceptar:
        Button btnAceptar = view.findViewById(R.id.btnEditarTransformacion);
        btnAceptar.setOnClickListener(v -> {
            //Cambiamos las caracteristicas de la transformacion a las nuevas
            transformacion.setNombre(nombre.getText().toString());

            //En caso de que se haya elegido una de la galería la inserto
            if (this.path != null)
            transformacion.setFoto(path);

            //Actualizamos el personaje en la base de datos
            dragonBallSQL.editarTransformacion(transformacion);

            //Actualizamos la activity
            activityPersonaje.actualizarTransformaciones();

            //Creo un Toast para avisar de que se ha creado
            Toast.makeText(activityPersonaje, "Transformación " + nombre.getText().toString() + " editada con éxito", Toast.LENGTH_SHORT).show();


            //Actualizamos con el adapter
            adapter.notifyDataSetChanged();
            dismiss(); //Cerramos el dialogo
        });
    }

    /**
     *
     */
    @Override
    public void pulsarCancelar(View view) {
        //Cuando pulse cancelar:
        Button btnCancelar = view.findViewById(R.id.btnCancelarEditarTransformacion);
        btnCancelar.setOnClickListener(v -> dismiss());
    }


    /**
     * Insertar imagen desde la galeria
     */
    @SuppressLint("IntentReset")
    @Override
    public void pulsarImagen(View view) {
        //Al pulsar en la imagen:
        imagen = view.findViewById(R.id.editarFotoTransformacion);

        //Nos aseguramos de que los permisos están dados
        activityPersonaje.asignarPermisos();

        imagen.setOnClickListener(v -> {
            //Creo un intent para darme la opción para acceder a la galería
            @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //Le asigno el tipo
            intent.setType("image/");

            //Lanzo la orden
            startActivityForResult(Intent.createChooser(intent, "Selecciona aplicación"), 10);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //Si está bien
            //Creamos una URI con los datos recogidos de la galería
            this.path = Objects.requireNonNull(data).getData();

            //Asignamos la foto al imageView con esa URI
            imagen.setImageURI(path);
        }
    }

}