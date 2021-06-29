package hanu.a2_1801040141;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import hanu.a2_1801040141.adapter.ProductAdapter;
import hanu.a2_1801040141.models.Product;

public class MainActivity extends AppCompatActivity {

    private static TextView productName;
    private static TextView productPrice;
    private static ImageView productImage;
    private EditText searchView;
    CharSequence search = "";

    private ProductAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productName = findViewById(R.id.itemName);
        productPrice = findViewById(R.id.itemPrice);

        searchView = findViewById(R.id.searchBox);

        // Load data
        String url = "https://mpr-cart-api.herokuapp.com/products";
        RestLoader restLoader = new RestLoader();
        restLoader.execute(url);

        // Search
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                adapter.getFilter().filter(charSequence);
                search = charSequence;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        startActivity(intent);
        return true;
    }

    public class RestLoader extends AsyncTask<String, Void, String>{

        private ArrayList<Product> list;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                Scanner sc = new Scanner(is);
                StringBuilder result = new StringBuilder();
                String line;
                while(sc.hasNextLine()){
                    line = sc.nextLine();
                    result.append(line);
                }
                Log.i("RESULT", "" + result);
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result == null){
                return;
            }

            list = new ArrayList<>();

            try {
                JSONArray array = new JSONArray(result);
                for(int i = 0; i < array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    Product product = new Product(obj);
                    list.add(product);

                }

                adapter = new ProductAdapter(list, MainActivity.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(gridLayoutManager);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}