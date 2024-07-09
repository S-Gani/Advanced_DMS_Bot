package com.example.rasabot;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

//import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.content.SharedPreferences;
//import android.graphics.drawable.BitmapDrawable;
//import android.nfc.Tag;
import android.os.Environment;
//import android.util.SparseArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
//import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
//import android.widget.EditText;
import android.widget.LinearLayout;
//import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.text.TextBlock;
//import com.google.android.gms.vision.text.TextRecognizer;
//import com.itextpdf.kernel.pdf.PdfDictionary;
//import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
//import com.itextpdf.kernel.pdf.PdfResources;
//import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
//import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
//import com.lowagie.text.pdf.PdfPage;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfReader;
//
//
//import net.sourceforge.tess4j.Tesseract;
//
//import org.apache.pdfbox.jbig2.Bitmap;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.xmlgraphics.image.loader.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private TextView chatTextView;
    private EditText messageEditText;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    int flag=0;
    private boolean matchFound=false;
//    private String response;
    private ImageButton setbtn;
    private File  folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setbtn = findViewById(R.id.settingsButton);

        // Settings button for changing the ipAddress
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter IP Address");

                // Retrieve the saved IP address from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String savedIpAddress = sharedPreferences.getString("ipAddress", "");

                // Create a LinearLayout to hold the EditText
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Create an EditText for the IP address
                final EditText ipAddressEditText = new EditText(MainActivity.this);
                ipAddressEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                ipAddressEditText.setHint("0.0.0.0"); // Set a placeholder
                // Set the saved IP address as the text of the EditText
                ipAddressEditText.setText(savedIpAddress);
                // Disable the EditText to make it read-only
                ipAddressEditText.setEnabled(false);
                layout.addView(ipAddressEditText);

                // Set the custom layout to the dialog
                builder.setView(layout);

                // Set the Edit and Save buttons
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle edit button click
                        ipAddressEditText.setEnabled(true);
                        // Do not dismiss the dialog
                    }
                });

                builder.setNegativeButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle save button click
                        String ipAddress = ipAddressEditText.getText().toString();

                        if(ipAddress != null && !ipAddress.isEmpty()) {
                            // Save the IP address to SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("ipAddress", ipAddress);
                            editor.apply();
                            // Disable the EditText to make it read-only
                            ipAddressEditText.setEnabled(false);
                            // Dismiss the dialog after saving
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter the IP address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Create and show the dialog
                final AlertDialog dialog = builder.create();
                dialog.show();

                // Override the OnClickListener for the Edit button to keep the dialog open
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle edit button click
                        ipAddressEditText.setEnabled(true);
                    }
                });
            }
        });


        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/RasabotFolder");
        if (!folder.exists()) {
            boolean success = folder.mkdirs();;
            if (success) {
                // Folder created successfully
                Log.d("ldoi","created successfully");
            } else {
                // Failed to create folder
                Log.d("ldoi","created notdone");
            }
        }
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Retrieve the saved IP address from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedIpAddress = sharedPreferences.getString("ipAddress", "");
        String RASA_SERVER_URL = "http://" + savedIpAddress + ":5005/webhooks/rest/webhook";
        Log.d(TAG, "main1, ipAddress is === " + RASA_SERVER_URL);
    }

   private void sendMessage() {
       final String message = messageEditText.getText().toString().trim();
       ProgressDialog prodia = new ProgressDialog(MainActivity.this);
       Handler handler = new Handler();
       if (!message.isEmpty()) {
           addMessageToChat(message, true);
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {

                   prodia.setMessage("Connecting to rasa..");
                   prodia.show();
               }
           }, 500); // 500 milliseconds (.5 seconds) delay

           SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
           RasaChatbot.sendMessage(MainActivity.this, message, sharedPreferences, new RasaChatbot.ChatbotResponseListener() {

               @Override
               public void onResponse(String response) {
                   Log.d(TAG, "resp1: " + response);
                   if (response != null) {
                       if (response.toLowerCase().contains("searching")) {
                           // Extracting the main pharases from the user message.
                           String mainPhrasesStr = response;
                           if (mainPhrasesStr != null) {
                               // Response is combined with phrases, so seperating by identifying the colon and get only pharases from it.
                               int colonIndex = mainPhrasesStr.indexOf(':');
                               if (colonIndex != -1) {
                                   mainPhrasesStr = mainPhrasesStr.substring(colonIndex + 1).trim();
                                   String[] mainPhrasesArray = mainPhrasesStr.split(", ");
                                   String keyword = "";
                                   for (String word : mainPhrasesArray) {
                                       // Display each word individually (e.g., using a TextView)
//                                   Log.d("RASA response", "the words are: " + word);
                                       keyword = keyword + " " + word;
                                   }
//                               Log.w("keys for search :", "Keyword"+keyword.trim());
                                   response = "I'm searching your " + "'" + keyword.replaceFirst("^\\s+", "") + "'" + " content..";
                                   startFileListActivity(keyword.trim());
                               } else {
//                               Log.e("RASA response", "Colon not found in response string");
                               }
                           } else {
//                           Log.e("RASA response", "Null response from Rasa server");
                           }
                       }
                       prodia.dismiss();
                       // Cancel the delayed operation
                       handler.removeCallbacksAndMessages(null);
                       addMessageToChat(response, false);
                   }
               }
           });
           
           
           messageEditText.getText().clear();
       }
   }
    private void startFileListActivity(String query) {
        // Before starting the search, show the progress bar
        ProgressDialog prodialog = new ProgressDialog(MainActivity.this);
        prodialog.setMessage("Please wait..");
        prodialog.show();

        // Perform the search operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform the search operation and get the list of matching PDF files
                List<String> pdfFiles = searchPdfFiles(query);

                // Once the search is completed, hide the progress bar and start PdfListActivity
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the progress bar
                        prodialog.dismiss();

                        // Start PdfListActivity with the list of matching PDF files
                        if (!pdfFiles.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, PdfListActivity.class);
                            intent.putStringArrayListExtra("pdfFiles", (ArrayList<String>) pdfFiles);
                            startActivity(intent);
                        } else {
                            // Show a message if no PDF files are found
                            Toast.makeText(MainActivity.this, "No PDF files found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private List<String> searchPdfFiles(String keyword) {
        // Before starting the search, show the progress bar
        List<String> matchingPdfFiles = new ArrayList<>();
//        ITesseract tesseract = new Tesseract();
        try {
            Log.d("error","entered into the file search");
//            File internalStorageDir = getFilesDir();
            File mainFolder = new File(folder.toURI());
//            Log.d("DirectoryCheck", "Directory path: " + internalStorageDir.getAbsolutePath());
            File[] files = mainFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Check if the file is a PDF
                    Log.d("FileSearch", "Processing file: " + file.getName());

                    if (file.getName().toLowerCase().endsWith(".pdf")) {
                        // Open the PDF file
                        FileInputStream inputStream = new FileInputStream(file);
                        PdfReader reader = new PdfReader(inputStream);
                        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(reader);
                        // Iterate over each page in the PDF
                        int numPages = pdfDocument.getNumberOfPages();
                        for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                            // Extract text from the current page
                            String pageText = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(pageNum));

                            // Check if the page text contains the keyword
                            if (pageText.toLowerCase().contains(keyword.toLowerCase())) {
                                // Add the file name to the list
                                matchingPdfFiles.add(file.getName());
                                matchFound = true;
                                // No need to continue searching other pages of this PDF
                                break;
                            }
                        }

                        // Close the PDF document
                        pdfDocument.close();
                    }
                }
            } else {
                // Directory is empty
                Log.d("FileCheck", "Directory is empty");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!matchFound) {
            matchingPdfFiles.add("No match found");
        }
        return matchingPdfFiles;


        /*try {
            // Get list of all files in the assets folder
            String[] files = getAssets().list("");
            if (files != null) {
                for (String file : files) {
                    // Check if the file is a PDF
                    if (file.toLowerCase().endsWith(".pdf")) {
                        // Open the PDF file
                        InputStream inputStream = getAssets().open(file);
                        PdfReader reader = new PdfReader(inputStream);
                        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(reader);
                        // Iterate over each page in the PDF
                        int numPages = pdfDocument.getNumberOfPages();
                        for (int pageNum = 1; pageNum <= numPages; pageNum++) {

//                            PDFTextStripper stripper = new PDFTextStripper();
//                            stripper.setStartPage(pageNum);
//                            stripper.setEndPage(pageNum);

                            // Extract text from the current page
                            String pageText = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(pageNum));

                            // Check if the page text contains the keyword
                            if (pageText.toLowerCase().contains(keyword.toLowerCase())) {
                                // Add the file name to the list
                                matchingPdfFiles.add(file);
                                matchFound=true;
                                // No need to continue searching other pages of this PDF
                                break;
                            }
//                            // Extract text from images using OCR
//                            List<PDImageXObject> images = pdfDocument.getPage(pageNum).getResources().getImages();
//                            for (PDImageXObject image : images) {
//                                String imageText = performOCR(image);
//                                if (imageText.toLowerCase().contains(keyword.toLowerCase())) {
//                                    matchingPdfFiles.add(file);
//                                    break;
//                                }
//                            }

                        }

                        // Close the PDF document
                        pdfDocument.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!matchFound) {
            matchingPdfFiles.add("No match found");
        }
        return matchingPdfFiles;*/
    }



    private void addMessageToChat(String message, boolean isUserMessage) {
        ChatMessage chatMessage = new ChatMessage(message, isUserMessage);
        adapter.addMessage(chatMessage);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
}

