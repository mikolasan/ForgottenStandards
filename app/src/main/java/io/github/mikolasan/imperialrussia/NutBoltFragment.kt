package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NutBoltFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_nut_bolt, container, false)

//        val texture = view.findViewById<NutBoltView>(R.id.texture_view)
//        texture.setRenderer(surfaceTextureListener.renderer)
//        texture.surfaceTextureListener = surfaceTextureListener

        return view
    }

}