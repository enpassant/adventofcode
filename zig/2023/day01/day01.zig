const std = @import("std");
const data = @embedFile("data01.txt");

pub fn main() !void {
    try step1();
    try step2();
}

fn step1() !void {
    var it = std.mem.tokenize(u8, data, "\n");
    var total: usize = 0;
    while (it.next()) |line| {
        const n1: ?usize = std.mem.indexOfAny(u8, line, "0123456789");
        const n2: ?usize = std.mem.lastIndexOfAny(u8, line, "0123456789");
        var numChars: [2]u8 = undefined;
        numChars[0] = line[n1.?];
        numChars[1] = line[n2.?];
        const num = try std.fmt.parseInt(usize, &numChars, 10);
        total += num;
    }
    std.debug.print("Sum 1: {}\n", .{total});
}

fn step2() !void {
    var it = std.mem.tokenize(u8, data, "\n");
    var total: usize = 0;
    while (it.next()) |line| {
        var numChars: [2]u8 = undefined;
        const n1: ?usize = std.mem.indexOfAny(u8, line, "0123456789");
        const d1: ?u8 = if (n1 != null) line[n1.?] else null;
        numChars[0] = indexOfDigits(line, n1, d1).?;
        const n2: ?usize = std.mem.lastIndexOfAny(u8, line, "0123456789");
        numChars[1] = lastIndexOfDigits(line, n2, if (n2 != null) line[n2.?] else null).?;
        const num = try std.fmt.parseInt(usize, &numChars, 10);
        total += num;
    }
    std.debug.print("Sum 2: {}\n", .{total});
}

const digits: [9][]const u8 = [_][]const u8{ "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

fn indexOfDigits(line: []const u8, pos: ?usize, d: ?u8) ?u8 {
    var minPos = pos orelse line.len;
    var result: ?u8 = d;
    for (digits, 0..) |digit, i| {
        const dch: ?usize = std.mem.indexOf(u8, line, digit);
        if (dch != null and dch.? < minPos) {
            minPos = dch.?;
            result = '1' + std.math.cast(u8, i).?;
        }
    }
    return result;
}

fn lastIndexOfDigits(line: []const u8, pos: ?usize, d: ?u8) ?u8 {
    var maxPos = pos orelse 0;
    var result: ?u8 = d;
    for (digits, 0..) |digit, i| {
        const dch: ?usize = std.mem.lastIndexOf(u8, line, digit);
        if (dch != null and dch.? > maxPos) {
            maxPos = dch.?;
            result = '1' + std.math.cast(u8, i).?;
        }
    }
    return result;
}
