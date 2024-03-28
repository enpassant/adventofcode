const std = @import("std");
const input = @embedFile("input.txt");
// const input = @embedFile("test.txt");

pub fn main() !void {
    try part1();
    try part2();
}

fn part1() !void {
    var total: usize = 1;
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var time = std.ArrayList(usize).init(alloc);
    var distance = std.ArrayList(usize).init(alloc);
    var line_it = std.mem.split(u8, input, "\n");
    var time_line = line_it.next();
    var time_it = std.mem.tokenize(u8, time_line.?, " ");
    _ = time_it.next();
    while (time_it.next()) |num_str| {
        var num = try std.fmt.parseInt(usize, num_str, 10);
        try time.append(num);
    }
    var distance_line = line_it.next();
    var distance_it = std.mem.tokenize(u8, distance_line.?, " ");
    _ = distance_it.next();
    while (distance_it.next()) |num_str| {
        var num = try std.fmt.parseInt(usize, num_str, 10);
        try distance.append(num);
    }
    for (time.items, 0..) |t, i| {
        var d = distance.items[i];
        var e = for (2..(@divFloor(t, 2))) |j| {
            if (j * (t - j) > d) {
                break j;
            }
        } else 0;
        var r = t - e - e + 1;
        total *= r;
    }
    std.debug.print("Result 1: {}\n", .{total});
}

fn readNum(nums: []const u8) !u64 {
    var buf: [1024]u8 = undefined;
    var i: u8 = 0;
    for (nums) |ch| {
        if (std.ascii.isDigit(ch)) {
            buf[i] = ch;
            i += 1;
        }
    }
    return try std.fmt.parseInt(usize, buf[0..i], 10);
}

fn part2() !void {
    var line_it = std.mem.split(u8, input, "\n");
    var time: u64 = try readNum(line_it.next().?);
    var distance: u64 = try readNum(line_it.next().?);
    var e = for (2..(@divFloor(time, 2))) |j| {
        if (j * (time - j) > distance) {
            break j;
        }
    } else 0;
    var result = time - e - e + 1;
    std.debug.print("Result 2: {}\n", .{result});
}
