const std = @import("std");
const input = @embedFile("input.txt");
// const input = @embedFile("test.txt");

pub fn main() !void {
    try part1();
    try part2();
}

const Node = struct {
    left: []const u8,
    right: []const u8,
};

fn part1() !void {
    var total: usize = 0;
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var maps = std.StringHashMap(Node).init(alloc);
    defer maps.deinit();

    var it = std.mem.tokenize(u8, input, "\n =(,)");
    var lr_inst = it.next().?;
    while (it.next()) |key| {
        try maps.put(key, Node{ .left = it.next().?, .right = it.next().? });
    }
    var pos: []const u8 = "AAA";
    var i: usize = 0;
    while (!std.mem.eql(u8, pos, "ZZZ")) {
        total += 1;
        var node = maps.get(pos).?;
        if (lr_inst[i] == 'L') {
            pos = node.left;
        } else {
            pos = node.right;
        }
        i = if (i < lr_inst.len - 1) i + 1 else 0;
    }
    std.debug.print("Result 1: {}\n", .{total});
}

fn part2() !void {
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var maps = std.StringHashMap(Node).init(alloc);
    defer maps.deinit();

    var it = std.mem.tokenize(u8, input, "\n =(,)");
    var lr_inst = it.next().?;
    while (it.next()) |key| {
        try maps.put(key, Node{ .left = it.next().?, .right = it.next().? });
    }

    var positions = std.ArrayList([]const u8).init(alloc);
    defer positions.deinit();

    var kit = maps.keyIterator();
    while (kit.next()) |key| {
        if (key.*[2] == 'A') {
            try positions.append(key.*);
        }
    }
    var total: usize = 0;
    for (positions.items) |p| {
        var pos = p;
        var i: usize = 0;
        var steps: usize = 0;
        while (pos[2] != 'Z') {
            steps += 1;
            var node = maps.get(pos).?;
            if (lr_inst[i] == 'L') {
                pos = node.left;
            } else {
                pos = node.right;
            }
            i = if (i < lr_inst.len - 1) i + 1 else 0;
        }
        total = if (total == 0)
            steps
        else
            total * steps / std.math.gcd(total, steps);
    }
    std.debug.print("Result 2: {}\n", .{total});
}
