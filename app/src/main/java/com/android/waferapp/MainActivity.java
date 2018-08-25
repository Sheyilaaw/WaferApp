package com.android.waferapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.waferapp.Adapters.CustomAdapter;
import com.android.waferapp.Adapters.ListViewAdapter;
import com.android.waferapp.Models.Country;
import com.android.waferapp.Utils.HttpHandler;
import com.android.waferapp.Utils.SwipeToDismissTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<Country> modelArrayList;
    private static final String URL              = "https://restcountries.eu/rest/v2/all";
    private String TAG                           = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView        = findViewById(R.id.listview);
        modelArrayList  = new ArrayList<>();
        customAdapter   = new CustomAdapter(this,modelArrayList);

        new GetContacts().execute();

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onPendingDismiss(ListViewAdapter recyclerView, int position) {
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                customAdapter.remove(position);
                            }
                        });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    // looping through All Countries
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject countryObject = jsonArray.getJSONObject(i);
                        String name              = countryObject.getString("name");

                        // Getting JSON Array node
                        JSONArray countryLanguage = countryObject.getJSONArray("languages");
                        JSONArray countryCurrency = countryObject.getJSONArray("currencies");

                        JSONObject languageObject = countryLanguage.getJSONObject(0);
                        JSONObject currencyObject = countryCurrency.getJSONObject(0);

                        String language           = languageObject.getString("name");
                        String currency           = currencyObject.getString("name");
                        Country country           = new Country(name, language, currency);
                        modelArrayList.add(country);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            // Updating parsed JSON data into ListView
            customAdapter.notifyDataSetChanged();
            listView.setAdapter(customAdapter);
        }
    }

}
