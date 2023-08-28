package com.swanky.countryinfo.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.swanky.countryinfo.R
import com.swanky.countryinfo.adapter.CountriesAdapter
import com.swanky.countryinfo.databinding.ActivityMainBinding
import com.swanky.countryinfo.model.CountryData
import com.swanky.countryinfo.service.CountryAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var BASE_URL = ""
    private var countryArray: ArrayList<CountryData>? = null
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var countryAdapter: CountriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BASE_URL = "https://restcountries.com/v3.1/"

        compositeDisposable = CompositeDisposable()
        loadData()
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CountryAPI::class.java)

        compositeDisposable?.add(retrofit.getAllCountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ countryList ->
                countryList.let {
                    countryArray = ArrayList(countryList)
                    setRecyclerView(countryArray!!)
                }
            }, { throwable ->
                throwable.printStackTrace()
            })
        )
    }

    private fun setRecyclerView(countryList: ArrayList<CountryData>) {
        val countryRecycler = binding.countryRecycler
        countryRecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        countryRecycler.setHasFixedSize(false)

        countryAdapter = CountriesAdapter(this, countryList)
        countryRecycler.adapter = countryAdapter

        recyclerItemAnimation(countryRecycler)
    }

    private fun recyclerItemAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.layout_animation_fall_down
            )
        recyclerView.layoutAnimation = controller
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }

        })
        return true
    }

    private fun filterData(query: String?) {
        val filteredList = ArrayList<CountryData>()

        if (query.isNullOrEmpty()) {
            filteredList.addAll(countryArray!!)
        } else {
            for (item in countryArray!!) {
                if (item.name.common.contains(query, true)) {
                    filteredList.add(item)
                }
            }
        }
        countryAdapter.submitList(filteredList)
    }

    override fun onDestroy() {
        compositeDisposable?.clear()
        super.onDestroy()
    }


}