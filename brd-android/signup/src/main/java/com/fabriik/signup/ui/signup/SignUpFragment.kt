package com.fabriik.signup.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fabriik.signup.R
import com.fabriik.signup.databinding.FragmentSignUpBinding
import com.fabriik.signup.ui.base.FabriikView
import com.fabriik.signup.ui.confirmemail.ConfirmEmailViewEffect
import com.fabriik.signup.ui.confirmemail.ConfirmEmailViewState
import com.fabriik.signup.ui.login.LogInFragmentDirections
import com.fabriik.signup.ui.login.LogInViewModel
import com.fabriik.signup.utils.clickableSpan

class SignUpFragment : Fragment(), FabriikView<SignUpViewState, SignUpViewEffect> {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        binding.btnSubmit.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionConfirmEmail()
            )
        }

        binding.tvTerms.clickableSpan(
            fullTextRes = R.string.SignUp_Terms,
            clickableParts = mapOf(
                R.string.SignUp_Terms_Link1 to {
                    Toast.makeText(context, "Terms 1", Toast.LENGTH_LONG).show()
                },
                R.string.SignUp_Terms_Link2 to {
                    Toast.makeText(context, "Terms 2", Toast.LENGTH_LONG).show()
                }
            )
        )
    }

    override fun render(state: SignUpViewState) {
        TODO("Not yet implemented")
    }

    override fun handleEffect(effect: SignUpViewEffect?) {
        TODO("Not yet implemented")
    }
}