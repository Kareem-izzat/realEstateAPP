# RealEstateHub – Android Real Estate Application  
**Course**: ENCS5150 – Advanced Computer Systems Engineering Laboratory  
**Semester**: Second Semester 2025  
**Team Members**:  
- Student 1: Kareem Qutob
- Student 2: Ahmad Hussein

---

## 📱 Application Overview

RealEstateHub is a mobile real estate application designed for a real estate agency. The app allows users to view, favorite, and reserve properties using a user-friendly interface. It supports both customer and admin roles, with dynamic features and a local SQLite database.

---

## ✅ Features

### 1. Welcome Screen 
- "Connect" button attempts to fetch property categories from a REST API.
- If successful → navigates to Login/Register.
- If failed → shows an error toast.

### 2. Login & Registration 
#### 🔐 Login
- Email & Password fields with validation.
- "Remember Me" using `SharedPreferences`.

#### 📝 Registration
- Valid email, names (min 3 characters), and secure password.
- Confirm Password match.
- Gender, Country (3), City (3 per country), and auto-prefixed phone number.

### 3. Home Layout 
- Navigation Drawer includes:
  - Home (About Us)
  - Properties Menu
  - Your Reservations
  - Favorites
  - Featured Properties
  - Profile Management
  - Contact Us
  - Logout

### 4. Properties Menu 
- View properties grouped by type.
- Filter/search by price, location, or type.
- Add to Favorites / Reserve buttons with animations.

### 5. Your Reservations 
- List of user-reserved properties with timestamps.

### 6. Your Favorites 
- Favorites list with ability to reserve or remove properties.

### 7. Featured Properties 
- View and interact with special offers.

### 8. Profile Management 
- View/edit name, password (with rules), phone.
- Change and save profile picture.

### 9. Contact Us 
- "Call Us", "Locate Us", and "Email Us" buttons:
  - Open phone app
  - Open Google Maps
  - Open Gmail intent

### 10. Logout 
- Logs out user and returns to Login screen.

---

## 👨‍💼 Admin Panel 
- Navigation Drawer includes:
  - Delete Customers
  - View All Reservations
  - Add Admin
  - Create Special Offers
  - Dashboard (user/reservation stats, gender %, top countries)
  - Logout
- Admin is differentiated via `role` field in the database.
- Static default Admin:
  - Email: `admin@admin.com`
  - Password: `Admin123!`

---

## 🎨 UI Design 
- Custom icons, animations, and material components.
- Clean color palette and responsive layouts.
- Animations include:
  - Property image fade-ins
  - Favorite heart pop
  - Reserve button bounce

---

## ⚙️ Technical Highlights

- Android Fragments and Navigation Drawer
- SQLite local database
- REST API integration with JSON parsing
- `SharedPreferences` for login persistence
- Exception handling and input validation
- Custom card layouts, dynamic spinners, and filtering logic
- Real-time drawer updates (profile image + name)
- Admin-only controls with secure role-based UI

---

## 🛠 How to Build & Run

1. Open in Android Studio.
2. Ensure target SDK is 26 (Pixel 3a XL).
3. Run on emulator/device or build APK:
   - `Build → Build APK(s)`  
4. APK is compatible with Android 8.0+.

---

© 2025 Kareem Alqutob and Ahmad Hussein – RealEstateHub
