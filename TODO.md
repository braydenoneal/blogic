## Todo

- store the edit box cursor position
- study how codecs work
- codec for converting kotlin nullables to java optionals (scope, elseIfStatement, ifStatement)
- controlled order of execution for multiple controllers in a network?
- store text edit draft (option to save text editing progress without activating the changes)
- fix double click selection issue caused by line number gutter
- ide extension or just mirroring files to allow editing code externally
- option to run imports from either context/location
- isChunkLoaded function
- if switch to read, import, export block for functions, then have network priority option to control order
- tab, shift-tab, auto-indent, auto open and close
- tab autocomplete
- add block for import?, access modifiers
- address while loop max iterations issue
- balancing: breaking blocks requires tool, and takes time

## Ideas

- balancing: ability to upgrade:
    - reach
    - number of actions per tick
    - types of actions it can do
    - number of items it can move per tick
    - speed of the actions
    - upgrades and code are stored in the controller when broken
    - upgrades and code can be taken out of the controller and stored as items
- portable controller that executes from player
- wait/sleep function to control tick time (prob requires multithreading)
- reader, importer, and exporter blocks instead of available everywhere?
- time/amount/too easy balancing
- maybe action functions (read, import, export) each take 1 tick to execute
- read entities like commands (@n, etc)
- storage and auto-crafting features like ae2
- storage blocks like drawers, trash, etc
- chunk loaders
- mob farm features like apothic spawners and mob grind utils
- redstone blocks like switches and indicators
- display blocks for text, items, pixels?, etc
- inventory management features like sophisticated backpacks
- building features like building gadgets
- wireless imports/exports
- fluids and energy
- xp pickup
- ways to control logic with keybinds (create-mod gamepad thingy)

## Farms

- stone/cobblestone
- moss/azalea
- dirt/oak
- all trees
- sugar cane
- bamboo
- dripstone
- lava
- piglin barter
- smart furnace array
- basic item flow control
- overflow
- advanced filtering with nbt, tags, etc
- easy item storage sorting

## Read

- redstone
- inventory
- block
- fluid
- world
- network

## Import

- inventory
- block
- fluid
- item
- energy

## Export

- redstone
- inventory
- block
- fluid
- item
- energy
- interaction
