# Klok - Custom Clock Module

A plain Android project showcasing a custom classic clock implementation using Jetpack Compose. The project demonstrates modular architecture with a reusable clock component.

## Project Structure

```
klok/
├── app/                           # Main application module
│   ├── src/main/
│   │   ├── java/com/example/klok/
│   │   │   └── MainActivity.kt    # Main activity showcasing the clock
│   │   ├── res/                   # App resources
│   │   └── AndroidManifest.xml    # App manifest
│   └── build.gradle.kts           # App module build configuration
├── clock/                         # Custom clock module
│   ├── src/main/
│   │   ├── java/com/example/clock/
│   │   │   └── ClassicClockDialog.kt  # Classic clock implementation
│   │   └── AndroidManifest.xml    # Clock module manifest
│   └── build.gradle.kts           # Clock module build configuration
├── build.gradle.kts               # Root project build configuration
├── settings.gradle.kts            # Project settings
├── gradle.properties              # Gradle properties
└── README.md                      # This file
```

## Features

- **Classic Clock Design**: A beautiful analog clock with hour, minute, and second hands
- **Real-time Updates**: The clock updates every second to show current time
- **Material Design 3**: Uses modern Material Design components and theming
- **Dialog Presentation**: Clock is displayed in an elegant dialog
- **Glance Widget**: A glanceable home screen widget that shows the current time
- **Modular Architecture**: Clock and widget are implemented as separate modules for reusability

## Clock Module Components

### ClassicClockDialog
A composable dialog that displays the classic clock with:
- Clock face with hour markers
- Hour, minute, and second hands
- Real-time updates
- Material Design 3 styling
- Dismissible behavior

### ClassicClock
The core clock composable that:
- Draws the clock face using Canvas
- Calculates and displays time hands
- Updates every second
- Uses smooth animations and proper styling

## Widget Module Components

### KlokGlanceWidget
A glanceable home screen widget that displays:
- Current time in digital format
- Current date
- Clock face representation
- Automatic updates every minute
- Material Design 3 theming

### KlokWidgetReceiver
Handles widget lifecycle events and updates

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34
- Kotlin 1.9.10
- Gradle 8.0+

### Building the Project

1. Clone or download the project
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Build and run on an emulator or device

### Using the Clock Module

The clock module can be easily integrated into other projects:

```kotlin
// Add dependency
implementation(project(":clock"))

// Use in your composable
ClassicClockDialog(
    onDismiss = { /* handle dismiss */ }
)
```

### Using the Widget Module

The widget module provides a glanceable home screen widget:

```kotlin
// Add dependency
implementation(project(":widget"))

// Widget is automatically available in the app
// Users can add it from the widget picker
```

## Customization

The clock can be customized by modifying the `ClassicClock` composable:
- Colors: Modify the Material Design color scheme
- Sizes: Adjust the clock dimensions and hand lengths
- Styling: Change the clock face design and markers
- Animations: Add smooth transitions and effects

## Architecture

The project follows modern Android development practices:
- **Jetpack Compose**: Modern declarative UI toolkit
- **Modular Design**: Separated concerns with dedicated modules
- **Material Design 3**: Latest design system implementation
- **Kotlin**: Modern programming language with coroutines support

## Dependencies

- **AndroidX Core**: Core Android functionality
- **Jetpack Compose**: UI toolkit
- **Material Design 3**: Design system components
- **Kotlin Coroutines**: Asynchronous programming

## License

This project is open source and available under the Apache License 2.0.

## Contributing

Feel free to contribute to this project by:
- Reporting bugs
- Suggesting new features
- Submitting pull requests
- Improving documentation 