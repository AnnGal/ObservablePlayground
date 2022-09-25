package an.gal.android.observableplayground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ObservableViewModel : ViewModel() {

    /*------- channel ---------*/
    // no second trigger on screen rotation
    private val eventChannel = Channel<UIEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun showChannelSnackbar(message: String) = viewModelScope.launch {
        eventChannel.send(UIEvent.SnackbarEvent(message))
    }

    private val _timerLiveData = MutableLiveData<Int?>(null)
    val timerLiveData get(): LiveData<Int?> = _timerLiveData

    fun startSimpleTimer() {
        if ((_timerLiveData.value ?: 0) <= 0) {

            viewModelScope.launch(context = Dispatchers.Default) {

                var currentValue = COUNTER_MAX_VALUE
                _timerLiveData.postValue(currentValue)

                while (currentValue > 0) {
                    delay(1000L)
                    currentValue -= 1
                    _timerLiveData.postValue(currentValue)
                }

                _timerLiveData.postValue(0)
            }
        }
    }

    companion object {
        const val COUNTER_MAX_VALUE = 15
    }
}