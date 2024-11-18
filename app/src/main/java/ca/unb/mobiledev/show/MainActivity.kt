package ca.unb.mobiledev.show

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        mapView = findViewById(R.id.mapImageView)
        startLocation = findViewById(R.id.startLocation)
        endLocation = findViewById(R.id.endLocation)
        findPathButton = findViewById(R.id.findPathButton)
        directionsTextView = findViewById(R.id.directionsTextView)

        // Define points of interest on the map
        val locations = listOf(
            LocationPoint("Room A", 100f, 200f),
            LocationPoint("Room B", 300f, 400f),
            LocationPoint("Room C", 500f, 600f),
            LocationPoint("Room D", 700f, 800f)
        )

        // Setup AutoCompleteTextView with location names
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locations.map { it.name })
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

    // Function to calculate a simple path (mock implementation for now)
    private fun calculatePath(start: LocationPoint, end: LocationPoint, locations: List<LocationPoint>): List<LocationPoint> {
        // This is a very simplified version. A real implementation would use A* or a similar algorithm
        val path = mutableListOf(start)
        var current = start
        while (current != end) {
            val next = locations.minByOrNull { location ->
                if (location != current) {
                    sqrt((location.x - end.x).toDouble().pow(2) + (location.y - end.y).toDouble().pow(2))
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
    private fun getDirections(path: List<LocationPoint>): List<String> {
        val directions = mutableListOf<String>()
        for (i in 0 until path.size - 1) {
            val from = path[i]
            val to = path[i + 1]
            val dx = to.x - from.x
            val dy = to.y - from.y
            val direction = when {
                dx > 0 && dy.absoluteValue < dx.absoluteValue -> "east"
                dx < 0 && dy.absoluteValue < dx.absoluteValue -> "west"
                dy > 0 && dx.absoluteValue < dy.absoluteValue -> "north"
                dy < 0 && dx.absoluteValue < dy.absoluteValue -> "south"
                else -> "diagonally"
            }
            directions.add("Go $direction from ${from.name} to ${to.name}.")
        }
        return directions
    }
}

// Data class for location points
data class LocationPoint(
    val name: String,
    val x: Float, // X-coordinate on the map
    val y: Float  // Y-coordinate on the map
)
