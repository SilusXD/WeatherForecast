package com.example.weatherforecast

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    private val url: String = "https://api.weather.yandex.ru/v2/forecast"
    private val apiKey: String = "b27036d7-8961-4aee-9fc3-d897907e7751"
    private var lat: String = "62.0339"
    private var lon: String = "129.733"

    private var textViewCityName: TextView? = null
    private var textViewCityTemp: TextView? = null
    private var textViewDate1: TextView? = null
    private var textViewDateTemp1: TextView? = null
    private var morningDayTemp1: TextView? = null
    private var dayDayTemp1: TextView? = null
    private var eveningDayTemp1: TextView? = null
    private var nightDayTemp1: TextView? = null

    private var textViewDate2: TextView? = null
    private var textViewDateTemp2: TextView? = null
    private var morningDayTemp2: TextView? = null
    private var dayDayTemp2: TextView? = null
    private var eveningDayTemp2: TextView? = null
    private var nightDayTemp2: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textViewCityName = findViewById(R.id.textViewCityName)
        textViewCityTemp = findViewById(R.id.textViewCityTemp)


        textViewDate1 = findViewById(R.id.textViewDate1)
        textViewDateTemp1 = findViewById(R.id.textViewDateTemp1)
        morningDayTemp1 = findViewById(R.id.morningDayTemp1)
        dayDayTemp1 = findViewById(R.id.dayDayTemp1)
        eveningDayTemp1 = findViewById(R.id.eveningDayTemp1)
        nightDayTemp1 = findViewById(R.id.nightDayTemp1)

        textViewDate2 = findViewById(R.id.textViewDate2)
        textViewDateTemp2 = findViewById(R.id.textViewDateTemp2)
        morningDayTemp2 = findViewById(R.id.morningDayTemp2)
        dayDayTemp2 = findViewById(R.id.dayDayTemp2)
        eveningDayTemp2 = findViewById(R.id.eveningDayTemp2)
        nightDayTemp2 = findViewById(R.id.nightDayTemp2)

        val requestQueue = Volley.newRequestQueue(this)

        getWeather(requestQueue, url, apiKey, lat, lon)
    }

    private fun getWeather(requestQueue: RequestQueue, url: String, apiKey: String,
                           lat: String, lon: String)
    {
        var buildUrl: String = url

        if(lat.isNotEmpty() && lon.isNotEmpty())
        {
            buildUrl += "?lat=$lat&lon=$lon"
        }

        val request = object : JsonObjectRequest(
            Method.GET,
            buildUrl,
            null,
            { response ->
                try
                {
                    textViewCityName?.text = response.getJSONObject("info").
                    getJSONObject("tzinfo").
                    getString("name")

                    textViewCityTemp?.text = response.getJSONObject("fact").
                    getInt("temp").
                    toString()


                    var element = response.getJSONArray("forecasts").
                    getJSONObject(0)

                    textViewDate1?.text =  getDate(element)
                    textViewDateTemp1?.text = "from ${getMin(element)} to ${getMax(element)}"
                    morningDayTemp1?.text = getAvg(element, "morning").toString()
                    dayDayTemp1?.text = getAvg(element, "day").toString()
                    eveningDayTemp1?.text = getAvg(element, "evening").toString()
                    nightDayTemp1?.text = getAvg(element, "night").toString()

                    element = response.getJSONArray("forecasts").
                    getJSONObject(1)
                    textViewDate2?.text =  getDate(element)
                    textViewDateTemp2?.text = "from ${getMin(element)} to ${getMax(element)}"
                    morningDayTemp2?.text = getAvg(element, "morning").toString()
                    dayDayTemp2?.text = getAvg(element, "day").toString()
                    eveningDayTemp2?.text = getAvg(element, "evening").toString()
                    nightDayTemp2?.text = getAvg(element, "night").toString()
                }
                catch (e: JSONException)
                {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String>
            {
                val headers = mutableMapOf<String, String>()
                headers["X-Yandex-Weather-Key"] = apiKey
                return headers
            }
        }

        requestQueue.add(request)
    }

    private fun getDate(element: JSONObject): String
    {
        return element.getString("date")
    }

    private fun getMin(element: JSONObject): Int
    {
        return element.
        getJSONObject("parts").
        getJSONObject("day").
        getInt("temp_min")
    }

    private fun getMax(element: JSONObject): Int
    {
        return element.
        getJSONObject("parts").
        getJSONObject("day").
        getInt("temp_max")
    }

    private fun getAvg(element: JSONObject, part: String): Int
    {
        return element.
        getJSONObject("parts").
        getJSONObject(part).
        getInt("temp_avg")
    }
}

