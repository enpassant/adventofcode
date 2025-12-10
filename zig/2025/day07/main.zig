const std = @import("std");

const Position = struct {
    x: u64,
    y: u64,
};

fn step1(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var beams = std.AutoHashMap(u64, void).init(allocator);
    defer beams.deinit();

    var x: usize = 0;
    var y: usize = 0;
    var start: Position = undefined;
    var it = std.mem.tokenizeAny(u8, data, " \r\n");
    while (it.next()) |line| {
        for (line) |ch| {
            if (ch == 'S') {
                start = Position{ .x = x, .y = y };
                try beams.put(x, {});
            } else if (ch == '^') {
                if (beams.contains(x)) {
                    _ = beams.remove(x);
                    if (x > 0) {
                        try beams.put(x - 1, {});
                    }
                    if (x < line.len - 1) {
                        try beams.put(x + 1, {});
                    }
                    total += 1;
                }
            }
            x += 1;
        }
        x = 0;
        y += 1;
    }

    return total;
}

fn step2(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var beams = std.AutoHashMap(u64, u64).init(allocator);
    defer beams.deinit();

    var x: usize = 0;
    var y: usize = 0;
    var start: Position = undefined;
    var it = std.mem.tokenizeAny(u8, data, " \r\n");
    while (it.next()) |line| {
        for (line) |ch| {
            if (ch == 'S') {
                start = Position{ .x = x, .y = y };
                try beams.put(x, 1);
            } else if (ch == '^') {
                if (beams.contains(x)) {
                    const removed = beams.fetchRemove(x);
                    if (x > 0) {
                        const result = try beams.getOrPut(x - 1);
                        if (result.found_existing) {
                            result.value_ptr.* += removed.?.value;
                        } else {
                            result.value_ptr.* = removed.?.value;
                        }
                    }
                    if (x < line.len - 1) {
                        const result = try beams.getOrPut(x + 1);
                        if (result.found_existing) {
                            result.value_ptr.* += removed.?.value;
                        } else {
                            result.value_ptr.* = removed.?.value;
                        }
                    }
                }
            }
            x += 1;
        }
        x = 0;
        y += 1;
    }

    var vit = beams.valueIterator();
    while (vit.next()) |count| {
        total += count.*;
    }

    return total;
}

test "Day 07 test Part 1 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(21, step1(data, std.testing.allocator));
}

test "Day 07 test Part 2 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(40, step2(data, std.testing.allocator));
}

test "Day 07 input Part 1 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(1646, step1(data, std.testing.allocator));
}

test "Day 07 input Part 2 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(32451134474991, step2(data, std.testing.allocator));
}
