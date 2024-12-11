const std = @import("std");
// const input = "125 17";
const input = "965842 9159 3372473 311 0 6 86213 48";

var allocator: std.mem.Allocator = undefined;

pub fn main() !void {
    std.debug.print("\n\nStart\n", .{});

    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    allocator = arena.allocator();

    stones = std.AutoHashMap(usize, usize).init(allocator);
    defer stones.deinit();

    try load();
    try calc(25);
    stones.clearAndFree();
    try load();
    try calc(75);
}

var count: usize = 0;
var stones: std.AutoHashMap(usize, usize) = undefined;

fn load() !void {
    var it = std.mem.tokenizeAny(u8, input, "\n ");
    count = 0;
    while (it.next()) |word| {
        const num = try std.fmt.parseInt(usize, word, 10);
        const result = try stones.getOrPut(num);
        if (result.found_existing) {
            result.value_ptr.* += 1;
        } else {
            result.value_ptr.* = 1;
        }
        count += 1;
    }
}

fn calc(stepCount: usize) !void {
    var buf: [20]u8 = undefined;

    for (0..stepCount) |_| {
        var newStones = std.AutoHashMap(usize, usize).init(allocator);
        var it = stones.iterator();
        while (it.next()) |stoneKV| {
            const stone = stoneKV.key_ptr.*;
            const stoneCount = stoneKV.value_ptr.*;

            if (stone == 0) {
                const stone1KV = try newStones.getOrPut(1);
                if (stone1KV.found_existing) {
                    stone1KV.value_ptr.* += stoneCount;
                } else {
                    stone1KV.value_ptr.* = stoneCount;
                }
            } else {
                const digits = try std.fmt.bufPrint(&buf, "{}", .{stone});
                if (digits.len % 2 == 0) {
                    const half: usize = digits.len / 2;
                    const num1 = try std.fmt.parseInt(usize, buf[0..half], 10);
                    const num2 = try std.fmt.parseInt(usize, buf[half..digits.len], 10);
                    const stone1KV = try newStones.getOrPut(num1);

                    if (stone1KV.found_existing) {
                        stone1KV.value_ptr.* += stoneCount;
                    } else {
                        stone1KV.value_ptr.* = stoneCount;
                    }
                    const stone2KV = try newStones.getOrPut(num2);

                    if (stone2KV.found_existing) {
                        stone2KV.value_ptr.* += stoneCount;
                    } else {
                        stone2KV.value_ptr.* = stoneCount;
                    }
                } else {
                    const stoneNewKV = try newStones.getOrPut(stone * 2024);
                    if (stoneNewKV.found_existing) {
                        stoneNewKV.value_ptr.* += stoneCount;
                    } else {
                        stoneNewKV.value_ptr.* = stoneCount;
                    }
                }
            }
        }

        stones = newStones;

        // std.debug.print("Step {}: {}\n", .{ step, stones.count() });
        // var it2 = stones.iterator();
        // while (it2.next()) |stoneKV| {
        //     const stone = stoneKV.key_ptr.*;
        //     const stoneCount = stoneKV.value_ptr.*;
        //     std.debug.print("Stone: {} -> {}\n", .{ stone, stoneCount });
        // }
        // std.debug.print("Step {}: {any}\n", .{ step, stones.items });
    }

    var total: usize = 0;
    var it = stones.iterator();
    while (it.next()) |stoneKV| {
        const stoneCount = stoneKV.value_ptr.*;
        total += stoneCount;
    }
    std.debug.print("Sum 1: {}\n", .{total});
}
