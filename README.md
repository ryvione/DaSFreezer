# DaSFreezer

A comprehensive player freezing plugin for PaperMC servers. DaSFreezer allows server staff to completely immobilize players, preventing all movement and interactions - perfect for moderation, investigations, and roleplay scenarios.

## Features

- Freeze players in place, preventing all movement and interactions
- Persistent freeze state across server restarts and player reconnects
- Visual feedback with titles and action bar messages
- Teleports frozen players back to their exact freeze location on rejoin
- Supports freezing offline players (they will be frozen when they log back in)
- Permission-based command system
- Lightweight and performant

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/freeze <player>` | Freeze a player | `dasfreezer.freeze` |
| `/unfreeze <player>` | Unfreeze a player | `dasfreezer.unfreeze` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `dasfreezer.freeze` | Allows freezing players | op |
| `dasfreezer.unfreeze` | Allows unfreezing players | op |

## What Gets Blocked When Frozen

When a player is frozen, the following actions are prevented:

- All movement (walking, running, jumping)
- Block breaking and placing
- Interacting with blocks (chests, doors, buttons, etc.)
- Interacting with entities (players, mobs, item frames, etc.)
- Dropping items
- Inventory interactions
- Attacking entities
- Any form of teleportation or position changes

## Installation

1. Download the latest release from Modrinth
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will generate a `frozen_players.yml` file in the `plugins/DaSFreezer` folder

## Requirements

- PaperMC 1.21.11 or newer
- Java 21 or newer

## How It Works

When a player is frozen:
- Their exact location is saved
- A large red "FROZEN" title appears on their screen
- They receive an action bar reminder every 2 seconds
- Any attempt to move teleports them back to the freeze location
- All interactions are blocked
- The freeze persists even if they disconnect and reconnect

When a player is unfrozen:
- All restrictions are removed
- Visual effects are cleared
- They can move and interact normally again

## Use Cases

- Staff investigations and moderation
- Preventing players from escaping during rule violations
- Roleplay scenarios requiring player restraint
- Holding players in place during events or trials
- Preventing movement during server maintenance or announcements

## Data Storage

Frozen player data is stored in `plugins/DaSFreezer/frozen_players.yml` and persists across server restarts. This ensures that frozen players remain frozen even if the server crashes or restarts unexpectedly.

## Support

If you encounter any issues or have feature requests, please open an issue on the GitHub repository.

## License

This plugin is released under the MIT License.
