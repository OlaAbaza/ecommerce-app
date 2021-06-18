package com.example.shopy.ui.signin

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
import com.example.shopy.ui.customerAddress.AddressViewModel
import com.example.shopy.ui.signIn.SignInViewModel
import com.example.shopy.utils.CoroutineTestRule
import com.example.shopy.utils.getOrAwaitValue
import com.google.firebase.auth.FirebaseAuth
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class SignInViewModelTest {

    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val taskRule = InstantTaskExecutorRule()


    private lateinit var repository: IRepository
    private lateinit var viewModel: SignInViewModel
    private lateinit var applicationMock: Application

    var customer = Customer("ola", "ola2@gmail.com", "12345")
    var customerx: CustomerX? = CustomerX(customer)

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        applicationMock = Mockito.mock(Application::class.java)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
    }

    @Test
    fun testWhenViewModelInit_VerifyFetchCustomersDataCalled() {
        // Given
        coEvery { repository.fetchCustomersData() } returns mockk()

        // When
        viewModel = SignInViewModel(repository, applicationMock)

        // Then
        coVerify { repository.fetchCustomersData() }
    }

    @Test
    fun testWhenViewModelInit_VerifycreateCustomersCalled() {
        // Given
        coEvery {
            if (customerx != null) {
                repository.createCustomers(customerx!!)
            }
        } returns mockk()

        // When
        viewModel = SignInViewModel(repository, applicationMock)

        // Then
        coVerify {
            if (customerx != null) {
                repository.createCustomers(customerx!!)
            }
        }
    }

    @Test
    fun testWhenCreateCustomersReturnedSuccessfully_VerifyCreateCustomersChanged() {
        coEvery {

            repository.createCustomers(customerx!!)

        } returns customerx

        viewModel = SignInViewModel(repository, applicationMock)

        assertEquals(customerx, viewModel.getPostResult().getOrAwaitValue())
    }
    @Test
    fun testWhenCreateCustomersReturnedError_VerifyShowErrorMessage() {
        coEvery {

            repository.createCustomers(customerx!!)

        } returns null

        viewModel = SignInViewModel(repository, applicationMock)

        assertEquals(null, viewModel.getPostResult().getOrAwaitValue())
    }

    @Test
    fun testSignInViewModelFactory() {
        coEvery { repository.fetchCustomersData() } returns mockk()

        val viewModel =
            ViewModelFactory(repository, applicationMock).create(SignInViewModel::class.java)

        assert(viewModel is SignInViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSignInViewModelFactoryOfUnknownType_VerifyExceptionThrown() {
        ViewModelFactory(repository, applicationMock).create(AddressViewModel::class.java)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

}