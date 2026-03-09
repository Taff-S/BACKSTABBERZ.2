# BACKSTABBERZ.2 - Changelog

## Uncommitted Changes (Since Last Pull)

This document summarizes all modifications, additions, and changes made to the codebase since the last git pull from origin/main.

### 📊 Summary Statistics
- **9 files modified**
- **105 lines added**
- **22 lines removed**
- **2 new directories added** (untracked)

---

## 🔧 Modified Files

### 1. `run_game.bat` (6 changes: +0, -6)
**Changes:** Fixed command execution paths for starting server and clients
- **Before:** Used `/d` flag with `start` command
- **After:** Changed to use `cmd /k "cd /d ... && java ..."` format
- **Impact:** Improved batch file execution reliability

### 2. `src/main/java/me/taff_s/game/enemies/Enemy.java` (57 changes: +42, -15)
**Major Changes:**
- **Added Timed Effects System:** New `TimedEffect` interface integration
- **Enhanced Armor System:** Removed overflow damage when armor breaks
- **Added Concussion Mechanics:** Enemies can now be concussed and miss turns
- **Improved Effect Management:** Added `List<TimedEffect>` and effect processing

**Key Modifications:**
- Added imports for `List`, `ArrayList`, `Iterator`, and `TimedEffect`
- Added `timedEffects` list and `concussed` boolean field
- Modified `isHit()` method: Armor now completely absorbs damage when breaking (no overflow)
- Enhanced `reduceAttack()`, `reduceDefence()` methods to accept duration parameters
- Added `applyConcussion()` with proper timed effect implementation
- Simplified `reduceArmour()` to remove duration parameter
- Added `onTurnStart()` method for effect processing
- Added `isConcussed()` method for combat logic

### 3. `src/main/java/me/taff_s/game/combat/CombatSystem.java` (31 changes: +26, -5)
**Major Changes:**
- **Added Turn Management System:** New turn counter and turn start notifications
- **Enhanced Weapon Effects:** Updated weapon special effects with proper durations

**Key Modifications:**
- Added `turnCounter` static field and getter
- Added `startNewTurn()` and `notifyTurnStart()` methods
- Updated weapon effects to use duration parameters:
  - Sword: `reduceAttack(5, 3)` instead of `reduceAttack(5)`
  - Axe: `reduceDefence(5, 3)` instead of `reduceDefence(5)`
  - Hammer: `reduceArmour(10)` instead of `reduceArmour(10, 3)`
- Added comments for future bow implementation
- Improved event messages for armor reduction

### 4. `src/main/java/me/taff_s/game/combat/CombatEncounter.java` (18 changes: +18, -0)
**Changes:** Added turn management integration
- Added call to `CombatSystem.startNewTurn()` at the beginning of each combat turn
- **Impact:** Enables proper turn-based effect processing for all combatants

### 5. `src/main/java/me/taff_s/game/enemies/EnemyFactory.java` (2 changes: +2, -0)
**Changes:** Minor import additions
- Added imports for `TimedEffect` and related classes
- **Impact:** Enables factory to work with enhanced enemy effects

### 6. `src/main/java/me/taff_s/game/player/Inventory.java` (4 changes: +2, -2)
**Changes:** Minor modifications (specific details not shown in diff)

### 7. `src/main/java/me/taff_s/game/player/Player.java` (4 changes: +2, -2)
**Changes:** Minor modifications (specific details not shown in diff)

### 8. `src/main/java/me/taff_s/game/player/PlayerHandler.java` (3 changes: +1, -2)
**Changes:** Minor modifications (specific details not shown in diff)

### 9. `src/main/java/me/taff_s/game/world/RoomManager.java` (2 changes: +2, -0)
**Changes:** Minor modifications (specific details not shown in diff)

---

## 📁 New Additions

### 1. `src/main/java/me/taff_s/game/effects/` Directory
**New Files:**
- `TimedEffect.java` - Interface for timed combat effects
- `TimedEffect.class` - Compiled class file

**Purpose:** Foundation for implementing temporary effects like concussion, stat reductions, etc.

### 2. `../scripts/` Directory (Outside Workspace)
**Status:** Untracked directory added outside the main src folder
**Note:** Contents not accessible from current workspace context

---

## 🎯 Key Feature Changes

### Armor System Overhaul
- **Breaking Change:** Enemy armor no longer allows overflow damage
- **Before:** Damage exceeding armor health would carry over to enemy HP
- **After:** Armor completely absorbs all damage until broken, then breaks with no HP damage
- **Impact:** Armored enemies are now significantly more tanky

### Timed Effects Framework
- **New System:** Enemies can now have temporary effects (concussion, stat debuffs)
- **Implementation:** Uses `TimedEffect` interface with turn-based expiration
- **Current Effects:** Concussion (causes enemies to potentially miss turns)

### Combat Turn Management
- **New Feature:** Proper turn counter and turn start notifications
- **Benefits:** Enables effect processing, future turn-based mechanics
- **Integration:** All combatants (players and enemies) now receive turn start events

### Weapon Effect Improvements
- **Duration-Based Effects:** Sword and axe effects now have proper durations
- **Simplified Hammer:** Armor reduction is now permanent rather than temporary
- **Future-Ready:** Comments added for bow weapon implementation

---

## 🔄 Migration Notes

### For Players
- **Combat Balance:** Armored enemies are now much harder to damage until armor is broken
- **New Mechanics:** Enemies can be concussed and miss turns
- **Weapon Changes:** Hammer weapon permanently reduces armor instead of temporarily

### For Developers
- **New Dependencies:** `TimedEffect` interface available for custom effects
- **Turn System:** Use `CombatSystem.startNewTurn()` for proper effect processing
- **Effect Creation:** Implement `TimedEffect` interface for custom timed abilities

---

## 🐛 Bug Fixes & Improvements

1. **Batch File Execution:** Fixed server and client startup commands
2. **Effect Persistence:** Added proper effect management and cleanup
3. **Turn Synchronization:** Combat encounters now properly notify all participants of turn starts
4. **Armor Logic:** Eliminated unintended overflow damage mechanic

---

*Generated on: March 9, 2026*
*Status: Uncommitted changes pending review and commit*