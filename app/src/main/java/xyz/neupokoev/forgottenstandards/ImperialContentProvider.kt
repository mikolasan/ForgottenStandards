package xyz.neupokoev.forgottenstandards

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.willowtreeapps.fuzzywuzzy.ToStringFunction
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import com.willowtreeapps.fuzzywuzzy.diffutils.algorithms.WeightedRatio
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import xyz.neupokoev.forgottenstandards.ImperialListAdapter.UnitToString
import java.util.Locale

class ImperialContentProvider(private val workingUnits: WorkingUnits) : ContentProvider() {
    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    class AllUnitsToString : ToStringFunction<ImperialUnit> {
        override fun apply(item: ImperialUnit): String {
            return item.unitName.name.lowercase(Locale.ROOT)
        }
    }

    override fun query(
        uri: Uri,
        projectionAlwaysNull: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrderAlswaysNull: String?
    ): Cursor {
        //val query: String = uri.lastPathSegment?.toLowerCase(Locale.ROOT) ?: ""
        val query: String = selectionArgs?.get(0) ?: ""
        val cursor = MatrixCursor(arrayOf("category", "unit", "short"))

        val filtered = FuzzySearch.extractSorted(
            query,
            workingUnits.allUnits.flatMap { mapEntry -> mapEntry.value.toList() },
            AllUnitsToString(),
            WeightedRatio(),
            50)
            .map { it.referent }

        filtered.forEach {

            cursor.addRow(arrayOf(it.unitType.name, it.unitName, ImperialSymbol.symbols[it.unitName] ?: ""))
        }
        return cursor
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}