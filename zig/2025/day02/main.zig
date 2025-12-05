const std = @import("std");

const Range = struct {
    u64,
    u64,
};

fn step1(data: []const u8) !u64 {
    var total: u64 = 0;

    var it = std.mem.tokenizeAny(u8, data, ",\r\n ");
    while (it.next()) |range| {
        var it_range = std.mem.tokenizeAny(u8, range, "-");
        const value1 = it_range.next();
        const value2 = it_range.next();
        const num1 = try std.fmt.parseInt(u64, value1.?, 10);
        const num2 = try std.fmt.parseInt(u64, value2.?, 10);
        // std.debug.print("Range: {} - {}\n", .{ num1, num2 });

        const numFloat1: f64 = @floatFromInt(num1);
        const log1: u64 = @intFromFloat(@log10(numFloat1));
        const logNum1: u64 = (log1 + 2) >> 1;
        const base1: u64 = try std.math.powi(u64, 10, logNum1) + 1;
        const min: u64 = try std.math.powi(u64, 10, logNum1 - 1);
        const multiplier1 = @max(min, @divTrunc(num1 + base1 - 1, base1));
        // std.debug.print("Nums 1: {}, {}, {}\n", .{ logNum1, base1, multiplier1 });

        const numFloat2: f64 = @floatFromInt(num2);
        const log2: u64 = @intFromFloat(@log10(numFloat2));
        const logNum2: u64 = (log2 + 1) >> 1;
        const base2: u64 = try std.math.powi(u64, 10, logNum2) + 1;
        const max: u64 = try std.math.powi(u64, 10, logNum2) - 1;
        const multiplier2 = @min(max, @divTrunc(num2, base2));
        // std.debug.print("Nums 2: {}, {}, {}\n", .{ logNum2, base2, multiplier2 });

        if (logNum1 <= logNum2 and multiplier1 <= multiplier2) {
            const sum: u64 = ((multiplier1 + multiplier2) * (multiplier2 - multiplier1 + 1)) >> 1;
            // std.debug.print("Sum: ({} + {} + 1) / 2 = {}\n", .{ multiplier1, multiplier2, sum });
            total += sum * base1;
        }
    }

    // std.debug.print("Total 1: {}\n", .{total});

    return total;
}

fn step2(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var it = std.mem.tokenizeAny(u8, data, ",\r\n ");
    while (it.next()) |range| {
        var used = std.AutoArrayHashMap(u64, bool).init(allocator);
        defer used.deinit();
        var it_range = std.mem.tokenizeAny(u8, range, "-");
        const value1 = it_range.next();
        const value2 = it_range.next();
        const num1 = try std.fmt.parseInt(u64, value1.?, 10);
        const num2 = try std.fmt.parseInt(u64, value2.?, 10);
        // std.debug.print("Range: {} - {}\n", .{ num1, num2 });
        var num = num1;
        while (num <= num2) {
            var buf1: [100]u8 = undefined;
            const value = try std.fmt.bufPrint(&buf1, "{}", .{num});
            for (1..value.len) |len| {
                const times = @divTrunc(value.len, len);
                if (@mod(value.len, len) != 0) continue;
                var buf: [100]u8 = undefined;
                for (0..times) |i| {
                    std.mem.copyForwards(u8, buf[i * len .. (i + 1) * len], value[0..len]);
                }
                if (std.mem.eql(u8, buf[0..value.len], value)) {
                    // std.debug.print("Found: {s}, {s} -> {s}\n", .{ value, value[0..len], buf[0..value.len] });
                    const result = try used.getOrPut(num);
                    if (!result.found_existing) {
                        result.value_ptr.* = true;
                        total += num;
                    }
                }
            }
            num += 1;
        }
    }

    // std.debug.print("Total 1: {}\n", .{total});

    return total;
}

fn step2_orig(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var it = std.mem.tokenizeAny(u8, data, ",\r\n ");
    while (it.next()) |range| {
        var used = std.AutoArrayHashMap(u64, bool).init(allocator);
        defer used.deinit();
        var it_range = std.mem.tokenizeAny(u8, range, "-");
        const value1 = it_range.next();
        const value2 = it_range.next();
        const num1 = try std.fmt.parseInt(u64, value1.?, 10);
        const num2 = try std.fmt.parseInt(u64, value2.?, 10);
        // std.debug.print("Range: {} - {}\n", .{ num1, num2 });

        const numFloat1: f64 = @floatFromInt(num1);
        const logNum1: u64 = @intFromFloat(@log10(numFloat1));

        const numFloat2: f64 = @floatFromInt(num2);
        const logNum2: u64 = @intFromFloat(@log10(numFloat2));
        // std.debug.print("Nums 2: {}, {}\n", .{ logNum1, logNum2 });

        if (logNum1 <= logNum2) {
            for (1..(@max(1, (logNum1 + 1) >> 1) + 1)) |len| {
                if (@mod(logNum1 + 1, len) == 0) {
                    total += try calc(num1, logNum1, num2, len, &used);
                }
            }
            if (logNum1 < logNum2) {
                for (1..(@max(1, (logNum2 + 1) >> 1) + 1)) |len| {
                    if (@mod(logNum2 + 1, len) == 0) {
                        total += try calc(num1, logNum2, num2, len, &used);
                    }
                }
            }
        }
    }

    // std.debug.print("Total 1: {}\n", .{total});

    return total;
}

fn calc(
    num1: u64,
    logNum1: u64,
    num2: u64,
    len: usize,
    used: *std.AutoArrayHashMap(u64, bool),
) !u64 {
    var total: u64 = 0;
    const width = @divTrunc(logNum1, len);
    var base: u64 = 0;
    for (1..width + 2) |_| {
        base = try std.math.powi(u64, 10, len) * base + 1;
    }
    const min: u64 = try std.math.powi(u64, 10, len - 1);
    const max: u64 = try std.math.powi(u64, 10, len) - 1;
    // std.debug.print("Calc: ({}, {} - {}, {}, {} - {})\n", .{ len, min, max, base, num1, num2 });
    const multiplier1 = @max(min, @divTrunc(num1 + base - 1, base));
    const multiplier2 = @min(max, @divTrunc(num2, base));
    std.debug.print("Sum: ({}, {}, {}, {} - {}, {})\n", .{ multiplier1, multiplier2, base, min, max, width });
    if (multiplier1 > multiplier2) return 0;
    for (multiplier1..multiplier2 + 1) |multiplier| {
        const value = multiplier * base;
        const result = try used.getOrPut(value);
        if (!result.found_existing) {
            result.value_ptr.* = true;
            total += value;
        }
    }
    std.debug.print("Total: ({} {})\n", .{ total, total });
    return total;
}

test "Day 02 test Part 1 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(1227775554, step1(data));
}

test "Day 02 test Part 2 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(4174379265, step2(data, std.testing.allocator));
}

test "Day 02 input Part 1 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(31000881061, step1(data));
}

test "Day 02 input Part 2 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(46769308485, step2(data, std.testing.allocator));
}
