import file_streams/file_stream
import file_streams/text_encoding
import gleam/dict
import gleam/function
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{type Option, None, Some}
import gleam/queue
import gleam/result
import gleam/string
import gleam/yielder
import gleamy/priority_queue as pq

pub fn load(file_name: String, parser: fn(String) -> Result(a, b)) -> List(a) {
  let assert Ok(stream) =
    file_stream.open_read_text(file_name, text_encoding.Latin1)

  yielder.repeatedly(fn() { file_stream.read_line(stream) })
  |> yielder.take_while(result.is_ok)
  |> yielder.filter_map(function.identity)
  |> yielder.map(parser)
  |> yielder.take_while(result.is_ok)
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

pub fn draw_with_char(str: String) {
  fn(map: dict.Dict(Pos, a)) -> dict.Dict(Pos, a) {
    draw_with(map, fn(_) { str })
  }
}

pub fn draw(map: dict.Dict(Pos, String)) -> dict.Dict(Pos, String) {
  draw_with(map, function.identity)
}

pub fn draw_with(
  map: dict.Dict(Pos, a),
  convert: fn(a) -> String,
) -> dict.Dict(Pos, a) {
  let max_pos =
    map
    |> dict.fold(Pos(0, 0), fn(pos, p, _) {
      Pos(int.max(pos.x, p.x), int.max(pos.y, p.y))
    })

  list.range(from: 0, to: max_pos.y)
  |> list.each(fn(y) {
    list.range(from: 0, to: max_pos.x)
    |> list.each(fn(x) {
      case dict.get(map, Pos(x, y)) {
        Ok(a) -> io.print(convert(a))
        _ -> io.print(" ")
      }
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

pub fn binary_search(
  min: Int,
  max: Int,
  search: fn(Int) -> Result(a, Bool),
) -> Result(a, #(Int, Bool)) {
  binary_search_rec(min, max, { max - min } / 2 + min, search)
}

fn binary_search_rec(
  min: Int,
  max: Int,
  n: Int,
  search: fn(Int) -> Result(a, Bool),
) -> Result(a, #(Int, Bool)) {
  case search(n) {
    Ok(a) -> Ok(a)
    Error(less) -> {
      case less && n > min, !less && n < max {
        True, _ ->
          binary_search_rec(
            min,
            n - 1,
            int.min(n - 1, { n - min } / 2 + min),
            search,
          )
        _, True ->
          binary_search_rec(
            n + 1,
            max,
            int.max(n + 1, { max - n } / 2 + n),
            search,
          )
        _, _ -> Error(#(n, less))
      }
    }
  }
}

pub fn find_dijkstra(
  map: dict.Dict(a, v),
  start_pos: a,
  next_positions: fn(dict.Dict(a, #(Int, v)), a) -> List(#(a, #(Int, v))),
  calc_price: fn(#(a, v), #(a, v)) -> #(Int, v),
) -> dict.Dict(a, #(Int, v)) {
  let queue =
    map
    |> dict.to_list
    |> list.map(fn(k) {
      case k.0 == start_pos {
        True -> #(k.0, #(0, k.1))
        _ -> #(k.0, #(1_000_000_000, k.1))
      }
    })
    |> pq.from_list(fn(a, b) { int.compare(a.1.0, b.1.0) })
  find_dijkstra_rec(queue, dict.new(), next_positions, calc_price)
}

fn find_dijkstra_rec(
  queue: pq.Queue(#(a, #(Int, v))),
  result: dict.Dict(a, #(Int, v)),
  next_positions: fn(dict.Dict(a, #(Int, v)), a) -> List(#(a, #(Int, v))),
  calc_price: fn(#(a, v), #(a, v)) -> #(Int, v),
) -> dict.Dict(a, #(Int, v)) {
  queue |> pq.count |> io.debug
  case pq.pop(queue) {
    Ok(first) -> {
      let new_result = dict.upsert(result, first.0.0, fn(_) { first.0.1 })
      let dq =
        first.1
        |> pq.to_list
        |> dict.from_list
      let positions = next_positions(dq, first.0.0)
      let new_queue =
        positions
        |> list.fold(dq, fn(d, pj) {
          dict.upsert(d, pj.0, fn(opt) {
            let kpj_kv = calc_price(#(first.0.0, first.0.1.1), #(pj.0, pj.1.1))
            let kpj = #(first.0.1.0 + kpj_kv.0, kpj_kv.1)
            case opt {
              Some(price) if price.0 < kpj.0 -> price
              _ -> kpj
            }
          })
        })
        |> dict.to_list
        |> pq.from_list(fn(a, b) { int.compare(a.1.0, b.1.0) })

      find_dijkstra_rec(new_queue, new_result, next_positions, calc_price)
    }
    _ -> result
  }
}
