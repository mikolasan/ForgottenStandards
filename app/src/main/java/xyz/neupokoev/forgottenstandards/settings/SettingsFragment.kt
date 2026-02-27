package xyz.neupokoev.forgottenstandards.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val languageSpinner: Spinner = view.findViewById(R.id.language_spinner)
        val languages = arrayOf("English", "Russian")
        val langAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = langAdapter

        val localeSpinner: Spinner = view.findViewById(R.id.locale_spinner)
        val locales = arrayOf("System Default", "United States", "Russia")
        val localeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locales)
        localeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        localeSpinner.adapter = localeAdapter

        val appNameVersion: TextView = view.findViewById(R.id.app_name_version)
        val appName = getString(R.string.app_name)
        val versionName = try {
            val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            pInfo.versionName
        } catch (e: Exception) {
            "1.0.0"
        }
        appNameVersion.text = getString(R.string.settings_app_name_version, appName, versionName)

        (activity as? MainActivity)?.setSubscriber(this)

        return view
    }
}
