const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

pub fn main() !void {
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    allocator = arena.allocator();

    try load();
    try step1();
    try step2();
}

var allocator: std.mem.Allocator = undefined;
const ArrayList = std.ArrayList;
var map: std.StringHashMap(bool) = undefined;
var update: ArrayList(ArrayList([]const u8)) = undefined;
var incorrect: ArrayList(ArrayList([]const u8)) = undefined;

fn load() !void {
    map = std.StringHashMap(bool).init(allocator);
    update = ArrayList(ArrayList([]const u8)).init(allocator);
    incorrect = ArrayList(ArrayList([]const u8)).init(allocator);

    var it = std.mem.tokenizeAny(u8, input, "\n");
    while (it.next()) |num_str| {
        if (num_str.len > 5) {
            try process_update(num_str);
            break;
        }

        _ = try map.getOrPutValue(num_str, true);
    }

    while (it.next()) |num_str| {
        try process_update(num_str);
    }
}

fn process_update(update_str: []const u8) !void {
    var list = ArrayList([]const u8).init(allocator);

    var it = std.mem.tokenizeScalar(u8, update_str, ',');
    while (it.next()) |num_str| {
        try list.append(num_str);
    }
    try update.append(list);
}

fn step1() !void {
    var total: usize = 0;

    for (update.items) |list| {
        var good = true;
        for (0..list.items.len) |i| {
            const num_str_1 = list.items[i];
            // const ls = map.get(num);
            for (i + 1..list.items.len) |j| {
                const num_str_2 = list.items[j];
                const key = map.getKey(try std.mem.concat(
                    allocator,
                    u8,
                    &[_][]const u8{ num_str_2, "|", num_str_1 },
                ));
                if (key != null) {
                    good = false;
                    break;
                }
            }
            if (!good) {
                break;
            }
        }
        if (good) {
            const idx = (list.items.len - 1) / 2;
            const num = try std.fmt.parseInt(usize, list.items[idx], 10);
            total += num;
        } else {
            try incorrect.append(list);
        }
    }

    std.debug.print("Sum 1: {}\n", .{total});
}

fn step2() !void {
    var total: usize = 0;

    for (incorrect.items) |list| {
        _ = std.mem.sort(
            []const u8,
            list.items,
            {},
            lessThen,
        );
        const idx = (list.items.len - 1) / 2;
        const num = try std.fmt.parseInt(usize, list.items[idx], 10);
        total += num;
    }

    std.debug.print("Sum 2: {}\n", .{total});
}

fn lessThen(_: void, num1: []const u8, num2: []const u8) bool {
    const key = map.getKey(std.mem.concat(
        allocator,
        u8,
        &[_][]const u8{ num2, "|", num1 },
    ) catch "");
    if (key != null) {
        return false;
    }
    const key2 = map.getKey(std.mem.concat(
        allocator,
        u8,
        &[_][]const u8{ num1, "|", num2 },
    ) catch "");
    if (key2 != null) {
        return true;
    }

    const n1 = std.fmt.parseInt(usize, num1, 10) catch 0;
    const n2 = std.fmt.parseInt(usize, num2, 10) catch 0;

    const result = std.sort.asc(usize)({}, n1, n2);
    return result;
}
