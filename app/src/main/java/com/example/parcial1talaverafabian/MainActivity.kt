package com.example.parcial1talaverafabian

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.parcial1talaverafabian.ui.theme.Parcial1TalaveraFabianTheme

class MainActivity : ComponentActivity() {
    private val CHANNEL_ID = "solicitud_channel"
    private lateinit var curpEditText: EditText
    private lateinit var nombreEditText: EditText
    private lateinit var apellidosEditText: EditText
    private lateinit var domicilioEditText: EditText
    private lateinit var ingresoEditText: EditText
    private lateinit var tipoPrestamoSpinner: Spinner
    private lateinit var validarButton: Button
    private lateinit var limpiarButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        curpEditText = findViewById(R.id.curpEditText)
        nombreEditText = findViewById(R.id.nombreEditText)
        apellidosEditText = findViewById(R.id.apellidosEditText)
        domicilioEditText = findViewById(R.id.domicilioEditText)
        ingresoEditText = findViewById(R.id.ingresoEditText)
        tipoPrestamoSpinner = findViewById(R.id.tipoPrestamoSpinner)
        validarButton = findViewById(R.id.validarButton)
        limpiarButton = findViewById(R.id.limpiarButton)

        val tiposPrestamo = arrayOf("personal", "negocio", "vivienda")
        tipoPrestamoSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposPrestamo)

        validarButton.setOnClickListener { validarSolicitud() }
        limpiarButton.setOnClickListener { limpiarCampos() }
    }

    private fun validarSolicitud() {
        val solicitud = Solicitud(
            curpEditText.text.toString(),
            nombreEditText.text.toString(),
            apellidosEditText.text.toString(),
            domicilioEditText.text.toString(),
            ingresoEditText.text.toString().toDouble(),
            tipoPrestamoSpinner.selectedItem.toString()
        )

        if (solicitud.validarIngreso()) {
            showNotification()
        } else {
            Toast.makeText(this, "No apto para el préstamo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        curpEditText.text.clear()
        nombreEditText.text.clear()
        apellidosEditText.text.clear()
        domicilioEditText.text.clear()
        ingresoEditText.text.clear()
        tipoPrestamoSpinner.setSelection(0)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Solicitud Channel"
            val descriptionText = "Channel for solicitud notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val citaIntent = Intent(this, CitaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val citaPendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, citaIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val prestamosIntent = Intent(this, PrestamosActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val prestamosPendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, prestamosIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Solicitud de Préstamo")
            .setContentText("Seleccione una opción")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_cita, "Cita", citaPendingIntent)
            .addAction(R.drawable.ic_prestamos, "Préstamos", prestamosPendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        if (notificationManager.areNotificationsEnabled()) {
            try {
                notificationManager.notify(1, builder.build())
            } catch (e: SecurityException) {
                Toast.makeText(this, "No se pueden mostrar notificaciones", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Las notificaciones están deshabilitadas", Toast.LENGTH_SHORT).show()
        }
}
}