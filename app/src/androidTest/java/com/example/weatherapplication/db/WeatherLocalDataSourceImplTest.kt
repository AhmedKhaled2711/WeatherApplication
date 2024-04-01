package com.example.weatherapplication.db

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.dayone.Five.WeatherDataBase
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.Current
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.model.Weather
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceImplTest{

    private lateinit var database: WeatherDataBase
    private lateinit var localDataSource: WeatherLocalDataSource

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        val app: Application = ApplicationProvider.getApplicationContext()
        localDataSource = WeatherLocalDataSourceImpl(app)

    }
    @After
    fun teardown() {
        database.close()
    }


    /**
     * Test to verify the functionality of deleting a location from the Room database.
     * It inserts a new location, retrieves all stored locations, checks if the new location exists,
     * deletes the new location, and verifies that it no longer exists in the stored locations.
     */
    @Test
    fun deleteLocationInRoomTest() = runBlockingTest {
        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        localDataSource.insertLocationInRoom(newCity)
        val storedLocations = localDataSource.getAllStoredLocations().first()
        assertTrue(storedLocations.contains(newCity))
        localDataSource.deleteLocationInRoom(newCity)
        assertFalse(storedLocations.equals(newCity))
    }


    /**
     * Test to verify the functionality of retrieving all stored locations from the Room database.
     * It inserts a new location, retrieves all stored locations, and checks if the inserted location
     * exists among the retrieved locations.
     */
    @Test
    fun getAllStoredLocations_retrievesLocations() = runBlockingTest {
        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        localDataSource.insertLocationInRoom(newCity)
        val storedLocation = localDataSource.getAllStoredLocations().first()
        assertThat(storedLocation.get(0), equalTo(newCity))
    }


    /**
     * Test to verify the functionality of inserting a location into the Room database.
     * It inserts a new location and then retrieves all stored locations to check if the inserted
     * location exists among them, specifically verifying if the name matches the expected value.
     */
    @Test
    fun insertLocationInRoomTest_retrievesLocations() = runBlockingTest {
        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        localDataSource.insertLocationInRoom(newCity)
        val storedLocation = localDataSource.getAllStoredLocations().first()
        assertThat(storedLocation.get(0).name, `is`("Menofia"))

    }


    /**
     * Test to verify the functionality of inserting current weather data into the Room database.
     * It inserts random weather data and then retrieves the stored current weather to check if it matches
     * the inserted data.
     */
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
        localDataSource.insertCurrentWeather(randomWeather)

        val storedWeather = localDataSource.getCurrentWeather().first()

        assertThat(storedWeather, equalTo(randomWeather))
    }


    /**
     * Test to verify the functionality of retrieving current weather data from the Room database.
     * It inserts random weather data into the database and then retrieves the stored current weather
     * to check if it matches the inserted data.
     */
    @Test
    fun getCurrentWeather_retrievesWeather() = runBlockingTest{
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
        localDataSource.insertCurrentWeather(randomWeather)
        val storedWeather = localDataSource.getCurrentWeather().first()
        assertThat(storedWeather, equalTo(randomWeather))
    }




}