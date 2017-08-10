package uk.co.bbc.kotlingameoflife

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameOfLifeTests {
    @Test
    fun testSingleCellDiesAfterEvolving() {
        val game = Game(setOf(Cell(1, 1)))
        val evolvedGame = game.evolve()
        assertFalse(evolvedGame.aliveCells.contains(Cell(1, 1)))
    }

    @Test
    fun testCellsWithThreeNeighboursComeToLifeAfterEvolving() {
        val game = Game(setOf(Cell(0, 0), Cell(1, 0), Cell(2, 0)))
        val evolvedGame = game.evolve()
        assertTrue(evolvedGame.aliveCells.contains(Cell(1, 1)))

//        GamePrinter(game).print()
        GamePrinter(evolvedGame).print()
    }
}

data class Cell(val x: Int, val y: Int) {
    fun neighbours(): Set<Cell> {
        val c = listOf(Cell(x - 1, y - 1),
                Cell(x, y - 1),
                Cell(x + 1, y - 1),
                Cell(x - 1, y),
                Cell(x + 1, y),
                Cell(x - 1, y + 1),
                Cell(x, y + 1),
                Cell(x + 1, y + 1))
        return HashSet<Cell>(c)
    }
}

class Game(val aliveCells: Set<Cell>) {
    fun evolve(): Game {
        val cellsToRemainAlive = aliveCells.filter {
            aliveNeighbourCount(it) in 2..3
        }

        val cellsToSpawn = aliveCells.flatMap {
            cellsToSpawn(it)
        }.toList()

        return Game(cellsToRemainAlive.union(cellsToSpawn))
    }

    private fun cellsToSpawn(cell: Cell): List<Cell> {
        return cell.neighbours().filter {
            aliveNeighbourCount(it) == 3
        }
    }

    private fun aliveNeighbourCount(cell: Cell): Int {
        return aliveCells.filter {
            it.neighbours().contains(cell)
        }.count()
    }
}

class GamePrinter(val game: Game) {
    fun print() {
        val minX = game.aliveCells.map { (x) -> x }.min()!! - 1
        val minY = game.aliveCells.map { cell -> cell.y }.min()!! - 1
        val maxX = game.aliveCells.map { (x) -> x }.max()!! + 1
        val maxY = game.aliveCells.map { cell -> cell.y }.max()!! + 1

        paintCellAt(game, minX, minY, maxX, maxY, minX, minY)
    }

    private fun paintCellAt(game: Game, minX: Int, minY: Int, maxX: Int, maxY: Int, x: Int, y: Int) {
        if (game.aliveCells.contains(Cell(x, y))) {
            print("X")
        } else {
            print(".")
        }

        if (x < maxX) {
            paintCellAt(game, minX, minY, maxX, maxY, x + 1, y)
        } else if (y < maxY) {
            println("")
            paintCellAt(game, minX, minY, maxX, maxY, minX, y + 1)
        } else {
            println("")
            println("evolution complete")
        }
    }
}