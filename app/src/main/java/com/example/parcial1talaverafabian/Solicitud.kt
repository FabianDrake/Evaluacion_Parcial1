package com.example.parcial1talaverafabian

class Solicitud(
    val curp: String,
    val nombre: String,
    val apellidos: String,
    val domicilio: String,
    val cantidadIngreso: Double,
    var tipoPrestamo: String
) {
    fun validarIngreso(): Boolean {
        return when (tipoPrestamo) {
            "personal" -> cantidadIngreso in 20000.0..40000.0
            "negocio" -> cantidadIngreso in 40001.0..60000.0
            "vivienda" -> cantidadIngreso in 15000.0..35000.0
            else -> false
        }
    }
}