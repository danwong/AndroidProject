package com.example.danielwong.androidproject

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import android.util.Log

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val url = "https://code.org/schools.json"
        GetSchoolsAsyncTask().execute(url)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val pasoRobles = LatLng(35.625648, -120.691322)
        val santaMaria = LatLng(34.948399, -120.435883)
        val bnds = LatLngBounds(santaMaria, pasoRobles)
        val longCentered = (pasoRobles.longitude + santaMaria.longitude) /2
        val center = LatLng(bnds.center.latitude, longCentered)


        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 9.toFloat()))
    }

    fun addSchools(googleMap: GoogleMap) {

        for(s in TheSchools.SCHOOLS){
            println("ASDFASDFDSFASDFASDFASDFASDFASDFASD" + TheSchools.SCHOOLS.size)
            if(!s.latitude!!.isNaN() && !s.longitude!!.isNaN() && !s.name.isNullOrEmpty()){
                val name = s.name
                val ll = LatLng(s.latitude!!, s.longitude!!)
                val city = s.city
                val state = s.state
                val zip = s.zip
                googleMap.addMarker(MarkerOptions()
                    .position(ll)
                    .title(name)
                    .snippet(city + ", " + state + " " + zip + " (" + s.latitude + ", " + s.longitude + ")")


                )
            }

        }

    }

    inner class GetSchoolsAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            // Before doInBackground
        }

        override fun doInBackground(vararg urls: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val url = URL(urls[0])

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS


                val inString = urlConnection.inputStream.bufferedReader().readText()

                publishProgress(inString)
            } catch (ex: Exception) {
                println("HttpURLConnection exception" + ex)
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var weatherData = Gson().fromJson(values[0], CompleteJson::class.java)
                val schools = weatherData.generateSchools()

                for (next in schools) {
                    println(next.city)
                }


            } catch (ex: Exception) {
                println("JSON parsing exception" + ex.printStackTrace())
            }
        }

        override fun onPostExecute(result: String?) {
            addSchools(mMap)
        }
    }
}

