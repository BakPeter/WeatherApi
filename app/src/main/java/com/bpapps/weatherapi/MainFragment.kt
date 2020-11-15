package com.bpapps.weatherapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var etCityName: AppCompatEditText
    private lateinit var spinnerArchitectureType: AppCompatSpinner
    private lateinit var spinnerReturnedDataType: AppCompatSpinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnShowWeather.setOnClickListener {
            Log.d(TAG, "text = ${etCityName.text.toString()}")
        }

        etCityName = view.findViewById(R.id.etCityName)
        etCityName.addTextChangedListener { text: Editable? ->
//            Log.d(TAG, text.toString())
            viewModel.cityName = text.toString()
//            Log.d(TAG, "viewModel city name = ${viewModel.cityName}")
        }

        spinnerArchitectureType = view.findViewById(R.id.spinnerArchitectureType)
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.architectureTypes
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerArchitectureType.adapter = adapter
        }
        spinnerArchitectureType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//                    Log.d(TAG, (view as AppCompatTextView).text.toString())
//                    Log.d(TAG, viewModel.architectureTypes[position])
                    viewModel.architectureType = (view as AppCompatTextView).text.toString()
//                    Log.d(TAG, "viewModel : ${viewModel.architectureType}")
                }
            }

        spinnerReturnedDataType = view.findViewById(R.id.spinnerReturnedDataType)
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.dataTypes
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerReturnedDataType.adapter = adapter
        }
        spinnerReturnedDataType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//                    Log.d(TAG, (view as AppCompatTextView).text.toString())
//                    Log.d(TAG, viewModel.architectureTypes[position])
                    viewModel.dataType = viewModel.dataTypes[position]
//                    Log.d(TAG, "viewModel : ${viewModel.dataType}")
                }
            }
    }

    companion object {
        fun newInstance() = MainFragment()

        private const val TAG = "TAG.com.bpapps.weatherapi"
    }
}