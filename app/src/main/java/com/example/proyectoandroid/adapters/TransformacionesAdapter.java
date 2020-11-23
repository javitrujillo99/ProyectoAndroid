package com.example.proyectoandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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
    private final Context context;
    private final List<Transformacion> transformaciones;

    /**
     * Constructor
     */
    public TransformacionesAdapter(Context context, List<Transformacion> transformaciones) {
        this.context = context;
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

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Llamamos a la vista
        View v;

        //Inflamos el layout a nuestro contexto
        LayoutInflater inflater = LayoutInflater.from(this.context);
        v = inflater.inflate(R.layout.transformaciones, null);

        //Obtenemos la transformacion de cada recorrido de la lista
        Transformacion currentTransformacion = transformaciones.get(position);

        //Colocamos el nombre y la imagen
        ImageView fotoTransformacion = v.findViewById(R.id.imagenTransformacion);
        TextView nombreTransformacion = v.findViewById(R.id.nombreTransformacion);

        if (currentTransformacion.getFoto().getClass().getSimpleName().equals("Integer")) {
            fotoTransformacion.setImageResource((Integer) currentTransformacion.getFoto());
        } else {
            fotoTransformacion.setImageURI((Uri) currentTransformacion.getFoto());
        }
        nombreTransformacion.setText(currentTransformacion.getNombre());

        return v;
    }
}
