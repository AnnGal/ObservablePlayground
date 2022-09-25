package an.gal.android.observableplayground

sealed class UIEvent {
    data class SnackbarEvent(val message: String): UIEvent()
}
