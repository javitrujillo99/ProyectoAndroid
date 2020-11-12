package com.example.proyectoandroid.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.TransformacionesAdapter;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Transformacion;

import java.util.List;


public class DialogCrearTransformacionFragment extends DialogFragment implements InterfazDialogFragment {

    private TransformacionesAdapter adapter;
    private List<Transformacion> transformaciones;
    private ImageView imagen;
    private EditText nombre;
    private Button btnAceptar;
    private Button btnCancelar;
    private Transformacion transformacion;


    /**
     * Constructor
     * @param adapter
     * @param transformaciones
     */
    public DialogCrearTransformacionFragment(TransformacionesAdapter adapter, List<Transformacion> transformaciones) {
        // En este caso, le pasamos el adapter y la lista, para añadir la transformacion creada a ella
        this.adapter = adapter;
        this.transformaciones = transformaciones;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_crear_transformacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagen = (ImageView) view.findViewById(R.id.nuevaFotoTransformacion);
        nombre = (EditText) view.findViewById(R.id.nuevoNombreTransformacion);

        pulsarAceptar(view);

        pulsarCancelar(view);
    }

    /**
     * Creo una transformacion nueva al pulsar aceptar con las caracteristicas insertadas
     */

    @Override
    public void pulsarAceptar(View view) {
        btnAceptar = (Button) view.findViewById(R.id.btnCrearTransformacion);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo una nueva transformacion
                //TODO: INSERTAR LA IMAGEN CON SU INT CORRESPONDIENTE
                transformacion = new Transformacion(nombre.getText().toString(), R.drawable.predeterminado);

                //Añado la transformacion a la lista
                transformaciones.add(transformacion);

                //Actualizamos el adapter
                adapter.notifyDataSetChanged();

                //Cerramos el dialogo
                dismiss();
            }
        });
    }

    /**
     * Cierro el dialogo sin guardar al pulsar cancelar
     */

    @Override
    public void pulsarCancelar(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelarTransformacion);
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