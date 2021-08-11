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
import com.jhonnatan.kalunga.domain.models.enumeration.CodeSnackBarCloseAction
import com.jhonnatan.kalunga.domain.models.enumeration.TypeSnackBar
import com.jhonnatan.kalunga.presentation.core.home.views.StartingScreenActivity
import com.jhonnatan.kalunga.presentation.core.session.viewModels.LogInViewModel
import com.jhonnatan.kalunga.presentation.core.session.viewModels.LogInViewModelFactory
import com.jhonnatan.kalunga.presentation.core.session.viewModels.SignUpViewModel
import com.jhonnatan.kalunga.presentation.core.session.viewModels.SignUpViewModelFactory
import com.jhonnatan.kalunga.presentation.core.utils.CustomSnackBar
import com.jhonnatan.kalunga.presentation.core.utils.LoadingDialog
import kotlinx.coroutines.DelicateCoroutinesApi

class LogInActivity : AppCompatActivity() {

    private lateinit var viewModel: LogInViewModel
    private lateinit var binding: ActivityLogInBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = LogInViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LogInViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        binding.lifecycleOwner = this
        binding.vModel = viewModel
        val loadingDialog = LoadingDialog(this, getString(R.string.configurando))

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

        viewModel.errorEmail.observe(this, {
            binding.textViewEmailError.text = it
        })

        viewModel.errorPassword.observe(this, {
            binding.textViewPasswordError.text = it
        })

        viewModel.buttonLogInDrawable.observe(this, {
            binding.buttonLogIn.setBackgroundResource(it)
        })

        viewModel.buttonLogInEnable.observe(this, {
            binding.buttonLogIn.isEnabled = it
        })

        binding.buttonLogIn.setOnClickListener {
            loadingDialog.startLoadingDialog()
            if (viewModel.checkOnline(this))
                viewModel.login()
            else {
                viewModel.snackBarAction.value = 0
            }
        }


        viewModel.snackBarTextWarning.observe(this, {
            CustomSnackBar().showSnackBar(
                it,
                binding.constraintLayout,
                TypeSnackBar.WARNING.code,
                this,
                viewModel.snackBarNavigate.value!!
            )
        })


        viewModel.snackBarTextError.observe(this, {
            CustomSnackBar().showSnackBar(
                it,
                binding.constraintLayout,
                TypeSnackBar.ERROR.code,
                this,
                viewModel.snackBarNavigate.value!!
            )
        })


        viewModel.snackBarTextInfo.observe(this, {
            CustomSnackBar().showSnackBar(
                it,
                binding.constraintLayout,
                TypeSnackBar.INFO.code,
                this,
                viewModel.snackBarNavigate.value!!
            )
        })

        viewModel.snackBarTextSuccess.observe(this, {
            CustomSnackBar().showSnackBar(
                it,
                binding.constraintLayout,
                TypeSnackBar.SUCCESS.code,
                this,
                viewModel.snackBarNavigate.value!!
            )
        })


        viewModel.snackBarAction.observe(this, {
            loadingDialog.hideLoadingDialog()
            when (it){
                0 -> {
                    viewModel.snackBarNavigate.postValue(CodeSnackBarCloseAction.NONE.code)
                    viewModel.snackBarTextWarning.postValue(getString(R.string.debe_tener_conecion_a_internet_para_continuar))
                }
                1 -> {
                    viewModel.snackBarNavigate.postValue(CodeSnackBarCloseAction.STARTING_ACTIVITY.code)
                    viewModel.snackBarTextInfo.postValue(getString(R.string.El_correo_ingresado_ya_tiene_una_cuenta_asociada_en_Kalunga))
                }
                2 -> {
                    viewModel.snackBarNavigate.postValue(CodeSnackBarCloseAction.STARTING_ACTIVITY.code)
                    viewModel.snackBarTextInfo.postValue(getString(R.string.El_correo_ingresado_no_ha_sido_validado_verifique_su_email))
                }
                3 -> {
                    viewModel.snackBarNavigate.postValue(CodeSnackBarCloseAction.STARTING_ACTIVITY.code)
                    viewModel.snackBarTextSuccess.postValue(getString(R.string.Hemos_enviado_un_correo_electrónico_valide_su_cuenta))
                }
                4 -> {
                    viewModel.snackBarNavigate.postValue(CodeSnackBarCloseAction.STARTING_ACTIVITY.code)
                    viewModel.snackBarTextError.postValue(getString(R.string.No_ha_sido_posible_enviarle_el_correo_electronico_contactese))
                }
                5 -> {
                    viewModel.snackBarNavigate.postValue(CodeSnackBarCloseAction.NONE.code)
                    viewModel.snackBarTextError.postValue(getString(R.string.Error_en_el_servidor_por_favor_intente_mas_tarde))
                }
            }
        })

        viewModel.navigateToSignUp.observe(this, {
            if (it == true)
                goToSignUp()
        })

        viewModel.navigateToForgetPassword.observe(this, {
            if (it == true)
                goToForgetPassword()
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
    private fun goToForgetPassword() {
        val intent = Intent(this@LogInActivity, ForgetPasswordActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.up_in, R.anim.up_out)
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