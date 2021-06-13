package com.example.shopy.ui.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.*
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentCategoryBinding
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.domainLayer.Utils


class CategoryFragment : Fragment(), MainCategoryRecyclerClick, SubCategoryRecyclerClick {
    var mainCategoryIndex=0
    var subCategoryIndex=0
    var colID:Long=268359696582
    lateinit var catViewModel:CategoriesViewModel
    lateinit var products:List<Product>
    lateinit var subList:List<Product>
    lateinit var  subcatList:Array<String>
    private var _binding: FragmentCategoryBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val remoteDataSource = RemoteDataSourceImpl()
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        catViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CategoriesViewModel::class.java)
        return view
    }

    override fun onSubClick(position: Int) {
        super.onSubClick(position)
        subCategoryIndex=position
        subList=getSubCategoryItems(position)
        binding.itemsRecView.adapter= CategoryItemAdapter(subList,requireContext())
        binding.subcategoriesRecView.adapter!!.notifyDataSetChanged()
    }
    override fun onMainClick(position: Int) {
        super.onMainClick(position)
        binding.subcategoriesRecView.adapter=SubCategoriesAdapter(subcatList,this)
        binding.subcategoriesRecView.adapter!!.notifyDataSetChanged()
        mainCategoryIndex=position
        colID=getMainCategory(position)
        catViewModel.fetchCatProducts(colID).observe(requireActivity(),{
            products=it
            binding.itemsRecView.adapter= CategoryItemAdapter(products,requireContext())
            Log.d("hitler","list size: "+it.size)
            binding.itemsRecView.adapter!!.notifyDataSetChanged()
        })
        binding.mainCategoriesRecView.adapter!!.notifyDataSetChanged()

        /*binding.mainCategoriesRecView.findViewHolderForAdapterPosition(position)!!.itemView.underLine.background=
            ColorDrawable(Color.parseColor("#ffffff"))*/
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(Utils.toolbarImg.visibility == View.GONE){
            Utils.toolbarImg.visibility = View.VISIBLE
        }
        if(Utils.cartView.visibility == View.GONE){
            Utils.cartView.visibility = View.VISIBLE
        }
        subcatList= arrayOf("Shoes","Accessories","T-Shirts")
        var mainCatList= arrayOf("home","kids","men","sales","women")
        binding.subcategoriesRecView.adapter= SubCategoriesAdapter(subcatList,this)
        binding.mainCategoriesRecView.adapter= MainCategoriesAdapter(mainCatList,this)
        catViewModel.fetchCatProducts(267715608774).observe(requireActivity(),{
            products=it
            binding.itemsRecView.adapter= CategoryItemAdapter(products,requireContext())
        })
    }

    fun getMainCategory(position:Int):Long{
        var main:Long=0
        when(position){
            0-> main=267715608774
            1-> main=268359663814
            2-> main=268359598278
            3-> main=268359696582
            4-> main=268359631046
            else-> main=0
        }
        return main
    }
    fun getSubCategoryItems(position:Int):List<Product>{
        lateinit var subCatList:List<Product>
        when(position){
            0-> subCatList=products.filter { it.productType.equals("SHOES")}
            1-> subCatList=products.filter { it.productType.equals("ACCESSORIES")}
            2-> subCatList=products.filter { it.productType.equals("T-SHIRTS")}
            else-> subCatList=products.filter { it.productType.equals("SHOES")}
        }
        return subCatList
    }

}