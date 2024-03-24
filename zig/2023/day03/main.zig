const std = @import("std");
const input = @embedFile("input.txt");

const rows: usize = 140;
const cols: usize = 140;

var data: [rows][cols]u8 = undefined;

pub fn main() !void {
    try load();
    try part1();
    try part2();
}

fn load() !void {
    var it = std.mem.tokenize(u8, input, "\n");
    var i: usize = 0;
    while (it.next()) |line| {
        std.mem.copy(u8, &data[i], line);
        i += 1;
    }
}

fn part1() !void {
    // std.debug.print("Sum 1: {any}\n", .{data});
    var total: usize = 0;
    var i: usize = 0;
    while (i < rows) : (i += 1) {
        var j: usize = 0;
        var s: ?usize = null;
        while (j < cols) : (j += 1) {
            if (data[i][j] >= '0' and data[i][j] <= '9') {
                if (s == null) s = j;
            } else if (s != null) {
                const num: usize = try std.fmt.parseInt(usize, data[i][s.?..j], 10);
                if (hasAdjacent(s.?, j, i)) {
                    total += num;
                }
                s = null;
            } else {
                s = null;
            }
        }
        if (s != null) {
            const num: usize = try std.fmt.parseInt(usize, data[i][s.?..j], 10);
            if (hasAdjacent(s.?, j - 1, i)) {
                total += num;
            }
            s = null;
        }
    }
    std.debug.print("Sum 1: {}\n", .{total});
}

fn isSymbol(i: usize, j: usize) bool {
    if (data[i][j] >= '0' and data[i][j] <= '9') {
        return false;
    } else return data[i][j] != '.';
}

fn hasAdjacent(s: usize, e: usize, i: usize) bool {
    const st = if (s > 0) s - 1 else s;
    const is = if (i > 0) i - 1 else i;
    const ie = if (i < rows - 1) i + 1 else i;
    for (is..(ie + 1)) |ii| {
        if (isSymbol(ii, st)) return true;
        if (isSymbol(ii, e)) return true;
    }
    for (st..(e + 1)) |c| {
        if (isSymbol(is, c)) return true;
        if (isSymbol(ie, c)) return true;
    }
    return false;
}

fn isGear(i: usize, j: usize) bool {
    return data[i][j] == '*';
}

fn getAdjacentGear(s: usize, e: usize, i: usize) ?struct { usize, usize } {
    const st = if (s > 0) s - 1 else s;
    const is = if (i > 0) i - 1 else i;
    const ie = if (i < rows - 1) i + 1 else i;
    for (is..(ie + 1)) |ii| {
        if (isGear(ii, st)) return .{ ii, st };
        if (isGear(ii, e)) return .{ ii, e };
    }
    for (st..(e + 1)) |c| {
        if (isGear(is, c)) return .{ is, c };
        if (isGear(ie, c)) return .{ ie, c };
    }
    return null;
}

fn part2() !void {
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var map = std.AutoHashMap(struct { usize, usize }, usize).init(alloc);
    var total: usize = 0;
    var i: usize = 0;
    while (i < rows) : (i += 1) {
        var j: usize = 0;
        var s: ?usize = null;
        while (j < cols) : (j += 1) {
            if (data[i][j] >= '0' and data[i][j] <= '9') {
                if (s == null) s = j;
            } else if (s != null) {
                const num: usize = try std.fmt.parseInt(usize, data[i][s.?..j], 10);
                const gearPos = getAdjacentGear(s.?, j, i);
                if (gearPos != null) {
                    if (map.contains(gearPos.?)) {
                        total += map.get(gearPos.?).? * num;
                    } else {
                        try map.put(gearPos.?, num);
                    }
                }
                s = null;
            } else {
                s = null;
            }
        }
        if (s != null) {
            const num: usize = try std.fmt.parseInt(usize, data[i][s.?..j], 10);
            const gearPos = getAdjacentGear(s.?, j - 1, i);
            if (gearPos != null) {
                if (map.contains(gearPos.?)) {
                    total += map.get(gearPos.?).? * num;
                } else {
                    try map.put(gearPos.?, num);
                }
            }
            s = null;
        }
    }
    std.debug.print("Sum 2: {}\n", .{total});
}
