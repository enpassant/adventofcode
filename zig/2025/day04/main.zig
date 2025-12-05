const std = @import("std");

var rows: u8 = 140;
var cols: u8 = 140;

fn load(input: []const u8, data: *[140][140]u8) !void {
    var it = std.mem.tokenizeAny(u8, input, "\n");
    var i: u8 = 0;
    while (it.next()) |line| {
        cols = @intCast(line.len);
        std.mem.copyForwards(u8, data[i][0..line.len], line);
        i += 1;
    }
    rows = i;
}

fn step1(data: [140][140]u8) !u64 {
    var total: usize = 0;

    for (0..rows) |row| {
        for (0..cols) |col| {
            if (data[row][col] == '@') {
                var count: usize = 0;
                for (0..3) |y| {
                    for (0..3) |x| {
                        if (x == 1 and y == 1) continue;

                        if (row + y == 0 or row + y > rows) continue;
                        if (col + x == 0 or col + x > cols) continue;
                        const ch = data[row + y - 1][col + x - 1];
                        if (ch == '@') {
                            count += 1;
                        }
                    }
                }
                if (count < 4) {
                    total += 1;
                }
            }
        }
    }

    return total;
}

fn step2(data: *[140][140]u8) !u64 {
    var total: usize = 0;

    while (true) {
        var sum: usize = 0;
        for (0..rows) |row| {
            for (0..cols) |col| {
                if (data[row][col] == '@') {
                    var count: usize = 0;
                    for (0..3) |y| {
                        for (0..3) |x| {
                            if (x == 1 and y == 1) continue;

                            if (row + y == 0 or row + y > rows) continue;
                            if (col + x == 0 or col + x > cols) continue;
                            const ch = data[row + y - 1][col + x - 1];
                            if (ch == '@') {
                                count += 1;
                            }
                        }
                    }
                    if (count < 4) {
                        sum += 1;
                        data[row][col] = 'x';
                    }
                }
            }
        }
        total += sum;
        if (sum == 0) break;
    }

    return total;
}

test "Day 04 test Part 1 result is good" {
    const input = @embedFile("test.txt");
    var data: [140][140]u8 = undefined;
    try load(input, &data);

    try std.testing.expectEqual(13, step1(data));
}

test "Day 04 test Part 2 result is good" {
    const input = @embedFile("test.txt");
    var data: [140][140]u8 = undefined;
    try load(input, &data);

    try std.testing.expectEqual(43, step2(&data));
}

test "Day 04 input Part 1 result is good" {
    const input = @embedFile("input.txt");
    var data: [140][140]u8 = undefined;
    try load(input, &data);

    try std.testing.expectEqual(1435, step1(data));
}

test "Day 04 input Part 2 result is good" {
    const input = @embedFile("input.txt");
    var data: [140][140]u8 = undefined;
    try load(input, &data);

    try std.testing.expectEqual(8623, step2(&data));
}
