package io.github.mikolasan.imperialrussia

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SwitchFragment : Fragment() {

    companion object {
        fun newInstance() = SwitchFragment()
    }

    private lateinit var viewModel: SwitchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.switch_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SwitchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}