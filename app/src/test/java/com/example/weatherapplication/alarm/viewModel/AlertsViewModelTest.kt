package com.example.weatherapplication.alarm.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.StateNotification
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModel
import com.example.weatherapplication.model.AlertNotification
import com.example.weatherapplication.model.FakeRepository
import com.example.weatherapplication.model.StoreLatitudeLongitude
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertsViewModelTest{

    private lateinit var fakeRepository: FakeRepository
    private lateinit var viewModel: AlertsViewModel

    @get:Rule
    val myRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        viewModel = AlertsViewModel(fakeRepository)
    }


    /**
     * This test function tests the insertion of a new alert notification into the view model.
     * It creates a new alert notification object with a time value of 12547834 and inserts it
     * into the view model using the `insertAlerts` function.
     * After insertion, it retrieves the list of alert notifications from the view model and
     * asserts that the first notification's time matches the expected value.
     */
    @Test
    fun insertAlerts_objectOfAlert_unit() = runBlockingTest{

        val newAlert = AlertNotification(12547834)
        viewModel.insertAlerts(newAlert)

        var value = emptyList<AlertNotification>()

        val storedLocation = viewModel.alertNotifications.first()

        when(storedLocation){
            is StateNotification.Success -> {
                value = storedLocation.data
            }
            else->{}
        }
        TestCase.assertEquals( 12547834 , value.get(0).time)
    }


    /**
     * This test function tests the retrieval of alert notifications from the view model.
     * It calls the `getAlerts` function on the view model to fetch alert notifications.
     * After fetching, it retrieves the list of alert notifications from the view model and
     * asserts that the list is not null.
     */
    @Test
    fun getAlerts_retrievesAlerts() = runBlockingTest {

        viewModel.getAlerts()

        var value = emptyList<AlertNotification>()

        val storedLocation = viewModel.alertNotifications.first()
        when (storedLocation) {
            is StateNotification.Success -> {
                value = storedLocation.data
            }

            else -> {}
        }
        assertThat(value, CoreMatchers.`is`(CoreMatchers.notNullValue()))
    }


    /**
     * This test function tests the deletion of an alert notification from the view model.
     * It first inserts a new alert notification with a time value of 12547834 into the view model
     * using the `insertAlerts` function.
     * Then, it retrieves the list of alert notifications from the view model and verifies
     * that the list is not null.
     * After that, it deletes the previously inserted alert notification using the `deleteAlert` function.
     * Finally, it asserts that the deleted alert notification is no longer present in the list
     * of alert notifications.
     */
    @Test
    fun deleteAlert_objectOfAlert_unit() = runBlockingTest {

        val newAlert = AlertNotification(12547834)
        viewModel.insertAlerts(newAlert)

        var value = emptyList<AlertNotification>()

        val storedLocation = viewModel.alertNotifications.first()

        when (storedLocation) {
            is StateNotification.Success -> {
                value = storedLocation.data
            }

            else -> {}
        }

        assertThat(value, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        viewModel.deleteAlert(newAlert)
        TestCase.assertFalse(value.equals(newAlert))
    }
}