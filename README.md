# BACKSTABBERZ.2
# BACKSTABBERZ.2

A two-player cooperative (and competitive) dungeon crawler played over a network, built in Java. Two players navigate a dungeon under the command of a Golden Dragon, battling enemies, haggling with shopkeepers, and betraying each other when the time is right.

---

## Premise

You and a partner have been summoned by the Golden Dragon to prove your worth. You'll run through his dungeon three times, collecting as much gold as possible. At the end, whoever brings back the most gold wins the Dragon's favour. Survive all three runs and you may even challenge the Dragon himself.

---

## How to Run

### Requirements
- Java 11+
- Maven (optional — the project includes a batch script for manual compilation)

### Quick Start (Windows)

Run `run_game.bat` from the project root. This script will:
1. Compile all source files into `bin/`
2. Launch the game server in a new terminal window
3. Open two client terminal windows automatically

```
run_game.bat
```

Each client connects to `localhost:5000`. Enter your name when prompted, then press `1` when ready. The game starts once both players are ready.

### Manual Compilation

```bash
javac -d bin -sourcepath src/main/java src/main/java/me/taff_s/game/**/*.java
java -cp bin me.taff_s.game.net.GameServer
java -cp bin me.taff_s.game.net.GameClient   # run twice in separate terminals
```

---

## Gameplay

### Room Structure

Each run consists of 13 rooms in a fixed sequence:

| Room | Type |
|------|------|
| 1–2 | Combat |
| 3 | Encounter |
| 4 | Rest |
| 5 | Combat |
| 6 | Encounter |
| 7 | Combat |
| 8 | Rest |
| 9 | Encounter |
| 10–11 | Combat |
| 12 | Rest |
| 13 | Boss (The Golden Dragon) |

### Combat

Both players act simultaneously each turn. Choose from:
- `1` — Attack the enemy
- `2` — Defend (blocks damage; perfect blocks are limited)

Enemies have varied weaknesses and resistances based on damage type (Slash, Force, Pierce). If a player dies mid-dungeon, the Dragon teleports them back but takes half their remaining coins as tribute.

### Encounters

Encounter rooms are randomly selected from:

- **Shop** — Buy potions, weapons, or armour with gold
- **Blood Machine** — Trade HP for coins or coins for HP
- **Mimic** — A prisoner's dilemma: reach for the chest or hold back? Outcomes depend on what both players choose
- **Blacksmith** — *(Coming soon)* Bid on high-value gear
- **Vampire Shop** — *(Coming soon)* Buy items with HP instead of gold
- **Vampire Queen** — *(Coming soon)* One player is taken hostage; the other decides their fate
- **Treasure** — *(Coming soon)* Players gain an item that they have the option to fight each other for

### Rest Rooms

Both players choose simultaneously:
- `1` — Rest peacefully (+7 HP)
- `2` — Rest warily (+3 HP; can defend if backstabbed)
- `3` — Open inventory
- `4` — Betray your partner (initiates a PvP backstab encounter)

Backstab outcomes depend on both players' choices — betray a wary partner and they'll be ready for you.

### Gold & Scoring

After each run, 75% of each player's coins are banked as tribute to the Dragon. After all three runs, banked totals are compared and the player with the most gold wins the Dragon's favour.

---

## Items

### Weapons

Weapons belong to a class that determines special effects on hit:

| Class | Effect |
|-------|--------|
| Sword | 20% chance to reduce enemy attack |
| Axe | 20% chance to reduce enemy defence |
| Blunt | 15% chance to concuss enemy (may skip turn) |
| Hammer | 15% chance to reduce enemy armour |
| Spear | Pierce-type damage |
| Bow | Bonus damage on the first turn of combat |
| Scythe | Life steal (heals attacker for ¼ of damage dealt) |

All weapons have durability. Broken weapons deal no damage until unequipped.

### Armour

Armour reduces incoming damage. Higher defence values mean a greater percentage reduction, capped at 80%.

### Potions

Potions can be used from the inventory during rest rooms:

- **Healing Potions** — Lesser / Standard / Greater (3 / 7 / 12 HP)
- **Strength Potions** — Lesser / Standard / Greater (temporary +1/+2/+3 damage for 5 turns)

### Charms

Charms are equippable trinkets with passive or triggered effects. Players have two charm slots. Charms are awarded by the Dragon after the first and second runs.

| Charm | Effect |
|-------|--------|
| The Fang | +1 strength permanently |
| The Rock | Reduces incoming damage by 5% |
| The Needle | Steals 3 HP from a peacefully resting partner |
| The Backstabber | Instantly kills partner during backstab, even if wary |
| The Shield | Grants one additional perfect block in combat |
| The Feather | Heals +2 HP during any rest |

---

## Enemies

| Enemy | Notes |
|-------|-------|
| Goblin | Standard / Armoured / Sword variants |
| Skeleton | Weak to Force damage; Armoured variant has heavy DR |
| Slime | Resistant to Force and Pierce; Acid and Stone variants |
| Kobold | Weak to Slash; Flying variant weak to Pierce; Spear variant hits hard |
| Thief | Fast and low HP |
| Dragon | Boss. High HP, hits hard. Defeat him to win. |

---

## Project Structure

```
src/main/java/me/taff_s/game/
├── core/           # Game loop, event system, narrator, game manager
├── net/            # Server, client, message routing
├── player/         # Player, inventory, equipment, handler, messenger
├── combat/         # Combat system, encounter logic, backstab
├── enemies/        # Enemy base class, factory, spawn weights
│   └── types/      # All enemy variants
├── items/
│   ├── weapons/    # Weapon class, library, damage types
│   ├── armour/     # Armour class and library
│   ├── potions/    # Potion system with decorator pattern
│   └── charms/     # Charm library and individual charms
├── world/          # Room manager, encounter types, all encounter classes
└── effects/        # Timed effect interface
```

---

## Architecture Notes

- **Networking** — Client/server model over TCP sockets. Each player connects as a client; the server manages all game state.
- **Concurrency** — Player inputs are collected simultaneously using `ExecutorService` and `Future`, so neither player waits on the other to act before their input is read.
- **Event System** — A simple observer pattern (`GameEventManager`, `GameEventObserver`) broadcasts coin changes and banking events to listeners like the `GameLogger`.
- **Potion Effects** — Built with a decorator pattern (`EffectDecorator`) allowing effects to be stacked and composed.
- **Enemy Variants** — Managed through `EnemyFactory` with a registry of named variants and weighted random spawning.


---

## License

Personal project. Not for redistribution.
