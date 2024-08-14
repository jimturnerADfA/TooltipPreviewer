package com.appdevforall.tooltippreviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.opencsv.CSVReader
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    private lateinit var openDocumentLauncher: ActivityResultLauncher<Intent>
    private lateinit var listView: ListView
    private var items: ArrayList<Pair<Pair<String, String>, String>> =
        ArrayList<Pair<Pair<String, String>, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        // Initialize the launcher for the open document intent
        openDocumentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        // Read the file content
                        readFileContent(uri)
                    }
                }
            }

        // Start file picker
        openDocument()
    }

    private fun openDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // You can specify the MIME type or filter for specific file types
        }
        openDocumentLauncher.launch(intent)
    }

    private fun readFileContent(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = CSVReader(inputStream.reader(Charset.defaultCharset()))
                val lines = reader.readAll()
                reader.close()

                for (line in lines) {
                    if (line.size >= 3) {
                        // Prepare data for ListView and tooltip
                        val row = line.take(3)
                        val firstTwo = Pair<String, String>(row[0], row[1])
                        val tooltip = row[2]
                        items.add(Pair<Pair<String, String>, String>(firstTwo, tooltip))

                        // Set up the ListView with the first two rows
                        val adapter =
                            CustomAdapter(
                                this,
                                items
                            )
                        listView.adapter = adapter

                        // Set up a tooltip for the third row
                        listView.setOnItemLongClickListener { _, _, position, _ ->
                            if (position == 1) {
                                Toast.makeText(this, tooltip, Toast.LENGTH_LONG).show()
                                true
                            } else {
                                false
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "CSV file does not contain enough rows",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

}


