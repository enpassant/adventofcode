const std = @import("std");
const data = @embedFile("input.txt");

pub fn main() !void {
    try part1();
    try part2();
}

fn part1() !void {
    var it = std.mem.tokenize(u8, data, "\n");
    var total: usize = 0;
    while (it.next()) |line| {
        var line_it = std.mem.tokenize(u8, line, ":;, ");
        _ = line_it.next();
        const idx = try std.fmt.parseInt(usize, line_it.next().?, 10);
        var is_possible = true;
        while (line_it.next()) |num_str| {
            const num = try std.fmt.parseInt(usize, num_str, 10);
            const color = line_it.next().?;
            if (std.mem.eql(u8, color, "red")) {
                if (num > 12) {
                    is_possible = false;
                    break;
                }
            } else if (std.mem.eql(u8, color, "green")) {
                if (num > 13) {
                    is_possible = false;
                    break;
                }
            } else if (std.mem.eql(u8, color, "blue")) {
                if (num > 14) {
                    is_possible = false;
                    break;
                }
            }
        }
        if (is_possible) {
            total += idx;
        }
    }
    std.debug.print("Sum 1: {}\n", .{total});
}

fn part2() !void {
    var it = std.mem.tokenize(u8, data, "\n");
    var total: usize = 0;
    while (it.next()) |line| {
        var line_it = std.mem.tokenize(u8, line, ":;, ");
        _ = line_it.next();
        _ = line_it.next();
        var red: usize = 1;
        var green: usize = 1;
        var blue: usize = 1;
        while (line_it.next()) |num_str| {
            const num = try std.fmt.parseInt(usize, num_str, 10);
            const color = line_it.next().?;
            if (std.mem.eql(u8, color, "red")) {
                if (num > red) {
                    red = num;
                }
            } else if (std.mem.eql(u8, color, "green")) {
                if (num > green) {
                    green = num;
                }
            } else if (std.mem.eql(u8, color, "blue")) {
                if (num > blue) {
                    blue = num;
                }
            }
        }
        total += red * green * blue;
    }
    std.debug.print("Sum 2: {}\n", .{total});
}
