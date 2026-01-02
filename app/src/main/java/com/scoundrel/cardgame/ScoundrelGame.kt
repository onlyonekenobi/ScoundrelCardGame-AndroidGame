package com.scoundrel.cardgame

import kotlin.random.Random

class ScoundrelGame {
    var undoEnabled: Boolean = true
    val stateHistory = mutableListOf<GameState>()  // Made public for UI access
    
    var health: Int = 20
    val maxHealth: Int = 20
    var dungeonDeck: MutableList<Card> = mutableListOf()
    var discardPile: MutableList<Card> = mutableListOf()
    var currentRoom: MutableList<Card> = mutableListOf()
    var equippedWeapon: Card? = null
    var lastDefeatedMonsterValue: Int? = null
    var canAvoid: Boolean = true
    var lastActionWasAvoid: Boolean = false
    var gameOver: Boolean = false
    var victory: Boolean = false
    var selectedCards: MutableList<Int> = mutableListOf()
    var roomResolved: Boolean = false
    
    init {
        resetGame()
    }
    
    fun resetGame() {
        health = 20
        dungeonDeck = createDeck()
        discardPile = mutableListOf()
        currentRoom = mutableListOf()
        equippedWeapon = null
        lastDefeatedMonsterValue = null
        canAvoid = true
        lastActionWasAvoid = false
        gameOver = false
        victory = false
        selectedCards = mutableListOf()
        roomResolved = false
        stateHistory.clear()
    }
    
    private fun createDeck(): MutableList<Card> {
        val deck = mutableListOf<Card>()
        
        // Add all black cards (Clubs and Spades) - monsters
        listOf(Suit.CLUBS, Suit.SPADES).forEach { suit ->
            // Number cards 2-10
            (2..10).forEach { value ->
                deck.add(Card(suit, value, value.toString()))
            }
            // Face cards
            deck.add(Card(suit, 11, "Jack"))
            deck.add(Card(suit, 12, "Queen"))
            deck.add(Card(suit, 13, "King"))
            deck.add(Card(suit, 14, "Ace"))
        }
        
        // Add Diamonds 2-10 (weapons)
        (2..10).forEach { value ->
            deck.add(Card(Suit.DIAMONDS, value, value.toString()))
        }
        
        // Add Hearts 2-10 (potions)
        (2..10).forEach { value ->
            deck.add(Card(Suit.HEARTS, value, value.toString()))
        }
        
        deck.shuffle()
        return deck
    }
    
    fun saveState() {
        if (!undoEnabled) return
        
        val state = GameState(
            health = health,
            dungeonDeck = dungeonDeck.map { it.toDict() },
            discardPile = discardPile.map { it.toDict() },
            currentRoom = currentRoom.map { it.toDict() },
            equippedWeapon = equippedWeapon?.toDict(),
            lastDefeatedMonsterValue = lastDefeatedMonsterValue,
            canAvoid = canAvoid,
            lastActionWasAvoid = lastActionWasAvoid,
            gameOver = gameOver,
            victory = victory,
            selectedCards = selectedCards.toList(),
            roomResolved = roomResolved
        )
        
        stateHistory.add(state)
        if (stateHistory.size > 50) {
            stateHistory.removeAt(0)
        }
    }
    
    fun undo(): Boolean {
        if (!undoEnabled || stateHistory.isEmpty()) return false
        
        val state = stateHistory.removeLastOrNull() ?: return false
        
        health = state.health
        dungeonDeck = state.dungeonDeck.map { it.toCard() }.toMutableList()
        discardPile = state.discardPile.map { it.toCard() }.toMutableList()
        currentRoom = state.currentRoom.map { it.toCard() }.toMutableList()
        equippedWeapon = state.equippedWeapon?.toCard()
        lastDefeatedMonsterValue = state.lastDefeatedMonsterValue
        canAvoid = state.canAvoid
        lastActionWasAvoid = state.lastActionWasAvoid
        gameOver = state.gameOver
        victory = state.victory
        selectedCards = state.selectedCards.toMutableList()
        roomResolved = state.roomResolved
        
        return true
    }
    
    fun drawRoom() {
        val cardsNeeded = 4 - currentRoom.size
        
        when {
            cardsNeeded <= 0 -> {
                // Already have 4 cards
            }
            dungeonDeck.size < cardsNeeded -> {
                currentRoom.addAll(dungeonDeck)
                dungeonDeck.clear()
            }
            else -> {
                currentRoom.addAll(dungeonDeck.take(cardsNeeded))
                dungeonDeck = dungeonDeck.drop(cardsNeeded).toMutableList()
            }
        }
        
        selectedCards.clear()
        roomResolved = false
    }
    
    fun canAvoidRoom(): Pair<Boolean, String> {
        if (!canAvoid) return Pair(false, "Cannot avoid (general restriction)")
        if (lastActionWasAvoid) return Pair(false, "Cannot avoid two rooms consecutively")
        if (currentRoom.isEmpty()) return Pair(false, "No room to avoid")
        
        val cardsAfterAvoid = dungeonDeck.size + currentRoom.size
        if (cardsAfterAvoid < 4) {
            return Pair(false, "Not enough cards remaining ($cardsAfterAvoid cards). Avoiding would leave insufficient cards to form the next room (need at least 4).")
        }
        
        return Pair(true, "")
    }
    
    fun avoidRoom(): Boolean {
        val (canAvoid, _) = canAvoidRoom()
        if (!canAvoid) return false
        
        saveState()
        
        if (currentRoom.isNotEmpty()) {
            dungeonDeck.addAll(currentRoom)
            currentRoom.clear()
            lastActionWasAvoid = true
            roomResolved = true
            return true
        }
        return false
    }
    
    fun selectCard(cardIndex: Int) {
        if (cardIndex < 0 || cardIndex >= currentRoom.size) return
        
        if (selectedCards.contains(cardIndex)) {
            selectedCards.remove(cardIndex)
        } else {
            if (selectedCards.size < 3) {
                selectedCards.add(cardIndex)
            }
        }
    }
    
    fun resolveRoom(): Boolean {
        if (selectedCards.size != 3 || roomResolved) return false
        
        saveState()
        
        val cardsToResolve = selectedCards.map { currentRoom[it] }
        val remainingCard = currentRoom.firstOrNull { 
            !selectedCards.contains(currentRoom.indexOf(it))
        } ?: return false
        
        var potionUsedThisTurn = false
        
        // Find highest value weapon if multiple weapons selected
        val weaponsInSelection = cardsToResolve.filter { it.isWeapon() }
        val highestWeapon = weaponsInSelection.maxByOrNull { it.value }
        
        cardsToResolve.forEach { card ->
            when {
                card.isPotion() -> {
                    if (!potionUsedThisTurn) {
                        health = minOf(maxHealth, health + card.value)
                        potionUsedThisTurn = true
                    }
                    discardPile.add(card)
                }
                card.isWeapon() -> {
                    if (card == highestWeapon) {
                        equippedWeapon = card
                        lastDefeatedMonsterValue = null
                    }
                    discardPile.add(card)
                }
                card.isMonster() -> {
                    var damage = card.value
                    
                    equippedWeapon?.let { weapon ->
                        lastDefeatedMonsterValue?.let { lastMonster ->
                            if (card.value > lastMonster) {
                                // Can't use weapon
                                health -= damage
                            } else {
                                damage = maxOf(0, damage - weapon.value)
                                health -= damage
                                lastDefeatedMonsterValue = card.value
                            }
                        } ?: run {
                            // First use of weapon
                            damage = maxOf(0, damage - weapon.value)
                            health -= damage
                            lastDefeatedMonsterValue = card.value
                        }
                    } ?: run {
                        // Fight barehanded
                        health -= damage
                    }
                    
                    discardPile.add(card)
                }
            }
        }
        
        // Keep remaining card for next room
        currentRoom = mutableListOf(remainingCard)
        selectedCards.clear()
        roomResolved = true
        lastActionWasAvoid = false
        
        // Check game over
        if (health <= 0) {
            gameOver = true
            victory = false
        } else if (dungeonDeck.isEmpty() && currentRoom.size <= 1) {
            gameOver = true
            victory = true
        }
        
        return true
    }
    
    data class PreviewResult(
        val healthChange: Int,
        val damageTaken: Int,
        val healthRestored: Int,
        val newWeapon: Card?,
        val weaponUsed: Boolean,
        val monstersFought: List<Card>,
        val potionsUsed: List<Card>,
        val finalHealth: Int,
        val details: List<String>
    )
    
    fun previewResolution(selectedIndices: List<Int>): PreviewResult {
        if (selectedIndices.size != 3) {
            return PreviewResult(0, 0, 0, null, false, emptyList(), emptyList(), health, emptyList())
        }
        
        val cardsToResolve = selectedIndices.map { currentRoom[it] }
        var simHealth = health
        var simWeapon = equippedWeapon
        var simLastMonster = lastDefeatedMonsterValue
        var totalDamage = 0
        var totalHealing = 0
        var newWeapon: Card? = null
        val monstersFought = mutableListOf<Card>()
        val potionsUsed = mutableListOf<Card>()
        val details = mutableListOf<String>()
        var potionUsedThisTurn = false
        
        // Find highest weapon
        val weaponsInSelection = cardsToResolve.filter { it.isWeapon() }
        val highestWeapon = weaponsInSelection.maxByOrNull { it.value }
        
        cardsToResolve.forEach { card ->
            when {
                card.isPotion() -> {
                    if (!potionUsedThisTurn) {
                        val healing = minOf(maxHealth - simHealth, card.value)
                        simHealth += healing
                        totalHealing += healing
                        potionsUsed.add(card)
                        details.add("Potion (${card.name}): +$healing health")
                        potionUsedThisTurn = true
                    } else {
                        details.add("Potion (${card.name}): Already used potion this turn")
                    }
                }
                card.isWeapon() -> {
                    if (card == highestWeapon) {
                        newWeapon = card
                        simWeapon = card
                        simLastMonster = null
                        details.add("Weapon (${card.name}): Equip (Value: ${card.value})")
                    } else {
                        details.add("Weapon (${card.name}): Not equipped (lower value than ${highestWeapon?.name})")
                    }
                }
                card.isMonster() -> {
                    var damage = card.value
                    
                    simWeapon?.let { weapon ->
                        simLastMonster?.let { lastMonster ->
                            if (card.value > lastMonster) {
                                simHealth -= damage
                                totalDamage += damage
                                details.add("Monster (${card.name}, Value: ${card.value}): $damage damage (barehanded - weapon restricted)")
                            } else {
                                val damageAfterWeapon = maxOf(0, damage - weapon.value)
                                simHealth -= damageAfterWeapon
                                totalDamage += damageAfterWeapon
                                simLastMonster = card.value
                                val blocked = damage - damageAfterWeapon
                                details.add("Monster (${card.name}, Value: ${card.value}): $damageAfterWeapon damage (weapon blocked $blocked)")
                            }
                        } ?: run {
                            val damageAfterWeapon = maxOf(0, damage - weapon.value)
                            simHealth -= damageAfterWeapon
                            totalDamage += damageAfterWeapon
                            simLastMonster = card.value
                            val blocked = damage - damageAfterWeapon
                            details.add("Monster (${card.name}, Value: ${card.value}): $damageAfterWeapon damage (weapon blocked $blocked)")
                        }
                    } ?: run {
                        simHealth -= damage
                        totalDamage += damage
                        details.add("Monster (${card.name}, Value: ${card.value}): $damage damage (barehanded)")
                    }
                    
                    monstersFought.add(card)
                }
            }
        }
        
        return PreviewResult(
            healthChange = simHealth - health,
            damageTaken = totalDamage,
            healthRestored = totalHealing,
            newWeapon = newWeapon,
            weaponUsed = details.any { "weapon" in it.lowercase() && "blocked" in it.lowercase() },
            monstersFought = monstersFought,
            potionsUsed = potionsUsed,
            finalHealth = maxOf(0, simHealth),
            details = details
        )
    }
    
    fun getScore(): Int {
        return if (victory) {
            health
        } else {
            val remainingDamage = dungeonDeck.filter { it.isMonster() }.sumOf { it.value } +
                    currentRoom.filter { it.isMonster() }.sumOf { it.value }
            -remainingDamage
        }
    }
}

