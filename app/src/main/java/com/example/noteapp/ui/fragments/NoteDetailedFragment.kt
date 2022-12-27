package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Note
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentDetailedNoteBinding
import com.example.noteapp.databinding.StateLoadingBinding
import com.example.noteapp.ui.dialogs.ChooseCategoryDialog
import com.example.noteapp.ui.fragments.events.NoteDetailedEvent
import com.example.noteapp.ui.util.errorOccurred
import com.example.noteapp.ui.util.ext.categoriesToFlowCategories
import com.example.noteapp.ui.util.ext.convertTUiString
import com.example.noteapp.ui.util.loadingFinished
import com.example.noteapp.ui.util.loadingStarted
import com.example.noteapp.ui.viewmodel.NoteDetailsViewModel
import com.example.noteapp.ui.viewmodel.handleState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailedFragment : Fragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!

    private var _stateLoadingBinding: StateLoadingBinding? = null
    private val stateLoadingBinding: StateLoadingBinding get() = _stateLoadingBinding!!

    private val viewModel: NoteDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            observeState()

            // !! validate empty edit text

            btnDelete.setOnClickListener {
                viewModel.onEvent(NoteDetailedEvent.DeleteNote)
            }

            etContent.doAfterTextChanged {

            }

            etTitle.doAfterTextChanged {

            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.note.collect { state ->
                state.handleState(
                    onLoadingAction = stateLoadingBinding::loadingStarted,
                    onSuccessAction = ::showNote,
                    onErrorAction = ::onErrorAction
                )
            }
        }
    }

    private fun onErrorAction(error: Throwable) {
        stateLoadingBinding.errorOccurred(error) {
            viewModel.onEvent(NoteDetailedEvent.TryLoadingNoteAgain)
        }
    }

    private fun showNote(note: Note) {
        stateLoadingBinding.loadingFinished()
        with(binding) {
            etTitle.hint = getString(R.string.hint_note_title)
            etContent.hint = getString(R.string.hint_note_content)
            note.run {
                etTitle.setText(title)
                etContent.setText(content)
                date?.let {
                    tvDate.text = it.convertTUiString()
                }
                categoriesToFlowCategories(flowCategories) {
//                    findNavController().
                }
            }
        }
        stateLoadingBinding.loadingFinished()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        _stateLoadingBinding = StateLoadingBinding.bind(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _stateLoadingBinding = null
        _binding = null
    }
}
