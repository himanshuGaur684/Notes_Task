package gaur.himanshu.august.notes.fragments.authentication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.notes.databinding.FragmentAuthenticationBinding
import gaur.himanshu.august.notes.utils.Constants
import gaur.himanshu.august.notes.utils.makeToast
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: FragmentAuthenticationBinding
    val RC_SIGN_IN = 0


    lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (sharedPreferences.getString(Constants.USERNAME, "").toString().isNotEmpty()) {
            findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToDashboardFragment())
        }

        binding.loginWithGoogle.setOnClickListener {
            googleSignIn()
        }

        // attach the onBackPressed
        attachBackPressed()

    }

    private fun attachBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    // google Sign in method
    private fun googleSignIn() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile()
                .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        signIn()
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            Log.d("TAG", "onActivityResult: RC_SIGN_IN")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                handleSignInClient(task)
            }
            if (task.isCanceled) {
                requireContext().makeToast("Cancelled!! Retry")
            }
        }
    }

    private fun handleSignInClient(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                sharedPreferences.edit().putString(Constants.USERNAME, account.displayName).apply()
                sharedPreferences.edit().putString(Constants.EMAIL, account.email).apply()
                sharedPreferences.edit().putString(Constants.PHOTO_URL, account.photoUrl.toString())
                    .apply()
                findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToDashboardFragment())
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            requireContext().makeToast(e.printStackTrace().toString())
        }


    }


}