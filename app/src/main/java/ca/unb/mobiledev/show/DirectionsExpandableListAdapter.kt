import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.google.android.filament.View

class DirectionsExpandableListAdapter(
    private val context: Context,
    private val directionsData: List<Pair<String, List<String>>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return directionsData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return directionsData[groupPosition].second.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return directionsData[groupPosition].first
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return directionsData[groupPosition].second[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: android.view.View?,
        parent: ViewGroup?
    ): android.view.View? {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        (view as TextView).text = getGroup(groupPosition) as String
        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: android.view.View?,
        parent: ViewGroup?
    ): android.view.View? {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        (view as TextView).text = getChild(groupPosition, childPosition) as String
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
