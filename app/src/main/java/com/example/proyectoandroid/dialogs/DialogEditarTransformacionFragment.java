package com.example.proyectoandroid.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.activities.ActivityPersonaje;
import com.example.proyectoandroid.adapters.TransformacionesAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Transformacion;


public class DialogEditarTransformacionFragment extends DialogFragment implements InterfazDialogFragment {

    private Button btnAceptar;
    private Button btnCancelar;
    private ImageView imageView;
    private EditText nombre;
    private TransformacionesAdapter adapter;
    private Transformacion transformacion;
    private ActivityPersonaje activityPersonaje;
    private DragonBallSQL dragonBallSQL;


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

        imageView = (ImageView) view.findViewById(R.id.editarFotoTransformacion);
        nombre = (EditText) view.findViewById(R.id.editarNombreTransformacion);

        cargarCaracteristicas();

        pulsarAceptar(view);

        pulsarCancelar(view);
    }

    /**
     * Cargo las características de la transformacion
     */
    private void cargarCaracteristicas() {
        imageView.setImageResource(transformacion.getFoto());
        nombre.setText(transformacion.getNombre());
    }

    /**
     *
     * @param view
     */
    @Override
    public void pulsarAceptar(View view) {
        //Cuando pulse aceptar:
        btnAceptar = (Button) view.findViewById(R.id.btnEditarTransformacion);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cambiamos las caracteristicas de la transformacion a las escritas en el EditText
                transformacion.setNombre(nombre.getText().toString());

                //Actualizamos el personaje en la base de datos
                dragonBallSQL.editarTransformacion(transformacion);

                //Actualizamos la activity
                activityPersonaje.actualizarTransformaciones();

                //Actualizamos con el adapter
                adapter.notifyDataSetChanged();
                dismiss(); //Cerramos el dialogo
            }
        });
    }

    /**
     *
     * @param view
     */
    @Override
    public void pulsarCancelar(View view) {
        //Cuando pulse cancelar:
        btnCancelar = (Button) view.findViewById(R.id.btnCancelarEditarTransformacion);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * TODO: Insertar imagen desde la galeria
     */
    @Override
    public void pulsarImagen(View view) {

    }
}