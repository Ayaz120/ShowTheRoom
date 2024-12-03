import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import ca.unb.mobiledev.show.R
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
    ): android.view.View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_group, parent, false)
        val textView = view.findViewById<TextView>(R.id.listTitle)
        textView.text = getGroup(groupPosition) as String
        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: android.view.View?,
        parent: ViewGroup?
    ): android.view.View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.expandedListItem)
        textView.text = getChild(groupPosition, childPosition) as String
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
