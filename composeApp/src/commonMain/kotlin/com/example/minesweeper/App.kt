package com.example.minesweeper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import minesweeper.composeapp.generated.resources.Res
import minesweeper.composeapp.generated.resources._1
import minesweeper.composeapp.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = { StatusScreen() }
        ){ paddingValues ->
            GameScreen(
                modifier = Modifier.padding(paddingValues = paddingValues),
                board = BoardGame(16,16)
            )
        }
    }
}

@Composable
fun StatusScreen() {
    Spacer(Modifier.height(64.dp))
    Column{
        Text(
            text = "Status 😊",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun GameScreen(modifier: Modifier = Modifier, board: BoardGame = BoardGame()){
    Column(modifier = modifier){
        Board(board = board)
    }
}

@Composable
fun Board(modifier: Modifier = Modifier, board: BoardGame = BoardGame()){
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
fun BoardPreview() {
    MaterialTheme {
        Board()
    }
}

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    face: DrawableResource,
    onLongClick: (() -> Unit) = {},
    onClick:(()-> Unit) = {}
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
    MaterialTheme {
        Cell(face = Res.drawable._1, onClick = {})
    }
}