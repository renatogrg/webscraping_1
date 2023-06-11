package com.example.avance_sm_wb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);
        Content content = new Content();
        content.execute();
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();

            // Obtener el TextView y establecer el contenido del encabezado financiero
            TextView headerTextView = findViewById(R.id.headerTextView);
            if (!parseItems.isEmpty()) {
                ParseItem headerItem = parseItems.get(0);
                headerTextView.setText(headerItem.getTitle());
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://finance.yahoo.com/";
                Document doc = Jsoup.connect(url).get();
                Element financeHeader = doc.selectFirst("div#Lead-3-FinanceHeader-Proxy");

                // Obtener el contenido del div Lead-3-FinanceHeader-Proxy
                String headerContent = financeHeader.text();
                Log.d("headerContent", headerContent);

                // Actualizar el contenido en parseItems (o en una variable adecuada) para mostrarlo en la interfaz de usuario
                parseItems.add(new ParseItem("", headerContent));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
