package com.jhonnatan.kalunga.presentation.core.session.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.databinding.ActivityLogInBinding
import com.jhonnatan.kalunga.databinding.ActivitySignUpBinding
import com.jhonnatan.kalunga.presentation.core.home.views.StartingScreenActivity
import com.jhonnatan.kalunga.presentation.core.session.viewModels.LogInViewModel
import com.jhonnatan.kalunga.presentation.core.session.viewModels.LogInViewModelFactory
import com.jhonnatan.kalunga.presentation.core.session.viewModels.SignUpViewModel
import com.jhonnatan.kalunga.presentation.core.session.viewModels.SignUpViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi

class LogInActivity : AppCompatActivity() {

    private lateinit var viewModel: LogInViewModel
    private lateinit var binding: ActivityLogInBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = LogInViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, viewModelFactory)[LogInViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        binding.lifecycleOwner = this
        binding.vModel = viewModel

        viewModel.showPassword.observe(this, {
            if (it){
                binding.editTextPassword.transformationMethod =
                    PasswordTransformationMethod()
                binding.imageViewShow.setBackgroundResource(R.drawable.ic_eye_line)
            } else {
                binding.editTextPassword.transformationMethod = null
                binding.imageViewShow.setBackgroundResource(R.drawable.ic_eye)
            }
            binding.editTextPassword.setSelection(binding.editTextPassword.length())
        })


        binding.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        viewModel.navigateToSignUp.observe(this, {
            if (it == true)
                goToSignUp()
        })
    }


    @DelicateCoroutinesApi
    private fun goToSignUp() {
        val intent = Intent(this@LogInActivity, SignUpActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        finish()
    }


    @DelicateCoroutinesApi
    override fun onBackPressed() {
        val intent = Intent(this@LogInActivity, StartingScreenActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
        finish()
    }

}