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
import com.jyotimoykashyap.runanddetect.databinding.FragmentLoginBinding
import kotlin.math.sign


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // your code goes here

        // initialize firebase auth
        auth = FirebaseAuth.getInstance()

        binding.run {
            emailAddressEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)

            // navigate to sign up fragment if not registered
            navToSignUpBtn.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }


            signInBtn.setOnClickListener{
                // authentication (sign in)
                auth.signInWithEmailAndPassword(emailAddressEditText.text.toString().trim(), passwordEditText.text.toString().trim())
                    .addOnCompleteListener(requireActivity()){
                        if(it.isSuccessful){
                            val user = auth.currentUser
                            findNavController().navigate(R.id.action_loginFragment_to_introFragment)
                        }else{
                            Toast.makeText(context, "Sign in failed Please try again", Toast.LENGTH_SHORT).show()
                            passwordEditText.setText("")
                        }
                    }


            }


        }


        return binding.root
    }

    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.run {
                var emailInput = emailAddressEditText.text.toString().trim()
                var passwordInput = passwordEditText.text.toString().trim()

                if(passwordEditText.text.toString().trim().length < 3){
                    passwordInputLayout.error = "Password is too small"
                }else passwordInputLayout.error = null

                if(!isEmailValid(emailAddressEditText.text.toString().trim())){
                    emailAddressInputLayout.error = "Invalid email address"
                }else emailAddressInputLayout.error = null

                // enabling or disabling the button for sign in
                signInBtn.isEnabled = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    fun isEmailValid(emailInput : String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}