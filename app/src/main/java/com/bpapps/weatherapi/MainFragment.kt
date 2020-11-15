package com.bpapps.weatherapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var etCityName: AppCompatEditText

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

        rgDataTypeChooser.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.rbXML -> {
//                    Log.d(TAG, "XML")
                    viewModel.dataType = MainViewModel.XML
                }
                R.id.rbJson -> {
//                    Log.d(TAG, "JSON")
                    viewModel.dataType = MainViewModel.JSON
                }
                R.id.rbVolley -> {
//                    Log.d(TAG, "VOLLEY")
                    viewModel.dataType = MainViewModel.VOLLEY
                }
                else -> {
                    viewModel.dataType = MainViewModel.NOUN
                }
            }
            when (viewModel.dataType) {
                MainViewModel.XML -> {
                    Log.d(TAG, "XML")
                }
                MainViewModel.JSON -> {
                    Log.d(TAG, "JSON")
                }
                MainViewModel.VOLLEY -> {
                    Log.d(TAG, "VOLLEY")
                }

                else -> {
                    Log.d(TAG, "NOUN")
                }
            }

        }
    }


    companion object {
        fun newInstance() = MainFragment()

        private const val TAG = "TAG.com.bpapps.weatherapi"
    }
}