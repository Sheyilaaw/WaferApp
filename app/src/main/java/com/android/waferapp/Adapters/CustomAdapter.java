package com.android.waferapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.waferapp.Models.Country;
import com.android.waferapp.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Country> ModelArrayList;

    public CustomAdapter(Context context, ArrayList<Country> ModelArrayList) {
        this.context = context;
        this.ModelArrayList = ModelArrayList;
    }

    public void remove(int position) {
        ModelArrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return ModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.list_item, null, true);
            }
            holder.tvname         =  convertView.findViewById(R.id.tv_country_name);
            holder.tvcurrency     =  convertView.findViewById(R.id.tv_country_currency);
            holder.tvlanguage     =  convertView.findViewById(R.id.tv_country_language);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvname.setText(ModelArrayList.get(position).getName());
        holder.tvcurrency.setText("Currency : "+ModelArrayList.get(position).getCurrency());
        holder.tvlanguage.setText("Langauge : "+ModelArrayList.get(position).getLanguage());
        return convertView;
    }

    private class ViewHolder {
        TextView tvname;
        TextView tvcurrency;
        TextView tvlanguage;
    }

}
