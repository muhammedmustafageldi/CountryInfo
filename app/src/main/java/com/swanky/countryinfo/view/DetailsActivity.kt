package com.swanky.countryinfo.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import com.squareup.picasso.Picasso
import com.swanky.countryinfo.databinding.ActivityDetailsBinding
import com.swanky.countryinfo.model.CountryDetails
import java.text.NumberFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countryDetails = intent.getSerializableExtra("country") as CountryDetails

        setContents(countryDetails)
    }

    private fun setContents(countryDetails: CountryDetails) {

        Picasso.get().load(countryDetails.flagUrl).into(binding.detailsImage)

        ViewCompat.setTransitionName(binding.detailsImage, "shared_image")

        ActivityCompat.postponeEnterTransition(this)
        binding.detailsImage.let {
            it.doOnPreDraw {
                ActivityCompat.startPostponedEnterTransition(this@DetailsActivity)
            }
        }

        // Set other details
        binding.countryNameTxt.text = countryDetails.countryName
        binding.capitalTxt.text = countryDetails.capital
        binding.languageTxt.text = countryDetails.language
        binding.populationTxt.text = formatNumberWithCommas(countryDetails.population.toLong())
        binding.currencyTxt.text = countryDetails.currency
        binding.timezoneTxt.text = countryDetails.timezones
        binding.startOfWeekTxt.text = countryDetails.startOfWeek

        binding.googleMapTxt.setOnClickListener {
            val uri = Uri.parse(countryDetails.maps)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun formatNumberWithCommas(number : Long) : String {
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())
        return numberFormat.format(number)
    }

}