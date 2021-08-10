package com.jhonnatan.kalunga.presentation.core.session.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jhonnatan.kalunga.R
import com.jhonnatan.kalunga.databinding.ActivityConfigurationBinding
import com.jhonnatan.kalunga.domain.models.enumeration.CodeSnackBarCloseAction
import com.jhonnatan.kalunga.domain.models.enumeration.CodeTypeSpinner
import com.jhonnatan.kalunga.domain.models.enumeration.TypeSnackBar
import com.jhonnatan.kalunga.domain.models.utils.UtilsCountry
import com.jhonnatan.kalunga.presentation.core.home.views.StartingScreenActivity
import com.jhonnatan.kalunga.presentation.core.legal.views.TermsAndPrivacyActivity
import com.jhonnatan.kalunga.presentation.core.session.viewModels.ConfigurationViewModel
import com.jhonnatan.kalunga.presentation.core.session.viewModels.ConfigurationViewModelFactory
import com.jhonnatan.kalunga.presentation.core.utils.CustomSnackBar
import com.jhonnatan.kalunga.presentation.core.utils.CustomSpinnerAdapter
import com.jhonnatan.kalunga.presentation.core.utils.ListDialog
import com.jhonnatan.kalunga.presentation.core.utils.LoadingDialog
import com.jhonnatan.kalunga.presentation.features.dashboard.views.DashboardActivity
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class ConfigurationActivity : AppCompatActivity() {
    private lateinit var viewModel: ConfigurationViewModel
    private lateinit var binding: ActivityConfigurationBinding
    private val tag = "Configuration"

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ConfigurationViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ConfigurationViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_configuration)
        binding.lifecycleOwner = this
        binding.vModel = viewModel
        val loadingDialog = LoadingDialog(this, getString(R.string.configurando))

        binding.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        binding.buttonCountry.setOnClickListener {
            createDialogSpinner(viewModel.countriesList, CodeTypeSpinner.COUNTRIES.code)
        }

        binding.buttonDocumentType.setOnClickListener {
            createDialogSpinner(viewModel.typeDocumentsList, CodeTypeSpinner.TYPE_DOCUMENT.code)
        }

        binding.buttonRegister.setOnClickListener {
            loadingDialog.startLoadingDialog()
            if (viewModel.checkOnline(this))
                viewModel.searchUser()
            else {
                viewModel.snackBarAction.value = 0
            }
        }

        binding.buttonDocumentType.setSelected(true)

        viewModel.countrySelectedPosition.observe(this, {
            if (it != null) {
                binding.buttonCountry.setCompoundDrawablesWithIntrinsicBounds(
                    UtilsCountry().getIdFlag(viewModel.countriesList[it].pais),
                    0,
                    R.drawable.ic_arrow_drop_down,
                    0
                )
            }
            viewModel.getDataCitiesByCodeCountry()
            binding.textViewCountryCode.text = viewModel.countriesList[it].codPais
            viewModel.numberPhone.value = ""
        })

        viewModel.typeDocumentSelectedPosition.observe(this, {
            if (it != null) {
                binding.buttonDocumentType.text =
                    viewModel.typeDocumentsList[it].abbreviate + getString(R.string.separador) + viewModel.typeDocumentsList[it].description
            }
        })

        viewModel.numberPhone.observe(this, {
            if (viewModel.numberPhoneStart.value == true) {
                binding.editTextPhone.setText(it)
                binding.editTextPhone.setSelection(binding.editTextPhone.length())
            } else {
                viewModel.numberPhoneStart.value = true
            }
        })

        viewModel.citiesList.observe(this, {
            citiesList -> binding.textViewCity.setAdapter(ArrayAdapter(this@ConfigurationActivity, R.layout.autocomplete,citiesList[0].data))
        })

        viewModel.errorIdentification.observe(this, {
            binding.textViewDocumentError.text = it
        })

        viewModel.errorCity.observe(this, {
            binding.textViewCityError.text = it
        })

        viewModel.errorPhone.observe(this, {
            binding.textViewPhoneError.text = it
        })

        viewModel.buttonRegisterDrawable.observe(this, {
            binding.buttonRegister.setBackgroundResource(it)
        })

        viewModel.buttonRegisterEnable.observe(this, {
            binding.buttonRegister.isEnabled = it
        })

        binding.textViewTerms.makeLinks(
            Pair(getString(R.string.condiciones_de_uso), View.OnClickListener {
                goToTermsAndPrivacy(getString(R.string.terms_of_use))
            }),
            Pair(getString(R.string.declaracion_de_privacidad), View.OnClickListener {
                goToTermsAndPrivacy(getString(R.string.privacy_statement))
            }))

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

        viewModel.emailValue.value = intent.getStringExtra("ACCOUNT")
        viewModel.nameValue.value = intent.getStringExtra("FULL_NAME")
        viewModel.passwordValue.value = intent.getStringExtra("PASSWORD_USER")
        viewModel.statusUser.value = intent.getIntExtra("STATUS_USER",0)
        viewModel.sessionState.value = intent.getBooleanExtra("SESSION_STATE",false)
        viewModel.typeUser.value = intent.getIntExtra("TYPE_USER",0)
        viewModel.setInitialValues()


        viewModel.navigateToLogIn.observe(this, {
            if (it == true)
                goToLogIn()
        })

        viewModel.navigateToStarting.observe(this, {
            if (it == true)
                onBackPressed()
        })

        viewModel.navigateToDashboard.observe(this, {
            if (it == true)
                goToDashboard()
        })


    }

    private fun createDialogSpinner(dataList: List<Any>, code: Int) {
        val dialog = ListDialog(
            dataList,
            code,
            object : CustomSpinnerAdapter.CustomActionSpinner {
                override fun onItemSelected(position: Int) {
                    when (code) {
                        CodeTypeSpinner.COUNTRIES.code -> viewModel.countrySelectedPosition.value =
                            position
                        CodeTypeSpinner.TYPE_DOCUMENT.code -> viewModel.typeDocumentSelectedPosition.value =
                            position
                    }
                }
            })
        dialog.show(this.supportFragmentManager, tag)
    }

    private fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                @SuppressLint("ResourceAsColor")
                override fun updateDrawState(textPaint: TextPaint) {
                    textPaint.color = ContextCompat.getColor(this@ConfigurationActivity,R.color.purple)
                    textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }


    override fun onBackPressed() {
        val intent = Intent(
            this@ConfigurationActivity,
            StartingScreenActivity::class.java
        )
        startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
        finish()
    }

    private fun goToTermsAndPrivacy(type: String) {
        val intent = Intent(this@ConfigurationActivity, TermsAndPrivacyActivity::class.java)
        intent.putExtra("TYPE", type)
        startActivity(intent)
        overridePendingTransition(R.anim.up_in, R.anim.up_out)
        finish()
    }

    private fun goToLogIn() {
        val intent = Intent(this@ConfigurationActivity, LogInActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        finish()
    }

    private fun goToDashboard() {
        val intent = Intent(this@ConfigurationActivity, DashboardActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
        finish()
    }
}

