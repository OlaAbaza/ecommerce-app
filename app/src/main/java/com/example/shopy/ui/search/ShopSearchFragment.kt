package com.example.shopy.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.myapplication.SearchCategoryItemAdapter
import com.example.shopy.R
import com.example.shopy.databinding.FragmentShopSearchBinding
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.entity.itemPojo.Product

import com.example.shopy.ui.shopTab.ShopTabViewModel


class ShopSearchFragment : Fragment() {

    init {
        setHasOptionsMenu(true)

    }
    lateinit var products:List<allProduct>
    lateinit var sortedProducts:List<allProduct>
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

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem: MenuItem? = menu?.findItem(R.id.fragmentsearch)
        val searchView : SearchView? = searchItem?.actionView as SearchView
        searchView?.setFocusable(true)
        searchView?.setIconified(false)


        val shopTabViewModel= ViewModelProvider(this).get(ShopTabViewModel::class.java)
        shopTabViewModel.fetchallProductsList().observe(viewLifecycleOwner,{
            Log.i("output","***********"+it.toString())
            products = it.products
            sortedProducts = products
            binding.itemsRecView.adapter=SearchCategoryItemAdapter(it.products,requireContext())

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

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("output","***********iiii")

                var filteredProducts = sortedProducts.filter { it.title!!.contains(query?:"none",true)&& it.productType!!.contains(productFilter,true)}
                binding.itemsRecView.adapter= SearchCategoryItemAdapter(filteredProducts,requireContext())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("output","***********ppp")

                var filteredProducts = sortedProducts.filter { it.title!!.contains(newText?:"none",true)&& it.productType!!.contains(productFilter,true) }//&& it.productType.equals("shoes",true)}
                binding.itemsRecView.adapter= SearchCategoryItemAdapter(filteredProducts,requireContext())
                return true
            }

        })

    }
}




