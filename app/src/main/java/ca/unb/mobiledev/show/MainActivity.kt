package ca.unb.mobiledev.show

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.show.R
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: ImageView
    private lateinit var startLocation: AutoCompleteTextView
    private lateinit var endLocation: AutoCompleteTextView
    private lateinit var findPathButton: Button
    private lateinit var directionsTextView: TextView
    private lateinit var floorSpinner: Spinner
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private lateinit var resetZoomButton: Button
    private lateinit var currentFloorTextView: TextView
    private lateinit var pathOverlayView: PathOverlayView
    private lateinit var directionsExpandableListView: ExpandableListView
    private lateinit var directionsAdapter: SimpleExpandableListAdapter
    private var scaleFactor = 1.0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    val locations = listOf(
        LocationPoint("Room A", 100f, 200f, 1),
        LocationPoint("Room B", 300f, 400f, 1),
        LocationPoint("Room C", 500f, 600f, 1),
        LocationPoint("Room D", 700f, 800f, 1),
        LocationPoint("Room 1", 100f, 300f, 2),
        LocationPoint("Room 2", 400f, 500f, 2),
        LocationPoint("Room 3", 600f, 700f, 2),
        LocationPoint("Room 4", 700f, 800f, 2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        mapView = findViewById(R.id.mapImageView)
        startLocation = findViewById(R.id.startLocation)
        endLocation = findViewById(R.id.endLocation)
        findPathButton = findViewById(R.id.findPathButton)
        directionsTextView = findViewById(R.id.directionsTextView)
        floorSpinner = findViewById(R.id.floorSpinner)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        gestureDetector = GestureDetector(this, GestureListener())
        currentFloorTextView = findViewById(R.id.currentFloorTextView)
        pathOverlayView = findViewById(R.id.pathOverlayView)
        directionsExpandableListView = findViewById(R.id.directionsExpandableListView)
        resetZoomButton = findViewById(R.id.resetZoomButton)
        resetZoomButton.setOnClickListener {
            resetZoomAndPosition()
        }
        mapView.isClickable = true
        mapView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            true
        }


        val floors = listOf("Floor 1", "Floor 2")
        val floorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors)
        floorSpinner.adapter = floorAdapter

        floorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                updateMapForFloor(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        floorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedFloor = position + 1
                updateMapForFloor(selectedFloor)
                currentFloorTextView.text = "Current Floor: $selectedFloor"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set initial floor
        updateMapForFloor(1)
        currentFloorTextView.text = "Current Floor: 1"

        // Define points of interest on the map


        // Setup AutoCompleteTextView with location names
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            locations.map { it.name })
        startLocation.setAdapter(adapter)
        endLocation.setAdapter(adapter)

        // Button click listener
        findPathButton.setOnClickListener {
            val start = locations.find { it.name == startLocation.text.toString() }
            val end = locations.find { it.name == endLocation.text.toString() }

            if (start != null && end != null) {
                val path = calculatePath(start, end, locations)
                val directions = getDirections(path)
                directionsTextView.text = directions.joinToString("\n")
            } else {
                Toast.makeText(this, "Invalid locations selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetZoomAndPosition() {
        scaleFactor = 1.0f
        mapView.scaleX = 1.0f
        mapView.scaleY = 1.0f
        mapView.translationX = 0f
        mapView.translationY = 0f
    }

    private fun updateLocationsForFloor(floor: Int) {
        val floorLocations = locations.filter { it.floor == floor }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            floorLocations.map { it.name })
        startLocation.setAdapter(adapter)
        endLocation.setAdapter(adapter)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.1f, 10.0f)
            mapView.scaleX = scaleFactor
            mapView.scaleY = scaleFactor
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            mapView.translationX -= distanceX
            mapView.translationY -= distanceY
            return true
        }
    }


    private fun updateMapForFloor(floor: Int) {
        val mapResourceId = when (floor) {
            1 -> R.drawable.campus_map
            2 -> R.drawable.floor_2
            else -> R.drawable.campus_map
        }
        mapView.setImageResource(mapResourceId)
        updateLocationsForFloor(floor)  // Add this line
    }

    // Function to calculate a simple path (mock implementation for now)
    private fun calculatePath(
        start: LocationPoint,
        end: LocationPoint,
        locations: List<LocationPoint>
    ): List<LocationPoint> {
        val path = mutableListOf(start)
        var current = start
        while (current != end) {
            val next = locations.minByOrNull { location ->
                if (location != current) {
                    val floorDifference =
                        (location.floor - end.floor).absoluteValue * 1000 // Penalize floor changes
                    sqrt(
                        (location.x - end.x).toDouble().pow(2) +
                                (location.y - end.y).toDouble().pow(2)
                    ) +
                            floorDifference
                } else {
                    Double.MAX_VALUE
                }
            } ?: break
            path.add(next)
            current = next
        }
        return path
    }

    // Function to generate directions
    private fun getDirections(path: List<LocationPoint>): List<Map<String, String>> {
        val directions = mutableListOf<Map<String, String>>()
        for (i in 0 until path.size - 1) {
            val from = path[i]
            val to = path[i + 1]
            val stepMap = mutableMapOf<String, String>()

            if (from.floor != to.floor) {
                stepMap["header"] = "Floor Change"
                stepMap["detail"] =
                    "Take the stairs or elevator from floor ${from.floor} to floor ${to.floor}."
            } else {
                val dx = to.x - from.x
                val dy = to.y - from.y
                val direction = when {
                    dx > 0 && dy.absoluteValue < dx.absoluteValue -> "east"
                    dx < 0 && dy.absoluteValue < dx.absoluteValue -> "west"
                    dy > 0 && dx.absoluteValue < dy.absoluteValue -> "north"
                    dy < 0 && dx.absoluteValue < dy.absoluteValue -> "south"
                    else -> "diagonally"
                }
                stepMap["header"] = "Step ${i + 1}"
                stepMap["detail"] =
                    "Go $direction from ${from.name} to ${to.name} on floor ${from.floor}."
            }
            directions.add(stepMap)
        }
        return directions
    }

    private fun updateDirectionsView(directions: List<Map<String, String>>) {
        val groupData = directions.map { mapOf("header" to it["header"]) }
        val childData = directions.map { listOf(mapOf("detail" to it["detail"])) }

        directionsAdapter = SimpleExpandableListAdapter(
            this,
            groupData,
            android.R.layout.simple_expandable_list_item_1,
            arrayOf("header"),
            intArrayOf(android.R.id.text1),
            childData,
            android.R.layout.simple_expandable_list_item_2,
            arrayOf("detail"),
            intArrayOf(android.R.id.text2)
        )

        directionsExpandableListView.setAdapter(directionsAdapter)
    }

    // Update the findPathButton click listener
}


// Data class for location points
data class LocationPoint(
    val name: String,
    val x: Float, // X-coordinate on the map
    val y: Float, // Y-coordinate on the map
    val floor: Int
)
