package xyz.neupokoev.forgottenstandards.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
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

        val themeSpinner: Spinner = view.findViewById(R.id.theme_spinner)
        val themes = arrayOf("System Default", "Light", "Dark")
        val themeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themes)
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = themeAdapter

        // Set current selection based on preference
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        themeSpinner.setSelection(when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> 1
            AppCompatDelegate.MODE_NIGHT_YES -> 2
            else -> 0
        })

        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val mode = when (position) {
                    1 -> AppCompatDelegate.MODE_NIGHT_NO
                    2 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                if (mode != AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
