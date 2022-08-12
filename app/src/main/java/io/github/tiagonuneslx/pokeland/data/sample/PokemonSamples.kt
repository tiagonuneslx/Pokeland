package io.github.tiagonuneslx.pokeland.data.sample

import io.github.tiagonuneslx.pokeland.data.model.Pokemon

object PokemonSamples {

    val Bulbasaur by lazy {
        Pokemon(
            id = "bulbasaur",
            number = 0,
            name = "Bulbasaur",
            description = "Pokemon description",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
            types = listOf(Pokemon.Type.Grass),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 50, 150, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val Ivysaur by lazy {
        Pokemon(
            id = "ivysaur",
            number = 0,
            name = "Ivysaur",
            description = "Pokemon description",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
            types = listOf(Pokemon.Type.Grass),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 50, 150, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val Venusaur by lazy {
        Pokemon(
            id = "venusaur",
            number = 0,
            name = "Venusaur",
            description = "Pokemon description",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
            types = listOf(Pokemon.Type.Grass),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 50, 150, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val Charmander by lazy {
        Pokemon(
            id = "charmander",
            number = 0,
            name = "Charmander",
            description = "Pokemon description",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
            types = listOf(Pokemon.Type.Fire),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 50, 150, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val Charmeleon by lazy {
        Pokemon(
            id = "charmeleon",
            number = 0,
            name = "Charmeleon",
            description = "Pokemon description",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/5.png",
            types = listOf(Pokemon.Type.Fire),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 50, 150, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val Charizard by lazy {
        Pokemon(
            id = "charizard",
            number = 0,
            name = "Charizard",
            description = "Pokemon description",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png",
            types = listOf(Pokemon.Type.Fire),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 50, 150, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val Squirtle by lazy {
        Pokemon(
            id = "squirtle",
            number = 0,
            name = "Squirtle",
            description = "This Pokemon is very unique in the following ways that I will not present, as this is a quick sample.",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png",
            types = listOf(Pokemon.Type.Water),
            height = 0.7f,
            weight = 6.9f,
            abilitySlots = listOf(
                "overgrow",
                "chlorophyll"
            ),
            baseExperience = 64,
            moveSlots = listOf("pound"),
            stats = Pokemon.Stats(100, 70, 120, 200, 230, 30),
            evolutionChainId = null,
        )
    }

    val PokemonList by lazy {
        listOf(
            Bulbasaur,
            Ivysaur,
            Venusaur,
            Charmander,
            Charmeleon,
            Charizard,
            Squirtle,
        )
    }

    object Moves {
        val Pound = Pokemon.Move(
            "pound",
            "Pound",
            100,
            "A big pound",
            100,
            80,
            Pokemon.Type.Fighting
        )
    }
}