package com.yusufcancakmak.weatherapp.view


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.yusufcancakmak.weatherapp.R
import com.yusufcancakmak.weatherapp.databinding.ActivityMainBinding
import com.yusufcancakmak.weatherapp.viewmodel.MainViewModel
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    //needed!
    private lateinit var GET:SharedPreferences
    private lateinit var SET:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET =getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewModel = androidx.lifecycle.ViewModelProvider(this).get(MainViewModel::class.java)

        val cName =GET.getString("cityName","")
        binding.editCityName.setText(cName)

        viewModel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.llDataView.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.pbLoading.visibility=View.GONE

            var cityName = GET.getString("cityName",cName)
            binding.editCityName.setText(cityName)
            viewModel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.imageSearchCityName.setOnClickListener {
            val cityName =binding.editCityName.text.toString()
            SET.putString("cityName",cityName)
            SET.commit()
            viewModel.refreshData(cityName)
            getLiveData()
        }
    }

    private fun getLiveData(){
        viewModel.weather_data.observe(this, Observer{ data ->
            data?.let {
            binding.llDataView.visibility = View.VISIBLE
                binding.pbLoading.visibility=View.GONE
                binding.tvDegree.text=data.main.temp.toString()+ "°C"
                binding.tvCountryCode.text =data.sys.country.toString()
                binding.tvCityName.text = data.name.toString()
                binding.tvHumidity.text=data.main.humidity.toString()
                binding.tvSpeed.text=data.wind.speed.toString()
                binding.tvLat.text=data.coord.lat.toString()
                binding.tvLon.text=data.coord.lon.toString()

                Glide.with(this).load("http://openweathermap.org/img/wn/" +data.weather.get(0).icon + "@2x.png")
                    .into(binding.imgWeatherIcon)
            }
        })

        viewModel.weather_load.observe(this, Observer { load ->
            load?.let {
                if (it){
                    binding.pbLoading.visibility =View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.llDataView.visibility = View.GONE
                }else{
                    binding.pbLoading.visibility = View.GONE
                }

            }
        })
        viewModel.weather_error.observe(this, Observer{ error ->
            error.let {
                if (it){
                    binding.tvError.visibility = View.VISIBLE
                    binding.llDataView.visibility=View.GONE
                    binding.pbLoading.visibility=View.GONE
                }else{
                    binding.tvError.visibility = View.GONE
                }
            }
        })
    }
}