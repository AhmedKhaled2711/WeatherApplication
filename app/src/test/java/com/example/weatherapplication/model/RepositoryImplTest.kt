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
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class RepositoryImplTest{

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource
    private lateinit var weatherLocalDataSource: WeatherLocalDataSource
    private lateinit var repository: RepositoryImpl

    @Before
    fun setup(){
        weatherRemoteDataSource = FakeWeatherRemoteDataSource()
        weatherLocalDataSource = FakeWeatherLocalDataSource()
        repository = RepositoryImpl(
            weatherRemoteDataSource,
            weatherLocalDataSource
        )
    }

    /**
     * This test function tests the insertion of a location into the favorite locations database.
     * It first creates a fake location object with latitude 31.21 and longitude 30.54, named "Menofia".
     * Then, it inserts this fake location into the favorite locations database via the repository.
     * After insertion, it retrieves all stored locations from the database and checks if the inserted
     * location is present among them. It asserts that the result contains the fake location.
     */
    @Test
    fun insertToFavorite_objectOfCity_unit() = runBlockingTest {
        val fakeLocation = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        repository.insertToFavorite(fakeLocation)
        val storedLocations = weatherLocalDataSource.getAllStoredLocations()
        val result = storedLocations.first()
        assertEquals(true , result.contains(fakeLocation))
    }


    /**
     * This test function tests the retrieval of favorite locations from the repository.
     * It first inserts a fake location object with latitude 31.21 and longitude 30.54, named "Menofia",
     * into the favorite locations database via the weather local data source.
     * Then, it retrieves the favorite locations from the repository using the `getFavoriteLocations` function.
     * After retrieval, it checks if the inserted fake location is present among the retrieved locations.
     * It asserts that the result contains the fake location.
     */
    @Test
    fun getFavoriteLocations_retrievesLocations() = runBlockingTest {
        val fakeLocation = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        weatherLocalDataSource.insertLocationInRoom(fakeLocation)

        val storedLocations = repository.getFavoriteLocations()
        val result = storedLocations.first()

        assertEquals(true , result.contains(fakeLocation))
    }


    /**
     * This test function tests the removal of a location from the favorite locations database.
     * It first inserts a fake location object with latitude 31.21 and longitude 30.54, named "Menofia",
     * into the favorite locations database via the weather local data source.
     * Then, it retrieves all stored locations from the database.
     * It asserts that the retrieved locations contain the fake location.
     * After that, it removes the fake location from the favorite locations database via the repository.
     * Finally, it asserts that the fake location is no longer present among the retrieved locations.
     */
    @Test
    fun removeFromFavorite_objectOfCity_unit() = runBlockingTest {
        val fakeLocation = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        weatherLocalDataSource.insertLocationInRoom(fakeLocation)

        val storedLocations = weatherLocalDataSource.getAllStoredLocations().first()
        assertTrue(storedLocations.contains(fakeLocation))
        repository.removeFromFavorite(fakeLocation)
        TestCase.assertFalse(storedLocations.equals(fakeLocation))

    }


    /**
     * This test function tests the retrieval of the current weather from the repository.
     * It creates a random weather object containing various weather-related data.
     * Then, it inserts this random weather object into the current weather database via the weather local data source.
     * After insertion, it retrieves the current weather from the repository using the `getCurrentWeather` function.
     * Finally, it asserts that the retrieved weather object matches the random weather object.
     */
    @Test
    fun getCurrentWeather_retrievesWeather() = runBlockingTest {
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


    /**
     * This test function tests the insertion of current weather into the current weather database.
     * It creates a random weather object containing various weather-related data.
     * Then, it inserts this random weather object into the current weather database via the repository.
     * After insertion, it retrieves the current weather from the database using the `getCurrentWeather` function
     * of the weather local data source.
     * Finally, it asserts that the retrieved weather object matches the random weather object.
     */
    @Test
    fun insertCurrentWeather_objectOfWeather_unit() = runBlockingTest {
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

        assertThat(storedWeather, equalTo(randomWeather))
    }


    /**
     * This test function tests the retrieval of all saved alerts from the repository.
     * It first inserts a fake alert notification object with a time value of 1711760033 into the
     * notification database via the weather local data source.
     * Then, it retrieves all saved alerts from the repository using the `getAllSavedAlerts` function.
     * After retrieval, it checks if the inserted fake alert notification is present among the retrieved alerts.
     * It asserts that the result contains the fake alert notification.
     */
    @Test
    fun getAllSavedAlerts_retrievesAlerts() = runBlockingTest {
        val fakeAlert = AlertNotification(1711760033)
        weatherLocalDataSource.insertNotification(fakeAlert)
        val storedNotification = repository.getAllSavedAlerts()
        val result = storedNotification.first()
        assertEquals(true , result.contains(fakeAlert))

    }


    /**
     * This test function tests the insertion of a notification into the notification database.
     * It creates a fake alert notification object with a time value of 1711760033.
     * Then, it inserts this fake alert notification into the notification database via the repository.
     * After insertion, it retrieves all saved alerts from the database using the `getAllSavedAlerts` function
     * of the weather local data source.
     * Finally, it asserts that the retrieved alerts contain the fake alert notification.
     */
    @Test
    fun insertNotificationTest_objectOfNotification_unit() = runBlockingTest {
        val fakeAlert = AlertNotification(1711760033)
        repository.insertNotification(fakeAlert)

        val storedNotification = weatherLocalDataSource.getAllSavedAlerts()
        val result = storedNotification.first()
        assertEquals(true , result.contains(fakeAlert))

    }


    /**
     * This test function tests the deletion of a notification from the notification database.
     * It first inserts a fake alert notification object with a time value of 1711760033 into the
     * notification database via the weather local data source.
     * Then, it retrieves all saved alerts from the database.
     * It asserts that the retrieved alerts contain the fake alert notification.
     * After that, it deletes the fake alert notification from the notification database via the repository.
     * Finally, it asserts that the fake alert notification is no longer present among the retrieved alerts.
     */
    @Test
    fun deleteNotification_objectOfNotification_unit() = runBlockingTest {
        val fakeAlert = AlertNotification(1711760033)
        weatherLocalDataSource.insertNotification(fakeAlert)

        val storedNotification = weatherLocalDataSource.getAllSavedAlerts()
        val result = storedNotification.first()

        assertTrue(result.contains(fakeAlert))
        repository.deleteNotification(fakeAlert)
        assertFalse(storedNotification.equals(fakeAlert))

    }
}