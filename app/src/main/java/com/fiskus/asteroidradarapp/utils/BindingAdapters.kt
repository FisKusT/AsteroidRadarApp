package com.fiskus.asteroidradarapp.utils

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fiskus.asteroidradarapp.R
import com.fiskus.asteroidradarapp.model.Asteroid
import com.fiskus.asteroidradarapp.model.NasaImage
import com.fiskus.asteroidradarapp.net.model.IMAGE_MEDIA_TYPE
import com.fiskus.asteroidradarapp.ui.asteroid_list.AsteroidsListAdapter

//Binding Adapters Utils

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean?) {
    setImageResource(if (isHazardous == true) {
        R.drawable.ic_status_potentially_hazardous
    } else {
        R.drawable.ic_status_normal
    })

    //set content description
    setHazardousImageContentDescription(isHazardous)
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean?) {
    setImageResource( if (isHazardous == true) {
        R.drawable.asteroid_hazardous
    } else {
        R.drawable.asteroid_safe
    })

    //set content description
    setHazardousImageContentDescription(isHazardous)
}

//Image content description helper
private fun ImageView.setHazardousImageContentDescription(isHazardous: Boolean?) {
    contentDescription = if (isHazardous == true) {
        context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: Double?) {
    text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double?) {
    text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: String?) {
    text = String.format(context.getString(R.string.km_s_unit_format), number)
}

//load nasa image using glide
@BindingAdapter("nasaImageLoad")
fun ImageView.loadImageUsingGlide(nasaImage: NasaImage?) {
    //check if media type is image
    if (nasaImage?.mediaType == IMAGE_MEDIA_TYPE) {
        //load image using glide
        val imgUri = nasaImage.url.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(this)
    }

    //set image content description
    contentDescription = String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), nasaImage?.explanation)
}

//set RV Asteroids list data
@BindingAdapter("asteroidListData")
fun RecyclerView.setListData(data: List<Asteroid>?) {
    //set data to adapter
    val adapter = this.adapter as AsteroidsListAdapter
    adapter.submitList(data)
}


//Handle internet request status
//internet request ENUM
enum class InternetStatus {LOADING, DONE, ERROR}
//Image button error status
@BindingAdapter("internetStatusErrorImage")
fun ImageView.bindStatus(status: InternetStatus?) {
    this.errorStatus(status)
}
//Text view error status
@BindingAdapter("internetStatusErrorText")
fun TextView.bindStatus(status: InternetStatus?) {
    this.errorStatus(status)
}

//handle error status views
private fun View.errorStatus(status: InternetStatus?) {
    //status handle HM
    val statusHM = createStatusHM(
        loadingStatus = hideLayout(this),
        errorStatus = showLayout(this),
        doneStatus = hideLayout(this)
    )

    //run status has map
    //since we are using livedata status it will be called whenever the data is changed
    runStatusHM(status, statusHM)
}

//Progress bar loading status
@BindingAdapter("internetStatusPB")
fun ProgressBar.bindStatus(status: InternetStatus?) {
    //status handle HM
    val statusHM = createStatusHM(
        loadingStatus = showLayout(this),
        errorStatus = hideLayout(this),
        doneStatus = hideLayout(this)
    )

    //run status has map
    //since we are using livedata status it will be called whenever the data is changed
    runStatusHM(status, statusHM)
}

//Layout status done
@BindingAdapter("internetStatusLayout")
fun View.bindStatus(status: InternetStatus?) {

    //status handle HM
    val statusHM = createStatusHM(
        loadingStatus = hideLayout(this),
        errorStatus = hideLayout(this),
        doneStatus = showLayout(this))

    //run status has map
    //since we are using livedata status it will be called whenever the data is changed
    runStatusHM(status, statusHM)
}

//help methods for internet request status
//run status hash map
private fun runStatusHM(internetStatusLayout: InternetStatus?, statusHM: HashMap<InternetStatus, ()-> Unit>) {
    statusHM[internetStatusLayout]?.let { it() }
}

//create status hash map
private fun createStatusHM(loadingStatus: ()-> Unit, errorStatus: ()-> Unit, doneStatus: ()-> Unit) = hashMapOf(
    Pair(InternetStatus.LOADING, loadingStatus),
    Pair(InternetStatus.ERROR, errorStatus),
    Pair(InternetStatus.DONE, doneStatus)
)

//loading, and error status- hide the layout
private fun hideLayout(layout: View): ()->Unit = {layout.visibility = View.INVISIBLE}
//done status- show the layout
private fun showLayout(layout: View): ()->Unit = {layout.visibility = View.VISIBLE}
