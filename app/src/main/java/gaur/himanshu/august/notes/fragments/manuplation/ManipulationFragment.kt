package gaur.himanshu.august.notes.fragments.manuplation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.notes.NotesViewModel
import gaur.himanshu.august.notes.databinding.FragmentManuplationBinding
import gaur.himanshu.august.notes.room.Notes
import gaur.himanshu.august.notes.utils.Constants
import gaur.himanshu.august.notes.utils.makeToast
import javax.inject.Inject

@AndroidEntryPoint
class ManipulationFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    val viewModel: NotesViewModel by activityViewModels()

    lateinit var binding: FragmentManuplationBinding


    val args: ManipulationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManuplationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        args.notes?.let {
            binding.titleEditText.setText(it.title)
            binding.descEditText.setText(it.description)
            binding.mainToolbar.title = "Edit Note"
        }


        binding.saveData.setOnClickListener {

            val title = binding.titleEditText.text.toString()
            val desc = binding.descEditText.text.toString()

            if (title.trim().isEmpty()) {
                requireContext().makeToast("Title is not Empty!!")
                return@setOnClickListener
            }
            if (desc.trim().isEmpty()) {
                requireContext().makeToast("Description can't be empty")
                return@setOnClickListener
            }

            val note = if (args.notes != null) {
                Notes(
                    title = title,
                    description = desc,
                    id = args.notes!!.id,
                    email = sharedPreferences.getString(Constants.EMAIL, "").toString()
                )
            } else {
                Notes(
                    title = title,
                    description = desc,
                    email = sharedPreferences.getString(Constants.EMAIL, "").toString()
                )
            }

            viewModel.insertNote(note)
            findNavController().popBackStack()
        }

    }


}