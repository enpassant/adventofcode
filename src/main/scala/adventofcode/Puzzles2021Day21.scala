package adventofcode

import scala.io.Source
import java.io.PrintWriter

object Puzzles2021Day21 {
  def puzzle(
    fileName: String
  ) = {
    val game = readData(fileName)
    val playedGame = play(game)
    val loser = playedGame.players(1 - game.next)
    playedGame.rolls.toLong * loser.score
  }

  def play(game: Game): Game = {
    val current = game.next
    val newGame = move(game)
    val player = newGame.players(current)
    if (player.score >= 1000) newGame else play(newGame)
  }

  def move(game: Game) = {
    val player = game.players(game.next)
    val dices = (1 to 3).scanLeft(game.dice) {
      case (d, i) => rolls(d)
    }.tail
    val newPosition = (player.position + dices.sum - 1) % 10 + 1
    val newPlayer = player.copy(
      position = newPosition,
      score = player.score + newPosition
    )
    val players = game.players.updated(game.next, newPlayer)
    Game(dices.last, game.rolls + 3, 1 - game.next, players)
  }

  def rolls(dice: Int) = (dice % 100) + 1

  case class Game(dice: Int, rolls: Int, next: Int, players: Vector[Player])
  case class Player(id: Int, position: Int, score: Long = 0)

  def puzzleTwo(
    fileName: String
  ) = {
    val game = readData(fileName)
    val gameDirac = GameDirac(game.rolls, game.next,
      game.players.map(player =>
        PlayerDirac(player.id, List(PosDirac(player.position, 1L, 0)))))
    val playedGame = playDirac(gameDirac)
    playedGame.players
      .map(_.winCount)
      .max
  }

  def playDirac(game: GameDirac): GameDirac = {
    val current = game.next
    val newGame = moveDirac(game)
    val player = newGame.players(current)
    val opponent = newGame.players(1 - current)
    val opponentUniverseCount = opponent.position
      .map(_.count)
      .sum
    val winCount = player.position
      .filter(_.score >= 21)
      .map(_.count * opponentUniverseCount)
      .sum
    val newPosition = player.position
      .filter(_.score < 21)
    val newPlayer = player.copy(
      position = newPosition,
      winCount = player.winCount + winCount
    )
    val evaluatedGame = newGame.copy(
      players = newGame.players.updated(current, newPlayer)
    )
    if (newPosition.isEmpty) evaluatedGame else playDirac(evaluatedGame)
  }

  def moveDirac(game: GameDirac) = {
    val player = game.players(game.next)
    val dices = rollsDirac()
    val newPosition = player.position.flatMap(
      posDirac => dices.map(dice => {
        val newPos = (posDirac.position + dice.position - 1) % 10 + 1
        PosDirac(
          newPos,
          posDirac.count * dice.count,
          posDirac.score + newPos
        )
      })
    )
    val newPlayer = player.copy(
      position = newPosition
    )
    val players = game.players.updated(game.next, newPlayer)
    GameDirac(game.rolls + 3, 1 - game.next, players)
  }

  def rollsDirac() = List(
    PosDirac(3, 1L),
    PosDirac(4, 3L),
    PosDirac(5, 6L),
    PosDirac(6, 7L),
    PosDirac(7, 6L),
    PosDirac(8, 3L),
    PosDirac(9, 1L)
  )

  case class PosDirac(position: Int, count: Long, score: Int = 0)
  case class GameDirac(
    rolls: Int,
    next: Int,
    players: Vector[PlayerDirac]
  )
  case class PlayerDirac(id: Int,
    position: List[PosDirac],
    winCount: Long = 0
  )

  def readData(
    fileName: String
  ) = {
    val pattern = "Player (\\d+) starting position: (\\d+)".r
    val players = Source.fromFile(fileName)
      .getLines
      .map(s => s match {
        case pattern(id, position) => Some(Player(id.toInt, position.toInt))
        case _ => None
      }).toVector
      .filter(_.isDefined)
      .map(_.get)

    Game(0, 0, 0, players)
  }
}
