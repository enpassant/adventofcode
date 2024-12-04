const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

pub fn main() !void {
    try load();
    try step1();
    try step2();
}

var rows: u8 = 140;
var cols: u8 = 140;
const word = "XMAS";

const Position = struct {
    x: u8,
    y: u8,
    ch: u8,
    count: [word.len]u8,

    pub fn init(x: u8, y: u8) Position {
        var count = [_]u8{0} ** word.len;
        count[0] = 1;
        return Position{
            .x = x,
            .y = y,
            .ch = 0,
            .count = count,
        };
    }

    pub fn update(self: Position, x: u8, y: u8, ch: u8) Position {
        return Position{
            .x = x,
            .y = y,
            .ch = ch,
            .count = self.count,
        };
    }
};

var data: [140][140]u8 = undefined;

fn update_pos(pos: Position, x: u8, y: u8, ch: u8) anyerror!Position {
    const ch_p = pos.ch + 1;
    if (ch_p < word.len and ch == word[ch_p]) {
        var count = pos.count;
        count[ch_p] += 1;
        return Position{
            .x = x,
            .y = y,
            .ch = ch_p,
            .count = count,
        };
    }
    return error.false;
}

fn load() !void {
    var it = std.mem.tokenize(u8, input, "\n");
    var i: u8 = 0;
    while (it.next()) |line| {
        cols = @intCast(line.len);
        std.mem.copyForwards(u8, &data[i], line);
        i += 1;
    }
    rows = i;
}

fn step1() !void {
    var total: usize = 0;

    for (0..rows) |row| {
        for (0..cols) |col| {
            if (data[row][col] == 'X') {
                for (0..3) |y| {
                    for (0..3) |x| {
                        if (x == 1 and y == 1) continue;

                        var pos = Position.init(@intCast(col), @intCast(row));
                        while (true) {
                            if (pos.y + y == 0 or pos.y + y > rows) break;
                            if (pos.x + x == 0 or pos.x + x > cols) break;
                            const ch = data[pos.y + y - 1][pos.x + x - 1];
                            pos = update_pos(
                                pos,
                                @intCast(pos.x + x - 1),
                                @intCast(pos.y + y - 1),
                                ch,
                            ) catch {
                                break;
                            };
                        }
                        var count: usize = 1;
                        for (1..word.len) |i| {
                            count *= pos.count[i];
                        }
                        total += count;
                    }
                }
            }
        }
    }

    std.debug.print("Sum 1: {}\n", .{total});
}

fn step2() !void {
    var total: usize = 0;

    for (1..rows - 1) |row| {
        for (1..cols - 1) |col| {
            if (data[row][col] == 'A') {
                const ch1 = data[row - 1][col - 1];
                const ch2 = data[row + 1][col + 1];
                const ch3 = data[row - 1][col + 1];
                const ch4 = data[row + 1][col - 1];
                if (((ch1 == 'M' and ch2 == 'S') or
                    (ch1 == 'S' and ch2 == 'M')) and
                    ((ch3 == 'M' and ch4 == 'S') or
                    (ch3 == 'S' and ch4 == 'M')))
                {
                    total += 1;
                }
            }
        }
    }

    std.debug.print("Sum 2: {}\n", .{total});
}
