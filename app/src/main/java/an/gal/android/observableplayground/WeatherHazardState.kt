package an.gal.android.observableplayground

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import kotlin.random.Random

enum class WeatherHazardState(
    @StringRes val stateName: Int,
    @ColorRes val color: Int
) {
    GREEN(R.string.weather_state_green, R.color.weather_green),
    YELLOW(R.string.weather_state_yellow, R.color.weather_yellow),
    ORANGE(R.string.weather_state_orange, R.color.weather_orange),
    RED(R.string.weather_state_red, R.color.weather_red);

    companion object {
        fun getRandom(): WeatherHazardState {
            val list = values()
            return list[Random.nextInt(0,list.size)]
        }
    }
}
