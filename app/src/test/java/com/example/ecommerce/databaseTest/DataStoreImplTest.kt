package com.example.ecommerce.databaseTest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataStoreImplTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var dataStore: DataStore<Preferences>

    private lateinit var dataStoreRepositoryImpl: com.example.core.datastore.DataStoreRepositoryImpl

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        dataStore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("preference")
        }
        dataStoreRepositoryImpl = com.example.core.datastore.DataStoreRepositoryImpl(dataStore)
    }

    @Test
    fun `test boarding data store`() = runBlocking {
        val expected = true
        // Save a boolean value to the DataStore
        dataStoreRepositoryImpl.saveOnBoardingState(expected)

        // Retrieve the boolean value from the DataStore
        val savedValue = dataStoreRepositoryImpl.getOnBoardingState().first()

        // Assert that the saved value matches the expected value
        assertEquals(true, savedValue)
    }

    @Test
    fun `test login data store`() = runBlocking {
        val expected = true
        // Save a boolean value to the DataStore
        dataStoreRepositoryImpl.saveHasLoginState(expected)

        // Retrieve the boolean value from the DataStore
        val savedValue = dataStoreRepositoryImpl.getLoginState().first()

        // Assert that the saved value matches the expected value
        assertEquals(expected, savedValue)
    }

    @Test
    fun `test profile data store`() = runBlocking {
        val expected = "name"
        // Save a boolean value to the DataStore
        dataStoreRepositoryImpl.saveProfileName(expected)

        // Retrieve the boolean value from the DataStore
        val savedValue = dataStoreRepositoryImpl.getProfileName().first()

        // Assert that the saved value matches the expected value
        assertEquals(expected, savedValue)
    }

    @Test
    fun `test access token data store`() = runBlocking {
        val expected = "token"
        // Save a boolean value to the DataStore
        dataStoreRepositoryImpl.saveAccessToken(expected)

        // Retrieve the boolean value from the DataStore
        val savedValue = dataStoreRepositoryImpl.getAccessToken().first()

        // Assert that the saved value matches the expected value
        assertEquals(expected, savedValue)
    }

    @Test
    fun `test token messaging data store`() = runBlocking {
        val expected = "token"
        // Save a boolean value to the DataStore
        dataStoreRepositoryImpl.saveTokenMessaging(expected)

        // Retrieve the boolean value from the DataStore
        val savedValue = dataStoreRepositoryImpl.getTokenMessaging().first()

        // Assert that the saved value matches the expected value
        assertEquals(expected, savedValue)
    }
}
