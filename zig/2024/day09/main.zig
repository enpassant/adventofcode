const std = @import("std");
// const input = @embedFile("test.txt");
const input = @embedFile("input.txt");

pub fn main() !void {
    std.debug.print("\n\nStart\n", .{});
    try step1();
    try step2();
}

var blocks: [20000]usize = undefined;
var length: [20000]usize = undefined;
var free_length: [20000]usize = undefined;
var free: [20000]usize = undefined;
var count: usize = 0;
var block_start: usize = 0;
var block_id: usize = undefined;
var block_pos: usize = undefined;
var block_size: usize = undefined;
var free_pos: usize = 1;
var free_remaining: usize = 0;
var start_block_id: usize = 0;

fn step1() !void {
    block_id = (input.len) / 2 - 1;
    block_pos = if (block_pos % 2 == 0)
        input.len - 2
    else
        input.len - 3;
    block_size = input[block_pos] - '0';

    while (block_start < input.len - 1) {
        if (free_remaining == 0) {
            if (free_pos >= block_pos) {
                blocks[count] = block_id;
                length[count] = block_size;
                count += 1;
                break;
            }
            blocks[count] = start_block_id;
            length[count] = input[block_start] - '0';
            block_start += 2;
            count += 1;
            start_block_id += 1;

            free_remaining = input[free_pos] - '0';
        }

        if (free_pos <= block_pos) {
            while (free_remaining >= block_size) {
                blocks[count] = block_id;
                length[count] = block_size;
                count += 1;
                free_remaining -= block_size;
                block_id -= 1;
                block_pos -= 2;
                block_size = input[block_pos] - '0';
            }

            if (free_remaining > 0) {
                blocks[count] = block_id;
                length[count] = free_remaining;
                // std.debug.print("Blocks 3: {} {}\n", .{ blocks[count], length[count] });
                count += 1;
                block_size -= free_remaining;
                free_remaining = 0;
            }
        } else if (free_remaining > 0) {
            break;
        }
        free_pos += 2;

        if (block_pos < 2) {
            break;
        }
    }

    const total = calc_checksum();
    std.debug.print("Sum 1: {}\n", .{total});
}

fn calc_checksum() usize {
    var checksum: usize = 0;
    block_pos = 0;
    for (0..count) |i| {
        block_id = blocks[i];
        for (0..length[i]) |_| {
            checksum += block_id * block_pos;
            block_pos += 1;
        }
    }
    return checksum;
}

fn calc_checksum2() usize {
    var checksum: usize = 0;
    for (0..count) |i| {
        block_pos = blocks[i];
        for (0..length[i]) |_| {
            checksum += i * block_pos;
            block_pos += 1;
        }
    }
    return checksum;
}

fn step2() !void {
    var i: usize = 0;
    count = input.len - 1;
    block_id = 0;
    block_pos = 0;

    while (i < count) {
        blocks[block_id] = block_pos;
        length[block_id] = input[i] - '0';
        block_pos += length[block_id];
        i += 1;

        if (i < count) {
            free[block_id] = block_pos;
            free_length[block_id] = input[i] - '0';
            block_pos += free_length[block_id];
        } else {
            free[block_id] = 0;
            free_length[block_id] = 0;
        }

        i += 1;
        block_id += 1;
    }

    count = block_id;

    while (block_id > 0) {
        block_id -= 1;
        block_size = length[block_id];
        for (0..block_id) |j| {
            if (free_length[j] >= block_size) {
                blocks[block_id] = free[j];
                free_length[block_id - 1] += block_size;
                free_length[j] -= block_size;
                free[j] += block_size;
                break;
            }
        }
    }

    // 7000708849430 Too high
    const total = calc_checksum2();
    std.debug.print("Sum 2: {}\n", .{total});
}
