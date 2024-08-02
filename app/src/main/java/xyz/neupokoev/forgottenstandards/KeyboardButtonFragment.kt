package xyz.neupokoev.forgottenstandards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class KeyboardButtonFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.keyboard_button_fragment, container, false)
        setButtonListeners(view)
        return view
    }

    private fun setButtonListeners(view: View) {
        view.run {
            findViewById<ImageView>(R.id.show_keyboard_button).setOnClickListener {
                (activity as MainActivity).showKeyboard()
            }
        }
    }
}