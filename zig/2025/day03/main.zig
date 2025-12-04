const std = @import("std");

fn step(data: []const u8, length: u8) !u64 {
    var total: u64 = 0;

    var it = std.mem.tokenizeAny(u8, data, ",\r\n ");
    while (it.next()) |line| {
        var joltage: u64 = 0;
        var max_index: u64 = 0;
        for (0..length) |item| {
            for (max_index + 1..line.len - length + 1 + item) |i| {
                if (line[i] > line[max_index]) {
                    max_index = i;
                }
            }
            joltage = joltage * 10 + (line[max_index] - '0');
            max_index += 1;
        }
        std.debug.print("joltage: {}\n", .{joltage});
        total += joltage;
    }

    std.debug.print("Total 1: {}\n", .{total});

    return total;
}

test "Day 03 example Part 1 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(357, step(data, 2));
}

test "Day 03 example Part 2 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(3121910778619, step(data, 12));
}

test "Day 03 Part 1 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(17443, step(data, 2));
}

test "Day 03 Part 2 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(172167155440541, step(data, 12));
}
