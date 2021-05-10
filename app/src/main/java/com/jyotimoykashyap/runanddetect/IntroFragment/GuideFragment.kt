package com.jyotimoykashyap.runanddetect.IntroFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialElevationScale
import com.jyotimoykashyap.runanddetect.R
import com.jyotimoykashyap.runanddetect.databinding.FragmentGuideBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GuideFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGuideBinding.inflate(inflater, container, false)

        enterTransition = MaterialElevationScale(true)
        exitTransition = MaterialElevationScale(true)
        reenterTransition = MaterialElevationScale(true)

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                GuideFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}