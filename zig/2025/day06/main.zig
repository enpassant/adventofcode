const std = @import("std");

const Problem = struct {
    numbers: [5]u64,
    width: u8,
    left: ?bool,
};

fn step1(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var list: std.ArrayList(Problem) = std.ArrayList(Problem).empty;
    defer list.deinit(allocator);

    var it = std.mem.tokenizeAny(u8, data, "\r\n");
    var row: usize = 0;
    while (it.next()) |range_str| {
        var col: usize = 0;
        var it_range = std.mem.tokenizeAny(u8, range_str, " ");
        while (it_range.next()) |value| {
            var problem: *Problem = undefined;
            if (row == 0) {
                problem = try list.addOne(allocator);
            } else {
                problem = &list.items[col];
            }
            const num_opt = std.fmt.parseInt(u64, value, 10);
            if (num_opt) |num| {
                problem.numbers[row] = num;
            } else |_| {
                if (value[0] == '+') {
                    var sum: u64 = 0;
                    for (0..row) |i| {
                        sum += problem.numbers[i];
                    }
                    total += sum;
                } else {
                    var product: u64 = 1;
                    for (0..row) |i| {
                        if (problem.numbers[i] > 0) {
                            product *= problem.numbers[i];
                        }
                    }
                    total += product;
                }
            }
            col += 1;
        }
        row += 1;
    }

    return total;
}

fn step2(data: []const u8, allocator: std.mem.Allocator) !u64 {
    var total: u64 = 0;

    var list: std.ArrayList(Problem) = std.ArrayList(Problem).empty;
    defer list.deinit(allocator);

    var it = std.mem.tokenizeAny(u8, data, "\r\n");
    while (it.next()) |line| {
        var it_token = std.mem.splitScalar(u8, line, ' ');
        var problem_opt: ?*Problem = null;
        while (it_token.next()) |token| {
            if (token.len > 0 and (token[0] == '*' or token[0] == '+')) {
                problem_opt = try list.addOne(allocator);
                problem_opt.?.left = null;
                problem_opt.?.width = 1;
                for (0..problem_opt.?.numbers.len) |i| {
                    problem_opt.?.numbers[i] = 0;
                }
            } else if (problem_opt) |problem| {
                problem.width += 1;
            }
        }
    }

    it = std.mem.tokenizeAny(u8, data, "\r\n");
    while (it.next()) |line| {
        var pos: usize = 0;
        var col: usize = 0;
        while (col < list.items.len) {
            var problem: *Problem = &list.items[col];
            if (problem.left == null) {
                if (line[0] == '+' or line[0] == '*') {
                    problem.left = false;
                    continue;
                }

                if (line[pos] == ' ') {
                    problem.left = false;
                } else if (line[pos + problem.width - 1] == ' ') {
                    problem.left = true;
                }
            }
            pos += problem.width + 1;
            col += 1;
        }
    }

    it = std.mem.tokenizeAny(u8, data, "\r\n");
    var row: usize = 0;
    while (it.next()) |range_str| {
        var col: usize = 0;
        var it_range = std.mem.tokenizeAny(u8, range_str, " ");
        while (it_range.next()) |value| {
            var problem: *Problem = &list.items[col];
            const num_opt = std.fmt.parseInt(u64, value, 10);
            if (num_opt) |num| {
                if (problem.left) |left| {
                    if (left) {
                        var number: u64 = num;
                        var count: usize = problem.width - value.len;
                        while (number > 0) {
                            const mod = @mod(number, 10);
                            problem.numbers[count] = problem.numbers[count] * 10 + mod;
                            count += 1;
                            number = @divTrunc(number, 10);
                        }
                    } else {
                        var number: u64 = num;
                        var count: usize = problem.width;
                        while (number > 0) {
                            count -= 1;
                            const mod = @mod(number, 10);
                            problem.numbers[count] = problem.numbers[count] * 10 + mod;
                            number = @divTrunc(number, 10);
                        }
                    }
                }
            } else |_| {
                if (value[0] == '+') {
                    var sum: u64 = 0;
                    for (0..row) |i| {
                        sum += problem.numbers[i];
                    }
                    total += sum;
                } else {
                    var product: u64 = 1;
                    for (0..row) |i| {
                        if (problem.numbers[i] > 0) {
                            product *= problem.numbers[i];
                        }
                    }
                    total += product;
                }
            }
            col += 1;
        }
        row += 1;
    }

    return total;
}

test "Day 02 test Part 1 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(4277556, step1(data, std.testing.allocator));
}

test "Day 02 test Part 2 result is good" {
    const data = @embedFile("test.txt");
    try std.testing.expectEqual(3263827, step2(data, std.testing.allocator));
}

test "Day 02 input Part 1 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(5322004718681, step1(data, std.testing.allocator));
}

test "Day 02 input Part 2 result is good" {
    const data = @embedFile("input.txt");
    try std.testing.expectEqual(9876636978528, step2(data, std.testing.allocator));
}
