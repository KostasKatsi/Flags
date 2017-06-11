package com.example.kostas.flags;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> countryName = new ArrayList<>();
    private ArrayList<String> flag = new ArrayList<>();
    private ArrayList<String> population = new ArrayList<>();
    private List<Country> myCountries= new ArrayList<Country>();
    private JSONArray array = new JSONArray();
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       list = (ListView) findViewById(R.id.countryListView);

        new JSONParsing().execute("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");




    }

    private class MyListAdapter extends ArrayAdapter<Country>
    {
        private List<Country> countryList;
        private int resource;
        private LayoutInflater inflater;


        public MyListAdapter(Context context, int resource, List<Country> objects) {
            super(context, resource, objects);
            countryList= objects;
            this.resource= resource;
            inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.custom_item, parent, false);
            }

            Country currCountry = myCountries.get(position);


            new DownloadImageTask((ImageView) itemView.findViewById(R.id.item_icon))
                    .execute(currCountry.getIconURL());
            TextView country = (TextView) itemView.findViewById(R.id.countName);
            country.setText(currCountry.getName());
            TextView tag = (TextView) itemView.findViewById(R.id.tag);
            TextView population = (TextView) itemView.findViewById(R.id.population);
            population.setText(currCountry.getPopulation());


            return itemView;

        }
    }

    public String httpRequest(String params) {
        try {
            HttpURLConnection urlConnection;
            BufferedReader reader;

            URL url;
            url = new URL(params);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class JSONParsing extends AsyncTask<String,String,List<Country>>{

        @Override
        protected List<Country> doInBackground(String... params) {
            String buffer = httpRequest(params[0]);
            try {
                array = new JSONObject(buffer).getJSONArray("worldpopulation");
                try {
                    for(int i=0; i<array.length(); i++) {
                        JSONObject country = array.getJSONObject(i);
                        flag.add(country.getString("flag"));
                        countryName.add(country.getString("country"));
                        population.add(country.getString("population"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<countryName.size(); i++)
                {
                    myCountries.add(new Country(countryName.get(i), population.get(i), flag.get(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return myCountries;
        }

        @Override
        protected void onPostExecute(List<Country> result) {
            super.onPostExecute(result);
            MyListAdapter adapter = new MyListAdapter(getApplicationContext(), R.layout.custom_item, result);
            list.setAdapter(adapter);

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
