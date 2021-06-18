package com.example.shopy.ui.customerProfile

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerProfile
import com.example.shopy.models.CustomerX
import com.example.shopy.models.CustomerXXX
import com.example.shopy.ui.customerAddress.AddressViewModel
import com.example.shopy.ui.profileFragment.ProfileViewModel
import com.example.shopy.utils.CoroutineTestRule
import com.example.shopy.utils.getOrAwaitValue
import io.mockk.*
import org.junit.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ProfileViewModelTest  {

    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val taskRule = InstantTaskExecutorRule()


    private lateinit var repository: IRepository
    private lateinit var viewModel: ProfileViewModel
    private lateinit var applicationMock: Application

    var customer = Customer("ola","null", "null")
    var customerx = CustomerX(customer)
    var customerxxx = CustomerXXX("ola",1234, "null","01023657698")
    var customerProfile = CustomerProfile(customerxxx)

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        applicationMock = Mockito.mock(Application::class.java)
    }

    @Test
    fun testWhenViewModelInit_VerifyGetCustomerDataCalled() {
        // Given
        coEvery { repository.getCustomer("123456789") } returns mockk()

        // When
        viewModel = ProfileViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getCustomer("123456789") }
    }

    @Test
    fun testWhenViewModelInit_VerifyUpdateCustomerCalled() {
        // Given
        coEvery {
            if (customerx != null) {
                repository.updateCustomer("1234567",customerProfile)
            }
        } returns mockk()

        // When
        viewModel = ProfileViewModel(repository, applicationMock)

        // Then
        coVerify {
            if (customerx != null) {
                repository.updateCustomer("1234567",customerProfile)
            }
        }
    }

    @Test
    fun testWhenCreateCustomersReturnedSuccessfully_VerifyCreateCustomersChanged() {
        coEvery {

            repository.getCustomer("123456789")

        } returns customerx

        viewModel = ProfileViewModel(repository, applicationMock)

        Assert.assertEquals(customerx, viewModel.getPostResult().getOrAwaitValue())
    }
    @Test
    fun testWhenCreateCustomersReturnedError_VerifyShowErrorMessage() {
        coEvery {

            repository.getCustomer("123456789")

        } returns null

        viewModel = ProfileViewModel(repository, applicationMock)

        Assert.assertEquals(null, viewModel.getPostResult().getOrAwaitValue())
    }

    @Test
    fun testProfileViewModelFactory() {
        coEvery { repository.fetchCustomersData() } returns mockk()

        val viewModel =
            ViewModelFactory(repository, applicationMock).create(ProfileViewModel::class.java)

        assert(viewModel is ProfileViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testProfileViewModelFactoryOfUnknownType_VerifyExceptionThrown() {
        ViewModelFactory(repository, applicationMock).create(AddressViewModel::class.java)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

}