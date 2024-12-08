const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

var rows: usize = 140;
var cols: usize = 140;
var antenna_count: usize = 0;

var data: [140][140]u8 = undefined;

const Pos = struct {
    x: usize,
    y: usize,
    fn eq(self: Pos, other: Pos) bool {
        return self.x == other.x and self.y == other.y;
    }
};

var allocator: std.mem.Allocator = undefined;
var map: std.AutoHashMap(Pos, bool) = undefined;
var mapAntenna: std.AutoHashMap(u8, std.ArrayList(Pos)) = undefined;

pub fn main() !void {
    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    allocator = arena.allocator();

    map = std.AutoHashMap(Pos, bool).init(allocator);
    mapAntenna = std.AutoHashMap(u8, std.ArrayList(Pos)).init(allocator);

    try load();
    try step1();
    try step2();
}

fn load() !void {
    var it = std.mem.tokenize(u8, input, "\n");
    rows = 0;
    while (it.next()) |line| {
        cols = @intCast(line.len);
        std.mem.copyForwards(u8, &data[rows], line);
        for (0..cols) |col| {
            const ch = line[col];
            if (std.ascii.isAlphanumeric(ch)) {
                if (!mapAntenna.contains(ch)) {
                    try mapAntenna.put(ch, std.ArrayList(Pos).init(allocator));
                }
                const result = try mapAntenna.getOrPut(ch);
                try result.value_ptr.*.append(Pos{ .x = col, .y = rows });
                antenna_count += 1;
            }
        }
        rows += 1;
    }
}

fn step1() !void {
    var it = mapAntenna.valueIterator();
    while (it.next()) |ls| {
        for (ls.items) |pos1| {
            for (ls.items) |pos2| {
                if (pos1.eq(pos2)) continue;
                const difx: i32 = @as(i32, @intCast(pos2.x)) - @as(i32, @intCast(pos1.x));
                const dify: i32 = @as(i32, @intCast(pos2.y)) - @as(i32, @intCast(pos1.y));
                const newx = @as(i32, @intCast(pos2.x)) + difx;
                const newy = @as(i32, @intCast(pos2.y)) + dify;
                if (is_onboard(newx, newy)) {
                    try map.put(Pos{ .x = @as(usize, @intCast(newx)), .y = @as(usize, @intCast(newy)) }, true);
                }
            }
        }
    }

    const total = map.count();
    std.debug.print("Sum 1: {}\n", .{total});
}

fn is_onboard(x: isize, y: isize) bool {
    return x >= 0 and y >= 0 and x < cols and y < rows;
}

fn step2() !void {
    var it = mapAntenna.valueIterator();
    while (it.next()) |ls| {
        for (ls.items) |pos1| {
            try map.put(Pos{ .x = pos1.x, .y = pos1.y }, true);
            for (ls.items) |pos2| {
                if (pos1.eq(pos2)) continue;
                const difx: i32 = @as(i32, @intCast(pos2.x)) - @as(i32, @intCast(pos1.x));
                const dify: i32 = @as(i32, @intCast(pos2.y)) - @as(i32, @intCast(pos1.y));
                var newx = @as(i32, @intCast(pos2.x)) + difx;
                var newy = @as(i32, @intCast(pos2.y)) + dify;
                while (is_onboard(newx, newy)) {
                    try map.put(Pos{ .x = @as(usize, @intCast(newx)), .y = @as(usize, @intCast(newy)) }, true);
                    newx = @as(i32, @intCast(newx)) + difx;
                    newy = @as(i32, @intCast(newy)) + dify;
                }
            }
        }
    }

    const total = map.count();
    std.debug.print("Sum 2: {}\n", .{total});
}
