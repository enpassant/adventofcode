const std = @import("std");
const input = @embedFile("input.txt");
// const input = @embedFile("test.txt");

// const win_count: usize = 5;
// const your_count: usize = 8;

const win_count: usize = 10;
const your_count: usize = 25;

pub fn main() !void {
    try part1();
    try part2();
}

fn load() !void {}

fn part1() !void {
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var map = std.AutoHashMap(usize, void).init(alloc);
    var total: usize = 0;
    var line_it = std.mem.tokenize(u8, input, "\n");
    while (line_it.next()) |line| {
        var match: usize = 0;
        var it = std.mem.tokenize(u8, line, "Card: |");
        _ = it.next();
        for (0..win_count) |_| {
            const num: usize = try std.fmt.parseInt(usize, it.next().?, 10);
            try map.put(num, {});
        }
        for (0..your_count) |_| {
            const num: usize = try std.fmt.parseInt(usize, it.next().?, 10);
            if (map.contains(num)) {
                match += 1;
            }
        }
        if (match > 0) {
            const score = std.math.pow(usize, 2, match - 1);
            // std.debug.print("score: {}\n", .{score});
            total += score;
        }
        map.clearAndFree();
    }
    std.debug.print("Sum 1: {}\n", .{total});
}

fn part2() !void {
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var map = std.AutoHashMap(usize, void).init(alloc);
    var cards = std.ArrayList(usize).init(alloc);
    var total: usize = 0;
    var card_num: usize = 0;
    var cards_count: usize = 0;
    var line_it = std.mem.tokenize(u8, input, "\n");
    while (line_it.next()) |line| {
        card_num = if (cards.items.len > 0) cards.items[0] else 1;
        if (cards_count > 0) {
            _ = cards.orderedRemove(0);
            cards_count -= 1;
        }
        var match: usize = 0;
        var it = std.mem.tokenize(u8, line, "Card: |");
        _ = it.next();
        for (0..win_count) |_| {
            const num: usize = try std.fmt.parseInt(usize, it.next().?, 10);
            try map.put(num, {});
        }
        for (0..your_count) |_| {
            const num: usize = try std.fmt.parseInt(usize, it.next().?, 10);
            if (map.contains(num)) {
                match += 1;
            }
        }
        if (match > 0) {
            if (cards_count < match) {
                try cards.resize(match);
                for (cards_count..match) |i| {
                    cards.items[i] = 1;
                }
                cards_count = match;
            }
            for (0..match) |i| {
                cards.items[i] += card_num;
            }
        }
        total += card_num;
        map.clearAndFree();
    }
    std.debug.print("Sum 2: {}\n", .{total});
}
