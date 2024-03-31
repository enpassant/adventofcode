const std = @import("std");
const input = @embedFile("input.txt");
// const input = @embedFile("test.txt");

pub fn main() !void {
    var result1: isize = 0;
    var result2: isize = 0;
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var arena = std.heap.ArenaAllocator.init(gpa.allocator());
    defer arena.deinit();
    var alloc = arena.allocator();

    var it = std.mem.tokenize(u8, input, "\n");
    while (it.next()) |line| {
        var line_it = std.mem.tokenize(u8, line, " ");
        var diffArr = std.ArrayList(std.ArrayList(isize)).init(alloc);
        var values = std.ArrayList(isize).init(alloc);
        while (line_it.next()) |num_str| {
            const num = try std.fmt.parseInt(isize, num_str, 10);
            try values.append(num);
        }
        try diffArr.append(values);
        var all_zero = false;
        var items = values.items;
        while (!all_zero) {
            var diffs = std.ArrayList(isize).init(alloc);
            var prev = items[0];
            all_zero = true;
            for (items[1..]) |item| {
                var diff = item - prev;
                try diffs.append(diff);
                if (diff != 0) all_zero = false;
                prev = item;
            }
            items = diffs.items;
            try diffArr.append(diffs);
        }
        var i: usize = diffArr.items.len - 2;
        var prev: isize = 0;
        while (i >= 0) {
            const diffs = diffArr.items[i];
            prev = diffs.getLast() + prev;
            if (i == 0) break else i -= 1;
        }
        result1 += prev;

        i = diffArr.items.len - 2;
        prev = 0;
        while (i >= 0) {
            const diffs = diffArr.items[i];
            prev = diffs.items[0] - prev;
            if (i == 0) break else i -= 1;
        }
        result2 += prev;
        values.clearAndFree();
    }
    std.debug.print("Result 1: {}\n", .{result1});
    std.debug.print("Result 2: {}\n", .{result2});
}
