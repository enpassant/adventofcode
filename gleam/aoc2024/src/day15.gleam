import gleam/dict
import gleam/erlang
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string

import util.{type Pos, Pos}

pub fn main() {
  io.println("Hello from Day 15!")

  // let file_name = "data/day15/test.txt"
  let file_name = "data/day15/input.txt"
  let map = util.load_matrix(file_name, fn(str) { Ok(str) })
  // |> draw

  let moves =
    util.load_second(file_name, fn(str) { Ok(string.replace(str, "\n", "")) })
    |> string.concat

  moves
  |> string.to_graphemes
  |> list.fold(map, move)
  // |> draw
  |> sum
  |> io.debug

  moves
  |> string.to_graphemes
  |> list.fold(map |> wider, move)
  // |> draw
  |> sum
  |> io.debug
}

fn wider(map: dict.Dict(Pos, String)) -> dict.Dict(Pos, String) {
  let max_pos =
    map
    |> dict.fold(Pos(0, 0), fn(pos, p, _) {
      Pos(int.max(pos.x, p.x), int.max(pos.y, p.y))
    })

  list.range(from: 0, to: max_pos.y)
  |> list.fold(map, fn(my, y) {
    list.range(from: max_pos.x - 1, to: 0)
    |> list.fold(my, fn(mx, x) { map_char(mx, x, y) })
  })
}

fn map_char(
  map: dict.Dict(Pos, String),
  x: Int,
  y: Int,
) -> dict.Dict(Pos, String) {
  let ch = map |> dict.get(Pos(x, y)) |> result.unwrap(".")
  let n_ch_1 = case ch {
    "O" -> "["
    _ -> ch
  }
  let n_ch_2 = case ch {
    "O" -> "]"
    "@" -> "."
    _ -> ch
  }
  map
  |> dict.upsert(Pos(x * 2 + 1, y), fn(_) { n_ch_2 })
  |> dict.upsert(Pos(x * 2, y), fn(_) { n_ch_1 })
}

fn sum(map: dict.Dict(Pos, String)) -> Int {
  map
  |> dict.fold(0, fn(sum, pos, ch) {
    case ch {
      "O" | "[" -> sum + pos.y * 100 + pos.x
      _ -> sum
    }
  })
}

fn draw(map: dict.Dict(Pos, String)) -> dict.Dict(Pos, String) {
  let max_pos =
    map
    |> dict.fold(Pos(0, 0), fn(pos, p, _) {
      Pos(int.max(pos.x, p.x), int.max(pos.y, p.y))
    })

  list.range(from: 0, to: max_pos.y)
  |> list.each(fn(y) {
    list.range(from: 0, to: max_pos.x)
    |> list.each(fn(x) {
      io.print(map |> dict.get(Pos(x, y)) |> result.unwrap("."))
    })
    io.println("")
  })

  map
}

fn move(map: dict.Dict(Pos, String), move: String) -> dict.Dict(Pos, String) {
  // #("Move: ", move)
  // |> io.debug
  // erlang.get_line("Enter to continue: ")

  let robot_pos = util.find_pos(map, "@")
  case move_rec(map, [robot_pos], move) {
    Ok(new_map) -> new_map
    Error(_) -> map
  }
  // |> draw
}

fn move_rec(
  map: dict.Dict(Pos, String),
  items: List(Pos),
  move: String,
) -> Result(dict.Dict(Pos, String), Nil) {
  let next =
    items
    |> list.map(fn(pos) { next_pos(pos, move) })

  let any_wall =
    next
    |> list.any(fn(pos) {
      let ch = dict.get(map, pos)
      ch == Ok("#")
    })

  let all_dot =
    next
    |> list.all(fn(pos) { dict.get(map, pos) == Ok(".") })

  let result = case all_dot {
    True -> Ok(#(map, items))
    _ ->
      case any_wall {
        True -> Error(Nil)
        _ -> {
          let next_items = case move {
            "<" | ">" -> next
            _ ->
              next
              |> list.fold(dict.new(), fn(m, p) {
                case dict.get(map, p) {
                  Ok("[") ->
                    dict.upsert(m, p, fn(_) { "[" })
                    |> dict.upsert(Pos(p.x + 1, p.y), fn(_) { "]" })
                  Ok("]") ->
                    dict.upsert(m, p, fn(_) { "]" })
                    |> dict.upsert(Pos(p.x - 1, p.y), fn(_) { "[" })
                  Ok("O") -> dict.upsert(m, p, fn(_) { "O" })
                  _ -> m
                }
              })
              |> dict.to_list
              |> list.map(fn(pair) { pair.0 })
          }
          move_rec(map, next_items, move)
          |> result.map(fn(m) { #(m, items) })
        }
      }
  }

  case result {
    Ok(#(nm, i)) ->
      Ok(
        i
        |> list.zip(next)
        |> list.fold(nm, fn(m, pair) {
          dict.upsert(m, pair.1, fn(_) {
            dict.get(m, pair.0) |> result.unwrap(".")
          })
          |> dict.upsert(pair.0, fn(_) { "." })
        }),
      )
    _ -> Error(Nil)
  }
}

fn next_pos(pos: Pos, move: String) -> Pos {
  case move {
    "<" -> Pos(pos.x - 1, pos.y)
    ">" -> Pos(pos.x + 1, pos.y)
    "^" -> Pos(pos.x, pos.y - 1)
    "v" -> Pos(pos.x, pos.y + 1)
    _ -> pos
  }
}
