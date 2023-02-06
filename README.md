# What's that?
Try to implement the usual functions of a classical 3D Jump'n run game to JMonkeyEngine.

# How to try it?
## Manual
Clone the repo local and add at least following libaries to the class path:

1) Java standard library (8+)
1) jme3-core (3.5.2)
1) jme3-desktop (3.5.2)
1) jme3-effects (3.5.2)
1) jme3-jogg (3.5.2)
1) jme3-lwjgl (3.5.2)
1) jme3-plugings (3.5.2)
1) jme3-terrain (3.5.2)
1) jme3-testdata (3.5.2)
1) Minie (Recommend version 6.2, needs additional Heart (8.1.0), sim-math (1.5.0) and slf4j-api (1.7.32))

## Grandle

Use the Gradle script to set up the project automatic. (Thanks to Stephen Gold)

## Precompiled

Here is a ready runnable jar version: https://mega.nz/file/aBpWgBiT#clNupyTkyjWE6xYAGeqC6H4dp8uGvEymO2ie8dMxqEM

Contains all required libraries but no source and is proberly not up-to-date.

## How to control

Current control input is

1) WASD - Move
1) Mouse - Move camera
1) Left mouse button - Jump and read sign
1) Right mouse button - Strike
1) Mouse wheel - Zoom
1) Tab - Console

## What if I falled througth the walls?

The collision detect works not perfect, there are some spots where it is possible to fall througth walls. In this case, open the console with the Tabulator key and execute this command:

SetSpatialTranslation player 0 2 0

Or any other coordiantes. Please note, the camera control is disabled while the console is visible.

## Where can I find the other console commands?

Please take a look on this page:

https://github.com/zt2mUTDm/JME3-JumpnRunTest/wiki/Console-commands

Please note, some commands are currently unstabled and some will may change or get removed in the future.
