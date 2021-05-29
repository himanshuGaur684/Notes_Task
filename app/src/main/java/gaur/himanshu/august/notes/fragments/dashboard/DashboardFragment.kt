package gaur.himanshu.august.notes.fragments.dashboard

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.notes.NotesViewModel
import gaur.himanshu.august.notes.databinding.FragmentDashboardBinding
import gaur.himanshu.august.notes.utils.Constants
import gaur.himanshu.august.notes.utils.makeToast
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: FragmentDashboardBinding
    val notesAdapter = NotesAdapter()


    val viewModel: NotesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getAllData(sharedPreferences.getString(Constants.EMAIL, "").toString())


        // profile details
        if (sharedPreferences.getString(Constants.EMAIL, "").toString().isNotEmpty()) {
            Glide.with(requireContext())
                .load(sharedPreferences.getString(Constants.PHOTO_URL, "").toString())
                .into(binding.profileImage)
        }

        // recycler
        binding.dashboardRecycler.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // add note
        binding.addNotes.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToManipulationFragment())
        }

        // observing the data
        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.notes.collect {
                when (it) {
                    is NotesViewModel.NotesController.Success -> {
                        binding.progressBar.visibility = View.GONE
                        notesAdapter.setContentList(it.notes.toMutableList())
                        if (it.notes.isEmpty()) {
                            binding.emptyList.visibility = View.VISIBLE
                            binding.textAddNotes.visibility = View.VISIBLE
                        } else {
                            binding.emptyList.visibility = View.GONE
                            binding.textAddNotes.visibility = View.GONE
                        }
                    }
                    is NotesViewModel.NotesController.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is NotesViewModel.NotesController.Empty -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is NotesViewModel.NotesController.Error -> {
                        binding.progressBar.visibility = View.GONE
                        requireContext().makeToast("Error Occurred")
                    }
                }
            }
        }


        // click handler on the list items (small click)
        notesAdapter.setOnItemClickListener {
            findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToManipulationFragment(
                    notes = it
                )
            )
        }
        // click handler on the list items (long click)
        notesAdapter.setOnLongItemClickListener {
            AlertDialog.Builder(requireContext()).setMessage("Are you sure to Delete?")
                .setPositiveButton("Yes") { _, _ ->
                    notesAdapter.removeItem(it)
                    viewModel.deleteNote(it)
                }.setNegativeButton("No") { _, _ ->

                }.show()
        }

        // handle logout
        binding.profileImage.setOnClickListener {
            logout()
        }

        // handle back pressed
        backPressedHandler()
    }

    private fun logout() {

        AlertDialog.Builder(requireContext())
            .setTitle(sharedPreferences.getString(Constants.USERNAME, "").toString())
            .setMessage("Are you sure to logout?")
            .setPositiveButton("Yes") { _, _ ->
                val gso: GoogleSignInOptions =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                        .requestProfile()
                        .build()
                val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

                googleSignInClient.signOut().addOnCompleteListener {
                    if (it.isSuccessful) {
                        sharedPreferences.edit().putString(Constants.EMAIL, "").apply()
                        sharedPreferences.edit().putString(Constants.USERNAME, "").apply()
                        sharedPreferences.edit().putString(Constants.PHOTO_URL, "").apply()
                        findNavController().popBackStack()
                    }
                    if (it.isCanceled) {
                        requireContext().makeToast("Task cancelled! Retry")
                    }

                }
            }.setNegativeButton("No") { _, _ ->
                requireContext().makeToast("Logout Cancelled!!")
            }.show()

    }

    fun backPressedHandler(){
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
    }

}