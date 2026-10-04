# EPSFX classic EPS emulator research

**Applies to:** original Ensoniq EPS; EPSFX custom-OS project. Snapshot prepared
2026-10-04 from project results through 2026-10-03.

These are setup and validation notes, not an included emulator distribution.
Hardware and ROM findings are in [eps/findings](../../findings/README.md).

## Boot injection

Low-address writes can be affected by the current CPU function-code/overlay
selection. The project's injector writes through `FF0000` and verifies readback.
MAME floppy boot remains incomplete in this test setup: injected boot success
does not validate the physical floppy-loading path. Gotek hardware boot is a
separate, successful observation.

## Emulator evidence and limits

The v2 harness passed 315 simulated seconds with lead/min/max 1024 and a
checkpoint of 3,844,543 input reads. Maximum input-read interval was 115.20 us
(steady-state maximum 108.60 us), versus the ideal 80 us. Coexistence regression
also passed. EpsLin extraction matched the generated EFE byte for byte.

Classic ADC, sample RAM and DOC consumption are synthetic in this harness.
The consumer is independently timed; real DUART timer/FIFO handling is exercised.
Physical bus wait states, analog fidelity and exact panel behavior are not fully
modeled. A five-minute emulator pass cannot substitute for hardware acceptance.
Run one MAME instance at a time in this project's test setup.

## Harness provenance

The boot injector is included as [`mame-inject.py`](../tools/mame-inject.py);
it works with any EPS OS exported as an EFE, including stock OS 2.49. The
EPSFX-specific harnesses and logs (`tools/mame_thru_test.lua`,
`tools/mame_coexist_test.lua`, `work/mame_thru_v2/long.log` and
`work/mame_thru_v2/regression.log` in the source project) are not bundled in
this repository. MAME 0.289 was installed on the development machine on
2026-10-04; the revision used for each logged run and the exact command lines
were not recorded, so this is not yet a standalone setup guide.
