package xyz.neupokoev.forgottenstandards.advanced

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.neupokoev.forgottenstandards.R

class SlavicCalendarFragment : Fragment() {

    private lateinit var calendarRenderer: CalendarRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_slavic_calendar, container, false)
        
        val glView = view.findViewById<GlView>(R.id.calendar_gl_view)
        val dpi = glView.getDpi(requireContext())
        val refreshRate = glView.getDisplayRefreshRate(requireContext())
        calendarRenderer = CalendarRenderer(refreshRate, dpi)

        // Inject the CalendarRenderer into the generic GlView
        glView.renderer = calendarRenderer
        
        // Override GlView's touch event if necessary for calendar rotation, but for now we rely on the generic touch handling

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure the renderer thread stops cleanly
        calendarRenderer.stopRendering()
    }
}
