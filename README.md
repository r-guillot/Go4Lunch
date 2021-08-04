# Go4Lunch
Android application to choose where to eat with coworkers with a list of restaurants near you, a map with there location and the list of your coworkers.

## Installation
- Clone or download this repository
```
git clone https://github.com/r-guillot/Go4Lunch.git
```
- Add your own googleMapKey.

Follow this link if you are not familiar with Googlekeys : https://developers.google.com/maps/documentation/android-sdk/get-api-key

- Run the application on your device or on a emulator

## Content of the App
### Map with the restaurants nears the user location.
<img src="https://github.com/r-guillot/Go4Lunch/blob/master/screen_map.jpg" width="250" height="500">

### RecyclerView with the list of nearest restaurant with info(name, opening time, score and distance)
<img src="https://github.com/r-guillot/Go4Lunch/blob/master/screen_list.jpg" width="250" height="500">

### Details of a restaurant with floating action button to choose this restaurant for lunch, buttons to call the restaurant, to visit the website and to add to your favorites.
<img src="https://github.com/r-guillot/Go4Lunch/blob/master/screen_detail.jpg" width="250" height="500">

### RecyclerView with the list of your coworkers and where they'll eat.
<img src="https://github.com/r-guillot/Go4Lunch/blob/master/screen_friend.jpg" width="250" height="500">

### Other features
- Notification with alarm manager every day at 12:00 pm if the user have choose a place to eat.
- Authentication with firebase and facebook, google, twitter or mail

## APIs
- Maps SDK for Android
- Places API
- Place autocomplete

## App Architecture
- MVVM
- Java
- BDD: Firestore

## Developer
Guillot Robin
