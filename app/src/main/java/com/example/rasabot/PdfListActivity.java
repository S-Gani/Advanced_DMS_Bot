package com.example.rasabot;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class PdfListActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView listView = findViewById(R.id.listView);

        // Retrieve the list of PDF files passed from MainActivity
        List<String> pdfFiles = getIntent().getStringArrayListExtra("pdfFiles");
        // Create an ArrayAdapter to display the list of PDF files
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pdfFiles);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        // Set item click listener to open selected PDF file
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPdfFile = pdfFiles.get(position);
//            Log.d("entered pdflistactivity",String.valueOf(selectedPdfFile));

            Intent intent = new Intent(PdfListActivity.this, PdfRendererActivity.class);
            intent.putExtra("pdfFileName", selectedPdfFile);
            startActivity(intent);
        });
//        Toast.makeText(getApplicationContext(),"ctrl is now File list Activity list.",Toast.LENGTH_SHORT).show();

    }

}
