package com.example.weatherapplication.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.dayone.Five.WeatherDataBase
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.Current
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.model.Weather
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var database: WeatherDataBase
    private lateinit var dao: WeatherDao
    private lateinit var model: Model

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao =  database.getWeatherDao()
    }
    @After
    fun teardown() {
        database.close()
    }


    /**
     * Test to verify that getAllSavedLocations retrieves the inserted locations correctly.
     * Inserts a new city into the database, retrieves the stored locations, and asserts that the retrieved
     * location matches the inserted one.
     */
    @Test
    fun getAllSavedLocationsTest_retrievesLocations() = runBlockingTest {
        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        dao.insertLocation(newCity)
        val storedLocation = dao.getAllSavedLocations().first()
        assertThat(storedLocation.get(0) , equalTo(newCity))
    }


    /**
     * Test to verify that insertLocation correctly stores a new city and retrieves it.
     * Inserts a new city into the database, retrieves the stored locations, and asserts that the name
     * of the retrieved location matches the name of the inserted city.
     */
    @Test
    fun insertLocationTest_retrievesLocations() = runBlockingTest {
        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        dao.insertLocation(newCity)
        val storedLocation = dao.getAllSavedLocations().first()
        assertThat(storedLocation.get(0).name , `is`("Menofia"))

    }


    /**
     * Test to verify that deleting a location from the database returns false when attempting to retrieve it.
     * Inserts a new city into the database, retrieves the stored locations, asserts that the new city is contained
     * in the stored locations, deletes the new city from the database, and asserts that the stored locations no
     * longer contain the deleted city.
     */
    @Test
    fun deleteLocationTest_retrievesFalse() = runBlockingTest {
        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        dao.insertLocation(newCity)
        val storedLocations = dao.getAllSavedLocations().first()
        assertTrue(storedLocations.contains(newCity))
        dao.deleteLocation(newCity)
        assertFalse(storedLocations.equals(newCity))
    }


    /**
     * Test to verify that getCurrentWeather retrieves the correct weather data from the database.
     * Inserts a random weather model into the database, retrieves the stored weather, and asserts
     * that it matches the inserted weather model.
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
        dao.insertCurrentWeather(randomWeather)
        val storedWeather = dao.getCurrentWeather().first()
        assertThat(storedWeather, equalTo(randomWeather))
    }


    /**
     * Test to verify that inserting current weather into the database retrieves the correct weather data.
     * Inserts a random weather model into the database, retrieves the stored weather, and asserts
     * that it matches the inserted weather model.
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
        dao.insertCurrentWeather(randomWeather)

        val storedWeather = dao.getCurrentWeather().first()

        assertThat(storedWeather, equalTo(randomWeather))
    }



}