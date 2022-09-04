package models

import kotlinx.serialization.Serializable

@Serializable
data class Product(val id: Int, val name: String, val description: String, val price: Double, val categoryReference: Int)