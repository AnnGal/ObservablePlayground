package an.gal.android.observableplayground

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ObservableViewModel : ViewModel() {

    private val eventChannel = Channel<UIEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    private val _timerLiveData = MutableLiveData<Int?>(null)
    val timerLiveData get(): LiveData<Int?> = _timerLiveData

    private val _stateFlow = MutableStateFlow(WeatherHazardState.GREEN)
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow  = _sharedFlow.asSharedFlow()

    fun showChannelSnackbar(message: String) = viewModelScope.launch {
        eventChannel.send(UIEvent.SnackbarEvent(message))
    }

    fun startLiveDataSimpleTimer() {
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

    fun getRandomWeatherState(){
        viewModelScope.launch {
            var res = WeatherHazardState.getRandom()

            while (res.stateName == stateFlow.value.stateName) {
                res = WeatherHazardState.getRandom()
            }

            _stateFlow.emit(res)
        }
    }

    companion object {
        const val COUNTER_MAX_VALUE = 15
    }
}