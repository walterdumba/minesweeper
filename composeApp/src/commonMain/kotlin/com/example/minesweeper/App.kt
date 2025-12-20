package com.example.minesweeper
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
        var showContent by remember { mutableStateOf(false) }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Board(board = BoardGame(8,8))
        }
    }
}


@Composable
fun Board(modifier: Modifier = Modifier, board: BoardGame = BoardGame()){
    val drawablesMap = Res.allDrawableResources
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(board.cols)
    ){
        items( board.getBoard()){ cell ->
            var key = when {
                !cell.isVisible -> "hidden"
                cell.isMine() -> "mine"
                cell.isEmpty() -> "empty"
                cell.isNotMine() -> "_${cell.value}"
                else -> "empty"
            }
            val drawable = drawablesMap[key]
            Cell(face = drawable!!)
        }
    }
}

@Preview
@Composable
fun BoardPreview(){
    MaterialTheme {
        Board()
    }
}

@Composable
fun Cell(modifier: Modifier = Modifier, face: DrawableResource){
    Column(
        modifier = modifier.border(width = 2.dp, color = Color.Black, shape = RectangleShape),
    ) {
        Image(painter = painterResource(resource = face), contentDescription = null)
    }
}

@Preview
@Composable
fun CellPreview(){
    MaterialTheme {
        Cell(face = Res.drawable._1)
    }
}