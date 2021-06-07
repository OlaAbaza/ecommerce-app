package com.example.shopy.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.myapplication.SearchCategoryItemAdapter
import com.example.shopy.databinding.FragmentShopSearchBinding
import com.example.shopy.datalayer.entity.itemPojo.Product

import com.example.shopy.ui.shopTab.ShopTabViewModel


class ShopSearchFragment : Fragment() {
    lateinit var products:List<Product>
    lateinit var sortedProducts:List<Product>
    var productFilter=""

    private var _binding: FragmentShopSearchBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shopTabViewModel= ViewModelProvider(this).get(ShopTabViewModel::class.java)
        shopTabViewModel.fetchAllProducts().observe(requireActivity(),{
            products= it
            sortedProducts=products
            binding.itemsRecView.adapter= SearchCategoryItemAdapter(products,requireContext())
        })

        binding.sortSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).equals("Alphabetically")){
                    sortedProducts = products.sortedBy { it.title }
                    // binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if (parent!!.getItemAtPosition(position).equals("price: Low to High")){
                    sortedProducts = products.sortedBy { it.variants!!.get(0).price}
                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if(parent!!.getItemAtPosition(position).equals("price: High to Low")){
                    sortedProducts = products.sortedByDescending { it.variants!!.get(0).price }
                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if (parent!!.getItemAtPosition(position).equals("none")) {
                    binding.itemsRecView.adapter = SearchCategoryItemAdapter(products, requireContext())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("hitler","NOTHING")
            }

        }

        binding.filterSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).equals("shoes")){
                    productFilter="shoes"
                }
                else if (parent!!.getItemAtPosition(position).equals("shirts")){
                    productFilter="shirts"
                }
                else if (parent!!.getItemAtPosition(position).equals("accessories")){
                    productFilter="accessories"
                }
                else if (parent!!.getItemAtPosition(position).equals("none")){
                    productFilter=""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                var filteredProducts = sortedProducts.filter { it.title!!.contains(query?:"none",true)&& it.product_type!!.contains(productFilter,true)}
                binding.itemsRecView.adapter= SearchCategoryItemAdapter(filteredProducts,requireContext())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var filteredProducts = sortedProducts.filter { it.title!!.contains(newText?:"none",true)&& it.product_type!!.contains(productFilter,true) }//&& it.productType.equals("shoes",true)}
                binding.itemsRecView.adapter= SearchCategoryItemAdapter(filteredProducts,requireContext())
                return true
            }

        })
    }
}
    /*init {
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


    }*/



