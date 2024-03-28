const std = @import("std");
const input = @embedFile("input.txt");
// const input = @embedFile("test.txt");

pub fn main() !void {
    try part1();
    try part2();
}

const HandBid = struct {
    hand: [5]u8,
    bid: usize,
    strength: u8,
    pub fn create(hand: [5]u8, bid: usize, joker: bool) HandBid {
        var counts: [15]u8 = undefined;
        @memset(&counts, 0);
        var max: u8 = 0;
        var max_i: usize = 0;
        var second: u8 = 0;
        var second_i: usize = 0;
        for (hand) |c| {
            counts[c] += 1;
        }
        for (counts, 0..) |c, i| {
            if (c > max) {
                second = max;
                second_i = max_i;
                max = c;
                max_i = i;
            } else if (c > second) {
                second = c;
                second_i = i;
            }
        }
        if (joker) {
            if (max_i == 1) {
                max += second;
            } else {
                max += counts[1];
            }
        }
        const strength: u8 = switch (max) {
            5 => 7,
            4 => 6,
            3 => if (second == 2) 5 else 4,
            2 => if (second == 2) 3 else 2,
            else => 1,
        };
        return .{ .hand = hand, .bid = bid, .strength = strength };
    }
};

fn cmpByHandBid(_: void, a: HandBid, b: HandBid) bool {
    if (a.strength < b.strength) {
        return true;
    } else if (a.strength > b.strength) {
        return false;
    } else {
        return for (a.hand, b.hand) |c1, c2| {
            if (c1 < c2) break true else if (c1 > c2) break false;
        } else false;
    }
}

fn part1() !void {
    var total: usize = 0;
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var hands = std.ArrayList(HandBid).init(alloc);
    defer hands.deinit();

    var it = std.mem.tokenize(u8, input, "\n ");
    while (it.next()) |str| {
        var hand: [5]u8 = undefined;
        for (str, 0..) |ch, i| {
            switch (ch) {
                'A' => hand[i] = 14,
                'K' => hand[i] = 13,
                'Q' => hand[i] = 12,
                'J' => hand[i] = 11,
                'T' => hand[i] = 10,
                else => hand[i] = ch - '0',
            }
        }
        // std.debug.print("Hand: {any}\n", .{hand});
        var bid = try std.fmt.parseInt(usize, it.next().?, 10);
        try hands.append(HandBid.create(hand, bid, false));
    }
    // std.debug.print("Hands: {any}\n", .{hands});
    var hands_sorted = try hands.toOwnedSlice();
    std.mem.sort(HandBid, hands_sorted, {}, cmpByHandBid);
    // std.debug.print("Hands sorted: {any}\n", .{hands_sorted});
    for (hands_sorted, 1..) |hand, i| {
        total += hand.bid * i;
    }
    alloc.free(hands_sorted);
    std.debug.print("Result 1: {}\n", .{total});
}

fn part2() !void {
    var total: usize = 0;
    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    var alloc = gpa.allocator();
    var hands = std.ArrayList(HandBid).init(alloc);
    defer hands.deinit();

    var it = std.mem.tokenize(u8, input, "\n ");
    while (it.next()) |str| {
        var hand: [5]u8 = undefined;
        for (str, 0..) |ch, i| {
            switch (ch) {
                'A' => hand[i] = 14,
                'K' => hand[i] = 13,
                'Q' => hand[i] = 12,
                'J' => hand[i] = 1,
                'T' => hand[i] = 10,
                else => hand[i] = ch - '0',
            }
        }
        // std.debug.print("Hand: {any}\n", .{hand});
        var bid = try std.fmt.parseInt(usize, it.next().?, 10);
        try hands.append(HandBid.create(hand, bid, true));
    }
    // std.debug.print("Hands: {any}\n", .{hands});
    var hands_sorted = try hands.toOwnedSlice();
    std.mem.sort(HandBid, hands_sorted, {}, cmpByHandBid);
    // std.debug.print("Hands sorted: {any}\n", .{hands_sorted});
    for (hands_sorted, 1..) |hand, i| {
        total += hand.bid * i;
    }
    alloc.free(hands_sorted);
    std.debug.print("Result 2: {}\n", .{total});
}
