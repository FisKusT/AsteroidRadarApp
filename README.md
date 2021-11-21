# AsteroidRadarApp
Asteroid Radar App

The app has two screens:

1. The first screen displays a list of asteroids from the Nasa API

2. The second screen displays detailed page about a specific asteroid

---

I used MVVM architectue with a repository architectue for data fetching (network or DB):
* Using glide for image load
* Using Retrofit2 for network request
* Using Room for DB (offline caching)
* Using dagger-hilt for dependency injection into viewmodels
* Using LiveData and Data Binding
* Using Junit for unit testing
