const std = @import("std");
// const data = @embedFile("test.txt");
const data = @embedFile("input.txt");

pub fn main() !void {
    try step1();
    try step2();
}

fn step1() !usize {
    const ArrayList = std.ArrayList;
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var list: ArrayList(i32) = ArrayList(i32).empty;
    defer list.deinit(allocator);

    var it = std.mem.tokenizeAny(u8, data, "\r\n");
    while (it.next()) |line| {
        const num = try std.fmt.parseInt(i32, line[1..], 10);
        if (line[0] == 'L') {
            (try list.addOne(allocator)).* = -num;
        } else {
            (try list.addOne(allocator)).* = num;
        }
    }

    const slice = try list.toOwnedSlice(allocator);

    var count: usize = 0;
    var total: isize = 50;
    for (slice) |num| {
        total = @mod(total + num, 100);
        if (total == 0) {
            count += 1;
        }
    }
    std.debug.print("Sum 1: {}\n", .{count});

    return count;
}

fn step2() !usize {
    const ArrayList = std.ArrayList;
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var list: ArrayList(i32) = ArrayList(i32).empty;
    defer list.deinit(allocator);

    var it = std.mem.tokenizeAny(u8, data, "\r\n");
    while (it.next()) |line| {
        const num = try std.fmt.parseInt(i32, line[1..], 10);
        if (line[0] == 'L') {
            (try list.addOne(allocator)).* = -num;
        } else {
            (try list.addOne(allocator)).* = num;
        }
    }

    const slice = try list.toOwnedSlice(allocator);

    var count: usize = 0;
    var total: isize = 50;
    for (slice) |num| {
        const next: isize = total + num;
        if (next <= 0) {
            if (total == 0) {
                count += @abs(next) / 100;
            } else {
                count += @abs(next) / 100 + 1;
            }
        } else {
            count += @abs(next) / 100;
        }
        total = @mod(next, 100);
    }
    std.debug.print("Sum 2: {}\n", .{count});

    return count;
}

test "Day 01 Part 1 result is good" {
    try std.testing.expectEqual(1074, step1());
}

test "Day 01 Part 2 result is good" {
    try std.testing.expectEqual(6254, step2());
}
