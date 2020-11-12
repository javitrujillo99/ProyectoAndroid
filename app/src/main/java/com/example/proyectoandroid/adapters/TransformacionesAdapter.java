package com.example.proyectoandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.model.Transformacion;


import java.util.List;

public class TransformacionesAdapter extends BaseAdapter {

    //Variables
    private Context context;
    private int layout;
    private List<Transformacion> transformaciones;
    private Transformacion currentTransformacion;

    /**
     * Constructor
     */
    public TransformacionesAdapter(Context context, int layout, List<Transformacion> transformaciones) {
        this.context = context;
        this.layout = layout;
        this.transformaciones = transformaciones;
    }

    @Override
    public int getCount() {
        return this.transformaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return this.transformaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Llamamos a la vista
        View v = convertView;

        //Inflamos el layout a nuestro contexto
        LayoutInflater inflater = LayoutInflater.from(this.context);
        v = inflater.inflate(R.layout.transformaciones, null);

        //Obtenemos la transformacion de cada recorrido de la lista
        currentTransformacion = transformaciones.get(position);

        //Colocamos el nombre y la imagen
        ImageView fotoTransformacion = (ImageView) v.findViewById(R.id.imagenTransformacion);
        TextView nombreTransformacion = (TextView) v.findViewById(R.id.nombreTransformacion);

        fotoTransformacion.setImageResource(currentTransformacion.getFoto());
        nombreTransformacion.setText(currentTransformacion.getNombre());

        return v;
    }
}
