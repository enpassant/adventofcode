import file_streams/file_stream
import file_streams/text_encoding
import gleam/function
import gleam/option.{type Option, None, Some}
import gleam/result
import gleam/yielder

pub fn load(file_name: String, parser: fn(String) -> Result(a, b)) -> List(a) {
  let assert Ok(stream) =
    // file_stream.open_read_text("src/test.txt", text_encoding.Latin1)
    file_stream.open_read_text(file_name, text_encoding.Latin1)

  yielder.repeatedly(fn() { file_stream.read_line(stream) })
  |> yielder.take_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(parser)
  |> yielder.filter_map(function.identity)
  |> yielder.to_list
}

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

pub type Pos {
  Pos(x: Int, y: Int)
}
