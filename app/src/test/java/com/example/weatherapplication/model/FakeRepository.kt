package com.example.weatherapplication.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : Repository {

    val fakeLocationsList = mutableListOf<StoreLatitudeLongitude>()
    val fakeWeatherList = mutableListOf<Model>()
    val fakeAlertsList = mutableListOf<AlertNotification>()
    val fakeWeatherModel = Model(
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

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Model {
        return Model(
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
    }

    override suspend fun getFavoriteLocations(): Flow<List<StoreLatitudeLongitude>> {
        return flowOf(fakeLocationsList)
    }

    override suspend fun insertToFavorite(location: StoreLatitudeLongitude) {
        fakeLocationsList.add(location)
    }

    override suspend fun removeFromFavorite(favoritePlaces: StoreLatitudeLongitude) {
        fakeLocationsList.removeAll{
           it == favoritePlaces
       }
    }

    override suspend fun getCurrentWeather(): Flow<Model> {
        return flowOf(fakeWeatherModel)
    }

    override suspend fun insertCurrentWeather(model: Model) {
        fakeWeatherList.add(model)
    }

    override suspend fun getAllSavedAlerts(): Flow<List<AlertNotification>> {
        return flowOf(fakeAlertsList)
    }

    override suspend fun insertNotification(alert: AlertNotification) {
        fakeAlertsList.add(alert)
    }

    override suspend fun deleteNotification(alert: AlertNotification) {
        fakeAlertsList.removeAll{
            it == alert
        }
    }
}