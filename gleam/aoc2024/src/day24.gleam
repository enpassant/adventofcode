import gleam/dict
import gleam/erlang
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string

import util.{type Pos, Pos}

pub fn main() {
  io.println("Hello from Day 24!")

  let file_name = "data/day18/test.txt"
  // let file_name = "data/day18/input.txt"
  let map = util.load_matrix(file_name, fn(str) { Ok(str) })
  // |> draw

  let moves =
    util.load_second(file_name, fn(str) { Ok(string.replace(str, "\n", "")) })
    |> string.concat
}
