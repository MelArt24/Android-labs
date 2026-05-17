package com.am24.lab4android

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@UnstableApi
class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var viewModel: PlayerViewModel

    // Зберігає посилання на файл, який зараз грає
    // (щоб знати, що відновити, якщо програма перезавантажиться)
    private var currentUri: Uri? = null

    // Запам'ятовує секунду відео
    private var playbackPosition = 0L

    // прапор, чи відео запущене, чи стоїть на паузі.
    private var playWhenReady = true

    // відкриття файлового менеджера
    private val filePickerLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        // перевірка на null
        uri?.let {
            playMedia(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        player?.let {
            outState.putParcelable("URI", currentUri)
            outState.putLong("POS", it.currentPosition)
            outState.putBoolean("PLAY", it.playWhenReady)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        playerView = findViewById(R.id.player_view)
        val btnPickFile = findViewById<Button>(R.id.btn_pick_file)
        val btnPlayUrl = findViewById<Button>(R.id.btn_play_url)
        val etUrl = findViewById<EditText>(R.id.et_url)

        playerView.player = viewModel.player

        if (savedInstanceState != null) {
            currentUri = savedInstanceState.getParcelable("URI")
            playbackPosition = savedInstanceState.getLong("POS")
            playWhenReady = savedInstanceState.getBoolean("PLAY")

            currentUri?.let { playMedia(it) }
        }

        btnPickFile.setOnClickListener {
            filePickerLauncher.launch("*/*") // будь-який тип файлу
        }

        btnPlayUrl.setOnClickListener {
            val urlText = etUrl.text.toString()
            if (urlText.isNotEmpty()) {
                playMedia(urlText.toUri())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUi()
    }

    private fun playMedia(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri) // представлення медіа-елементу
        viewModel.player?.let {
            it.setMediaItem(mediaItem) // встановлення файлу, який треба завантажити
            it.prepare() // плеєр переходить зі стану бездіяльності у стан буферизації або готового
            it.play() // запуск
        }
    }

    // приховування навігаційної панелі, кнопки і тд
    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
