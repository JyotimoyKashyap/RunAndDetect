package com.jyotimoykashyap.runanddetect.IntroFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.jyotimoykashyap.runanddetect.R
import com.jyotimoykashyap.runanddetect.databinding.FragmentIntroBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class IntroFragment : Fragment() , EasyPermissions.PermissionCallbacks{

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        // your code goes here

        binding.openCameraBtn.setOnClickListener{
            if(!hasCameraPermission()) requestCameraPermission()
            else{
                // showing a toast message
                Toast.makeText(
                    context,
                    "Camera Permission Given",
                    Toast.LENGTH_SHORT
                ).show()

                /**
                 * Opening the camera activity
                 */
                findNavController().navigate(R.id.action_introFragment_to_camera)

            }
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasCameraPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.CAMERA
        )

    private fun requestCameraPermission(){
        EasyPermissions.requestPermissions(
            this,
            "Camera will be used by this application to detect your running pose",
            PERMISSION_REQUEST_FOR_CAMERA,
            android.Manifest.permission.CAMERA

        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestCameraPermission()
        }
    }

    companion object {

        const val PERMISSION_REQUEST_FOR_CAMERA = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IntroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}