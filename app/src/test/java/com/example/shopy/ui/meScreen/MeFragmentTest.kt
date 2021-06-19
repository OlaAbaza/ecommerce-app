package com.example.shopy.ui.meScreen

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.utils.CoroutineTestRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Callable


class MeFragmentTest : TestCase() {



    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    @Rule
    var rxSchedulerRule = RxSchedulerRule()

    private lateinit var repository: IRepository
    private lateinit var applicationMock: Application
    private lateinit var viewModel: MeViewModel



    @Before
    public override fun setUp() {
        super.setUp()
        repository = mockk(relaxed = true)
        applicationMock = Mockito.mock(Application::class.java)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }

    }

    @Test
    fun testGetFourItemFromWithList(){

        coEvery { repository.getFourFromWishList() } returns mockk()

        viewModel = MeViewModel(repository, applicationMock)
        val observer=mockk<Observer<List<Product>>>()
        viewModel.getFourWishList().observeForever(observer)

        coVerify  { viewModel.getFourWishList() }
    }

    @After
    public override fun tearDown() {
        clearAllMocks()
    }
}