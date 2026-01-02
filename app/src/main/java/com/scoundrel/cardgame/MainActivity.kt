package com.scoundrel.cardgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.scoundrel.cardgame.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoundrelTheme {
                ScoundrelGameScreen()
            }
        }
    }
}

@Composable
fun ScoundrelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF1a4d7a),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E)
        ),
        content = content
    )
}

@Composable
fun ScoundrelGameScreen() {
    val game = remember { ScoundrelGame() }
    var showEndGame by remember { mutableStateOf(false) }
    var gameState by remember { mutableStateOf(0) } // Force recomposition
    
    // Trigger recomposition when game state changes
    val triggerUpdate: () -> Unit = { gameState++ }
    
    LaunchedEffect(Unit) {
        game.drawRoom()
        triggerUpdate()
    }
    
    LaunchedEffect(game.gameOver) {
        if (game.gameOver) {
            showEndGame = true
        }
    }
    
    // Use gameState to force recomposition
    gameState
    
    if (showEndGame && game.gameOver) {
        EndGameScreen(
            game = game,
            onNewGame = {
                game.resetGame()
                game.drawRoom()
                showEndGame = false
                triggerUpdate()
            }
        )
    } else {
        GamePlayScreen(game = game, onStateChange = triggerUpdate)
    }
}

@Composable
fun GamePlayScreen(game: ScoundrelGame, onStateChange: () -> Unit) {
    var undoEnabled by remember { mutableStateOf(true) }
    game.undoEnabled = undoEnabled
    
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Left panel - Game info
        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // New Game button
            Button(
                onClick = {
                    game.resetGame()
                    game.drawRoom()
                    onStateChange()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("New Game")
            }
            
            // Undo controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = undoEnabled,
                    onCheckedChange = { undoEnabled = it }
                )
                Text("Enable Undo", modifier = Modifier.weight(1f))
                Button(
                    onClick = { 
                        if (game.undo()) {
                            onStateChange()
                        }
                    },
                    enabled = undoEnabled && game.stateHistory.isNotEmpty() && !game.gameOver
                ) {
                    Text("Undo")
                }
            }
            
            // Health display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Health", fontWeight = FontWeight.Bold)
                    Text(
                        "${game.health} / ${game.maxHealth}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = { game.health.toFloat() / game.maxHealth },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = when {
                            game.health <= 5 -> Color.Red
                            game.health <= 10 -> Color(0xFFFF9800)
                            else -> Color.Green
                        }
                    )
                }
            }
            
            // Weapon display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Equipped Weapon", fontWeight = FontWeight.Bold)
                    if (game.equippedWeapon != null) {
                        CardDisplay(card = game.equippedWeapon!!, size = 80.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Value: ${game.equippedWeapon!!.value}", fontSize = 12.sp)
                        game.lastDefeatedMonsterValue?.let {
                            Text("Max monster: $it", fontSize = 10.sp)
                        }
                    } else {
                        Text("None", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
            
            // Deck info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Deck", fontWeight = FontWeight.Bold)
                    DeckVisualization(deckSize = game.dungeonDeck.size)
                    Text("Cards: ${game.dungeonDeck.size}", fontSize = 12.sp)
                }
            }
            
            // Discard pile
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Discard Pile", fontWeight = FontWeight.Bold)
                    Text("Cards: ${game.discardPile.size}", fontSize = 12.sp)
                }
            }
        }
        
        // Right panel - Game area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Room",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Room cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                game.currentRoom.forEachIndexed { index, card ->
                    CardDisplay(
                        card = card,
                        isSelected = game.selectedCards.contains(index),
                        onClick = {
                            if (!game.gameOver && !game.roomResolved) {
                                game.selectCard(index)
                                onStateChange()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val (canAvoid, avoidReason) = game.canAvoidRoom()
                Button(
                    onClick = {
                        if (game.avoidRoom()) {
                            if (game.dungeonDeck.isNotEmpty()) {
                                game.drawRoom()
                            }
                            onStateChange()
                        }
                    },
                    enabled = canAvoid && !game.gameOver && !game.roomResolved,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Avoid Room")
                }
                
                Button(
                    onClick = {
                        if (game.resolveRoom()) {
                            if (!game.gameOver) {
                                if (game.dungeonDeck.isNotEmpty() || game.currentRoom.size < 4) {
                                    game.drawRoom()
                                }
                                if (game.dungeonDeck.isEmpty() && game.currentRoom.size <= 1) {
                                    game.gameOver = true
                                    game.victory = true
                                }
                            }
                            onStateChange()
                        }
                    },
                    enabled = game.selectedCards.size == 3 && !game.gameOver && !game.roomResolved,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Resolve Selected Cards")
                }
            }
            
            // Preview
            if (game.selectedCards.size == 3) {
                val preview = game.previewResolution(game.selectedCards)
                PreviewCard(preview = preview, currentHealth = game.health)
            }
            
            // Status
            Text(
                when {
                    game.gameOver -> if (game.victory) "Victory!" else "Defeat!"
                    game.selectedCards.isEmpty() -> "Select 3 cards to resolve, or avoid the room"
                    else -> "Selected: ${game.selectedCards.size}/3 cards"
                },
                fontSize = 14.sp,
                color = when {
                    game.gameOver && game.victory -> Color.Green
                    game.gameOver -> Color.Red
                    else -> Color.White
                }
            )
            
            // Rules section
            RulesSection()
        }
    }
}

@Composable
fun CardDisplay(
    card: Card,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    size: androidx.compose.ui.unit.Dp = 120.dp,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        card.isMonster() -> Color(0xFF2d2d2d)
        card.isWeapon() -> Color(0xFFFFD700)
        card.isPotion() -> Color(0xFFFF6B6B)
        else -> Color.White
    }
    
    val textColor = if (card.isMonster()) Color.White else Color.Black
    
    Box(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, Color.Green, RoundedCornerShape(8.dp))
                } else {
                    Modifier.border(1.dp, Color.White, RoundedCornerShape(8.dp))
                }
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                card.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                when (card.suit) {
                    Suit.CLUBS -> "♣"
                    Suit.SPADES -> "♠"
                    Suit.HEARTS -> "♥"
                    Suit.DIAMONDS -> "♦"
                },
                fontSize = 32.sp,
                color = if (card.suit == Suit.HEARTS || card.suit == Suit.DIAMONDS) Color.Red else textColor
            )
            Text(
                "Value: ${card.value}",
                fontSize = 10.sp,
                color = textColor
            )
            if (isSelected) {
                Text("✓ SELECTED", fontSize = 10.sp, color = Color.Green, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DeckVisualization(deckSize: Int) {
    val layers = if (deckSize == 0) 0 else maxOf(1, (deckSize / 44.0 * 5).toInt())
    
    Box(
        modifier = Modifier
            .size(80.dp, 120.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        if (deckSize == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1a4d7a))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
            ) {
                Text(
                    "Empty",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        } else {
            // Draw stacked cards effect using card back image
            val cardBackPainter = painterResource(id = R.drawable.card_back)
            
            // Calculate maximum offset needed for the stack (in dp)
            val maxOffsetDp = (layers - 1) * 2
            
            // Center the stack (canvas is 80x120 dp, card is 60x100 dp)
            val baseX = (80 - 60 - maxOffsetDp) / 2
            val baseY = (120 - 100 - maxOffsetDp) / 2
            
            (0 until layers).forEach { i ->
                val offsetX = i * 2
                val offsetY = i * 2
                
                Image(
                    painter = cardBackPainter,
                    contentDescription = "Card back",
                    modifier = Modifier
                        .offset(x = (baseX + offsetX).dp, y = (baseY + offsetY).dp)
                        .size(60.dp, 100.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun PreviewCard(preview: ScoundrelGame.PreviewResult, currentHealth: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Preview: What Will Happen", fontWeight = FontWeight.Bold)
            
            if (preview.healthChange != 0) {
                Text(
                    "Health: $currentHealth → ${preview.finalHealth} " +
                            if (preview.healthChange > 0) "(+${preview.healthRestored})" 
                            else "(${preview.damageTaken} damage)",
                    color = if (preview.healthChange > 0) Color.Green 
                           else if (preview.finalHealth <= 0) Color.Red 
                           else Color(0xFFFF9800),
                    fontWeight = FontWeight.Bold
                )
            }
            
            preview.newWeapon?.let {
                Text("Equip Weapon: ${it.name} (Value: ${it.value})", color = Color.Blue)
            }
            
            preview.details.forEach { detail ->
                Text("• $detail", fontSize = 11.sp)
            }
            
            if (preview.finalHealth <= 0) {
                Text("⚠ WARNING: This will defeat you!", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RulesSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Rules & Card Types", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Card Types:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("♣ ♠ Monsters (Clubs, Spades): Deal damage equal to card value")
            Text("♦ Weapons (Diamonds 2-10): Reduce monster damage by weapon value")
            Text("♥ Potions (Hearts 2-10): Restore health (1 per turn, max 20)")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Game Rules:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("• Each turn, 4 cards form a Room")
            Text("• Select 3 cards to resolve, or avoid the room")
            Text("• Cannot avoid two rooms consecutively")
            Text("• Win: Clear the entire dungeon")
            Text("• Lose: Health reaches zero")
        }
    }
}

@Composable
fun EndGameScreen(game: ScoundrelGame, onNewGame: () -> Unit) {
    val score = game.getScore()
    val isVictory = score >= 0
    
    val rankings = listOf(
        20 to "Legendary Scoundrel",
        15 to "Master Scoundrel",
        10 to "Expert Scoundrel",
        5 to "Skilled Scoundrel",
        0 to "Novice Scoundrel",
        -10 to "Unlucky Scoundrel",
        -20 to "Hapless Scoundrel",
        -50 to "Defeated Scoundrel"
    )
    
    val currentRank = rankings.firstOrNull { score >= it.first }?.second ?: "Defeated Scoundrel"
    
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    if (isVictory) "Victory!" else "Defeat",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isVictory) Color.Green else Color.Red
                )
                
                Text("Final Score: $score", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                
                Text("Your Rank: $currentRank", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                
                Text("Ranking Scale:", fontWeight = FontWeight.Bold)
                rankings.forEach { (threshold, rank) ->
                    Text(
                        "${threshold}+ : $rank",
                        color = if (threshold >= 0) Color.Green else Color.Red,
                        fontSize = 12.sp
                    )
                }
                
                Button(onClick = onNewGame) {
                    Text("New Game")
                }
                
                Text(
                    if (isVictory) "Congratulations! You successfully navigated the dungeon!"
                    else "Better luck next time! The dungeon proved too challenging.",
                    textAlign = TextAlign.Center,
                    color = if (isVictory) Color.Green else Color.Red
                )
            }
        }
    }
}

