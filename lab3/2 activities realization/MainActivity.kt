package com.am24.lab3android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.am24.lab3android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sizeGroup = listOf(binding.cbSmall, binding.cbMedium, binding.cbLarge)
        val typeGroup = listOf(binding.cbPepperoni, binding.cbMargherita, binding.cbBBQChicken)

        sizeGroup.forEach { box ->
            box.setOnClickListener { sizeGroup.forEach { if (it != box) it.isChecked = false } }
        }
        typeGroup.forEach { box ->
            box.setOnClickListener { typeGroup.forEach { if (it != box) it.isChecked = false } }
        }

        binding.btnOrder.setOnClickListener {
            val result = StringBuilder("Your order:\n\n")
            var isValidOrder = true

            val selectedSize = sizeGroup.find { it.isChecked }?.text
            if (selectedSize == null) {
                Toast.makeText(this, "You must choose size for pizza", Toast.LENGTH_SHORT).show()
                isValidOrder = false
            } else {
                result.append("Size: $selectedSize\n")
            }

            val selectedType = typeGroup.find { it.isChecked }?.text
            if (selectedType == null) {
                Toast.makeText(this, "You must choose type for pizza", Toast.LENGTH_SHORT).show()
                isValidOrder = false
            } else {
                result.append("Type: $selectedType\n")
            }

            if (isValidOrder) {
                val extras = mutableListOf<String>()
                if (binding.cbTomatoes.isChecked) extras.add("Tomatoes")
                if (binding.cbPineapple.isChecked) extras.add("Pineapple")
                if (binding.cbCheese.isChecked) extras.add("Cheese")
                if (binding.cbMushrooms.isChecked) extras.add("Mushrooms")

                result.append("Extras: ${if(extras.isEmpty()) "none" else extras.joinToString(", ")}")

                val dataFile = DataFile(this)
                dataFile.saveToFile(Constants.DATA_FILENAME, result.toString())
            }
        }

        binding.btnOpen.setOnClickListener {
            val intent = Intent(this, ViewOrderActivity::class.java)
            startActivity(intent)
        }
    }
}
