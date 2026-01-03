# ESV Bible Plugin for Obsidian

Fetch Bible passages from the ESV API and insert them as Markdown. Works on both desktop and mobile.

## Installation

1. Copy `main.js` and `manifest.json` to your vault's `.obsidian/plugins/esv-bible/` folder
2. Enable the plugin in Obsidian Settings → Community plugins
3. Set your ESV API key in Settings → ESV Bible

## Getting an API Key

1. Go to https://api.esv.org/
2. Create an account and request an API key
3. Enter the key in the plugin settings

## Commands

- **Insert passage at cursor** - Prompts for a passage reference, inserts Markdown at cursor
- **Create new note with passage** - Creates a new note with the passage content

## Usage

1. Open command palette (`Ctrl+P` / `Cmd+P`)
2. Type "ESV" to find the commands
3. Enter a passage reference (e.g., "John 3:16", "Romans 8:28-39")
