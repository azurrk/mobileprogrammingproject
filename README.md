# Mobile Programming Project - Expense Tracker

A modern Android application built with Jetpack Compose to help users track their income and expenses efficiently. This project demonstrates the implementation of clean architecture, dependency injection, and local data persistence in a mobile environment.

## Features

*   **User Authentication**: Secure Login and Registration system.
*   **Transaction Management**: Add, view, and manage your daily transactions.
*   **Expense Categorization**: Categorize your spending into predefined categories like:
    *   Food
    *   Transport
    *   Entertainment
    *   Health
    *   Utilities
    *   Other
*   **Income Tracking**: Record your income sources.
*   **Dashboard**: Overview of your financial health.
*   **Persistent Storage**: Data is saved locally ensuring offline access.

## Tech Stack

*   **Language**: Kotlin
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern toolkit for building native UI.
*   **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture principles.
*   **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) - For managing dependencies.
*   **Database**: [Room](https://developer.android.com/training/data-storage/room) - Abstraction layer over SQLite for robust database access.
*   **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose) - For navigating between composables.
*   **Asynchronous Processing**: Kotlin Coroutines & Flow.
*   **Data Serialization**: Kotlinx Serialization.

## Project Structure

```
com.example.mobileprogrammingproject
├── dao             # Data Access Objects for Room
├── database        # Database configuration and TypeConverters
├── di              # Dependency Injection modules (Hilt)
├── model           # Data classes and Entities (User, Transaction)
├── repository      # Repository layer for data handling
├── session         # Session management
├── ui              # User Interface layer
│   ├── components  # Reusable UI components
│   ├── navigation  # Navigation graph and destinations
│   ├── screens     # Composable screens (Login, Home, Transaction, etc.)
│   ├── theme       # Theme and styling definitions
│   └── viewmodel   # ViewModels for state management
└── util            # Utility classes
```

## Getting Started

### Prerequisites

*   Android Studio Ladybug | 2024.2.1 or newer (recommended).
*   JDK 17 or higher.
*   Android SDK API Level 35 (compileSdk).

### Installation

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd mobileprogrammingproject
    ```

2.  **Open in Android Studio**:
    *   Launch Android Studio.
    *   Select "Open" and navigate to the project directory.

3.  **Sync Project with Gradle Files**:
    *   Wait for Android Studio to download dependencies and index the project.

4.  **Run the App**:
    *   Connect an Android device or start an emulator (API 24+).
    *   Click the **Run** button (green play icon) in the toolbar.

