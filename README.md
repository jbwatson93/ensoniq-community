# ensoniq-community

A community knowledge base for Ensoniq sampler modding, reverse engineering, and emulation: hardware research, firmware findings, tools, and tested discoveries.

## Repository structure

```
ensoniq-community/
├── ensoniq/
│   ├── media/
│   └── cross-model-findings/
├── tools/
│   └── emulators/
├── mirage/
│   ├── docs/
│   ├── findings/
│   ├── roms/
│   ├── emulation/
│   ├── sounds/
│   └── os/
├── eps/
│   ├── docs/
│   ├── findings/
│   ├── roms/
│   ├── emulation/
│   ├── sounds/
│   └── os/
├── asr-10/
│   └── ...
└── ...
```

There are three kinds of top-level folder:

- **`ensoniq/`** for anything that isn't tied to one model.
- **`tools/`** for shared emulators and utilities used across models.
- **One folder per sampler model** for everything specific to that machine.

### `ensoniq/`

- **`media/`** holds general Ensoniq material: photos, videos, brochures, adverts, magazine scans, company history.
- **`cross-model-findings/`** holds reverse-engineered knowledge that applies to more than one model, such as the disk format shared by the EPS, EPS-16 Plus, and ASR-10. List the models it applies to at the top of the file.

### `tools/`

- **`emulators/`** holds shared emulator projects and general setup material that applies to several models. Model-specific setup guides, configs, patches, and test results belong in that model's `emulation/` folder; link to shared projects instead of duplicating them.
- Shared disk utilities, format converters, sample editors, and scripts each get their own folder directly under `tools/`, e.g. `tools/disk-image-converter/`.

### Model folders

One folder per model at the top level, named in lowercase with hyphens:

| Model       | Folder        |
| ----------- | ------------- |
| Mirage      | `mirage`      |
| EPS         | `eps`         |
| EPS-16 Plus | `eps-16-plus` |
| ASR-10      | `asr-10`      |
| ASR-88      | `asr-88`      |
| ASR-X       | `asr-x`       |
| ASR-X Pro   | `asr-x-pro`   |

If your model isn't listed, create its folder using the same naming style, with the same six subfolders, and add it to this table.

Each model folder's own `README.md` is its index: it lists where that model's ROMs, operating systems, manuals and sound disks can be downloaded. See [`eps/README.md`](eps/README.md) for an example.

Every model folder has the same six subfolders:

- **`docs/`** holds existing reference material: service manuals, schematics, datasheets, pinouts, format descriptions. Things that were published or written down somewhere else and are collected here, each with its source link and checksum.
- **`findings/`** holds original reverse-engineered knowledge about that model: firmware disassembly notes, file format breakdowns, SCSI and MIDI/SysEx behavior, hardware mods, repair discoveries. Things you worked out yourself.
- **`roms/`** holds ROM identification notes for that model: ROM version and chip position if known, checksums, and a link to an existing download source. ROM dumps themselves are not hosted here.
- **`emulation/`** holds model-specific emulator projects, setup guides, configs, patches, test harnesses, and results. Link to shared emulators under `tools/emulators/` where applicable.
- **`sounds/`** indexes sound disks and instrument files for that model: source links, checksums, formats and disk content listings. Sounds you made yourself can be uploaded directly.
- **`os/`** holds operating system versions for that model, in a subfolder per version, e.g. `asr-10/os/3.53/`. Put the disk image there along with its source link, checksums and any release notes.

Not sure whether something is a doc or a finding? If you figured it out, it's a finding. If a finding covers more than one model, it goes in `ensoniq/cross-model-findings/` instead.

## Contributing

1. Put your files in the right folder, e.g. `asr-10/findings/scsi-boot-sequence.md`.
2. Name files in lowercase with hyphens, and make the name describe the content (`disk-format-directory-blocks.md`, not `notes2.md`).
3. Write findings in Markdown where possible so they are readable and searchable on GitHub.

### Writing up a finding

A finding is most useful when someone else can reproduce it. Include:

- **What you found**, stated up front.
- **What you tested on**: model, OS/ROM version, and hardware revision or expansions if relevant.
- **How you verified it**, or a note that it is unverified or a guess.
- **Sources or tools** you used.

### Hosted files and links

Operating system images and manuals are hosted here. ROM dumps and other people's sound disks are linked, not uploaded. Either way, record where the file came from:

- Link to the page the file is offered on (an Archive.org item, the author's site) in preference to a bare file URL.
- Record the file name, size and a SHA-256 or SHA-1 checksum so others can confirm they have the same file, and the date you last checked the link.
- Say what you verified: that the download matches a file you tested with, or only that the link exists.
- Do not link to third-party commercial sound libraries that are still sold.

### Sharing sounds

- Sounds you made yourself are welcome as files. Include a short `README.md` or text file alongside saying what the sound is.
- For factory disks and anything made by someone else, add an index entry with a source link instead of the files.
- Keep individual files under 50 MB. For anything larger, host it elsewhere and add a link.

## Preservation and copyright

Ensoniq hardware has been out of production and unsupported for decades. This repository hosts original Ensoniq operating system images and reference manuals, and links to ROM dumps and sound disks that are already available elsewhere, so the people who still use, repair, and study these machines can keep them running. Every hosted or linked file is recorded with its source and checksum. Nothing here is sold, and none of it is claimed as our own.

If you hold the rights to something hosted or linked in this repository and want it removed, open an issue and it will be taken down.

### License scope

Any licenses adopted for this repository cover original community contributions
only, within their stated scope. Third-party manuals, schematics, ROM dumps,
operating systems, factory sounds, and other reference material retain their
existing copyrights and license terms. Hosting or linking to a file here does
not claim ownership of it or relicense it. Availability elsewhere, including Archive.org,
does not itself establish redistribution permission.

Contributors should identify the source, author or rights holder where known,
and any applicable license or permission. Do not apply a community license to
someone else's work without authorization. Original contribution licenses have
not yet been selected in this repository; this section does not itself grant a
license.
