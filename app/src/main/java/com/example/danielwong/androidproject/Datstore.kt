package com.example.danielwong.androidproject

import java.util.*
import android.util.Log


data class School (
    val name : String? = "",
    val city : String? = "",
    val state: String? = "",
    val zip: String? = null,
    val latitude : Double? = null,
    val longitude : Double? = null
)

data class CompleteJson(val description : String, val generated : String, val license : String, val schools : List<School>) {
    fun generateSchools(): List<School> {
        for (s in schools) {
            if (!s.zip.isNullOrEmpty() && s.zip!!.startsWith("93")) {
                val temp = School(
                    s.name,
                    s.city,
                    s.state,
                    s.zip,
                    s.latitude,
                    s.longitude
                )
                TheSchools.SCHOOLS.add(temp)
            }
        }
        return TheSchools.SCHOOLS
    }
}

object TheSchools {
    val SCHOOLS : MutableList<School> = mutableListOf()
}
