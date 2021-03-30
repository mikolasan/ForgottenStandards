package io.github.mikolasan.imperialrussia

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SwitchFragment : Fragment(R.layout.fragment_switch) {

//    companion object {
//        fun newInstance() = SwitchFragment()
//    }

    private lateinit var viewModel: SwitchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_switch, container, false)
        view.findViewById<TextView>(R.id.test_text).setOnClickListener{
            (activity as MainActivity).hideTypeSwitcher()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SwitchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}