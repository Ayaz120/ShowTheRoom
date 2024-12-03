# Show Me The Room

## Table of Contents
1. Project Overview
2. Features
3. Usage Instructions
4. Test Casses
5. Suppported API levels
6. Known Issues
7. Future Enhancements
8. Contributors

## 1. Project Overview
*"Show Me the Room"* is a mobile app designed to assist students and visitors of the University of New Brunswick (UNB) in navigating campus buildings and outdoor spaces. With this app, users can:

- Get step-by-step directions from any entrance to a specific room or building.
- Seamlessly navigate between floors for indoor navigation.
- View an intuitive map with overlaid navigation paths.
The app bridges the gap between traditional outdoor maps and indoor navigation, making campus life simpler.

## 2. Features
- **Dynamic Route Finder:** Users can input an entrance and a destination to receive clear, step-by-step directions.
- **Indoor and Outdoor Navigation:** Switch seamlessly between floors or navigate outdoor paths across campus.
- **Interactive Map:** A visual map with path overlays helps users follow the route easily.
- **Expandable Directions List:** Directions are displayed in an organized, collapsible format for clarity.
- **Reset Zoom:** Reset the map view to its default scale with a single click.

## 3. Usage Instructions
1. Launch the app on your Android device.
2. In the "Entrance" field, input the starting location (e.g., GWC Entrance).
3. In the "Destination" field, input the target room or building (e.g., ITC 317).
4. Tap "Find Path":
   - Text directions are displayed in the bottom sheet.
5. Use the Floor Spinner to navigate indoor floors.
6. Tap "Reset Zoom" to restore the map to its default view.

## 4. Test Casses
| --------- | ---------------- |
| Test case | Expected outcome |
| --------- | ---------------- |
| Type "GWC Entrance" and "ITC 317" as Start and Destination | Directions to ITC 317 are displayed in the directions list |
| Type "OutDoor Entrance" and "ITD 414" as start and destination | Directions to ITD 414 are displayed in the directions list |
| Change the floor map from "Floor 1" to "Floor 2" | Updates the map to show the "Floor 2" layout |
| Type an Entrance and Unknown Destination | Displays an error or a default message: "No directions available for this path." |
| Reset Zoom | Map view resets to default zoom level |
| Invalid or blank input for Start/Destination | Prompts user to enter valid locations. |
| --------- | ---------------- |

### Scenarios for testing:
- Type "GWC Entrance" and "ITC317" as Entrance and Destination
  Output: Directions to ITC 317 are displayed in the directions list
- Type "OutDoor Entrance" and "ITD415" as Entrance and destination
  Output: Directions to ITD 415 are displayed in the directions list
- Type "OutDoor Square" and "ITD414" as Entrance and destination
  Output: Directions to ITD 414 are displayed in the directions list
- Change the floor map from "Floor 1" to "Floor 2"
  Output: Updates the map according to the selected floor.
- Invalid or blank input for Start/Destination
  Output: Unknown Location: No directions available for this path
- Type an Entrance and leave the destination blank
  Output: Unknown Location: No directions available for this path

## 5. Supported API levels
- Minimum: API 21 (Android 5.0 - Lollipop)
- Target: API 34 (Android 14)

## 6. Known Issues
- **Indoor Accuracy:** Indoor navigation relies on Wi-Fi triangulation, which may vary in accuracy depending on the building's infrastructure.
- **Edge Cases:** Directions may not render for some unsupported input combinations (e.g., non-existent destinations).
- **Android Version Issue:** In lower android versions (eg. Android 9), while turning off/on 'Dark mode', the app refreshes with a black screen for a split second.
- **Settings Button:** While finding path for some of the rooms, the last step direction gets overlapped by the settings button.

## 7. Future Enhancements
- **Real-time Position Tracking:** Incorporate Bluetooth beacons or GPS for live user tracking.
- **Multi-Language Support:** Add support for French and other languages.
- **Offline Mode:** Enable users to download campus maps and access navigation offline.
- **Enhanced Indoor Navigation:** Integrate AR for intuitive indoor guidance.
- **Toast Message Issue:** When trying to find directions for an unmapped location, the toast messeage prints "Finding path from *Entrance* to *Destination*", insteaof "No directions available for this path".

## 8. Contributors
- Nikhil Shibu (Developer)
- Ayaz Ahmed (Developer)
- Haren Sharma (Developer)