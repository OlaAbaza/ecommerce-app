package com.example.shopy.ui.shopTab.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopy.R
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.ui.shopTab.ShopTabViewModel
import kotlinx.android.synthetic.main.fragment_shop_search.itemsRecView
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class ShopSearchFragment : Fragment() {

    init {
        setHasOptionsMenu(true)

    }
    private lateinit var searchListFromApi : Deferred<ArrayList<allProduct>>
    private  var searchList = ArrayList<allProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val shopTabViewModel= ViewModelProvider(this).get(ShopTabViewModel::class.java)

        shopTabViewModel.fetchallProductsList().observe(this, {
            if (it != null) {

                GlobalScope.launch {
                    searchListFromApi = async { it.products } as Deferred<ArrayList<allProduct>>
                    Log.i("search", "inside ShopSearch create " + searchListFromApi.await().size)
                    searchList.add(searchListFromApi.await().get(0))
                    //shopTabViewModel.shopSearchListLiveData.postValue(searchListFromApi.await())
                }

            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem: MenuItem? = menu?.findItem(R.id.fragmentsearch)
        val searchView : SearchView? = searchItem?.actionView as SearchView
        searchView?.setFocusable(true)
        searchView?.setIconified(false)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //itemsRecView.adapter = searchAdapter(this@MainActivity, searchList)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                GlobalScope.launch(Dispatchers.Main) {
                    if (newText!!.isNotEmpty()) {
                        searchList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        searchListFromApi.await().forEach {
                            if (it.productType.toLowerCase(Locale.getDefault()).contains(search)) {
                                searchList.add(it)
                            }
                        }
                        itemsRecView.adapter= searchAdapter(requireContext(),searchList)

                        //itemsRecView.adapter!!.notifyDataSetChanged()
                        Log.i("search","inside MainActivity searchFilter "+searchList.toString())


                    } else {
                        searchList.clear()
                        searchList.addAll(searchListFromApi.await())
                        //itemsRecView.adapter!!.notifyDataSetChanged()
                        itemsRecView.adapter= searchAdapter(requireContext(),searchList)

                    }

                }

                return true
            }

        })


    }

}

