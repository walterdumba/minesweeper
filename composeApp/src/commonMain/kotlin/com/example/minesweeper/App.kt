package com.example.minesweeper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        var showContent by remember { mutableStateOf(false) }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Board(board = BoardGame(16,16))
        }
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
                !cell.visible -> "hidden"
                cell.isMine() -> "mine"
                cell.isEmpty() -> "empty"
                cell.isNotMine() -> "_${cell.value}"
                else -> "empty"
            }
            val drawable = drawablesMap[key]

            //TODO: Right click will add the flag
            AnimatedCell(modifier = modifier, face = drawable!!, visible = { cell.visible }) {
                board.expand(cell)
                if(cell.isMine()){
                    //TODO: Game over
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
fun Cell(modifier: Modifier = Modifier, face: DrawableResource, onClick:()-> Unit){
    Card(
        onClick = onClick,
        modifier = modifier.background(color = Color.Black)
    ) {
        Image(painter = painterResource(resource = face), contentDescription = null)
    }
}

@Composable
fun AnimatedCell(
    modifier: Modifier = Modifier,
    face: DrawableResource,
    visible:() ->Boolean = {false},
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible(),
        enter = fadeIn(
            animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
        ),
        modifier = Modifier.background(Color.White)
    ){
        Cell(modifier = modifier, face = face, onClick = onClick)
    }
    if(!visible()) {
        Cell(modifier = modifier, face = face, onClick = onClick)
    }
}


@Preview
@Composable
fun AnimatedCellPreview(){
    AnimatedCell(visible = {false}, face = Res.drawable._1, onClick = {})
}

@Preview
@Composable
fun CellPreview(){
    MaterialTheme {
        Cell(face = Res.drawable._1, onClick = {})
    }
}