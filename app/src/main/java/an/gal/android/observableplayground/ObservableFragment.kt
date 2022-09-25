package an.gal.android.observableplayground

import an.gal.android.observableplayground.databinding.FragmentObservableBinding
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ObservableFragment : Fragment() {

    //private lateinit var binding: FragmentObservableBinding
    private var _binding: FragmentObservableBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ObservableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_observable, container, false)


        _binding = FragmentObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ObservableViewModel::class.java)
        // TODO: Use the ViewModel
    }

}