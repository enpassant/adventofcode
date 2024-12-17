import gleam/dict.{type Dict}
import gleam/float
import gleam/int
import gleam/list
import gleam/result

pub type Gene(a) {
  Gene(gene: Int, fitness: Int, data: a)
}

pub fn run_genetic_algorithm(
  min: Int,
  max: Int,
  size: Int,
  exit_fn: fn(Dict(Int, Gene(a))) -> Bool,
  fitness_fn: fn(Int) -> #(Int, a),
) -> Int {
  let length = max - min
  let genes =
    list.range(0, size)
    |> list.map(fn(_) { generate_random(min, max, fitness_fn) })
  let popu = genes |> sort

  step(popu, min, max, size, exit_fn, fitness_fn)

  1
}

fn generate_random(min: Int, max: Int, fitness_fn: fn(Int) -> #(Int, a)) {
  let length = max - min
  let gene = int.random(length) + min
  let f = fitness_fn(gene)
  Gene(gene, f.0, f.1)
}

fn step(
  popu: Dict(Int, Gene(a)),
  min: Int,
  max: Int,
  size: Int,
  exit_fn: fn(Dict(Int, Gene(a))) -> Bool,
  fitness_fn: fn(Int) -> #(Int, a),
) -> Dict(Int, Gene(a)) {
  case exit_fn(popu) {
    True -> popu
    _ -> {
      list.range(0, size * 5 / 10)
      |> list.fold(popu |> dict.values, fn(ls, _) {
        case int.random(10_000) < 5 {
          True -> list.prepend(ls, generate_random(min, max, fitness_fn))
          False -> {
            let i1 = biased_random(0, size)
            let i2 = biased_random(0, size)
            case dict.get(popu, i1), dict.get(popu, i2) {
              Ok(g1), Ok(g2) -> {
                let pair = cross(g1, g2, fitness_fn)
                ls |> list.prepend(pair.0) |> list.prepend(pair.1)
              }
              _, _ -> ls
            }
          }
        }
      })
      |> list.take(size)
      |> sort
      |> step(min, max, size, exit_fn, fitness_fn)
    }
  }
}

fn sort(popu: List(Gene(a))) {
  popu
  |> list.sort(fn(g1, g2) { int.compare(g2.fitness, g1.fitness) })
  |> list.index_map(fn(g, i) { #(i, g) })
  |> dict.from_list
}

fn cross(
  g1: Gene(a),
  g2: Gene(a),
  fitness_fn: fn(Int) -> #(Int, a),
) -> #(Gene(a), Gene(a)) {
  case int.digits(g1.gene, 2), int.digits(g2.gene, 2) {
    Ok(d1), Ok(d2) -> {
      let pos = int.random(list.length(d1))
      let new1 = d1 |> list.take(pos) |> list.append(list.drop(d2, pos))
      let new2 = d2 |> list.take(pos) |> list.append(list.drop(d1, pos))
      case int.undigits(new1, 2), int.undigits(new2, 2) {
        Ok(ng1), Ok(ng2) -> {
          let f1 = fitness_fn(ng1)
          let f2 = fitness_fn(ng2)
          #(Gene(ng1, f1.0, f1.1), Gene(ng2, f2.0, f2.1))
        }
        _, _ -> #(g1, g2)
      }
    }
    _, _ -> #(g1, g2)
  }
}

pub fn biased_random(min: Int, max: Int) -> Int {
  let r = float.random()
  let biased = float.power(r, 2.0) |> result.unwrap(1.0)
  let scaled = biased *. int.to_float(max - min)
  min + float.truncate(scaled)
}
