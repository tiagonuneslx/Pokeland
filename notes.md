# Notes

I decided to build the PokeLand app, a simple and clean app showcasing all the different Pokemon, and some of their details.

For most of the design decisions I took, the most influential factor to consider was time, as I'm currently working full-time, and it's hard to find the time to invest in this project.

## App features

1. List of Pokemon Species, merged with information from the respective default Pokemon
    - For each species of Pokemon, a card is displayed with a color corresponding to their default Pokemon primary type, and an image of that Pokemon is also displayed.
    - This list is paginated. As the user scrolls down, placeholders are shown until the next page is loaded.
    - When a chunk of species is downloaded, an asynchronous call is made for each species to fetch information about their default Pokemon.

2. Clicking on any Pokemon card will show the Details Screen
    - This screen shows the following sections:
      1. About, with information from the Pokemon Species and from its default Pokemon.
      2. Moves, with a list of Moves the Pokemon learns.
      3. Stats, showing a progress bar from 0 to 255 for each stat.
      4. Evolutions, with all possible evolutions from the species. To make it simpler, I only show the minimum level needed to trigger the evolution.
    - The information about the selected Pokemon is shared via a shared ViewModel. This way, we don't need to fetch information that we already had while on the List screen.

3. Night mode is fully supported.

4. Landscape orientation changes the layouts of the app.

5. Unit testing and UI included (though not too robust).

6. Dependency Injection used.

7. Pagination and placeholders.

I didn't have enough time to implement favorites / webhooks. I also thought of implementing caching / offline support, but there was no time.

## Choice of Tools

Architecture:
MVVM, because it's what I'm used to.

UI:
Jetpack Compose, because it's faster for me to develop with, it's a newer technology and it's more enjoyable than View System. However, I'm also used to work with View System.

Navigation:
Jetpack Compose Navigation, which was probably a mistake, because the animations are still experimental and buggy

Pagination:
Paging 3

Async:
Kotlin Coroutines

Networking:
Retrofit

Dependency Injection:
Hilt

Unit Testing:
JUnit 4

UI Testing:
JUnit 4 Compose
