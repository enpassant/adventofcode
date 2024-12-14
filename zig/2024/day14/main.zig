const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

const width = 101;
const height = 103;

pub fn main() !void {
    std.debug.print("\n\nStart\n", .{});

    try load();
    try step1();
    try step2();
}

var robots: [500]Robot = undefined;
var robot_count: usize = 0;

var data: [140][140]u8 = undefined;

const Pos = struct {
    x: isize,
    y: isize,
};

const Robot = struct {
    x: isize,
    y: isize,
    vx: isize,
    vy: isize,
    fn eq(self: Robot, other: Robot) bool {
        return self.x == other.x and self.y == other.y;
    }
};

fn load() !void {
    var it = std.mem.tokenizeAny(u8, input, "\n");
    robot_count = 0;
    while (it.next()) |line| {
        var il = std.mem.tokenizeAny(u8, line, "p=,v ");
        robots[robot_count] = Robot{
            .x = try std.fmt.parseInt(isize, il.next().?, 10),
            .y = try std.fmt.parseInt(isize, il.next().?, 10),
            .vx = try std.fmt.parseInt(isize, il.next().?, 10),
            .vy = try std.fmt.parseInt(isize, il.next().?, 10),
        };
        robot_count += 1;
    }
}

fn step1() !void {
    for (0..height) |y| {
        for (0..width) |x| {
            data[y][x] = 0;
        }
    }
    for (0..robot_count) |r| {
        const x = @mod(robots[r].x + robots[r].vx * 100, width);
        const y = @mod(robots[r].y + robots[r].vy * 100, height);
        data[@as(usize, @intCast(y))][@as(usize, @intCast(x))] += 1;
    }
    var quadrant: [2][2]usize = undefined;
    for (0..2) |y| {
        for (0..2) |x| {
            quadrant[y][x] = 0;
        }
    }

    const half_width = @divTrunc(width, 2) + 1;
    const half_height = @divTrunc(height, 2) + 1;

    for (0..height) |y| {
        if (y == half_height - 1) continue;
        for (0..width) |x| {
            if (x == half_width - 1) continue;
            quadrant[y / half_height][x / half_width] += data[y][x];
        }
    }

    var total: usize = 1;

    for (0..2) |y| {
        for (0..2) |x| {
            total *= quadrant[y][x];
        }
    }

    std.debug.print("Sum 1: {}\n", .{total});
}

fn step2() !void {
    var total: isize = 0;

    var min: u64 = 1000000000;
    var min_step: isize = undefined;
    const half: isize = 50;

    while (true) {
        total += 1;

        var max: u64 = 0;

        for (0..robot_count) |r| {
            const x = @mod(robots[r].x + robots[r].vx * total, width);
            const y = @mod(robots[r].y + robots[r].vy * total, height);
            const pos = Pos{ .x = x, .y = y };
            max += @abs(pos.x - half) + @abs(pos.y - half);
        }
        if (min > max) {
            min = max;
            min_step = total;
        }
        if (total > 10000) break;
    }

    for (0..height) |y| {
        for (0..width) |x| {
            data[y][x] = 0;
        }
    }

    for (0..robot_count) |r| {
        const x = @mod(robots[r].x + robots[r].vx * min_step, width);
        const y = @mod(robots[r].y + robots[r].vy * min_step, height);
        data[@as(usize, @intCast(y))][@as(usize, @intCast(x))] += 1;
    }
    for (42..height - 28) |y| {
        for (34..width - 36) |x| {
            std.debug.print("{}", .{data[y][x]});
        }
        std.debug.print("\n", .{});
    }

    std.debug.print("Sum 2: {}\n", .{min_step});
}
