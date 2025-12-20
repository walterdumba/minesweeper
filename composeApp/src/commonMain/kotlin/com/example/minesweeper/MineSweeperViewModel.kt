package com.example.minesweeper

import androidx.lifecycle.ViewModel
import kotlin.random.Random



data class Cell(var value: Char){
    var isVisible:Boolean = false
        get() = field
        set(value) {
            field = value
        }

    constructor():this('0')

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
    private val board = MutableList(rows) { _->
        MutableList(cols){ _-> Cell() }
    }

    init {
        initBoard()
    }

    fun getBoard(): List<Cell> {
        val linear = MutableList(0) { _-> Cell() }
        board.forEach { eachRow ->
            linear.addAll(eachRow)
        }
        return linear
    }

    private fun initBoard() {
        val minePercentage = Random.nextDouble(0.05, 0.2)
        val numberOfMines = (rows * cols * (minePercentage)).toInt()
        val minesLocation = MutableList(0){ _-> 0 to 0}
        //FIXME: Arbitrary number of 9 mines, wrong number of adjacent mines
        (numberOfMines downTo 1).forEach { _ ->
            val y = Random.nextInt(rows)
            val x = Random.nextInt(cols)
            val cell = board[y][x]
            if(!cell.isMine()){
                cell.setMine()
            }
            minesLocation.add(Pair(y, x))
        }

        //Set the number of adjacent mines for the neighbors
        for(pair in minesLocation) {
            val row = pair.first
            val col = pair.second
            for(r in row - 1..row + 1) {
                for(c in col - 1..col + 1) {
                    if(isWithinBoundaries(r, c)) {
                        val cell = board[r][c]
                        if(cell.isNotMine()) {
                            cell.incAdjacentMines()
                        }
                    }
                }
            }
        }
    }

    private fun isWithinBoundaries(row: Int, col: Int): Boolean = row >= 0 && row < rows && col >= 0 && col < cols

    fun print() {
        for(row in 0..< rows) {
            for(col in 0..<cols) {
                val cell = board[row][col]
                print(cell)
            }
            println()
        }
    }
}

class MineSweeperViewModel(var boardGame: BoardGame = BoardGame(16,16)): ViewModel() {
    fun getBoardGame() = boardGame.getBoard()
}
