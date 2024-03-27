const std = @import("std");
const input = @embedFile("input.txt");
// const input = @embedFile("test.txt");

pub fn main() !void {
    try part1();
    try part2();
}

fn load() !void {}

const Range = struct {
    dest: usize,
    src: usize,
    len: usize,
};

fn part1() !void {
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var seeds = std.ArrayList(usize).init(alloc);
    var line_it = std.mem.split(u8, input, "\n");
    var seeds_line = line_it.next();
    var seeds_it = std.mem.splitSequence(u8, seeds_line.?, ":");
    _ = seeds_it.next();
    var seeds_str = seeds_it.next();
    var num_it = std.mem.tokenize(u8, seeds_str.?, " ");
    while (num_it.next()) |num_str| {
        var num = try std.fmt.parseInt(usize, num_str, 10);
        try seeds.append(num);
    }

    var maps: []std.ArrayList(Range) = undefined;
    maps = try alloc.alloc(std.ArrayList(Range), 7);
    _ = line_it.next();

    for (0..7) |i| {
        _ = line_it.next();
        maps[i] = std.ArrayList(Range).init(alloc);
        while (line_it.next()) |line| {
            if (line.len == 0) break;
            var it = std.mem.tokenize(u8, line, " ");
            var range = Range{
                .dest = try std.fmt.parseInt(usize, it.next().?, 10),
                .src = try std.fmt.parseInt(usize, it.next().?, 10),
                .len = try std.fmt.parseInt(usize, it.next().?, 10),
            };
            try maps[i].append(range);
        }
    }
    var min: usize = std.math.maxInt(usize);

    for (seeds.items) |seed| {
        var num = seed;
        for (maps) |map| {
            for (map.items) |range| {
                if (num >= range.src and num < range.src + range.len) {
                    num = range.dest + num - range.src;
                    break;
                }
            }
        }
        if (num < min) {
            min = num;
        }
    }

    std.debug.print("Sum 1: {}\n", .{min});
}

fn part2() !void {
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var seeds = std.ArrayList(usize).init(alloc);
    var line_it = std.mem.split(u8, input, "\n");
    var seeds_line = line_it.next();
    var seeds_it = std.mem.splitSequence(u8, seeds_line.?, ":");
    _ = seeds_it.next();
    var seeds_str = seeds_it.next();
    var num_it = std.mem.tokenize(u8, seeds_str.?, " ");
    while (num_it.next()) |num_str| {
        var num = try std.fmt.parseInt(usize, num_str, 10);
        try seeds.append(num);
    }

    var maps: []std.ArrayList(Range) = undefined;
    maps = try alloc.alloc(std.ArrayList(Range), 7);
    _ = line_it.next();

    for (0..7) |i| {
        _ = line_it.next();
        maps[i] = std.ArrayList(Range).init(alloc);
        while (line_it.next()) |line| {
            if (line.len == 0) break;
            var it = std.mem.tokenize(u8, line, " ");
            var range = Range{
                .dest = try std.fmt.parseInt(usize, it.next().?, 10),
                .src = try std.fmt.parseInt(usize, it.next().?, 10),
                .len = try std.fmt.parseInt(usize, it.next().?, 10),
            };
            try maps[i].append(range);
        }
    }
    var min: usize = std.math.maxInt(usize);

    var len = seeds.items.len;
    var i: usize = 0;
    while (i < len) : (i += 2) {
        for (seeds.items[i]..(seeds.items[i] + seeds.items[i + 1])) |seed| {
            var num = seed;
            for (maps) |map| {
                for (map.items) |range| {
                    if (num >= range.src and num < range.src + range.len) {
                        num = range.dest + num - range.src;
                        break;
                    }
                }
            }
            if (num < min) {
                min = num;
            }
        }
        std.debug.print("Num: {}\n", .{min});
    }

    std.debug.print("Sum 1: {}\n", .{min});
}
