const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

pub fn main() !void {
    std.debug.print("\n\nStart\n", .{});

    const total1 = try calc(0);
    const total2 = try calc(10000000000000);

    std.debug.print("Sum 1: {}\n", .{total1});
    std.debug.print("Sum 2: {}\n", .{total2});
}

fn calc(start: isize) !usize {
    var total: usize = 0;
    var it = std.mem.tokenizeAny(u8, input, "\n");
    while (it.next()) |line| {
        var itLine = std.mem.tokenizeAny(u8, line, ":+,");
        _ = itLine.next();
        _ = itLine.next();
        const ax = try std.fmt.parseInt(isize, itLine.next() orelse "", 10);
        _ = itLine.next();
        const ay = try std.fmt.parseInt(isize, itLine.next() orelse "", 10);
        itLine = std.mem.tokenizeAny(u8, it.next() orelse "", ":+,");
        _ = itLine.next();
        _ = itLine.next();
        const bx = try std.fmt.parseInt(isize, itLine.next() orelse "", 10);
        _ = itLine.next();
        const by = try std.fmt.parseInt(isize, itLine.next() orelse "", 10);
        itLine = std.mem.tokenizeAny(u8, it.next() orelse "", ":=,");
        _ = itLine.next();
        _ = itLine.next();
        const px = start + try std.fmt.parseInt(isize, itLine.next() orelse "", 10);
        _ = itLine.next();
        const py = start + try std.fmt.parseInt(isize, itLine.next() orelse "", 10);

        const c1 = @divTrunc(ax * py, ax * by - bx * ay);
        const c1m = std.math.mod(isize, ax * py, ax * by - bx * ay) catch -1;
        const c2 = @divTrunc(ay * px, ax * by - bx * ay);
        const c2m = std.math.mod(isize, ay * px, ax * by - bx * ay) catch -1;
        const b = c1 + c1m - c2 - c2m;
        const a = std.math.divExact(isize, px - bx * b, ax) catch 0;

        if (a * ax + b * bx == px and a * ay + b * by == py) {
            total += @as(usize, @intCast(a * 3)) + @as(usize, @intCast(b));
        }
    }
    return total;
}
