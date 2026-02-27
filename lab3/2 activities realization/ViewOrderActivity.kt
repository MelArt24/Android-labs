package com.am24.lab3android

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.am24.lab3android.databinding.ActivityViewOrderBinding

class ViewOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewOrderBinding
    private lateinit var dataFile: DataFile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataFile = DataFile(this)

        refreshUI()

        binding.btnEdit.setOnClickListener {
            val editText = EditText(this)
            editText.setText(binding.tvResult.text)

            AlertDialog.Builder(this)
                .setTitle("Edit")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newText = editText.text.toString()
                    dataFile.saveToFile(Constants.DATA_FILENAME, newText)
                    refreshUI()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.btnClear.setOnClickListener {
            dataFile.clearFile(Constants.DATA_FILENAME)
            refreshUI()
        }

        binding.btnBack.setOnClickListener {
            finish()    // uses to close the current Activity
        }
    }

    private fun refreshUI() {
        val orderText = dataFile.readFile(Constants.DATA_FILENAME)
        if (orderText.isNullOrEmpty()) {
            binding.tvResult.text = "Your file is empty"
        } else {
            binding.tvResult.text = orderText
        }
    }
}