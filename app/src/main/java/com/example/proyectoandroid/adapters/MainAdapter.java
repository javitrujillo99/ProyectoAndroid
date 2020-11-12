package com.example.proyectoandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.model.Personaje;

import java.util.List;

public class MainAdapter extends BaseAdapter {

    //Creamos un contexto, layout e inyectamos la lista
    private Context context;
    private int layout;
    private List<Personaje> personajes;


    /**
     * Constructor
     * @param context
     * @param layout
     * @param personajes
     */
    public MainAdapter(Context context, int layout, List<Personaje> personajes) {
        this.context = context;
        this.layout = layout;
        this.personajes = personajes;
    }

    @Override
    public int getCount() {
        return this.personajes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.personajes.get(position);
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
        v = inflater.inflate(R.layout.lista, null);

        //Obtenemos el personaje de cada recorrido de la lista
        Personaje currentPersonaje = personajes.get(position);

        //Colocamos cada atributo del personaje en su layout correspondiente
        ImageView foto = (ImageView) v.findViewById(R.id.foto);
        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        TextView descripcion = (TextView) v.findViewById(R.id.descripcion);

        foto.setImageResource(currentPersonaje.getFoto());
        nombre.setText(currentPersonaje.getNombre());
        descripcion.setText(currentPersonaje.getDescripcion());

        return v;
    }
}
