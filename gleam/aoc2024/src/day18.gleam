import gleam/dict
import gleam/function
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string

import util.{type Pos, Pos}

pub fn main() {
  io.println("Hello from Day 18!")

  // let file_name = "data/day18/test.txt"
  // let count = 12
  let file_name = "data/day18/input.txt"
  // let count = 1024

  let data_list = util.load(file_name, read)

  let max_pos =
    data_list
    |> list.fold(Pos(0, 0), fn(max, pair) {
      let pos = pair.0
      case max.x < pos.x, max.y < pos.y {
        True, True -> pos
        True, _ -> Pos(pos.x, max.y)
        _, True -> Pos(max.x, pos.y)
        _, _ -> max
      }
    })
  // |> io.debug

  let max_count = list.length(data_list)
  // max_count |> io.debug

  let index_pair =
    util.binary_search(0, max_count - 1, fn(count) {
      count |> io.debug
      case find_result(max_pos, data_list, count) {
        Ok(x) if x.0 >= 1_000_000_000 -> Error(True)
        _ -> Error(False)
      }
    })
    |> result.unwrap_error(#(0, True))
    |> io.debug

  let index = case index_pair {
    #(idx, True) -> idx - 1
    #(idx, _) -> idx
  }

  data_list
  |> list.index_map(fn(a, i) { #(i, a) })
  |> list.find(fn(e) { e.0 == index })
  |> io.debug

  Nil
}

fn find_result(max_pos: Pos, data_list: List(#(Pos, String)), count: Int) {
  let data =
    data_list
    |> list.take(count)
    |> dict.from_list
  // |> util.draw

  let nodes =
    list.range(0, max_pos.y)
    |> list.fold(dict.new(), fn(d, y) {
      list.range(0, max_pos.x)
      |> list.fold(d, fn(d, x) {
        let pos = Pos(x, y)
        case dict.get(data, pos) {
          Ok(_) -> d
          _ -> dict.upsert(d, pos, fn(_) { 1_000_000_000 })
        }
      })
    })
  // |> util.draw_with_char(".")

  util.find_dijkstra(
    nodes,
    Pos(0, 0),
    fn(dq, p) { neighbors(max_pos, dq, p) },
    fn(p1, p2) {
      #(
        int.absolute_value({ p1.0 }.x - { p2.0 }.x)
          + int.absolute_value({ p1.0 }.y - { p2.0 }.y),
        0,
      )
    },
  )
  // |> dict.to_list
  // |> priority_queue.from_list(fn(a, b) { int.compare(a.1, b.1) })
  // |> priority_queue.to_list
  |> dict.get(max_pos)
  |> io.debug
}

fn neighbors(max_pos: Pos, dq: dict.Dict(Pos, a), pos: Pos) {
  let ns = [Pos(1, 0), Pos(0, 1), Pos(-1, 0), Pos(0, -1)]
  ns
  |> list.map(fn(p) { Pos(pos.x + p.x, pos.y + p.y) })
  |> list.filter(fn(p) {
    p.x >= 0 && p.y >= 0 && p.x <= max_pos.x && p.y <= max_pos.y
  })
  |> list.map(fn(p) { dict.get(dq, p) |> result.map(fn(i) { #(p, i) }) })
  |> list.filter(result.is_ok)
  |> list.filter_map(function.identity)
}

fn read(line: String) -> Result(#(Pos, String), Nil) {
  let nums =
    line
    |> string.to_graphemes
    |> list.chunk(by: fn(ch) { string.contains("0123456789-", ch) })
    |> list.map(string.concat)
    |> list.filter_map(int.parse)

  case nums {
    [x, y] -> Ok(#(Pos(x, y), "#"))
    _ -> Error(Nil)
  }
}
