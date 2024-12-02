const std = @import("std");
// const data = @embedFile("test.txt");
const data = @embedFile("input.txt");

pub fn main() !void {
    try step1();
    try step2();
}

fn step1() !void {
    const ArrayList = std.ArrayList;
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var list: [2]ArrayList(i32) = undefined;
    list[0] = ArrayList(i32).init(allocator);
    list[1] = ArrayList(i32).init(allocator);
    var it = std.mem.tokenize(u8, data, "\r\n");
    var list_index: usize = 0;
    while (it.next()) |line| {
        var lit = std.mem.tokenizeScalar(u8, line, ' ');
        while (lit.next()) |nums| {
            const num = try std.fmt.parseInt(i32, nums, 10);
            try list[list_index].append(num);
            list_index = 1 - list_index;
        }
    }

    var slice: [2][](i32) = undefined;
    slice[0] = try list[0].toOwnedSlice();
    slice[1] = try list[1].toOwnedSlice();

    std.mem.sort(i32, slice[0], {}, comptime std.sort.asc(i32));
    std.mem.sort(i32, slice[1], {}, comptime std.sort.asc(i32));

    var i: usize = 0;
    var total: usize = 0;
    for (slice[0]) |num| {
        total += @abs(num - slice[1][i]);
        i += 1;
    }
    std.debug.print("Sum 1: {}\n", .{total});
}

fn step2() !void {
    const ArrayList = std.ArrayList;
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var list: [2]ArrayList(i32) = undefined;
    list[0] = ArrayList(i32).init(allocator);
    list[1] = ArrayList(i32).init(allocator);
    var it = std.mem.tokenize(u8, data, "\r\n");
    var list_index: usize = 0;
    while (it.next()) |line| {
        var lit = std.mem.tokenizeScalar(u8, line, ' ');
        while (lit.next()) |nums| {
            const num = try std.fmt.parseInt(i32, nums, 10);
            try list[list_index].append(num);
            list_index = 1 - list_index;
        }
    }

    var slice: [2][](i32) = undefined;
    slice[0] = try list[0].toOwnedSlice();
    slice[1] = try list[1].toOwnedSlice();

    var map: [2](std.AutoHashMap(i32, i32)) = undefined;
    map[0] = std.AutoHashMap(i32, i32).init(allocator);
    defer map[0].deinit();
    map[1] = std.AutoHashMap(i32, i32).init(allocator);
    defer map[1].deinit();

    for (0..2) |idx| {
        for (slice[idx]) |num| {
            if (map[idx].contains(num)) {
                const result = try map[idx].getOrPut(num);
                result.value_ptr.* += 1;
            } else {
                try map[idx].put(num, 1);
            }
        }
    }

    var iterator = map[0].iterator();
    var total: i32 = 0;
    while (iterator.next()) |entry| {
        const num: i32 = entry.key_ptr.*;
        const count: i32 = entry.value_ptr.*;

        // std.debug.print("Num: {} {}\n", .{ num, count });

        if (map[1].contains(num)) {
            const count2: i32 = map[1].get(num).?;
            total += count * num * count2;
            std.debug.print("Num2: {}\n", .{count2});
        }
    }
    std.debug.print("Sum 2: {}\n", .{total});
}
