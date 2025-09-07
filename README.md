🍎 NutriTrack

Version: 1.0
Course: FIT2081 – Mobile App Development
Date: March 2025

NutriTrack is a mobile app that helps users track their food intake and get insights into their dietary habits.
This repo implements the student version of NutriTrack using Kotlin + Jetpack Compose with CSV data integration.

📌 Project Overview

The app has three main goals:

Allow users to log in with their pre-registered ID and phone number (from CSV).

Display food quality scores and allow edits through a questionnaire.

Provide insights into dietary breakdown via visual progress indicators.

📂 App Structure

The app is structured into three core areas:

Welcome & Login

Branding, disclaimer, and login entry point.

CSV-based validation (User ID + phone number).

Home

Displays overall food quality score.

Quick access to questionnaire edits.

Insights

Category-wise breakdown of diet scores.

Visualised with progress bars.

Extra buttons for sharing/improvement (stubbed).

🖼️ Screens & Features
1. Welcome Screen

App logo + name (NutriTrack).

Disclaimer + link to Monash Nutrition Clinic.

Login button → navigates to login.

Student signature (e.g. Alex Scott (14578373)) must appear somewhere.

2. Login Screen

Dropdown: select User ID (from CSV).

Text field: enter phone number (must match CSV).

Validation rules:

User ID must exist.

Phone must match entry.

If invalid → error message.

Continue → questionnaire.

3. Food Intake Questionnaire

Checkboxes: food categories (fruits, veg, grains, etc.).

Persona selection (buttons).

Dropdown: choose best-fitting persona.

Time pickers:

Biggest meal time

Sleep time

Wake-up time

Save → stores locally in SharedPreferences.

4. Home Screen

Greeting: “Hello, [UserID]”.

Displays Food Quality Score (from CSV).

Edit button → back to questionnaire.

Explanation text: meaning of the score.

Navigation to Insights via bottom bar or buttons.

5. Insights Screen

Progress bars for:

Vegetables, Fruits, Grains & Cereals, Whole Grains, Meat, Dairy, etc.

Total score display (from CSV).

Buttons:

Share with someone

Improve my diet (stub — no action yet).

📊 Data Handling

User data provided in CSV file (IDs, phone numbers, food quality scores).

CSV lookup is required at login (UserID + phone validation).

After login:

Home screen loads total food quality score.

Insights screen loads category-wise breakdown.

✅ Implementation Notes

Package name must include student’s firstname + student ID.

Navigation handled via Jetpack Compose Navigation.

Persistent questionnaire data stored in SharedPreferences.

CSV parsing can be done with built-in Kotlin libraries or third-party parsers.
