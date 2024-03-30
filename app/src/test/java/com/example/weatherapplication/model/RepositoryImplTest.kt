package com.example.weatherapplication.model

import com.example.weatherapplication.db.FakeWeatherLocalDataSource
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.remoteDataSource.FakeWeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test


class RepositoryImplTest{

    lateinit var weatherRemoteDataSource: WeatherRemoteDataSource
    lateinit var weatherLocalDataSource: WeatherLocalDataSource
    lateinit var repository: RepositoryImpl

    @Before
    fun setup(){
        weatherRemoteDataSource = FakeWeatherRemoteDataSource()
        weatherLocalDataSource = FakeWeatherLocalDataSource()
        repository = RepositoryImpl(
            weatherRemoteDataSource,
            weatherLocalDataSource
        )
    }
    @Test
    fun insertToFavorite() = runBlockingTest {
        val fakeLocation = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        repository.insertToFavorite(fakeLocation)
        val storedLocations = weatherLocalDataSource.getAllStoredLocations()
        val result = storedLocations.first()
        assertEquals(true , result.contains(fakeLocation))
    }

    @Test
    fun getFavoriteLocations() = runBlockingTest {
        val fakeLocation = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        weatherLocalDataSource.insertLocationInRoom(fakeLocation)

        val storedLocations = repository.getFavoriteLocations()
        val result = storedLocations.first()

        assertEquals(true , result.contains(fakeLocation))
    }

    @Test
    fun removeFromFavorite() = runBlockingTest {
        val fakeLocation = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        weatherLocalDataSource.insertLocationInRoom(fakeLocation)

        val storedLocations = weatherLocalDataSource.getAllStoredLocations().first()
        assertTrue(storedLocations.contains(fakeLocation))
        repository.removeFromFavorite(fakeLocation)
        TestCase.assertFalse(storedLocations.equals(fakeLocation))

    }

    @Test
    fun getCurrentWeather() = runBlockingTest {
        val randomWeather = Model(
            id = 1,
            alerts = listOf(
                Alert(
                    description = "Sample Alert",
                    end = 123456789,
                    event = "Sample Event",
                    sender_name = "Sample Sender",
                    start = 987654321,
                    tags = listOf("Tag1", "Tag2")
                )
            ),
            current = Current(
                clouds = 50,
                dew_point = 10.5,
                dt = 1616761200,
                feels_like = 15.2,
                humidity = 80,
                pressure = 1010,
                sunrise = 1616745600,
                sunset = 1616790000,
                temp = 20.5,
                uvi = 7.8,
                visibility = 10000,
                weather = listOf(
                    Weather(
                        description = "Sample Weather",
                        icon = "01d",
                        id = 800,
                        main = "Clear"
                    )
                ),
                wind_deg = 180,
                wind_speed = 5.5
            ),
            daily = emptyList(),
            hourly = emptyList(),
            lat = 37.7749,
            lon = -122.4194,
            timezone = "America/Los_Angeles",
            timezone_offset = -25200
        )
        weatherLocalDataSource.insertCurrentWeather(randomWeather)
        val storedWeather = repository.getCurrentWeather().first()
        MatcherAssert.assertThat(storedWeather, equalTo(randomWeather))

    }

    @Test
    fun insertCurrentWeather_retrievesWeather() = runBlockingTest {
        val randomWeather = Model(
            id = 1,
            alerts = listOf(
                Alert(
                    description = "Sample Alert",
                    end = 123456789,
                    event = "Sample Event",
                    sender_name = "Sample Sender",
                    start = 987654321,
                    tags = listOf("Tag1", "Tag2")
                )
            ),
            current = Current(
                clouds = 50,
                dew_point = 10.5,
                dt = 1616761200,
                feels_like = 15.2,
                humidity = 80,
                pressure = 1010,
                sunrise = 1616745600,
                sunset = 1616790000,
                temp = 20.5,
                uvi = 7.8,
                visibility = 10000,
                weather = listOf(
                    Weather(
                        description = "Sample Weather",
                        icon = "01d",
                        id = 800,
                        main = "Clear"
                    )
                ),
                wind_deg = 180,
                wind_speed = 5.5
            ),
            daily = emptyList(),
            hourly = emptyList(),
            lat = 37.7749,
            lon = -122.4194,
            timezone = "America/Los_Angeles",
            timezone_offset = -25200
        )

        repository.insertCurrentWeather(randomWeather)

        val storedWeather = weatherLocalDataSource.getCurrentWeather().first()

        MatcherAssert.assertThat(storedWeather, equalTo(randomWeather))
    }

    @Test
    fun getAllSavedAlerts() = runBlockingTest {
        val fakeAlert = AlertNotification(1711760033)
        weatherLocalDataSource.insertNotification(fakeAlert)
        val storedNotification = repository.getAllSavedAlerts()
        val result = storedNotification.first()
        assertEquals(true , result.contains(fakeAlert))

    }

    @Test
    fun insertNotification() = runBlockingTest {
        val fakeAlert = AlertNotification(1711760033)
        repository.insertNotification(fakeAlert)

        val storedNotification = weatherLocalDataSource.getAllSavedAlerts()
        val result = storedNotification.first()
        assertEquals(true , result.contains(fakeAlert))

    }

    @Test
    fun deleteNotification() = runBlockingTest {
        val fakeAlert = AlertNotification(1711760033)
        weatherLocalDataSource.insertNotification(fakeAlert)

        val storedNotification = weatherLocalDataSource.getAllSavedAlerts()
        val result = storedNotification.first()

        assertTrue(result.contains(fakeAlert))
        repository.deleteNotification(fakeAlert)
        assertFalse(storedNotification.equals(fakeAlert))

    }
}