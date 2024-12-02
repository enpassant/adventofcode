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

    var list: ArrayList(i32) = ArrayList(i32).init(allocator);
    defer list.deinit();

    var it = std.mem.tokenize(u8, data, "\r\n");

    var total: usize = 0;
    while (it.next()) |line| {
        list.clearAndFree();
        var lit = std.mem.tokenizeScalar(u8, line, ' ');
        while (lit.next()) |nums| {
            const num = try std.fmt.parseInt(i32, nums, 10);
            try list.append(num);
        }

        const items = list.items;
        const is_safe = checkSafe(items);
        if (is_safe) {
            total += 1;
        }
    }

    std.debug.print("Sum 1: {}\n", .{total});
}

fn checkSafe(items: []i32) bool {
    var first = items[0];
    var is_decrease: ?bool = null;
    for (items[1..]) |num| {
        if (num == first) {
            return false;
        }
        if (is_decrease == null) {
            is_decrease = num < first;
        } else {
            if ((is_decrease.? and num > first) or
                (!is_decrease.? and num < first))
            {
                return false;
            }
        }
        if (@abs(num - first) > 3) {
            return false;
        }
        first = num;
    }
    return true;
}

fn step2() !void {
    const ArrayList = std.ArrayList;
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    const allocator = arena.allocator();

    var list: ArrayList(i32) = ArrayList(i32).init(allocator);
    defer list.deinit();

    var it = std.mem.tokenize(u8, data, "\r\n");

    var total: usize = 0;
    while (it.next()) |line| {
        list.clearAndFree();
        var lit = std.mem.tokenizeScalar(u8, line, ' ');
        while (lit.next()) |nums| {
            const num = try std.fmt.parseInt(i32, nums, 10);
            try list.append(num);
        }

        const count = list.items.len;
        var remove: ?usize = null;
        while (true) {
            var clonedList = try list.clone();
            if (remove == null) {
                remove = 0;
            } else {
                if (remove.? >= count) {
                    break;
                }
                _ = clonedList.orderedRemove(remove.?);
                remove.? += 1;
            }
            const items = clonedList.items;
            const is_safe = checkSafe(items);
            if (is_safe) {
                total += 1;
                break;
            }
        }
    }

    std.debug.print("Sum 2: {}\n", .{total});
}
