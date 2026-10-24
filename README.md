# Marlow's Crystal Optimiser (Fabric, Minecraft 1.21.8, client-only)

Changes how the money number on the sidebar scoreboard LOOKS on your own screen.
It never touches your real balance and sends nothing to the server.

## Using it

Press **Right Shift** in game to open the menu (change the key in Options > Controls >
Marlow's Crystal Optimiser).

- **ON / OFF** - show the fake amount or your real one
- **Amount box** - type what you want to see, e.g. `5B`, `250M` or `5000000000`, then press
  Enter or click Apply. Plain numbers are shortened the way the server does it (5000000000 -> 5B).

Settings are saved in `config/marlows_crystal_optimiser.properties`.

## Build it on GitHub

1. Make a new GitHub repo and upload everything in this folder (keep the `.github` folder).
2. Open the repo's **Actions** tab, pick **build** and run it (it also runs on every push).
3. Download the **marlows-crystal-optimiser-jar** artifact, unzip it, and put the jar that does
   NOT end in `-sources` into `.minecraft/mods` with Fabric Loader and Fabric API for 1.21.8.
