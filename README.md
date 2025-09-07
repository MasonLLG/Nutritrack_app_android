🍎 NutriTrack

Platform: Android (Kotlin + Jetpack Compose)
Status: Active Project
Purpose: Help users track their food intake and understand dietary habits through scores and insights.

📌 Overview

NutriTrack is a mobile app built to make nutrition tracking simple and visual.
The app evolves across two major versions:

NutriTrack Core (v1.0) – first release with CSV-based login and food score display.

NutriTrack Pro (v2.0) – upgraded release with a local Room database, multi-user accounts, and fruit insights powered by an external API.

🚀 Features
v1.0 – Core

Welcome screen with branding, disclaimer, and login access.

Login via CSV (UserID + Phone validation).

Food intake questionnaire (categories, personas, meal/sleep times).

Home screen showing food quality score.

Insights screen with category breakdown and progress bars.

Local storage using SharedPreferences.

v2.0 – Pro

Room database (migrated from CSV).

Multi-user login system:

First-time login = claim account (UserID + Phone + set password).

Future logins = UserID + password.

Auto-login until explicit logout.

Fruit insights:

Integration with FruityVice API
.

Low fruit score → users can query fruit nutrition facts.

Optimal fruit score → app displays placeholder imagery.

Settings screen:

Show logged-in user info.

Logout option.

Built with MVVM architecture using Repository → ViewModel → LiveData.

Networking with Retrofit + Coroutines.

📊 Version Comparison
Feature	v1.0 (Core)	v2.0 (Pro)
CSV login + validation	✅	➖ (replaced by DB)
SharedPreferences store	✅	➖ (migrated to DB)
Room DB	❌	✅
Multi-user accounts	❌	✅
Food score display	✅	✅
Insights breakdown	✅	✅
Fruit insights (API)	❌	✅
Settings + logout	❌	✅
📱 User Flow (v2.0)

Launch → Welcome

Login (claim account or authenticate)

Questionnaire (optional update)

Home → view overall food score

Insights → category breakdown + “Improve my diet”

Fruit Insights → nutrition facts or placeholder image

Settings → view user info + logout

🛠️ Tech Stack

Language: Kotlin

UI: Jetpack Compose

State Management: ViewModel + LiveData

Persistence: Room Database, SharedPreferences (v1.0 only)

Networking: Retrofit + Coroutines

Architecture: MVVM
