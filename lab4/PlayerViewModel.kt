// Використано ViewModel, щоб гарантувати, що ExoPlayer залишається
// в пам'яті при зміні конфігурації пристрою (повороті екрана).
// Це забезпечує плавне відтворення без перезавантаження відео,
// а також відповідає архітектурним принципам Android Jetpack
// для правильного керування життєвим циклом ресурсів


package com.am24.lab4android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory


@UnstableApi
class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    // інструмент, який вирішує, як саме читати медіа-дані
    private val dataSourceFactory = DefaultDataSource.Factory(application)


    // ExoPlayer — це спеціалізована бібліотека з відкритим кодом, яка
    // потужніша за стандартний MediaPlayer Android
    var player: ExoPlayer? = ExoPlayer.Builder(application)
        .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
        .setSeekBackIncrementMs(5 * 1000)
        .setSeekForwardIncrementMs(5 * 1000)
        .build()


    // блок ініціалізатора, який використовується для виконання логіки
    // налаштування під час створення екземпляра класу
    init {
        player?.apply {
            // CLOSEST_SYNC: плеєр миттєво стрибає до найближчого "ключового кадру"
            setSeekParameters(SeekParameters.CLOSEST_SYNC)
        }
    }

    // викликається лише один раз, коли користувач остаточно закриває додаток
    override fun onCleared() {
        super.onCleared()
        player?.release() // для звільнення обмежених ресурсів, таких як відеодекодери та пам'ять, коли екземпляр програвача більше не потрібен
        player = null
    }
}
