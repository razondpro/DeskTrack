package com.wagit.desktrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

//

/**
 * Base fragment class to reduce boilerplate code
 * @see https://manavtamboli.medium.com/reusability-in-android-fragment-with-data-binding-60fdc1721da
 */
abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId : Int) : Fragment(){

    private var _binding : T? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding : T get() = _binding!!

    abstract fun T.initialize()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

        // Optionally set lifecycle owner if needed
        binding.lifecycleOwner = viewLifecycleOwner

        // Calling the extension function
        binding.initialize()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}