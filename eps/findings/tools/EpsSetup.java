// Ghidra headless setup for EPS classic recon.
// Adds OS 2.49 RAM segments (per boot ROM scatter map), seeds known entry
// points, analyzes, then exports a function list and full decompilation.
// args: <os249.bin path> <output dir>
import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileResults;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.mem.MemoryBlock;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class EpsSetup extends GhidraScript {

    private void addBlock(String name, long addr, byte[] data) throws Exception {
        MemoryBlock b = currentProgram.getMemory().createInitializedBlock(
                name, toAddr(addr), new ByteArrayInputStream(data), data.length,
                monitor, false);
        b.setRead(true); b.setWrite(true); b.setExecute(true);
    }

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        byte[] os = Files.readAllBytes(Paths.get(args[0]));
        String outDir = args[1];

        addBlock("os_vectors", 0x000000, Arrays.copyOfRange(os, 0x00000, 0x00200));
        addBlock("os_kcfg",    0x000200, Arrays.copyOfRange(os, 0x14C00, 0x14C28));
        currentProgram.getMemory().createUninitializedBlock(
                "kernel_vars", toAddr(0x228), 0x1600 - 0x228, false);
        addBlock("os_low1",    0x001600, Arrays.copyOfRange(os, 0x14000, 0x14C00));
        addBlock("os_low2",    0x002200, Arrays.copyOfRange(os, 0x00200, 0x06000));
        addBlock("os_hi",      0xFF8000, Arrays.copyOfRange(os, 0x06000, 0x0E000));
        // 68000 sign-extends word pointers (movea.w/jmp) to 0xFFFFxxxx; the
        // 24-bit bus masks that to 0xFFxxxx. Ghidra models 32-bit addresses,
        // so mirror upper RAM at the sign-extended alias too.
        addBlock("os_hi_mirror", 0xFFFF8000L, Arrays.copyOfRange(os, 0x06000, 0x0E000));

        long[][] seeds = {
            {0xC0BE38L}, {0xC0729CL}, {0xC0B55CL}, {0xC0BFEEL}, {0xC0C132L},
            {0x171EL}, {0xFFAC14L}, {0x583AL}, {0xFFB1D0L},
            {0xFF85CEL}, {0xFF964AL},
            {0xC07686L}, {0xC0737AL}, {0xC0746CL}, {0xC0747EL}, {0xC074A8L},
            {0xC074BCL}, {0xC074DEL}, {0xC07510L}, {0xC07536L}, {0xC07544L},
            {0xFF832AL}, {0xFF8326L}, {0xC07580L}, {0xC075FCL}, {0xC075ECL},
            {0xC0745CL}, {0xC0937CL}, {0xC076D4L},
        };
        String[] names = {
            "ROM_reset", "ROM_kernel_start", "ROM_read_block", "ROM_os_loader",
            "ROM_boot_error_dispatch",
            "TASK1_171e", "TASK2_ac14", "TASK3_583a", "TASK4_b1d0",
            "OS_init_hook1", "OS_init_hook2",
            "SYS_trap0", "SYS_trap1", "SYS_trap2", "SYS_trap3", "SYS_trap4",
            "SYS_trap5", "SYS_trap6_wait", "SYS_trap7", "SYS_trap8_sleep",
            "SYS_trap9_send",
            "SYS_trap10_os", "SYS_trap11_os", "SYS_trap12", "SYS_trap13",
            "SYS_trap14", "SYS_trap15", "IRQ1_duart", "IRQ_spurious",
        };
        for (int i = 0; i < seeds.length; i++) {
            Address a = toAddr(seeds[i][0]);
            disassemble(a);
            if (getFunctionAt(a) == null) createFunction(a, names[i]);
            else getFunctionAt(a).setName(names[i],
                    ghidra.program.model.symbol.SourceType.USER_DEFINED);
        }

        analyzeAll(currentProgram);

        PrintWriter fl = new PrintWriter(outDir + "/functions.txt");
        FunctionIterator it = currentProgram.getFunctionManager()
                .getFunctions(true);
        int count = 0;
        while (it.hasNext()) {
            Function f = it.next();
            fl.printf("%08x %6d %s%n", f.getEntryPoint().getOffset(),
                    f.getBody().getNumAddresses(), f.getName());
            count++;
        }
        fl.close();
        println("functions: " + count);

        DecompInterface dec = new DecompInterface();
        dec.openProgram(currentProgram);
        PrintWriter out = new PrintWriter(outDir + "/decompiled.c");
        it = currentProgram.getFunctionManager().getFunctions(true);
        while (it.hasNext()) {
            Function f = it.next();
            monitor.checkCancelled();
            DecompileResults r = dec.decompileFunction(f, 60, monitor);
            out.println("// ===== " + f.getName() + " @ "
                    + f.getEntryPoint() + " =====");
            if (r != null && r.decompileCompleted()
                    && r.getDecompiledFunction() != null) {
                out.println(r.getDecompiledFunction().getC());
            } else {
                out.println("// decompile failed");
            }
        }
        out.close();
        dec.dispose();
        println("export done -> " + outDir);
    }
}
