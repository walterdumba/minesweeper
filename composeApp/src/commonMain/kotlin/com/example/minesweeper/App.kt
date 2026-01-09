package com.example.minesweeper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelab.basiclayouts.ui.theme.MineSweeperTheme
import minesweeper.composeapp.generated.resources.PressStart2P_Regular
import minesweeper.composeapp.generated.resources.Res
import minesweeper.composeapp.generated.resources._1
import minesweeper.composeapp.generated.resources.allDrawableResources
import minesweeper.composeapp.generated.resources.restart
import minesweeper.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MineSweeperTheme{
        Scaffold(
        ){ paddingValues ->
            GameScreen(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                board = BoardGame(12,8)
            )
        }
    }
}

@Composable
fun StatusScreen(modifier: Modifier = Modifier) {
    Spacer(modifier.height(16.dp))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
         Cell(modifier = Modifier, face = Res.drawable.restart
         ){
             //TODO:
         }
        Spacer(Modifier.padding(16.dp))
        TimerPanel(text = "0:00")
        Spacer(Modifier.padding(16.dp))
        Cell(modifier = Modifier, face = Res.drawable.settings
        ){
            //TODO:
        }

    }
    Spacer(modifier.height(16.dp))
}

@Preview
@Composable
fun StatusScreenPreview(){
    MineSweeperTheme {
        StatusScreen()
    }
}

@Composable
fun TimerPanel(
    modifier: Modifier = Modifier,
    text: String,
    onStart: (()-> Unit) = {}, //TODO:
    onGameOver:(()->Unit) = {}//TODO:
){
    Row (modifier = modifier){
        val brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Yellow))
        val fontFamily = FontFamily(Font(Res.font.PressStart2P_Regular))
        Text(
            fontFamily = fontFamily,
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            style = TextStyle(brush = brush, fontWeight = FontWeight.Bold)
        )
    }
}

@Preview
@Composable
fun TimerPanelPreview(){
    MineSweeperTheme {
        TimerPanel(text = "0:00")
    }
}

@Composable
fun GameScreen(modifier: Modifier = Modifier, board: BoardGame = BoardGame()){
    Column(modifier = modifier){
        StatusScreen()
        BoardScreen(board = board)
    }
}

@Preview
@Composable
fun GameScreenPreview(){
    MineSweeperTheme {
        GameScreen(board =  BoardGame(8,8))
    }

}

@Composable
fun BoardScreen(modifier: Modifier = Modifier, board: BoardGame = BoardGame()){
    val drawablesMap = Res.allDrawableResources
    LazyVerticalGrid(
        modifier = modifier.background(color = Color.Black),
        columns = GridCells.Fixed(board.cols)
    ){
        items(
            board.getBoard()
        ){ cell ->
            val key = when {
                cell.isFlagged -> "flag"
                !cell.visible -> "hidden"
                cell.isMine() -> "mine"
                cell.isEmpty() -> "empty"
                cell.isNotMine() -> "_${cell.value}"
                else -> "empty"
            }
            val drawable = drawablesMap[key]
            AnimatedCell(
                modifier = modifier,
                face = drawable!!,
                visible = { cell.visible },
                onLongClick = { cell.isFlagged = !cell.isFlagged },
            ) {
                if(!cell.isFlagged) {
                    board.expand(cell)
                    if(cell.isMine()) {
                        //TODO: Game over
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BoardScreenPreview() {
    MineSweeperTheme {
        BoardScreen()
    }
}

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    face: DrawableResource,
    onLongClick: (() -> Unit) = {},
    onClick: (()-> Unit) = {}
){
    Card(
        modifier = modifier
            .background(color = Color.Black)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Image(painter = painterResource(resource = face), contentDescription = null)
    }
}

@Composable
fun AnimatedCell(
    modifier: Modifier = Modifier,
    face: DrawableResource,
    visible: () -> Boolean = { false },
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible(),
        enter = fadeIn(
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        ),
        modifier = Modifier.background(Color.White)
    ){
        Cell(modifier = modifier, face = face)
    }
    if(!visible()) {
        Cell(modifier = modifier, face = face, onLongClick = onLongClick, onClick = onClick)
    }
}


@Preview
@Composable
fun AnimatedCellPreview(){
    AnimatedCell(face = Res.drawable._1, onLongClick = {}) {}
}

@Preview
@Composable
fun CellPreview(){
    MineSweeperTheme {
        Cell(face = Res.drawable._1, onClick = {})
    }
}