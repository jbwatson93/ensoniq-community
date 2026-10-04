#!/usr/bin/env python3
"""Quick m68k disassembly helper for EPS boot ROM / OS recon (capstone)."""
import sys
import capstone

BASE = 0xC00000  # boot ROM base in EPS address space

def disasm(data, base, start, count=60):
    md = capstone.Cs(capstone.CS_ARCH_M68K, capstone.CS_MODE_M68K_000)
    off = start - base
    for insn in md.disasm(bytes(data[off:off + 4096]), start):
        print(f"{insn.address:08x}: {insn.mnemonic:10s} {insn.op_str}")
        count -= 1
        if count == 0:
            break

if __name__ == "__main__":
    path, base, start = sys.argv[1], int(sys.argv[2], 16), int(sys.argv[3], 16)
    count = int(sys.argv[4]) if len(sys.argv) > 4 else 60
    data = open(path, "rb").read()
    disasm(data, base, start, count)
