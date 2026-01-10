package com.example.minesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import kotlin.random.Random


class Position(var x:Int, var y:Int)

data class Cell(var value: Char){
    var isFlagged by mutableStateOf(false)
    var visible by mutableStateOf(false)
    lateinit var position: Position

    constructor(): this('0')
    constructor(x: Int,  y: Int) : this() {
        position = Position(x,y)
    }

    fun isMine() = value == '*'
    fun isNotMine() = !isMine()
    fun isEmpty() = value == '0'
    fun setMine() { value = '*' }
    fun incAdjacentMines() { value+= 1 }
    override fun toString(): String {
        return "[$value]"
    }

}


class BoardGame(var rows:Int = 16, var cols: Int = 16){
    private var started by mutableStateOf(false)
    private lateinit var _board: MutableList<MutableList<Cell>>
    private var board by mutableStateOf(listOf<Cell>())

    init {
        initBoard()
    }

    fun getSnapShot(): List<Cell> {
        return board
    }

    private fun newSnapshot(): List<Cell> {
        val linear = MutableList(0) { _ -> Cell() }.toMutableStateList()
        _board.forEach { eachRow ->
            linear.addAll(eachRow)
        }
        return linear
    }

    private fun initBoard() {
        //This is required so that the snapshotList is get a different state and trigger the recomposition
        //TODO: _board created twice for no good reason
        //TODO: Maybe change the _board to a linear array as creating arrays constantly during the recomposition doesn't sound ideal
        _board = MutableList(rows) { y ->
            MutableList(cols){ x-> Cell(x , y) }
        }
        val minePercentage = Random.nextDouble(0.05, 0.2)
        val numberOfMines = (rows * cols * (minePercentage)).toInt()
        val minesLocation = MutableList(0){ _-> 0 to 0}
        //FIXME: Arbitrary number of 9 mines, wrong number of adjacent mines
        (numberOfMines downTo 1).forEach { _ ->
            val y = Random.nextInt(rows)
            val x = Random.nextInt(cols)
            val cell = _board[y][x]
            if(!cell.isMine()){
                cell.setMine()
            }
            minesLocation.add(Pair(y, x))
        }

        //Set the number of adjacent mines for the neighbors
        for (pair in minesLocation) {
            val row = pair.first
            val col = pair.second
            for (r in row - 1..row + 1) {
                for (c in col - 1..col + 1) {
                    if (isWithinBoundaries(r, c)) {
                        val cell = _board[r][c]
                        if (cell.isNotMine()) {
                            cell.incAdjacentMines()
                        }
                    }
                }
            }
        }
        board = newSnapshot()
    }

    fun restart() = initBoard()

    private fun isWithinBoundaries(row: Int, col: Int): Boolean = row >= 0 && row < rows && col >= 0 && col < cols

    fun print() {
        for(row in 0..< rows) {
            for(col in 0..<cols) {
                val cell = _board[row][col]
                print(cell)
            }
            println()
        }
    }

    fun expand(cell: Cell) {
        if(!started){
            started = true
        }
        if(cell.isFlagged){
            return
        }
        if(!cell.isEmpty()){
            cell.visible = true
        }else{
            expandRecursively(cell)
        }
    }

    private fun expandRecursively(cell: Cell) {
        if(cell.visible){
            return
        }
        cell.visible = true
        val row = cell.position.y
        val col = cell.position.x
        for(r in row - 1..row + 1) {
            for(c in col - 1..col + 1) {
                if(isWithinBoundaries(r, c) && !(r == row && c == col)) {
                    val neighbor = _board[r][c]
                    //FIXME: right now it is expanding flagged cells as well
                    if(neighbor.isEmpty()) {
                        expandRecursively(neighbor)
                    }
                }
            }
        }
    }
}

class MineSweeperViewModel(var boardGame: BoardGame = BoardGame(16,16)): ViewModel() {

    fun getBoardGame() = boardGame.getSnapShot()
}
