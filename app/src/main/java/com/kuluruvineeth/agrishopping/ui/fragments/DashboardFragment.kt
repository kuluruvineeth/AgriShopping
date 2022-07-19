package com.kuluruvineeth.agrishopping.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kuluruvineeth.agrishopping.R
//import com.kuluruvineeth.agrishopping.activities.databinding.FragmentDashboardBinding
import com.kuluruvineeth.agrishopping.databinding.FragmentDashboardBinding
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.Product
import com.kuluruvineeth.agrishopping.ui.activities.SettingsActivity

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //If we want to use the option menu in fragment we need to add it
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>){
        hideProgressDialog()
        for(i in dashboardItemsList){
            Log.i("Item Title",i.title)
        }
    }

    private fun getDashboardItemsList(){
        //show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemsList(this)
    }
}