package xyz.neupokoev.forgottenstandards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.noties.markwon.Markwon
import java.io.IOException

class DescriptionFragment : BottomSheetDialogFragment() {

    private var unitName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            unitName = it.getString(ARG_UNIT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.description_text)
        val name = unitName ?: return

        val description = getDescription(name)
        val markwon = Markwon.create(requireContext())
        markwon.setMarkdown(textView, description)
    }

    private fun getDescription(name: String): String {
        // 1. Try to get from strings.xml
        val resId = resources.getIdentifier("desc_$name", "string", requireContext().packageName)
        if (resId != 0) {
            return getString(resId)
        }

        // 2. Fallback to assets
        return try {
            requireContext().assets.open("$name.txt").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            "No description available for $name"
        }
    }

    companion object {
        private const val ARG_UNIT_NAME = "unit_name"

        @JvmStatic
        fun newInstance(unitName: ImperialUnitName) =
            DescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UNIT_NAME, unitName.name)
                }
            }
    }
}
