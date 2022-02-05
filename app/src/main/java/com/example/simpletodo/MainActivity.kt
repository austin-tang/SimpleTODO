package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onLongClickListener(position: Int) {
                // remove the item from the list
                listOfTasks.removeAt(position)
                // notify the adapter that the data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Set up button and input field
        findViewById<Button>(R.id.button).setOnClickListener {
            // grab the text the user has inputted into @id/addTaskField
            val userInputTask = inputTextField.text.toString()

            // add the text to the list of tasks: listOfTasks
            listOfTasks.add(userInputTask)

            // notify adapter input has been added
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // reset the text field
            inputTextField.setText("")

            saveItems()
        }
    }

    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}