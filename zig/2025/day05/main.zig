const std = @import("std");

const Range = struct {
    start: u64,
    end: u64,

    pub fn width(self: Range) u64 {
        return self.end - self.start + 1;
    }
};

fn add_range(
    ranges: *std.ArrayList(Range),
    range: Range,
    allocator: std.mem.Allocator,
) !void {
    var missing = true;
    for (ranges.items) |item| {
        if (range.start < item.start and range.end >= item.start) {
            try add_range(ranges, Range{ .start = range.start, .end = item.start - 1 }, allocator);
            if (range.end > item.end) {
                try add_range(ranges, Range{ .start = item.end + 1, .end = range.end }, allocator);
            }
            missing = false;
            break;
        }
        if (range.start <= item.end and range.end > item.end) {
            try add_range(ranges, Range{ .start = item.end + 1, .end = range.end }, allocator);
            missing = false;
            break;
        }
        if (range.start >= item.start and range.end <= item.end) {
            missing = false;
            break;
        }
    }

    if (missing) {
        (try ranges.addOne(allocator)).* = range;
    }
}

fn step1(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var list: std.ArrayList(Range) = std.ArrayList(Range).empty;
    defer list.deinit(allocator);

    var it = std.mem.tokenizeAny(u8, data, ",\r\n ");
    while (it.next()) |range_str| {
        var it_range = std.mem.tokenizeAny(u8, range_str, "-");
        const value1 = it_range.next();
        const value2_opt = it_range.next();
        const num1 = try std.fmt.parseInt(u64, value1.?, 10);
        if (value2_opt) |value2| {
            const num2 = try std.fmt.parseInt(u64, value2, 10);
            try add_range(&list, Range{ num1, num2 }, allocator);
        } else {
            for (list.items) |range| {
                if (num1 >= range.start and num1 <= range.end) {
                    total += 1;
                    break;
                }
            }
        }
    }

    return total;
}

fn step2(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var list: std.ArrayList(Range) = std.ArrayList(Range).empty;
    defer list.deinit(allocator);

    var it = std.mem.tokenizeAny(u8, data, ",\r\n ");
    while (it.next()) |range_str| {
        var it_range = std.mem.tokenizeAny(u8, range_str, "-");
        const value1 = it_range.next();
        const value2_opt = it_range.next();
        const num1 = try std.fmt.parseInt(u64, value1.?, 10);
        if (value2_opt) |value2| {
            const num2 = try std.fmt.parseInt(u64, value2, 10);
            try add_range(&list, Range{ .start = num1, .end = num2 }, allocator);
        } else {
            break;
        }
    }
    for (list.items) |range| {
        total += range.width();
    }

    return total;
}

test "Day 02 test Part 1 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(3, step1(data, std.testing.allocator));
}

test "Day 02 test Part 2 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(14, step2(data, std.testing.allocator));
}

test "Day 02 input Part 1 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(681, step1(data, std.testing.allocator));
}

test "Day 02 input Part 2 result is good" {
    const data = @embedFile("input.txt");
    // Too high
    try std.testing.expectEqual(348820208020395, step2(data, std.testing.allocator));
}
