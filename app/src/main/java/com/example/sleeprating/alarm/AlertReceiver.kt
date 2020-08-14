package com.example.sleeprating.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.sleeprating.R

class AlertReceiver : BroadcastReceiver() {

    private lateinit var alarmSound: MediaPlayer

    private lateinit var vibrator: Vibrator

    override fun onReceive(context: Context?, intent: Intent?) {
        alarmSound = MediaPlayer.create(context, R.raw.alarm_tone)
        alarmSound.start()
        vibrator = context?.applicationContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
        val notificationHelper = NotificationHelper(context)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager!!.notify(1, nb.build())
    }
}