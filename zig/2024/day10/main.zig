const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

var allocator: std.mem.Allocator = undefined;

pub fn main() !void {
    std.debug.print("\n\nStart\n", .{});

    var arena = std.heap.ArenaAllocator.init(std.heap.page_allocator);
    defer arena.deinit();
    allocator = arena.allocator();

    try load();
    try step1();
}

var rows: usize = 90;
var cols: usize = 90;

var data: [61][61]u8 = undefined;

var heads: std.ArrayList(Pos) = undefined;
var nines: std.AutoArrayHashMap(Pos, std.ArrayList(Pos)) = undefined;

var distinct: usize = 0;

const Pos = struct {
    x: usize,
    y: usize,
    dir: usize,
    fn eq(self: Pos, other: Pos) bool {
        return self.x == other.x and self.y == other.y;
    }
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

fn step1() !void {
    heads = std.ArrayList(Pos).init(allocator);
    nines = std.AutoArrayHashMap(Pos, std.ArrayList(Pos)).init(allocator);

    for (0..rows) |row| {
        for (0..cols) |col| {
            data[row][col] -= '0';
            if (data[row][col] == 0) {
                try heads.append(Pos{ .x = col, .y = row, .dir = 0 });
            }
        }
    }
    for (heads.items) |nine| {
        var ls = std.ArrayList(Pos).init(allocator);
        defer ls.deinit();

        try ls.append(nine);
        try build(&ls);
    }

    var total: usize = 0;

    var it = nines.iterator();
    while (it.next()) |kv| {
        total += kv.value_ptr.*.items.len;
    }

    std.debug.print("Sum 1: {}\n", .{distinct});
    std.debug.print("Sum 2: {}\n", .{total});
}

fn build(ls: *std.ArrayList(Pos)) !void {
    var pos = ls.getLastOrNull();
    while (pos != null) {
        const next: ?Pos = nextPos(pos.?);
        if (next != null) {
            const value = data[next.?.y][next.?.x];
            if (value == ls.items.len) {
                if (ls.items.len >= 9) {
                    const nine = try nines.getOrPut(ls.items[0]);
                    if (!nine.found_existing) {
                        nine.value_ptr.* = std.ArrayList(Pos).init(allocator);
                    }
                    if (true) {
                        var found = false;
                        for (nine.value_ptr.*.items) |item| {
                            if (item.eq(next.?)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            distinct += 1;
                        }
                        try nine.value_ptr.*.append(next.?);
                    } else {
                        try nine.value_ptr.*.append(next.?);
                    }
                } else {
                    var list = try ls.clone();
                    try list.append(next.?);
                    try build(&list);
                }
            }
        }
        const nextDir = pos.?.dir + 1;
        if (nextDir < 4) {
            pos = Pos{ .x = pos.?.x, .y = pos.?.y, .dir = nextDir };
        } else {
            pos = null;
        }
    }
}

fn nextPos(pos: Pos) ?Pos {
    return switch (pos.dir) {
        0 => if (pos.y > 0) Pos{ .x = pos.x, .y = pos.y - 1, .dir = 0 } else null,
        1 => if (pos.x < cols) Pos{ .x = pos.x + 1, .y = pos.y, .dir = 0 } else null,
        2 => if (pos.y < rows) Pos{ .x = pos.x, .y = pos.y + 1, .dir = 0 } else null,
        3 => if (pos.x > 0) Pos{ .x = pos.x - 1, .y = pos.y, .dir = 0 } else null,
        else => null,
    };
}
