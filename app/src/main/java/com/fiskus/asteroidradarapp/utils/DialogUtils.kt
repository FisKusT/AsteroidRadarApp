package com.fiskus.asteroidradarapp.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import com.fiskus.asteroidradarapp.R

/**
 * This is a class that shows an alert dialog
 * @param context - The activity/fragment context
 * @param title- The alert dialog title
 * @param msg- The alert dialog message/instructions
 */
class ShowInfoDialog(context: Context,
                     private val title: String,
                     private val msg: String) {
    init {
        //init the info dialog
        val builder = AlertDialog.Builder(context, R.style.StyleInfoDialog)
        //set the alert dialog builder fields
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(context.getString(R.string.ok_button)) { _,_ ->
            //do nothing
        }

        //show the alert dialog
        builder.show()
    }
}