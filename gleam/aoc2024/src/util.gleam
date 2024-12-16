import file_streams/file_stream
import file_streams/text_encoding
import gleam/dict
import gleam/function
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{type Option, None, Some}
import gleam/result
import gleam/string
import gleam/yielder

pub fn load(file_name: String, parser: fn(String) -> Result(a, b)) -> List(a) {
  let assert Ok(stream) =
    file_stream.open_read_text(file_name, text_encoding.Latin1)

  yielder.repeatedly(fn() { file_stream.read_line(stream) })
  |> yielder.take_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(parser)
  |> yielder.filter_map(function.identity)
  |> yielder.to_list
}

pub fn load_second(
  file_name: String,
  parser: fn(String) -> Result(a, b),
) -> List(a) {
  let assert Ok(stream) =
    file_stream.open_read_text(file_name, text_encoding.Latin1)

  yielder.repeatedly(fn() { file_stream.read_line(stream) })
  |> yielder.take_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(fn(line) {
    case line {
      "\n" -> Error("")
      _ -> Ok(line)
    }
  })
  |> yielder.drop_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(parser)
  |> yielder.filter_map(function.identity)
  |> yielder.to_list
}

pub fn load_matrix(
  file_name: String,
  parser: fn(String) -> Result(a, b),
) -> dict.Dict(Pos, a) {
  let assert Ok(stream) =
    file_stream.open_read_text(file_name, text_encoding.Latin1)

  yielder.repeatedly(fn() { file_stream.read_line(stream) })
  |> yielder.take_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(fn(line) {
    case line {
      "\n" -> Error("")
      _ -> Ok(line)
    }
  })
  |> yielder.take_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(fn(str) {
    string.to_graphemes(str)
    |> list.map(parser)
    |> list.filter_map(function.identity)
    |> yielder.from_list
    |> yielder.index
  })
  |> yielder.index
  |> yielder.flat_map(fn(yi) {
    yi.0
    |> yielder.map(fn(vi) { #(Pos(vi.1, yi.1), vi.0) })
  })
  |> yielder.fold(from: dict.new(), with: fn(d, t) {
    dict.upsert(d, t.0, fn(opt) {
      case opt {
        _ -> t.1
      }
    })
  })
}

pub fn draw(map: dict.Dict(Pos, String)) -> dict.Dict(Pos, String) {
  let max_pos =
    map
    |> dict.fold(Pos(0, 0), fn(pos, p, _) {
      Pos(int.max(pos.x, p.x), int.max(pos.y, p.y))
    })

  list.range(from: 0, to: max_pos.y)
  |> list.each(fn(y) {
    list.range(from: 0, to: max_pos.x)
    |> list.each(fn(x) {
      io.print(map |> dict.get(Pos(x, y)) |> result.unwrap(" "))
    })
    io.println("")
  })

  map
}

pub const way4_list = [
  PosDir(1, 0, 0), PosDir(0, 1, 1), PosDir(-1, 0, 2), PosDir(0, -1, 3),
]

pub fn increment(x: Option(Int)) {
  case x {
    Some(i) -> i + 1
    None -> 1
  }
}

pub fn increment_with(x: Option(Int), d: Int) {
  case x {
    Some(i) -> i + d
    None -> d
  }
}

pub fn mod(n: Int, m: Int) {
  case n {
    _ if n < 0 -> { m + { n % m } } % m
    _ -> n % m
  }
}

pub type PosDir {
  PosDir(x: Int, y: Int, dir: Int)
}

pub type Pos {
  Pos(x: Int, y: Int)
}

pub fn find_pos(map: dict.Dict(Pos, String), ch: String) -> Pos {
  map
  |> dict.filter(fn(_, str) { str == ch })
  |> dict.keys
  |> list.first
  |> result.unwrap(Pos(0, 0))
}
