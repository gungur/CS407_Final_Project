# Landmark App

## Overview
The Landmark App is an Android application that allows users to explore landmarks, collect badges, and connect with friends. The app features user authentication, profile management, friend systems, and location-based badge collection.

## Features

### Core Features
- **User Authentication**: Sign up and login functionality with persistent sessions
- **Profile Management**: View and edit username, password, and email
- **Badge Collection**: Collect badges by visiting real-world landmarks
- **Friend System**: Add friends, view friend requests, and manage connections
- **Location Services**: Uses Google Maps to display landmarks and track user location

### Technical Features
- **Room Database**: Persistent local storage for user data
- **LiveData**: Reactive data observation for UI updates
- **AsyncTask**: Background operations for database access
- **SharedPreferences**: Secure storage for user sessions
- **Google Maps Integration**: Interactive map with custom markers

## File Structure

### Main Components
- **Activities**:
  - `MainActivity`: Login screen
  - `SignUp`: User registration
  - `Home`: Main navigation hub
  - `SettingsActivity`: User settings management
  - `ChangeUsername`: Username modification
  - `ChangePassword`: Password modification

- **Fragments**:
  - `HomeFragment`: Map view with landmarks
  - `ProfileFragment`: User profile display
  - `FriendsFragment`: Friend management system

- **Database**:
  - `AppDatabase`: Room database setup
  - `User`: Entity class
  - `UserDao`: Database access interface
  - `Converter`: Type converters for complex data

- **Adapters**:
  - `FriendsListAdapter`: Displays friends list
  - `IncomingFriendListAdapter`: Manages incoming friend requests
  - `SentFriendListAdapter`: Handles sent friend requests

## Setup Instructions

### Prerequisites
- Android Studio
- Android SDK (API level 31 or higher)
- Google Maps API key (for map functionality)

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Add your Google Maps API key to `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```
4. Build and run the application

## Usage

### Authentication
1. **Sign Up**:
   - Enter email, username, and password
   - Confirm password
   - Submit to create account

2. **Login**:
   - Enter username and password
   - Option to save login credentials

### Main Features
1. **Map View**:
   - View landmarks as markers on the map
   - Collect badges by visiting landmark locations

2. **Profile**:
   - View collected badges
   - Access settings

3. **Friends**:
   - Search and add friends
   - View and manage friend requests
   - See friends' badge collections

### Settings
1. **Change Username**:
   - Enter new username
   - Must be unique

2. **Change Password**:
   - Verify old password
   - Enter and confirm new password

3. **Logout**:
   - Ends current session

## Technical Details

### Database Schema
The app uses Room database with the following main tables:
- `users`: Stores all user information including:
  - Credentials (username, password, email)
  - Friends lists
  - Friend requests (sent and received)
  - Collected badges

### Architecture
- **MVVM Pattern**: Using LiveData for reactive UI updates
- **Repository Pattern**: Database access through DAOs
- **Async Operations**: Database operations performed in background threads

### Dependencies
- AndroidX libraries
- Room Persistence Library
- Google Play Services (Maps and Location)
- Gson for type conversion

## Known Issues
- Map functionality requires Google Play Services
- Location permissions must be granted for badge collection
- Test friends are generated on first launch if no friends exist

## Future Enhancements
- Implement AR functionality for badge collection
- Add more landmarks and badge types
- Implement push notifications for friend requests
- Add social features (messaging, sharing)

## Screenshots
<img width="1440" height="3120" alt="image" src="https://github.com/user-attachments/assets/7b945c4e-7ba9-43cc-bd81-95040e2b709f" />

<img width="1440" height="3120" alt="image" src="https://github.com/user-attachments/assets/a69d7233-99c7-4208-aab3-d4e2f08ff3ba" />

<img width="1440" height="3120" alt="image" src="https://github.com/user-attachments/assets/d2817fe6-310b-458f-8612-f6ab1b8cef53" />
