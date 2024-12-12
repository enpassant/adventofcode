const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

var allocator: std.mem.Allocator = undefined;
var map: std.AutoHashMap(Pos, usize) = undefined;
var mapRegion: std.AutoHashMap(usize, std.ArrayList(Pos)) = undefined;

pub fn main() !void {
    std.debug.print("\n\nStart\n", .{});

    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    allocator = arena.allocator();

    map = std.AutoHashMap(Pos, usize).init(allocator);
    mapRegion = std.AutoHashMap(usize, std.ArrayList(Pos)).init(allocator);

    try load();
    try steps();
}

var rows: usize = 140;
var cols: usize = 140;

var plotId: usize = 0;

var data: [140][140]u8 = undefined;

const Pos = struct {
    x: usize,
    y: usize,
    fn eq(self: Pos, other: Pos) bool {
        return self.x == other.x and self.y == other.y;
    }
};

const Side = struct {
    x: usize,
    y: usize,
    dir: usize,
};

fn load() !void {
    var it = std.mem.tokenize(u8, input, "\n");
    rows = 0;
    while (it.next()) |line| {
        cols = @intCast(line.len);
        std.mem.copyForwards(u8, &data[rows], line);
        rows += 1;
    }
}

fn build(x: usize, y: usize) !void {
    const plant = data[y][x];
    const pos = Pos{ .x = x, .y = y };
    const leftPlotId: ?usize = if (x > 0 and data[y][x - 1] == plant)
        map.get(Pos{ .x = x - 1, .y = y })
    else
        null;
    const topPlotId: ?usize = if (y > 0 and data[y - 1][x] == plant)
        map.get(Pos{ .x = x, .y = y - 1 })
    else
        null;
    if (leftPlotId != null and topPlotId != null and leftPlotId != topPlotId) {
        const gopLeft = try mapRegion.getOrPut(leftPlotId.?);
        const gopTop = try mapRegion.getOrPut(topPlotId.?);
        const ls = gopLeft.value_ptr.*.items;
        for (ls) |p| {
            try map.put(p, topPlotId.?);
        }
        try gopTop.value_ptr.*.appendSlice(ls);
        _ = mapRegion.remove(leftPlotId.?);
        // std.debug.print("Merged region: {c} ({}, {})\n", .{ plant, x, y });
    }
    if (topPlotId != null) {
        // std.debug.print("TopPlotId: {c} {} ({}, {})\n", .{ plant, topPlotId.?, x, y });
        const gop = try mapRegion.getOrPut(topPlotId.?);
        try gop.value_ptr.append(pos);
        try map.put(Pos{ .x = x, .y = y }, topPlotId.?);
    } else if (leftPlotId != null) {
        const gop = try mapRegion.getOrPut(leftPlotId.?);
        try gop.value_ptr.append(pos);
        try map.put(pos, leftPlotId.?);
    } else {
        var ls = std.ArrayList(Pos).init(allocator);
        try ls.append(pos);
        try mapRegion.put(plotId, ls);
        try map.put(pos, plotId);
        plotId += 1;
        // std.debug.print("Add region: {c} ({}, {})\n", .{ plant, x, y });
    }
}

fn calcPerimeter(x: usize, y: usize) usize {
    const plant = data[y][x];
    var fenceCount: usize = 0;
    if (x <= 0 or data[y][x - 1] != plant) {
        fenceCount += 1;
    }
    if (y <= 0 or data[y - 1][x] != plant) {
        fenceCount += 1;
    }
    if (x >= cols - 1 or data[y][x + 1] != plant) {
        fenceCount += 1;
    }
    if (y >= rows - 1 or data[y + 1][x] != plant) {
        fenceCount += 1;
    }
    return fenceCount;
}

fn calcSide(x: usize, y: usize) usize {
    const plant = data[y][x];
    var sideCount: usize = 0;
    if (x <= 0 or data[y][x - 1] != plant) {
        const exists = if (y == 0)
            false
        else
            mapSide.get(Side{ .x = x, .y = y - 1, .dir = 1 }) != null;
        if (!exists) {
            sideCount += 1;
        }
        mapSide.put(Side{ .x = x, .y = y, .dir = 1 }, true) catch {};
    }
    if (y <= 0 or data[y - 1][x] != plant) {
        const exists = if (x == 0)
            false
        else
            mapSide.get(Side{ .x = x - 1, .y = y, .dir = 2 }) != null;
        if (!exists) {
            sideCount += 1;
        }
        mapSide.put(Side{ .x = x, .y = y, .dir = 2 }, true) catch {};
    }
    if (x >= cols - 1 or data[y][x + 1] != plant) {
        const exists = if (y == 0)
            false
        else
            mapSide.get(Side{ .x = x, .y = y - 1, .dir = 3 }) != null;
        if (!exists) {
            sideCount += 1;
        }
        mapSide.put(Side{ .x = x, .y = y, .dir = 3 }, true) catch {};
    }
    if (y >= rows - 1 or data[y + 1][x] != plant) {
        const exists = if (x == 0)
            false
        else
            mapSide.get(Side{ .x = x - 1, .y = y, .dir = 4 }) != null;
        if (!exists) {
            sideCount += 1;
        }
        mapSide.put(Side{ .x = x, .y = y, .dir = 4 }, true) catch {};
    }
    // std.debug.print(
    //     "SideCount: {c} ({}, {}) {}\n",
    //     .{ plant, x, y, sideCount },
    // );
    return sideCount;
}

var mapSide: std.AutoHashMap(Side, bool) = undefined;

fn steps() !void {
    for (0..rows) |y| {
        for (0..cols) |x| {
            try build(x, y);
        }
    }

    var it = mapRegion.iterator();
    var total1: usize = 0;
    var total2: usize = 0;
    while (it.next()) |region| {
        const ls = region.value_ptr.*;

        mapSide = std.AutoHashMap(Side, bool).init(allocator);
        defer mapSide.deinit();

        var fenceCount: usize = 0;
        var sideCount: usize = 0;

        for (0..ls.items.len) |i| {
            const pos = ls.items[i];
            fenceCount += calcPerimeter(pos.x, pos.y);
            sideCount += calcSide(pos.x, pos.y);
        }
        total1 += ls.items.len * fenceCount;
        total2 += ls.items.len * sideCount;
        // const pos0 = ls.items[0];
        // std.debug.print(
        //     "Price: {c} {} * {}\n",
        //     .{ data[pos0.y][pos0.x], ls.items.len, fenceCount },
        // );
        // std.debug.print(
        //     "Price: {c} {} * {}\n",
        //     .{ data[pos0.y][pos0.x], ls.items.len, sideCount },
        // );
    }

    std.debug.print("Sum 1: {}\n", .{total1});
    std.debug.print("Sum 1: {}\n", .{total2});
}
