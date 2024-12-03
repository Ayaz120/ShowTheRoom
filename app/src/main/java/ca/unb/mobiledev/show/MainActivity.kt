package ca.unb.mobiledev.show

import DirectionsExpandableListAdapter
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ca.unb.mobiledev.show.PathOverlayView
import ca.unb.mobiledev.show.R

class MainActivity : AppCompatActivity() {

    private lateinit var startLocation: AutoCompleteTextView
    private lateinit var endLocation: AutoCompleteTextView
    private lateinit var findPathButton: Button
    private lateinit var resetZoomButton: Button
    private lateinit var floorSpinner: Spinner
    private lateinit var currentFloorTextView: TextView
    private lateinit var mapImageView: ZoomableImageView
    private lateinit var pathOverlayView: PathOverlayView
    private lateinit var directionsExpandableListView: ExpandableListView
    private lateinit var bottomSheet: LinearLayout
    private lateinit var directionsTextView: TextView

    private val floorAdapter by lazy {
        ArrayAdapter.createFromResource(
            this, R.array.floors, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }
    private fun updateMapForFloor(floor: Int) {
        val mapResourceId = when (floor) {
            1 -> R.drawable.campus_map
            2 -> R.drawable.floor_2
            else -> R.drawable.campus_map // Default map
        }
        mapImageView.setImageResource(mapResourceId)
    }


    // Holds the directions data to be dynamically updated
    private var directionsData: List<Pair<String, List<String>>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        startLocation = findViewById(R.id.startLocation) // Entrance
        endLocation = findViewById(R.id.endLocation) // Destination
        findPathButton = findViewById(R.id.findPathButton)
        resetZoomButton = findViewById(R.id.resetZoomButton)
        floorSpinner = findViewById(R.id.floorSpinner)
        currentFloorTextView = findViewById(R.id.currentFloorTextView)
        mapImageView = findViewById(R.id.mapImageView)
        pathOverlayView = findViewById(R.id.pathOverlayView)
        directionsExpandableListView = findViewById(R.id.directionsExpandableListView)
        bottomSheet = findViewById(R.id.bottomSheet)
        directionsTextView = findViewById(R.id.directionsTextView)

        // Set up the floor spinner
        floorSpinner.adapter = floorAdapter

        // Handle button clicks
        findPathButton.setOnClickListener {
            val start = startLocation.text.toString() // User's input for entrance
            val end = endLocation.text.toString() // User's input for destination

            // Get directions based on user input (entrance and destination)
            directionsData = getDirectionsDataFromServer(start, end)

            // Update the directions view with new data
            val directionsAdapter = DirectionsExpandableListAdapter(this, directionsData)
            directionsExpandableListView.setAdapter(directionsAdapter)

            // Show bottom sheet with directions
            Toast.makeText(this, "Finding path from $start to $end", Toast.LENGTH_SHORT).show()
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        resetZoomButton.setOnClickListener {
            mapImageView.imageMatrix = Matrix()
            mapImageView.invalidate()
            Toast.makeText(this, "Zoom reset", Toast.LENGTH_SHORT).show()
        }

        // Handle floor change (if needed)
        floorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFloor = floorSpinner.selectedItem.toString()
                currentFloorTextView.text = "Current Floor: $selectedFloor"
                updateMapForFloor(position + 1)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    // Modified method to return directions based on user input (entrance and destination)
    private fun getDirectionsDataFromServer(start: String, end: String): List<Pair<String, List<String>>> {
        return when {
            start.equals("Entrance", ignoreCase = true) && end.equals("315", ignoreCase = true) -> {
                listOf(
                    "315" to listOf("Step 1: Go straight", "Step 2: Turn left", "Step 3: Arrive at Building 1")
                )
            }
            start.equals("Entrance", ignoreCase = true) && end.equals("314", ignoreCase = true) -> {
                listOf(
                    "314" to listOf("Step 1: Go right", "Step 2: Turn left", "Step 3: Arrive at Building 2")
                )
            }
            else -> {
                // Handle other locations or provide a default set of directions
                listOf(
                    "Unknown Location" to listOf("No directions available for this path.")
                )
            }
        }
    }
}
