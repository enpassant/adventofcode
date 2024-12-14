import gleam/dict
import gleam/int
import gleam/io
import gleam/string
import gleam/yielder

import util

pub fn main() {
  io.println("Hello from Day 14!")

  // let width = 11
  // let height = 7

  let width = 101
  let height = 103

  let half_width = width / 2 + 1
  let half_height = height / 2 + 1

  // let robots = util.load("data/day14/test.txt", read_robots)
  let robots = util.load("data/day14/input.txt", read_robots)

  let data =
    robots
    |> yielder.from_list
    |> yielder.fold(from: dict.new(), with: fn(data, r) {
      dict.upsert(
        data,
        util.Pos(
          util.mod(r.x + r.vx * 100, width),
          util.mod(r.y + r.vy * 100, height),
        ),
        util.increment,
      )
    })

  data
  |> dict.fold(from: dict.new(), with: fn(quadrant, pos, count) {
    case pos {
      util.Pos(x, y) if x == half_width - 1 || y == half_height - 1 -> quadrant
      util.Pos(x, y) ->
        dict.upsert(
          quadrant,
          util.Pos(x / half_width, y / half_height),
          util.increment_with(_, count),
        )
    }
  })
  |> dict.fold(from: 1, with: fn(total, _, count) { total * count })
  |> io.debug

  let min_step =
    yielder.range(1, 10_000)
    |> yielder.fold(from: #(0, 1_000_000), with: fn(min, i) {
      case
        {
          robots
          |> yielder.from_list
          |> yielder.fold(from: 0, with: fn(sum, r) {
            sum
            + int.absolute_value(util.mod(r.x + r.vx * i, width) - 50)
            + int.absolute_value(util.mod(r.y + r.vy * i, height) - 50)
          })
        }
      {
        max if max < min.1 -> #(i, max)
        _ -> min
      }
    })

  min_step.0
  |> io.debug

  let min_data =
    robots
    |> yielder.from_list
    |> yielder.fold(from: dict.new(), with: fn(data, r) {
      dict.upsert(
        data,
        util.Pos(
          util.mod(r.x + r.vx * min_step.0, width),
          util.mod(r.y + r.vy * min_step.0, height),
        ),
        util.increment,
      )
    })

  yielder.range(from: 42, to: height - 28)
  |> yielder.map(fn(y) {
    yielder.range(from: 34, to: width - 36)
    |> yielder.map(fn(x) {
      io.print(case dict.get(min_data, util.Pos(x, y)) {
        Ok(_) -> "X"
        _ -> "."
      })
    })
    |> yielder.to_list
    io.println("")
  })
  |> yielder.to_list
}

pub fn read_robots(line: String) -> Result(Robot, String) {
  let nums =
    line
    |> string.to_graphemes
    |> yielder.from_list
    |> yielder.chunk(by: fn(ch) { string.contains("0123456789-", ch) })
    |> yielder.map(string.concat)
    |> yielder.filter_map(int.parse)
    |> yielder.to_list

  case nums {
    [x, y, vx, vy] -> Ok(Robot(x, y, vx, vy))
    _ -> Error("")
  }
}

pub type Robot {
  Robot(x: Int, y: Int, vx: Int, vy: Int)
}
