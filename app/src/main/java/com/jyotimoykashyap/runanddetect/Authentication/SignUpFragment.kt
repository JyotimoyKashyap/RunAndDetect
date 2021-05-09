package com.jyotimoykashyap.runanddetect.Authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jyotimoykashyap.runanddetect.R
import com.jyotimoykashyap.runanddetect.databinding.FragmentSignUpBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SignUpFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // your code goes here

        auth = FirebaseAuth.getInstance()

        binding.run {
            emailSignEditText.addTextChangedListener(textWatcher)
            passwordConfirmSignEditText.addTextChangedListener(textWatcher)
            passwordSignEditText.addTextChangedListener(textWatcher)

            // sign up
            signUpBtn.setOnClickListener {
                // authentication (sign up)
                auth.createUserWithEmailAndPassword(emailSignEditText.text.toString().trim(),
                passwordConfirmSignEditText.text.toString().trim())
                    .addOnCompleteListener(requireActivity()){
                        if(it.isSuccessful){
                            Toast.makeText(context, "Sign up completed", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signUpFragment_to_introFragment)
                        }else{
                            Toast.makeText(context, "Authentication Failed" , Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }





        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.run {
                var emailInput = emailSignEditText.text.toString().trim()
                var passwordInput = passwordSignEditText.text.toString().trim()
                var confirmPasswordInput = passwordConfirmSignEditText.text.toString().trim()

                // enabling or disabling the button for sign in
                signUpBtn.isEnabled = emailInput.isNotEmpty()
                        && passwordInput.isNotEmpty()
                        && confirmPasswordInput.isNotEmpty()

                if(passwordConfirmSignEditText.text.toString().trim().length < 3){
                    passwordConfirmSignInputLayout.error = "Password is too small"
                }else passwordConfirmSignInputLayout.error = null

                if(!isEmailValid(emailSignEditText.text.toString().trim())){
                    emailSignInputLayout.error = "Invalid email address"
                }else emailSignInputLayout.error = null

                if(passwordInput != confirmPasswordInput){
                    passwordConfirmSignInputLayout.error = "Mismatch in password"
                }else passwordConfirmSignInputLayout.error = null
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            binding.run {

            }

        }

    }

    fun isEmailValid(emailInput : String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}