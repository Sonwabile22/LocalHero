# Local Hero - Community Issue Reporting App

## Overview

Local Hero is a community-powered Android application designed to help community members report and track local issues such as potholes, water leaks, streetlight outages, and other infrastructure problems. The app connects community members with their local councillors and municipality departments to facilitate quick resolution of community issues.

## Features

### 🔐 Authentication & User Management
- **User Registration**: Community members can create accounts with email and password
- **User Login**: Secure authentication using Firebase Auth
- **Profile Setup**: Users must select their municipality and ward during registration
- **Role-Based Access**: Different user roles (Community Member, Councillor, Municipality Staff, Admin)

### 🏘️ Community Organization
- **Ward-Based Groups**: Users are automatically added to their ward group
- **Municipality Integration**: Support for multiple municipalities and their departments
- **Automatic Group Assignment**: Users join their ward group with councillors upon registration

### 📍 Issue Reporting
- **GPS Location**: Automatic location capture for accurate issue reporting
- **Photo Upload**: Support for multiple photos with offline capability
- **Issue Categories**: Predefined categories (Potholes, Water Leaks, Streetlight Outages, etc.)
- **Priority Levels**: Configurable priority system (Low, Medium, High, Urgent)
- **Detailed Descriptions**: Rich text descriptions for comprehensive issue reporting

### 💬 Communication
- **Ward Group Chats**: Community members can communicate with each other and councillors
- **Private Chats**: Direct messaging between users
- **Issue-Specific Chats**: Dedicated chat threads for individual issues
- **Real-time Messaging**: Firebase-powered real-time chat functionality

### 📊 Issue Management
- **Status Tracking**: Complete issue lifecycle from reported to resolved
- **Progress Updates**: Municipality staff can update issue status and progress
- **Assignment System**: Issues can be assigned to specific departments and staff
- **Timeline View**: Complete history of issue updates and changes

### 📱 User Experience
- **Modern Material Design**: Clean, intuitive interface following Material Design guidelines
- **Offline Support**: Users can upload photos offline and sync when online
- **Responsive Layout**: Optimized for various screen sizes
- **Bottom Navigation**: Easy navigation between main app sections

## Technical Architecture

### Frontend
- **Language**: Java
- **UI Framework**: Android Native with Material Design Components
- **Architecture**: Fragment-based navigation with ViewBinding
- **Minimum SDK**: API 24 (Android 7.0)

### Backend Services
- **Authentication**: Firebase Authentication
- **Database**: Cloud Firestore
- **Storage**: Firebase Storage for images
- **Real-time Updates**: Firebase Cloud Messaging
- **Analytics**: Firebase Analytics

### Key Dependencies
```gradle
// Firebase
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-firestore'
implementation 'com.google.firebase:firebase-storage'
implementation 'com.google.firebase:firebase-messaging'

// Location Services
implementation 'com.google.android.gms:play-services-location'
implementation 'com.google.android.gms:play-services-maps'

// UI Components
implementation 'com.google.android.material:material'
implementation 'androidx.recyclerview:recyclerview'
implementation 'androidx.cardview:cardview'

// Image Handling
implementation 'com.github.bumptech.glide:glide'
```

## Project Structure

```
app/src/main/java/com/example/localheroapp/
├── auth/                    # Authentication activities
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   └── ProfileSetupActivity.java
├── fragments/              # Main app fragments
│   ├── HomeFragment.java
│   ├── IssuesFragment.java
│   ├── ChatsFragment.java
│   ├── ProfileFragment.java
│   └── ReportIssueFragment.java
├── models/                 # Data models
│   ├── User.java
│   ├── Issue.java
│   ├── IssueUpdate.java
│   ├── Chat.java
│   ├── Message.java
│   ├── Ward.java
│   └── Municipality.java
├── utils/                  # Utility classes
│   └── LocationUtils.java
└── MainActivity.java       # Main activity with navigation
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24+
- Google Services account for Firebase
- Google Maps API key

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/localhero.git
   cd localhero
   ```

2. **Firebase Setup**
   - Create a new Firebase project
   - Enable Authentication, Firestore, Storage, and Messaging
   - Download `google-services.json` and place it in the `app/` directory
   - Update Firebase configuration in the project

3. **Google Maps API**
   - Get a Google Maps API key from Google Cloud Console
   - Update the API key in `AndroidManifest.xml`

4. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Build and run on an emulator or device

### Configuration

#### Firebase Configuration
The app requires the following Firebase services to be enabled:
- **Authentication**: Email/password authentication
- **Firestore**: Database for users, issues, chats, and metadata
- **Storage**: Image storage for issue photos
- **Messaging**: Push notifications and real-time updates

#### Municipality Data Setup
Before the app can be used, you need to populate the database with:
- Municipality information
- Ward boundaries and councillor assignments
- Department structures

## Usage Guide

### For Community Members
1. **Registration**: Create an account and select your municipality and ward
2. **Report Issues**: Use the floating action button to report new issues
3. **Track Progress**: Monitor issue status and updates
4. **Community Chat**: Participate in ward group discussions

### For Councillors
1. **Ward Management**: Oversee issues in your assigned ward
2. **Communication**: Engage with community members through group chats
3. **Issue Escalation**: Escalate critical issues to municipality departments

### For Municipality Staff
1. **Issue Assignment**: Receive and manage assigned issues
2. **Progress Updates**: Update issue status and provide progress reports
3. **Resolution Tracking**: Mark issues as completed when resolved

## Security Features

- **Role-Based Access Control**: Users can only access features appropriate to their role
- **Data Isolation**: Users can only see data from their ward and municipality
- **Secure Authentication**: Firebase Auth with email verification
- **Input Validation**: Comprehensive validation for all user inputs

## Offline Capabilities

- **Photo Storage**: Images can be captured and stored offline
- **Data Caching**: Recent data is cached for offline viewing
- **Sync on Reconnect**: Offline data automatically syncs when connection is restored

## Future Enhancements

- **Web Dashboard**: Administrative web interface for municipality staff
- **Mobile App for Staff**: Dedicated app for field workers
- **Advanced Analytics**: Issue trend analysis and reporting
- **Integration APIs**: Connect with existing municipality systems
- **Multi-language Support**: Support for multiple local languages
- **Push Notifications**: Real-time updates for issue status changes

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Contact the development team at support@localhero.com
- Check the documentation at docs.localhero.com

## Acknowledgments

- Firebase team for the excellent backend services
- Google Maps team for location services
- Material Design team for the design system
- Android developer community for best practices and guidance

---

**Local Hero** - Making communities better, one issue at a time! 🏘️✨



