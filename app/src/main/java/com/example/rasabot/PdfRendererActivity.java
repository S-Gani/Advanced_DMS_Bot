package com.example.rasabot;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfRendererActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_renderer);
        String PDF_FILE_NAME = getIntent().getStringExtra("pdfFileName");

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true); // Optional: Hide the zoom controls
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


        try {
            // Copy the PDF file from assets to internal storage
//            String pdfFilePath = copyPdfFromAssets(PDF_FILE_NAME);
            String pdfFilePath = copyPdfFromInternalStorage(PDF_FILE_NAME);
            // Render PDF pages to bitmaps
            List<Bitmap> bitmaps = renderPdfToBitmaps(pdfFilePath);
            // Convert bitmaps to Base64 strings
            List<String> imageStrings = convertBitmapsToBase64(bitmaps);
            // Display images in WebView
            displayImagesInWebView(imageStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String copyPdfFromInternalStorage(String pdfFileName) throws IOException {
        File inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/RasabotFolder/" + pdfFileName);
        FileInputStream inputStream = new FileInputStream(inputFile);
        File outputFile = new File(getFilesDir(), pdfFileName);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return outputFile.getAbsolutePath();
    }

    // when we access the file from the assets folder .
   /* private String copyPdfFromAssets(String pdfFileName) throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open(pdfFileName);
        File outputFile = new File(getFilesDir(), pdfFileName);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return outputFile.getAbsolutePath();
    }*/

    private List<Bitmap> renderPdfToBitmaps(String filePath) throws IOException {
        List<Bitmap> bitmaps = new ArrayList<>();
        PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY));
        for (int i = 0; i < renderer.getPageCount(); i++) {
            PdfRenderer.Page page = renderer.openPage(i);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE); // Set background color to white
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            bitmaps.add(bitmap);
            page.close();
        }
        renderer.close();
        return bitmaps;
    }

    private List<String> convertBitmapsToBase64(List<Bitmap> bitmaps) {
        List<String> imageStrings = new ArrayList<>();
        for (Bitmap bitmap : bitmaps) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            imageStrings.add(imageString);
        }
        return imageStrings;
    }

    private void displayImagesInWebView(List<String> imageStrings) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body style=\"background-color: white;\">");
        for (String imageString : imageStrings) {
            htmlBuilder.append("<img src=\"data:image/png;base64,");
            htmlBuilder.append(imageString);
            htmlBuilder.append("\" width=\"100%\" /><br>");
        }
        htmlBuilder.append("</body></html>");

        webView.setBackgroundColor(Color.WHITE); // Set WebView background color
        webView.loadDataWithBaseURL(null, htmlBuilder.toString(), "text/html", "utf-8", null);
    }

}


