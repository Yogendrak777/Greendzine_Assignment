package com.example.greendzine_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    private ListView lv;
    String id, firstName, lastName, email;

    private static String GIVEN_URL = "https://reqres.in/api/users?page=2";
    ArrayList<HashMap<String,String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        lv = findViewById(R.id.listview);
        searchView = (SearchView) findViewById(R.id.srcView);

        GetData getData = new GetData();
        getData.execute();


    }


    public class GetData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String CurrentStage = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(GIVEN_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();
                    while (data != -1) {
                        CurrentStage += (char) data;
                        data = inputStreamReader.read();
                    }
                    return CurrentStage;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
                return  CurrentStage;

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    id = jsonObject1.getString("id");
                    firstName = jsonObject1.getString("first_name");
                    lastName = jsonObject1.getString("last_name");
                    email = jsonObject1.getString("email");

                    HashMap<String, String> employee = new HashMap<>();
                    employee.put("fname", firstName);
                    employee.put("lname", lastName);
                    employee.put("idofEmployee", id);
                    employee.put("EmailofEmployee","Email id :"+ email);

                    list.add(employee);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, list,R.layout.row_of_employee,
                    new String[] {"fname","lname","idofEmployee","EmailofEmployee"},
                    new int[] {R.id.fname,R.id.lname,R.id.id,R.id.email});

           lv.setAdapter(adapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    adapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                    return false;
                }
            });



        }


    }
}