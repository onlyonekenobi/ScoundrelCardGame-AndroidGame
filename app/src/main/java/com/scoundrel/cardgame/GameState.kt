package com.scoundrel.cardgame

data class GameState(
    val health: Int,
    val dungeonDeck: List<CardDict>,
    val discardPile: List<CardDict>,
    val currentRoom: List<CardDict>,
    val equippedWeapon: CardDict?,
    val lastDefeatedMonsterValue: Int?,
    val canAvoid: Boolean,
    val lastActionWasAvoid: Boolean,
    val gameOver: Boolean,
    val victory: Boolean,
    val selectedCards: List<Int>,
    val roomResolved: Boolean
)

data class CardDict(
    val suit: String,
    val value: Int,
    val name: String
)

fun Card.toDict(): CardDict = CardDict(suit.name, value, name)

fun CardDict.toCard(): Card = Card(
    suit = Suit.valueOf(suit),
    value = value,
    name = name
)

