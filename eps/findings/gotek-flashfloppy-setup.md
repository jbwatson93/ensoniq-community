# Gotek and FlashFloppy setup on the original EPS

**Finding:** an original EPS boots stock OS 2.49, loads sound disks and boots a
custom OS from a Gotek running FlashFloppy with the configuration below. The
`chgrst = delay-3` line is what cleared a DISK DRIVE NOT READY failure when
loading sounds.

**Tested on:** original EPS, stock OS 2.49 and a custom disk OS, through
2026-10-03. The Gotek hardware revision and FlashFloppy version were not
recorded.

## Configuration

`FF.CFG` on the USB stick:

```
interface = shugart
host = ensoniq
chgrst = delay-3
```

Gotek jumpers: S0 only, JA not fitted.

## What was observed

- Before `chgrst` was set, loading sounds under the stock OS failed with DISK
  DRIVE NOT READY. With `chgrst = delay-3` added, a sound bank and its stored
  sequence loaded and played.
- Both image types worked: the stock OS booted from an HFE image
  (`DSKA0000.hfe`), and sound disks and the custom OS from raw 800 KiB (819200
  byte) IMG files (`DSKA0003.img` and up).
- One of 18 downloaded sound disks was confirmed loading and playing on
  hardware; the rest were only checked for readability with EpsLin Neo. See the
  [sound disk index](../sounds/archive-org/README.md).

## Not established

Which FlashFloppy versions need the `chgrst` setting, whether other
disk-change options also work, and whether the same configuration suits the
EPS-16 Plus or ASR-10.

## References

- [FlashFloppy host platforms](https://github.com/keirf/flashfloppy/wiki/Host-Platforms), which covers Ensoniq hosts.
- [FlashFloppy issue 137](https://github.com/keirf/flashfloppy/issues/137), on EPS disk-change behaviour.
