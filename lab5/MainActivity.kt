package com.am24.compassapp

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    var sensorManager: SensorManager? = null
    var accelerometerSensor: Sensor? = null     // Акселерометр
    var magneticSensor: Sensor? = null          // Магнітометр

    lateinit var compassImage: ImageView
    lateinit var rotationTV: TextView
    lateinit var accuracyIndicator: View

    var currentDegree: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        compassImage = findViewById(R.id.imageView)
        rotationTV = findViewById(R.id.textView)
        accuracyIndicator = findViewById(R.id.accuracyIndicator)
    }

    // повернення до Activity
    override fun onResume() {
        super.onResume()
        accelerometerSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        magneticSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    // Коефіцієнт фільтрації
    private val ALPHA = 0.1f

    // Масиви для зберігання згладжених даних
    private var gravityFiltered = FloatArray(3)
    private var geomagneticFiltered = FloatArray(3)

    // Функція фільтрації
    private fun lowPass(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
        return output
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            gravityFiltered = lowPass(event.values.clone(), gravityFiltered)
        }

        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            val values = event.values
            // Обчислюємо силу поля за теоремою Піфагора для 3D
            val magnitude = sqrt(
                (values[0] * values[0] + values[1] * values[1] + values[2] * values[2]).toDouble()
            )

            // Оновлюємо колір, передаючи поточну силу поля
            updateAccuracyIndicator(event.accuracy, magnitude)

            geomagneticFiltered = lowPass(values.clone(), geomagneticFiltered)
        }

        // 3*3=9
        val rMatrix = FloatArray(9) // Rotation Matrix - головна матриця повороту
        val iMatrix = FloatArray(9) // Inclination Matrix - матриця нахилу
        val success = SensorManager.getRotationMatrix(rMatrix, iMatrix, gravityFiltered, geomagneticFiltered)

        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rMatrix, orientation)

            val azimuthInRadians = orientation[0]
            // Перетворення в градуси
            val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

            val rotationAnimation = RotateAnimation(
                currentDegree,      // старий кут
                -azimuthInDegrees,    // новий кут
                Animation.RELATIVE_TO_SELF, 0.5f, // центр повороту по X (50%)
                Animation.RELATIVE_TO_SELF, 0.5f  // центр повороту по Y (50%)
            )

            rotationAnimation.duration = 150
            rotationAnimation.fillAfter = true

            compassImage.startAnimation(rotationAnimation)
            currentDegree = -azimuthInDegrees

            val displayDegrees = (azimuthInDegrees + 360) % 360
            rotationTV.text = "${displayDegrees.toInt()}°"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            updateAccuracyIndicator(accuracy)
        }
    }

    private fun updateAccuracyIndicator(accuracy: Int, fieldStrength: Double = 50.0) {
        if (fieldStrength !in 15.0..150.0) {
            accuracyIndicator.setBackgroundColor(Color.RED)
            return
        }

        when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> accuracyIndicator.setBackgroundColor(Color.GREEN)
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> accuracyIndicator.setBackgroundColor(Color.YELLOW)
            else -> accuracyIndicator.setBackgroundColor(Color.RED)
        }
    }
}