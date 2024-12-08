const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

pub fn main() !void {
    try load();
}

var data: [200]usize = undefined;

fn load() !void {
    var total: usize = 0;
    var total2: usize = 0;
    var it = std.mem.tokenizeAny(u8, input, "\n");
    while (it.next()) |line| {
        var i: u5 = 0;
        var it_line = std.mem.tokenizeAny(u8, line, ": ");
        const num = try std.fmt.parseInt(usize, it_line.next().?, 10);
        while (it_line.next()) |num_str| {
            data[i] = try std.fmt.parseInt(usize, num_str, 10);
            i += 1;
        }
        const count = @as(u64, 1) << (i - 1);
        var found = false;
        for (0..count) |j| {
            var ib: u5 = 0;
            var sum: u64 = data[0];
            while (ib < i - 1) {
                const bit = (@as(u64, j) & (@as(u64, 1) << @as(u5, ib)) > 0);
                if (bit) {
                    sum += data[ib + 1];
                } else {
                    sum *= data[ib + 1];
                }
                ib += 1;
            }
            if (sum == num) {
                total += num;
                found = true;
                break;
            }
        }
        if (!found) {
            const count2 = std.math.pow(u64, 3, i - 1);
            for (0..count2) |j| {
                var jn = j;
                var pow = std.math.pow(u64, 3, i - 2);
                var ib: u5 = 0;
                var sum: u64 = data[0];
                while (ib < i - 1) {
                    const bit = jn / pow;
                    jn = jn % pow;
                    pow = pow / 3;
                    if (bit == 0) {
                        sum += data[ib + 1];
                    } else if (bit == 1) {
                        sum *= data[ib + 1];
                    } else {
                        sum = concat(sum, data[ib + 1]);
                    }
                    ib += 1;
                }
                if (sum == num) {
                    total2 += num;
                    found = true;
                    break;
                }
            }
        }
    }

    std.debug.print("Sum 1: {}\n", .{total});
    std.debug.print("Sum 2: {}\n", .{total + total2});
}

fn concat(num1: u64, num2: u64) u64 {
    var buffer: [40]u8 = undefined;
    const resultString = std.fmt.bufPrint(&buffer, "{}{}", .{ num1, num2 }) catch "";

    return std.fmt.parseInt(u64, resultString, 10) catch 0;
}
