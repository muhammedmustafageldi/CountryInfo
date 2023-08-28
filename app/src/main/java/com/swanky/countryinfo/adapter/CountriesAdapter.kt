package com.swanky.countryinfo.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.swanky.countryinfo.databinding.CountryRowBinding
import com.swanky.countryinfo.model.CountryData
import com.swanky.countryinfo.model.CountryDetails
import com.swanky.countryinfo.view.DetailsActivity
import java.util.*
import kotlin.collections.ArrayList

class CountriesAdapter(
    private var context: Context, private var countryList: ArrayList<CountryData>
) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(filteredList : ArrayList<CountryData>){
        this.countryList = filteredList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: CountryRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CountryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var countryName = countryList[position].name.common
        if (countryName.length >= 14) {
            countryName = countryList[position].name.common.substring(0, 14) + "..."
        }
        holder.binding.countryNameRecycler.text = countryName

        Picasso.get().load(
            countryList[position].flags.png
        ).into(holder.binding.flagImage)

        holder.itemView.setOnClickListener {
            val country = countryList[position]

            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(
                "country", CountryDetails(
                    country.flags.png,
                    country.startOfWeek,
                    country.name.common,
                    country.currencies[country.currencies.keys.first()]?.name,
                    country.capital[0],
                    country.languages[country.languages.keys.first()],
                    country.maps.googleMaps,
                    country.population,
                    country.timezones[0]
                )
            )

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                holder.binding.flagImage,
                ViewCompat.getTransitionName(holder.binding.flagImage) ?: ""
            )
            context.startActivity(intent, options.toBundle())
        }
    }

}