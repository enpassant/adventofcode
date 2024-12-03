const std = @import("std");
// const data = @embedFile("test.txt");
const data = @embedFile("input.txt");

pub fn main() !void {
    try step1();
    try step2();
}

fn step1() !void {
    var total: usize = 0;
    var start_pos: usize = 0;
    while (true) {
        const pos = std.mem.indexOfPos(u8, data, start_pos, "mul(");
        if (pos == null) break;

        const pos_comma = std.mem.indexOfPos(u8, data, pos.?, ",");
        if (pos_comma == null) break;

        const pos_end = std.mem.indexOfPos(u8, data, pos_comma.?, ")");
        if (pos_end == null) break;

        if (pos_comma.? - pos.? <= 4 or pos_comma.? - pos.? > 7) {
            start_pos = pos.? + 4;
            continue;
        }
        if (pos_end.? - pos_comma.? <= 1 or pos_end.? - pos_comma.? > 4) {
            start_pos = pos.? + 4;
            continue;
        }

        const start = pos.? + 4;
        const end = pos_comma.?;
        const num1 = std.fmt.parseInt(usize, data[start..end], 10) catch 0;
        const num2 = std.fmt.parseInt(usize, data[end + 1 .. pos_end.?], 10) catch 0;
        // std.debug.print("Span: {any} {any}\n", .{ num1, num2 });
        total += num1 * num2;

        start_pos = pos_end.?;
    }

    std.debug.print("Sum 1: {}\n", .{total});
}

fn step2() !void {
    var total: usize = 0;
    var start_pos: usize = 0;
    while (true) {
        const pos_dont = std.mem.indexOfPos(u8, data, start_pos, "don't()");

        const pos = std.mem.indexOfPos(u8, data, start_pos, "mul(");
        if (pos == null) break;

        if (pos_dont) |pn| {
            if (pn < pos.?) {
                const pos_do = std.mem.indexOfPos(u8, data, start_pos, "do()");
                if (pos_do == null) break;

                start_pos = pos_do.?;
                continue;
            }
        }

        const pos_comma = std.mem.indexOfPos(u8, data, pos.?, ",");
        if (pos_comma == null) break;

        const pos_end = std.mem.indexOfPos(u8, data, pos_comma.?, ")");
        if (pos_end == null) break;

        if (pos_comma.? - pos.? <= 4 or pos_comma.? - pos.? > 7) {
            start_pos = pos.? + 4;
            continue;
        }
        if (pos_end.? - pos_comma.? <= 1 or pos_end.? - pos_comma.? > 4) {
            start_pos = pos.? + 4;
            continue;
        }

        const start = pos.? + 4;
        const end = pos_comma.?;
        const num1 = std.fmt.parseInt(usize, data[start..end], 10) catch 0;
        const num2 = std.fmt.parseInt(usize, data[end + 1 .. pos_end.?], 10) catch 0;
        // std.debug.print("Span: {any} {any}\n", .{ num1, num2 });
        total += num1 * num2;

        start_pos = pos_end.?;
    }

    std.debug.print("Sum 2: {}\n", .{total});
}
