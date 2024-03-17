package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(layoutInflater,container,false)

        binding.floatButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_saveFragment)
        }

        binding.search.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        return binding.root
    }

}