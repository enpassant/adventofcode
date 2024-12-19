import gleam/dict.{type Dict}
import gleam/io
import gleam/list
import gleam/string

import util

pub fn main() {
  io.println("Hello from Day 19!")

  // let file_name = "data/day19/test.txt"
  let file_name = "data/day19/input.txt"
  let towels =
    util.load(file_name, read)
    |> list.flatten
  // |> io.debug

  let designs =
    util.load_second(file_name, fn(str) { Ok(string.replace(str, "\n", "")) })
  // |> io.debug

  let step1 =
    designs
    |> list.fold(#(0, dict.new()), fn(pr, design) {
      case find_tree(design, towels, pr.1) {
        Ok(#(_, suffix)) -> {
          #(pr.0 + 1, suffix)
        }
        _ -> pr
      }
    })

  let step2 =
    designs
    |> list.fold(#(0, dict.new()), fn(pr, design) {
      case find_tree(design, towels, pr.1) {
        Ok(#(count, suffix)) -> {
          #(pr.0 + count, suffix)
        }
        _ -> pr
      }
    })

  #("Step 1: ", step1.0, "Step 2: ", step2.0)
  |> io.debug

  Nil
}

type Suffix =
  Dict(String, Int)

fn find_tree(
  design: String,
  towels: List(String),
  suffix: Suffix,
) -> Result(#(Int, Suffix), Nil) {
  case suffix |> dict.get(design) {
    Ok(count) -> Ok(#(count, suffix))
    _ -> {
      case design {
        "" -> Ok(#(1, suffix))
        _ -> {
          let children =
            towels
            |> list.filter(fn(towel) { design |> string.starts_with(towel) })
            |> list.fold(#(0, suffix), fn(pr, towel) {
              case
                find_tree(
                  design |> string.drop_start(string.length(towel)),
                  towels,
                  pr.1,
                )
              {
                Ok(#(count, new_suffix)) -> #(pr.0 + count, new_suffix)
                Error(_) -> pr
              }
            })

          let sum = children.0

          case sum == 0 {
            True -> Error(Nil)
            _ -> {
              Ok(#(sum, children.1 |> dict.insert(design, sum)))
            }
          }
        }
      }
    }
  }
}

fn read(line: String) -> Result(List(String), Nil) {
  case line {
    "\n" -> Error(Nil)
    _ ->
      Ok(
        line
        |> string.replace("\n", "")
        |> string.split(", "),
      )
  }
  // |> io.debug
}
