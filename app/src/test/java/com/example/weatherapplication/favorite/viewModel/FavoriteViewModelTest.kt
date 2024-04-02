package com.example.weatherapplication.favorite.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.model.FakeRepository
import com.example.weatherapplication.model.StoreLatitudeLongitude
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest{

    private lateinit var fakeRepository: FakeRepository
    private lateinit var viewModel: FavoriteViewModel

    @get:Rule
    val myRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        viewModel = FavoriteViewModel(fakeRepository)
    }


    /**
     * This test function tests the insertion of a new favorite city into the view model.
     * It creates a new city object with latitude 31.21 and longitude 30.54, named "Menofia".
     * Then it inserts this city into the view model using the `insertFavorite` function.
     * After insertion, it retrieves the list of favorite locations from the view model and asserts
     * that the first location's name matches "Menofia".
     */
    @Test
     fun insertFavoriteTest() = runBlockingTest{

        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        viewModel.insertFavorite(newCity)

        var value = emptyList<StoreLatitudeLongitude>()

        val storedLocation = viewModel.favoriteLocation.first()

        when(storedLocation){
            is StateDB.Success -> {
                value = storedLocation.data
            }
            else->{}
        }

        assertEquals( "Menofia" , value.get(0).name)
    }


    /**
     * This test function tests the retrieval of locations from the database by the view model.
     * It calls the `getLocationsFromDB` function on the view model to fetch locations from the database.
     * After fetching, it retrieves the list of favorite locations from the view model and asserts that
     * the list is not null.
     */
    @Test
    fun getLocationsFromDBTest() = runBlockingTest {

        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        viewModel.insertFavorite(newCity)


        viewModel.getLocationsFromDB()

        var value = emptyList<StoreLatitudeLongitude>()

        val storedLocation = viewModel.favoriteLocation.first()
        when (storedLocation) {
            is StateDB.Success -> {
                value = storedLocation.data
            }

            else -> {}
        }
        assertThat(value, `is`(notNullValue()))
    }


    /**
     * This test function tests the deletion of a location from the view model.
     * It first inserts a new city named "Menofia" with latitude 31.21 and longitude 30.54
     * into the view model using the `insertFavorite` function.
     * Then, it retrieves the list of favorite locations from the view model and verifies
     * that the list is not null.
     * After that, it deletes the previously inserted city using the `deleteLocation` function.
     * Finally, it asserts that the deleted city is no longer present in the list of favorite locations.
     */
    @Test
    fun deleteLocationTest() = runBlockingTest {

        val newCity = StoreLatitudeLongitude(31.21 , 30.54 ,"Menofia" )
        viewModel.insertFavorite(newCity)


        var value = emptyList<StoreLatitudeLongitude>()

        val storedLocation = viewModel.favoriteLocation.first()

        when (storedLocation) {
            is StateDB.Success -> {
                value = storedLocation.data
            }

            else -> {}
        }

        assertThat(value, `is`(notNullValue()))
        viewModel.deleteLocation(newCity)
        assertFalse(value.equals(newCity))
    }


}