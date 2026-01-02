package com.scoundrel.cardgame

data class Card(
    val suit: Suit,
    val value: Int,
    val name: String
) {
    fun isMonster(): Boolean = suit == Suit.CLUBS || suit == Suit.SPADES
    fun isWeapon(): Boolean = suit == Suit.DIAMONDS
    fun isPotion(): Boolean = suit == Suit.HEARTS
    
    override fun toString(): String = "$name of ${suit.name.lowercase().replaceFirstChar { it.uppercase() }}"
}

