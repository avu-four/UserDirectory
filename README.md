# User Directory

A simple offline-first Android app that fetches users from JSONPlaceholder, caches them in a Room database and displays them with Jetpack Compose. Supports local search (by name or email) and works offline by showing cached data.

## How it works (core functionalities)
- Fetch users with Retrofit (`GET https://jsonplaceholder.typicode.com/users`).
- Store users in Room (`@Insert(onConflict = OnConflictStrategy.REPLACE)`).
- UI reads from Room as the single source of truth using `Flow`.
- On app start: display Room immediately → attempt network fetch → on success update Room (UI auto-updates); on failure keep showing cached data.
- Search runs locally in Room with a `@Query` that filters by name or email.

## Screenshots
![Alt text](/app/SCREENSHOT411A.png?raw=true "Screenshot 1")
![Alt text](/app/SCREENSHOT2411A.png?raw=true "Screenshot 2")
1. Main list view (shows id, name, email, phone)
2. Search in action
3. Offline behavior (cached results shown)

## How to run
1. Clone repo
2. Build and run in Android Studio (ensure `INTERNET` permission)
3. App will load cached users (if any) and refresh from network automatically.

## Files of interest
- `network/UsersApi.kt`, `network/UserDto.kt`
- `db/UserEntity.kt`, `db/UserDao.kt`, `db/UserDatabase.kt`
- `repository/UserRepository.kt`
- `ui/UserViewModel.kt`, `ui/UserListScreen.kt`
