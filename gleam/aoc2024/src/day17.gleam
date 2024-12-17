import genetic
import gleam/dict.{type Dict}
import gleam/erlang
import gleam/function
import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string

// const program = [0, 1, 5, 4, 3, 0]

// const program = [0, 3, 5, 4, 3, 0]

const program = [2, 4, 1, 1, 7, 5, 1, 5, 0, 3, 4, 3, 5, 5, 3, 0]

pub fn main() {
  io.println("Hello from Day 17!")

  // let context = Context(729, 0, 0, 0, [])
  let context = Context(56_256_477, 0, 0, 0, [])
  // let context = Context(2024, 0, 0, 0, [])

  run(context)
  // result: 4,6,3,5,6,3,5,2,1,0
}

type Context {
  Context(a: Int, b: Int, c: Int, ip: Int, out: List(Int))
}

type Command {
  Command(instruction: Int, op: Int)
}

fn run(context: Context) {
  io.println("\nStart\n")

  genetic.run_genetic_algorithm(
    100_000_000_000_000,
    164_545_346_498_237,
    10_000,
    fn(popu) {
      case dict.get(popu, 0) {
        Ok(g) -> {
          g |> io.debug
          g.fitness >= 16
        }
        _ -> False
      }
    },
    fn(gene) {
      case run_all(program, Context(..context, a: gene)) {
        ctx -> {
          #(
            ctx.out
              |> list.zip(program)
              |> list.take_while(fn(p) { p.0 == p.1 })
              |> list.length,
            ctx,
          )
        }
      }
    },
  )

  // let a = 164_545_346_498_237

  // list.range(a, a)
  // |> list.take_while(fn(i) {
  //   case i % 100_000 {
  //     0 -> i |> io.debug
  //     _ -> i
  //   }
  //   // io.debug(i)
  //   case run_all(program, Context(..context, a: i)) {
  //     Context(_, _, _, _, out) if out == program -> {
  //       io.debug(i)
  //       False
  //     }
  //     _ -> True
  //   }
  // })

  // ctx.out
  // |> list.map(fn(i) { int.to_string(i) })
  // |> list.intersperse(",")
  // |> string.concat
  // |> io.debug

  Nil
}

fn run_all(program: List(Int), context: Context) -> Context {
  let prg =
    program
    |> list.sized_chunk(2)
    |> list.map(fn(e) {
      case e {
        [i, o] -> Ok(Command(i, o))
        _ -> Error(Nil)
      }
    })
    |> list.filter(result.is_ok)
    |> list.filter_map(function.identity)
    |> list.index_map(fn(x, i) { #(i, x) })
    |> dict.from_list

  process(context, prg)
}

fn process(context: Context, prg: Dict(Int, Command)) -> Context {
  // io.debug(context)
  case prg |> dict.get(context.ip) {
    Ok(command) ->
      case do(context, command) {
        Ok(ctx) -> ctx |> process(prg)
        _ -> context
      }
    _ -> context
  }
}

fn do(context: Context, command: Command) -> Result(Context, Nil) {
  case command.instruction {
    0 -> Ok(adv(context, command.op))
    1 -> Ok(bxl(context, command.op))
    2 -> Ok(bst(context, command.op))
    3 -> Ok(jnz(context, command.op))
    4 -> Ok(bxc(context, command.op))
    5 -> out(context, command.op)
    6 -> Ok(bdv(context, command.op))
    7 -> Ok(cdv(context, command.op))
    _ -> Ok(context)
  }
}

fn adv(context: Context, op: Int) {
  Context(
    ..context,
    ip: context.ip + 1,
    a: context.a / { int.bitwise_shift_left(1, combo(context, op)) },
  )
}

fn bxl(context: Context, op: Int) {
  Context(
    ..context,
    ip: context.ip + 1,
    b: context.b |> int.bitwise_exclusive_or(op),
  )
}

fn bst(context: Context, op: Int) {
  Context(..context, ip: context.ip + 1, b: combo(context, op) % 8)
}

fn jnz(context: Context, op: Int) {
  case context.a {
    0 -> Context(..context, ip: context.ip + 1)
    _ -> Context(..context, ip: op)
  }
}

fn bxc(context: Context, _: Int) {
  Context(
    ..context,
    ip: context.ip + 1,
    b: context.b |> int.bitwise_exclusive_or(context.c),
  )
}

fn out(context: Context, op: Int) {
  let out =
    context.out
    |> list.append([combo(context, op) % 8])

  case program |> list.zip(out) |> list.all(fn(pair) { pair.0 == pair.1 }) {
    True -> {
      case out |> list.length > 4 {
        True -> {
          // #("Good: ", out, context) |> io.debug
          // erlang.get_line("Enter to continue: ")
          Nil
        }
        _ -> Nil
      }
      Ok(Context(..context, ip: context.ip + 1, out: out))
    }
    _ -> Error(Nil)
  }
}

fn bdv(context: Context, op: Int) {
  Context(
    ..context,
    ip: context.ip + 1,
    b: context.a / { int.bitwise_shift_left(1, combo(context, op)) },
  )
}

fn cdv(context: Context, op: Int) {
  Context(
    ..context,
    ip: context.ip + 1,
    c: context.a / { int.bitwise_shift_left(1, combo(context, op)) },
  )
}

fn combo(context: Context, op: Int) -> Int {
  case op {
    0 | 1 | 2 | 3 -> op
    4 -> context.a
    5 -> context.b
    6 -> context.c
    _ -> 7
  }
}
