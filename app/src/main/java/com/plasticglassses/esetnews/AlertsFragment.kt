package com.plasticglassses.esetnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

class AlertsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_alerts, container, false)

        val addAlertButton = rootView.findViewById<Button>(R.id.addAlert)
        addAlertButton.setOnClickListener {
            addAlert(rootView, inflater)

        }

        return rootView
    }

    private fun addAlert(rootView: View?, inflater: LayoutInflater) {
        val newAlertText = rootView?.findViewById<EditText>(R.id.newAlertText)
        val alertChipGroup = rootView?.findViewById<ChipGroup>(R.id.alertChipGroup)


        val chip = layoutInflater.inflate(R.layout.chip_layout, alertChipGroup, false) as Chip
        if (newAlertText != null) {
            chip.text = newAlertText.text
            val snackbar = Snackbar.make(rootView, "Alert Added", Snackbar.LENGTH_SHORT)
            snackbar.show()
            alertChipGroup!!.addView(chip)
            newAlertText.text.clear()
        }


    }


    }