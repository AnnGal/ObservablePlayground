package an.gal.android.observableplayground

import an.gal.android.observableplayground.databinding.FragmentObservableBinding
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ObservableFragment : Fragment() {

    private var _binding: FragmentObservableBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ObservableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ObservableViewModel::class.java]

        subscribeViewModel()
        setListeners()
    }

    private fun setListeners() {
        binding.btnShowChannelSnackbar.setOnClickListener {
            viewModel.showChannelSnackbar(binding.editSnackbarMessage.text.toString())
        }

        binding.btnCounterLiveData.setOnClickListener {
            viewModel.startLiveDataSimpleTimer()
        }

        binding.btnRandomizeColorStateFlow.setOnClickListener {
            viewModel.getRandomWeatherState()
        }

        // flow
        binding.btnSimpleFlow.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getCounterDataAsFlow().collectLatest { text ->
                    binding.textCounterSimpleFlow.text = text
                }
            }
        }

        binding.btnSharedFlow.setOnClickListener {
            viewModel.getSharedFlowCounterData()
        }
    }

    private fun subscribeViewModel() {
        // channel
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is UIEvent.SnackbarEvent -> {
                        Snackbar.make(
                            binding.root,
                            event.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // liveData
        viewModel.timerLiveData.observe(viewLifecycleOwner) { count ->
            binding.textCounterLiveData.text = when {
                count == null -> ""
                count > 0 -> getString(R.string.live_data_counter_text, count.toString())
                else -> getString(R.string.live_data_counter_text, "Done!")
            }
        }

        // stateFlow
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest { state ->
                binding.textWeatherState.text = getString(state.stateName)
                binding.colorWeatherState.background = AppCompatResources.getDrawable(requireContext(), state.color)
            }
        }

        // sharedFlow
        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest { text ->
                binding.textCounterSharedFlow.text = text
            }
        }
    }
}