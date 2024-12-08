const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

// 1644 < ? 1792, 1793, 1839, 1843, 1856, 1863, 1879, 1899, 1900, 1919, 1920 < 1930
var rows: usize = 140;
var cols: usize = 140;

var guard_x: usize = undefined;
var guard_y: usize = undefined;

var data: [140][140]u8 = undefined;

var dir: u3 = 0;

const dirs_x = [_]usize{ 1, 2, 1, 0 };
const dirs_y = [_]usize{ 0, 1, 2, 1 };

var dir_x: usize = 1;
var dir_y: usize = 0;

const dir_items = "|-|-";
const base_char = 128;

const Pos = struct {
    x: usize,
    y: usize,
};

var allocator: std.mem.Allocator = undefined;
var map: std.AutoHashMap(Pos, bool) = undefined;

pub fn main() !void {
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    allocator = arena.allocator();

    map = std.AutoHashMap(Pos, bool).init(allocator);

    try load();
    try step1();
}

fn load() !void {
    var it = std.mem.tokenize(u8, input, "\n");
    var i: usize = 0;
    while (it.next()) |line| {
        cols = @intCast(line.len);
        std.mem.copyForwards(u8, &data[i], line);
        for (0..cols) |j| {
            if (data[i][j] == '^') {
                guard_x = j;
                guard_y = i;
            }
        }
        i += 1;
    }
    rows = i;
}

fn step1() !void {
    var total: usize = 1;
    var obstacle: usize = 0;

    guard_x = guard_x + dir_x - 1;
    guard_y = guard_y + dir_y - 1;

    while (true) {
        if (out_board(guard_x, guard_y, dir_x, dir_y)) {
            total += 1;
            break;
        }

        const item = data[guard_y][guard_x];
        if (item == '.') {
            const new_bits = @as(u8, 1) << dir;
            data[guard_y][guard_x] = base_char + new_bits;
            total += 1;
        } else {
            const orig_bits = if (item == '.' or item == '^') 0 else (item - base_char);
            const new_bits = @as(u8, 1) << dir;
            const new_item = base_char + (orig_bits | new_bits);
            data[guard_y][guard_x] = new_item;
        }
        if (data[guard_y + dir_y - 1][guard_x + dir_x - 1] == '#') {
            while (data[guard_y + dir_y - 1][guard_x + dir_x - 1] == '#') {
                turn_right();
            }
            continue;
        }

        var o_dir = (dir + 1) % 4;
        var o_dir_x = dirs_x[o_dir];
        var o_dir_y = dirs_y[o_dir];

        const xn = guard_x + dir_x - 1;
        const yn = guard_y + dir_y - 1;

        if (!(map.get(Pos{ .x = xn, .y = yn }) orelse false)) {
            if (!out_board(guard_x, guard_y, dir_x, dir_y)) {
                var count: usize = 0;
                var x = guard_x;
                var y = guard_y;

                while (!out_board(x, y, o_dir_x, o_dir_y)) {
                    if (count > 1000000) {
                        break;
                    }
                    count += 1;
                    var o_item = next_item(x, y, o_dir_x, o_dir_y);
                    if (o_item >= base_char and ((o_item - base_char) & (@as(u8, 1) << o_dir) > 0)) {
                        obstacle += 1;
                        try map.put(Pos{ .x = xn, .y = yn }, true);
                        break;
                    } else {
                        while (o_item == '#') {
                            o_dir = (o_dir + 1) % 4;
                            o_dir_x = dirs_x[o_dir];
                            o_dir_y = dirs_y[o_dir];
                            o_item = next_item(x, y, o_dir_x, o_dir_y);
                        }
                    }
                    x = x + o_dir_x - 1;
                    y = y + o_dir_y - 1;
                }
                // }
            }
        }

        guard_x = guard_x + dir_x - 1;
        guard_y = guard_y + dir_y - 1;
    }

    std.debug.print("Sum 1: {}\n", .{total});
    std.debug.print("Sum 2: {}\n", .{obstacle});
}

fn out_board(x: usize, y: usize, dx: usize, dy: usize) bool {
    return (x + dx < 1 or x + dx - 1 >= cols or y + dy < 1 or y + dy - 1 >= rows);
}

fn next_item(x: usize, y: usize, dx: usize, dy: usize) u8 {
    return data[y + dy - 1][x + dx - 1];
}

fn turn_right() void {
    dir = (dir + 1) % 4;
    dir_x = dirs_x[dir];
    dir_y = dirs_y[dir];
}
