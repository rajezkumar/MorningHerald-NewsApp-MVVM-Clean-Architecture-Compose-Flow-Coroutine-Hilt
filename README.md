### Morning Herald: A Jetpack Compose MVVM Project

### Core Features

- **Dynamic News Articles Fetching:** Integrates with [NewsAPI.org](https://newsapi.org/docs/endpoints/sources) to fetch the latest news articles.
- **Bottom Navigation Interface:** Facilitates easy navigation between different sections of the app.
- **Offline Capability:** Allows for reading news articles even when offline, enhancing accessibility
- **Pagination:** Implements pagination for loading news articles in chunks, improving performance and user experience.
- **Unit Testing:** Ensures code quality and reliability through unit tests.

### Leveraged Libraries & Technologies
- **MVVM Clean Architecture**: For a clear separation of concerns and easy maintenance
- **Kotlin**: As the primary programming language for its conciseness and expressiveness.
- **Jetpack Compose**: For building the UI with a declarative and reactive approach.
- **Dagger Hilt**: For dependency injection and managing the app's object graph.
- **Coroutines & Flow**: For efficient asynchronous programming and real-time data streaming.
- **Retrofit & OkHttp**: For making network requests and handling API responses.
- **StateFlow**: For seamlessly updating and managing UI content based on data state changes.
- **Pagination**: For loading news articles in chunks and enhancing user experience.
- **Coil**: For loading and caching images efficiently.
- **Room**: For storing and managing news articles in a local database.
- **Kotlin DSL**: For configuring Gradle build scripts in a more concise and readable manner.
- **Mockk**: A Kotlin-focused mocking library for effectively unit testing.
- **Turbine**: For testing flows and coroutines in a simple and concise manner.

### Planned Upgrades
- Refactoring and enhancing code quality for better maintainability.
- Implementing UI tests for better coverage and reliability.
- Adding more sources and categories for a wider range of news articles.
- Refactor Jetpack Compose UI for better performance and user experience.
- Background Sync with WorkManager: To periodically update news articles in the background.
- Extended Offline Support: Aiming for offline functionality across all pages for uninterrupted news access.

### Screen Shots

![Home](app/src/main/assets/home.png)
![News](app/src/main/assets/source.png)
![Browse](app/src/main/assets/browse.png)




