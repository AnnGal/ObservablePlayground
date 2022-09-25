package an.gal.android.observableplayground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ObservableViewModel : ViewModel() {

    // no second trigger on screen rotation
    private val eventChannel = Channel<UIEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun showChannelSnackbar(message: String) = viewModelScope.launch {
        eventChannel.send(UIEvent.SnackbarEvent(message))
    }

}