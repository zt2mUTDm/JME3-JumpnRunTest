from java.lang import Object

import math
from collections import OrderedDict

from base import globals as glob

from base import input
from base import keycodes
from base import debug
from base import physics

from base.player import Player

from java.lang import Math

from com.jme3.math import Quaternion
from com.jme3.math import Vector3f
from com.jme3.math import FastMath

from online.money_daisuki.api.monkey.basegame.collections import SafeSet
from online.money_daisuki.api.monkey.basegame.math import FloatLinearInterpolation2D
from online.money_daisuki.api.monkey.basegame.cinematic import PythonMotionPathListener

MOVE_SPEED = 0.3

keyMasks = {
    "PlayerUp": 1,
    "PlayerRight": 2,
    "PlayerDown": 4,
    "PlayerLeft": 8,
    "PlayerStrike": 16,
    "PlayerActionA": 32,
    "PlayerActionB": 64,
    "PlayerActionC": 128
}
"""moveRotationAngles = {
    1:  0,                       # Up
    2:  -(math.pi / 2),          # Right
    3:  -(math.pi / 4),          # Up/Right
    4:  math.pi,                 # Down
    5:  math.pi,                 # Down/Up
    6:  math.pi + (math.pi / 4), # Down/Right
    7:  math.pi,                 # Down/Up/Right
    8:  math.pi / 2,             # Left
    9:  (math.pi / 4),           # Left/Up
    10: (math.pi / 2),           # Left/Right
    11: (math.pi / 4),           # Left/Up/Right
    12: math.pi - (math.pi / 4), # Left/Down
    13: math.pi - (math.pi / 4), # Left/Down/Up
    14: math.pi - (math.pi / 4), # Left/Down/Right
    15: math.pi - (math.pi / 4), # Left/Down/Up/Right
}"""
keyboardMoveVector = {
    1:  [0, 1],   # Up
    2:  [1, 0],   # Right
    3:  [0.7071067811865475, 0.7071067811865475],   # Up/Right
    4:  [0, -1],  # Down
    5:  [0, -1],  # Down/Up
    6:  [0.7071067811865475, -0.7071067811865475],  # Down/Right
    7:  [0.7071067811865475, -0.7071067811865475],  # Down/Up/Right
    8:  [-1, 0],  # Left
    9:  [-0.7071067811865475, 0.7071067811865475],  # Left/Up
    10: [-1, 0],  # Left/Right
    11: [-0.7071067811865475, 0.7071067811865475],  # Left/Up/Right
    12: [-0.7071067811865475, -0.7071067811865475], # Left/Down
    13: [-0.7071067811865475, -0.7071067811865475], # Left/Down/Up
    14: [-0.7071067811865475, -0.7071067811865475], # Left/Down/Right
    15: [-0.7071067811865475, -0.7071067811865475], # Left/Down/Up/Right
}

MOVEMENT_BRAKE_MIN_SPEED = 0.1

DEFAULT_JUMP_SPEED = 20.0
HIGH_JUMP_SPEED = 25.0
TRAMPOLINE_JUMP_SPEED = 45.0
DOUBLE_JUMP_SPEED = 15.0

class PlayerCollectableManager(object):
    def setCollectable(self, name, value):
        if value < 0:
            raise Exception("Cannot set count of collectable %s to a negative value (%s)" % (name, value))

        if not hasattr(self, "collectables"):
            if value == 0:
                return
            self.collectables = {}
        
        if value == 0:
            del self.collectables[name]

            if len(self.collectables) == 0:
                del self.collectables
        else:
            self.collectables[name] = value

    def changeCollectable(self, name, count):
        self.setCollectable(name, self.getCollectableCount(name) + count)

    def getCollectableCount(self, name):
        if not hasattr(self, "collectables"):
            return 0
        
        if name not in self.collectables:
            return 0

        return self.collectables[name]

    def save(self):
        if not hasattr(self, "collectables"):
            return None
        return self.collectables

    def load(self, collectables):
        if collectables is None:
            return

        self.collectables = collectables

class PlayerKeyboardInputHandler(object):
    def __init__(self):
        self.pressedButtons = 0
    
    def onBinaryInputEvent(self, mapping, pressed, tpf):
        if mapping == "Test":
            if pressed:
                from base import cam
                cam.activeCamera.setEnabled(False)
                cam.moveLinearTo(Vector3f(0, 0, 0), Quaternion(0, 1, 0, 0), 1)
            return

        if pressed:
            self._setPressedKey(mapping)
        else:
            self._unsetPressedKey(mapping)
    
    def _setPressedKey(self, mapping):
        global keyMasks
        mask = keyMasks[mapping]
        self.pressedButtons|= mask
    
    def _unsetPressedKey(self, mapping):
        global keyMasks
        mask = keyMasks[mapping]
        self.pressedButtons&= ~mask
    
    def isDirectionMovement(self):
        return self.pressedButtons & 15 != 0
    
    def isActionAKeyPressed(self):
        return self.pressedButtons & keyMasks["PlayerActionA"]
    
    def isActionBKeyPressed(self):
        return self.pressedButtons & keyMasks["PlayerActionB"]

    def isActionCKeyPressed(self):
        return self.pressedButtons & keyMasks["PlayerActionC"]
    
    #def getDirectionAngle(self):
    #   return moveRotationAngles[self.pressedButtons & 15]
    
    def getX(self):
        # Swapped x and y
        return keyboardMoveVector[self.pressedButtons & 15][1]
        
    def getY(self):
        # Swapped x and y and negated
        return -keyboardMoveVector[self.pressedButtons & 15][0]
    
    def registerForInputs(self):
        input.registerForBinaryInputEvent(self, "PlayerUp")
        input.registerForBinaryInputEvent(self, "PlayerRight")
        input.registerForBinaryInputEvent(self, "PlayerDown")
        input.registerForBinaryInputEvent(self, "PlayerLeft")
        
        input.registerForBinaryInputEvent(self, "PlayerActionA")
        input.registerForBinaryInputEvent(self, "PlayerActionB")
        input.registerForBinaryInputEvent(self, "PlayerActionC")


        input.registerForBinaryInputEvent(self, "Test")
    
    def unregisterForInputs(self):
        input.unregisterForBinaryInputEvent(self, "PlayerUp")
        input.unregisterForBinaryInputEvent(self, "PlayerRight")
        input.unregisterForBinaryInputEvent(self, "PlayerDown")
        input.unregisterForBinaryInputEvent(self, "PlayerLeft")
        
        input.unregisterForBinaryInputEvent(self, "PlayerActionA")
        input.unregisterForBinaryInputEvent(self, "PlayerActionB")
        input.unregisterForBinaryInputEvent(self, "PlayerActionC")


        input.unregisterForBinaryInputEvent(self, "Test")
        
class PlayerJoystickInputHandler(object):
    def __init__(self):
        self.x = 0
        self.y = 0
        self.pressedButtons = 0
    
    def onAnalogInputEvent(self, mapping, value, tpf):
        if mapping == "JoystickLeft":
            x = -value
        elif mapping == "JoystickRight":
            x = value
    
    def onBinaryInputEvent(self, mapping, pressed, tpf):
        if pressed:
            self._setPressedKey(mapping)
        else:
            self._unsetPressedKey(mapping)
    
    def _setPressedKey(self, mapping):
        if mapping == "JoystickLeft":
            x = -1
        elif mapping == "JoystickRight":
            x = 1
        else:
            global keyMasks
            mask = keyMasks[mapping]
            self.pressedButtons|= mask
    
    def _unsetPressedKey(self, mapping):
        if mapping == "JoystickLeft":
            x = 0
        elif mapping == "JoystickRight":
            x = 0
        else:
            global keyMasks
            mask = keyMasks[mapping]
            self.pressedButtons&= ~mask
    
    def isDirectionMovement(self):
        return self.x != 0 or self.y != 0
    
    def isActionAKeyPressed(self):
        return self.pressedButtons & keyMasks["PlayerActionA"]
    
    def isActionBKeyPressed(self):
        return self.pressedButtons & keyMasks["PlayerActionB"]
            
    def registerForInputs(self):
        input.addJoystickAxisMappings(glob.getInputManager().getJoysticks()[0].getAxes().get(1), "Joystick0AxisV")
        input.addJoystickAxisMappings(glob.getInputManager().getJoysticks()[0].getAxes().get(0), "Joystick0AxisH")
        
        input.registerForJoystickAxisInputEvents(self, "Joystick0AxisH")
        input.registerForJoystickAxisInputEvents(self, "Joystick0AxisV")
        
        input.addJoystickButtonMappings(glob.getInputManager().getJoysticks()[0].getButtons().get(0), "PlayerActionA")
        input.registerForJoystickButtonInputEvents(self, "PlayerActionA")
        
    def onJoystickButtonEvent(self, mapping, button, pressed):
        if pressed:
            self._setPressedKey(mapping)
        else:
            self._unsetPressedKey(mapping)
    
    def unregisterForInputs(self):
        pass # TODO
        
    def onJoystickAxisEvent(self, mapping, value):
        if mapping == "Joystick0AxisV":
            self.y = -value
        elif mapping == "Joystick0AxisH":
            self.x = -value
            
    def getX(self):
        return self.x
        
    def getY(self):
        return self.y

class PlayerGlobalManager(object):
    def __init__(self):
        super(PlayerGlobalManager, self).__init__()

        self.cucumbersCollected = 0

        self.canHighJump = True
        self.onTrampoline = False

        self.bottomCollideWithScene = False
        self.canDoubleJump = False

        self.tmpVec = Vector3f()
        self.tmpQuad = Quaternion()

        self.shootingMode = False

        self.inActivatablesTriggers = OrderedDict()

    def onTouchEnter(self, myName, otherForm, otherName):
        if otherName != "PlayerShape":
            if myName == "PlayerGround":
                self.bottomCollideWithScene = True
                if self.canHighJump and otherName == "HighJump":
                    self.onTrampoline = True
            elif myName == "PlayerShape":
                if otherName.startswith("Activatable-Trigger-"):
                    self.inActivatablesTriggers[otherName] = otherForm

    def onTouchLeave(self, myName, otherForm, otherName):
        if otherName != "PlayerShape":
            if myName == "PlayerGround":
                self.bottomCollideWithScene = False
                if otherName == "HighJump":
                    self.onTrampoline = False
            elif myName == "PlayerShape":
                if otherName.startswith("Activatable-Trigger-"):
                    if otherName in self.inActivatablesTriggers:
                        del self.inActivatablesTriggers[otherName]

    def onAnimationEvent(self, mapping, loop):
        pass

class PlayerState(object):
    def onUpdate(self, tpf):
        pass

    def register(self):
        pass

    def unregister(self):
        pass

    def onAnimationEvent(self, aniName, loop):
        pass

    def setInputHandler(self, handler):
        self.inputHandler = handler

    def setPlayer(self, player):
        self.player = player

    def setGlob(self, glob):
        self.playerGlob = glob

    def onTouchEnter(self, myName, otherForm, otherName):
        pass

    def onTouchLeave(self, myName, otherForm, otherName):
        pass

class StandState(PlayerState):
    def onUpdate(self, tpf):
        if (not self.player.isControlEnabled()):
            #print "No control"
            return
        elif self.inputHandler.isActionAKeyPressed():
            if len(self.playerGlob.inActivatablesTriggers) == 0:
                self.player.setState(JumpStartState())
            else:
                trigger = next(reversed(self.playerGlob.inActivatablesTriggers))
                self.playerGlob.inActivatablesTriggers[trigger].activateTrigger()
        elif self.inputHandler.isActionBKeyPressed():
            self.player.setState(StrikeState())
        elif self.inputHandler.isActionCKeyPressed():
            self.player.setState(AcionCKeyPressedState())
        elif self.inputHandler.isDirectionMovement():
            self.player.setState(WalkState())

    def register(self):
        self.player.setAnimationSpeed(1)
        self.player.playAnimation("Idle", True)
            
class WalkState(PlayerState):
    def onUpdate(self, tpf):
        if not self.player.isControlEnabled():
            self.player.setState(StandState())
            return
        if self.inputHandler.isActionAKeyPressed():
            if len(self.playerGlob.inActivatablesTriggers) == 0:
                self.player.setState(JumpStartState())
            else:
                trigger = next(reversed(self.playerGlob.inActivatablesTriggers))
                self.playerGlob.inActivatablesTriggers[trigger].activateTrigger()
            return
        elif self.inputHandler.isActionBKeyPressed():
            self.player.setState(StrikeWhileRunningState())
            return
        elif self.inputHandler.isActionCKeyPressed():
            self.player.setState(AcionCKeyPressedState())
            return

        self.player._updateMoveDirection(True, tpf)

        vecX = self.player.internalMovementVector.getX()
        vecY = self.player.internalMovementVector.getY()
        if not self.inputHandler.isDirectionMovement() and (vecX < MOVEMENT_BRAKE_MIN_SPEED and vecX > -MOVEMENT_BRAKE_MIN_SPEED and vecY < MOVEMENT_BRAKE_MIN_SPEED and vecY > -MOVEMENT_BRAKE_MIN_SPEED):
            self.player.internalMovementVector.setTranslation(0, 0)
            self.player.setMoveDirection(Vector3f.ZERO)
            
            self.player.setState(StandState())

    def register(self):
        self.player.playAnimation("Run", True)

class JumpStartState(PlayerState):
    def register(self):
        self.player.setAnimationSpeed(1)
        self.player.playAnimation("JumpStart", False)

    def onAnimationEvent(self, aniName, loop):
        if aniName == "JumpStart":
            self.player.setState(JumpMiddleState())

class JumpMiddleState(PlayerState):
    def onUpdate(self, tpf):
        vec = self.player.getVelocity(self.playerGlob.tmpVec)
        if vec.y == 0 and self.playerGlob.bottomCollideWithScene:
            self.player.setState(JumpEndState())
        elif vec.y < 0 and self.playerGlob.canDoubleJump and not self.isDoubleJumped and self.inputHandler.isActionAKeyPressed():
            self.player.setJumpSpeed(DOUBLE_JUMP_SPEED)
            self.player.jump()
            self.isDoubleJumped = True
            
        if self.player.inputHandler.isDirectionMovement() and self.player.isControlEnabled():
            self.player._updateMoveDirection(True, tpf, updateAnimationSpeed=False)
        else:
            self.player.stopMoving()
            self.player.internalMovementVector.setTranslation(0, 0)

    def register(self):
        self.player.setAnimationSpeed(1)
        self.isDoubleJumped = False

        self.player.playAnimation("JumpMiddle", True)

        if self.playerGlob.onTrampoline:
            self.player.setJumpSpeed(TRAMPOLINE_JUMP_SPEED)

        self.player.jump()

class JumpEndState(PlayerState):
    def onUpdate(self, tpf):
        if self.inputHandler.isActionAKeyPressed():
            if not self.player.topCollideWithScene:
                self.player.playAnimation("JumpStart", False)
                self.player.setState(JumpStartState())

    def register(self):
        self.player.setAnimationSpeed(1)
        self.player.playAnimation("JumpEnd", False)
        self.player.setJumpSpeed(DEFAULT_JUMP_SPEED)

    def onAnimationEvent(self, aniName, loop):
        if aniName == "JumpEnd":
            self.player.setState(WalkState())

class StrikeState(PlayerState):
    def register(self):
        self.player.playAnimation("Strike", False)

    def onAnimationEvent(self, aniName, loop):
        if aniName == "Strike":
            self.player.setState(StandState())

class StrikeWhileRunningState(PlayerState):
    def onUpdate(self, tpf):
        self.timer-= tpf
        if self.timer <= 0:
            self.player.setState(WalkState())
        else:
            self.player._doMoving(self.targetX, self.targetY, tpf)
    
    def register(self):
        angel = self.player.internalMovementVector.getAngle()

        self.targetX = FastMath.cos(angel)
        self.targetY = FastMath.sin(angel)

        self.player.setAnimationSpeed(1)
        self.player.playAnimation("StrikeWhileRunning", False)
        self.timer = 1.0

class AcionCKeyPressedState(PlayerState):
    def onUpdate(self, tpf):
        self.player._doMoving(0, 0, tpf)
        if not self.inputHandler.isActionCKeyPressed():
            self.player.setState(StandState())
        elif self.inputHandler.isActionAKeyPressed():
            self.player.setState(HighJumpState())

    def register(self):
        self.player.setAnimationSpeed(1)
        self.player.playAnimation("Dance", True)

    def onAnimationEvent(self, aniName, loop):
        if aniName == "JumpEnd":
            self.player.setState(StandState())

# Dublicate
class HighJumpState(PlayerState):
    def onUpdate(self, tpf):
        vec = self.player.getVelocity(self.playerGlob.tmpVec)
        if vec.y == 0 and self.playerGlob.bottomCollideWithScene:
            self.player.setState(JumpEndState())
            
        if self.player.inputHandler.isDirectionMovement() and self.player.isControlEnabled():
            self.player._updateMoveDirection(True, tpf)
        else:
            self.player.stopMoving()
            self.player.internalMovementVector.setTranslation(0, 0)

    def register(self):
        self.player.playAnimation("JumpMiddle", True)
        self.player.setJumpSpeed(HIGH_JUMP_SPEED)
        self.player.jump()

class WalkLinearToState(PlayerState):
    def __init__(self, vec):
        super(WalkLinearToState, self).__init__()
        self.vec = vec

    def onUpdate(self, tpf):
        vec = self.player.getVelocity(self.playerGlob.tmpVec)
        if vec.y == 0 and self.playerGlob.bottomCollideWithScene:
            self.player.setState(JumpEndState())
            
        if self.player.inputHandler.isDirectionMovement() and self.player.isControlEnabled():
            self.player._updateMoveDirection(True, tpf)
        else:
            self.player.stopMoving()
            self.player.internalMovementVector.setTranslation(0, 0)

    def register(self):
        self.player.playAnimation("JumpMiddle", True)
        self.player.setJumpSpeed(HIGH_JUMP_SPEED)
        self.player.jump()

class ThrowItemState(PlayerState):
    def __init__(self, motion):
        super(ThrowItemState, self).__init__()
        
        self.motion = motion
        
        self.animationDone = False
        self.movementDone = False

        self.state = "Ready"

    def onUpdate(self, tpf):
        if self.state == "Ready":
            if self.playerGlob.bottomCollideWithScene:
                self.player.playAnimation("Strike", False)
                self.state = "ThrowAniRunning"
        elif self.animationDone and self.movementDone:
            self.player.setState(StandState())
            if hasattr(self.player, "thrownListeners"):
                for listener in self.player.thrownListeners.getArray():
                    listener.onPlayerThrowDone()

    def register(self):
        self.player.setAnimationSpeed(1)

    def onAnimationEvent(self, aniName, loop):
        if self.state == "ThrowAniRunning":
            self.motion.getPath().addListener(PythonMotionPathListener(self))
            self.motion.play()
            self.player.playAnimation("StrikeWhileRunning", False)
            self.state = "Throwing"
        elif self.state == "Throwing":
            self.animationDone = True

    def onWaypointReach(self, motion, waypointIndex):
        if(waypointIndex == motion.getPath().getNbWayPoints() - 1):
            self.movementDone = True

class BasicPlayer(Player):
    def __init__(self):
        super(BasicPlayer, self).__init__()
        self.glob = PlayerGlobalManager()
        self.collectables = PlayerCollectableManager()

        self.state = None
        
        self.topCollideWithScene = False
        self.bottomCollideWithScene = False
        self.highJumpActive = False
        self.canHighJump = True
        self.shootingMode = False
        
        glob.player = self
    
    def onInit(self):
        input.registerKeyMapping("PlayerUp", keycodes.KEY_W)
        input.registerKeyMapping("PlayerRight", keycodes.KEY_D)
        input.registerKeyMapping("PlayerDown", keycodes.KEY_S)
        input.registerKeyMapping("PlayerLeft", keycodes.KEY_A)
        input.registerKeyMapping("PlayerActionC", keycodes.KEY_R)
        
        input.registerKeyMapping("Test", keycodes.KEY_F10)
        input.registerKeyMapping("SwitchToController", keycodes.KEY_F11)
        input.registerKeyMapping("SwitchToKeyboard", keycodes.KEY_F12)
        
        input.registerForBinaryInputEvent(self, "SwitchToController", "SwitchToKeyboard")
        
        input.registerMouseButtonMapping("PlayerActionA", keycodes.MOUSE_BUTTON_LEFT)
        input.registerMouseButtonMapping("PlayerActionB", keycodes.MOUSE_BUTTON_RIGHT)
        
        """input.registerForBinaryInputEvent(self.inputHandler, "PlayerUp")
        input.registerForBinaryInputEvent(self.inputHandler, "PlayerRight")
        input.registerForBinaryInputEvent(self.inputHandler, "PlayerDown")
        input.registerForBinaryInputEvent(self.inputHandler, "PlayerLeft")
        input.registerForBinaryInputEvent(self, "PlayerSpecialA")
        
        input.registerForBinaryInputEvent(self, "PlayerActionA")
        input.registerForBinaryInputEvent(self, "PlayerActionB")"""
                
        self.internalMovementVector = FloatLinearInterpolation2D(5)
        
        self.inputHandler = PlayerKeyboardInputHandler()
        self.inputHandler.registerForInputs()
        
        self.registerForAnimationEvents()
        
        self.addViewReceiver("PlayerGeom")
        
        physics.registerForTouchTest(self, "Top")
        physics.registerForTouchTest(self, "PlayerShape")
        physics.registerForTouchTest(self, "PlayerGround")

        self.setState(StandState())
    
    def requestLoad(self, data):
        persident = data["persistent"]
        if "cucumbersCollected" in persident:
            self.glob.cucumbersCollected = persident["cucumbersCollected"]
        
        if "collectables" in persident:
            self.collectables.load(persident["collectables"])

        if "canDoubleJump" in persident:
            self.glob.canDoubleJump = persident["canDoubleJump"]
        
        return True

    def beforeUnload(self):
        persident = {}

        if self.glob.cucumbersCollected > 0:
            persident["cucumbersCollected"] = self.glob.cucumbersCollected

        persident["canDoubleJump"] = self.glob.canDoubleJump

        col = self.collectables.save()
        if col is not None:
            persident["collectables"] = col

        return {
            "persistent": persident
        }

    def onBinaryInputEvent(self, mapping, pressed, tpf):
        if pressed:
            if mapping == "SwitchToController":
                self.inputHandler.unregisterForInputs()
                self.inputHandler = PlayerJoystickInputHandler()
                self.inputHandler.registerForInputs()
            elif mapping == "SwitchToKeyboard":
                self.inputHandler.unregisterForInputs()
                self.inputHandler = PlayerKeyboardInputHandler()
                self.inputHandler.registerForInputs()
    
    def onCleanup(self):
        input.unregisterMapping("PlayerUp")
        input.unregisterMapping("PlayerRight")
        input.unregisterMapping("PlayerDown")
        input.unregisterMapping("PlayerLeft")
        input.unregisterMapping("PlayerSpecialA")
        
        input.unregisterMapping("PlayerActionA")
        input.unregisterMapping("PlayerActionB")
        
        input.unregisterForBinaryInputEvent(self, "PlayerUp")
        input.unregisterForBinaryInputEvent(self, "PlayerRight")
        input.unregisterForBinaryInputEvent(self, "PlayerDown")
        input.unregisterForBinaryInputEvent(self, "PlayerLeft")
        input.unregisterForBinaryInputEvent(self, "PlayerSpecialA")
        
        input.unregisterForBinaryInputEvent(self, "PlayerActionA")
        input.unregisterForBinaryInputEvent(self, "PlayerActionB")
    
    def onUpdate(self, tpf):
        self.state.onUpdate(tpf)

    def _updateMoveDirection(self, updateFromControl, tpf, updateAnimationSpeed=True):
        global moveRotationAngles

        if not self.isControlEnabled():
            import pdb; pdb.set_trace()

        if self.isControlEnabled() and self.inputHandler.isDirectionMovement() and updateFromControl:
            targetX = self.inputHandler.getX()
            targetY = self.inputHandler.getY()
        else:
            targetX = 0
            targetY = 0
        
        self._doMoving(targetX, targetY, tpf)
        
        if updateAnimationSpeed:
            self.setAnimationSpeed(self.internalMovementVector.getDistance())

    def _doMoving(self, targetX, targetY, tpf):
        global moveRotationAngles
                
        self.internalMovementVector.translate(targetX, targetY, tpf)
        moveAngle = self.internalMovementVector.getAngle()
        
        q = self.glob.tmpQuad
        q.fromAngleNormalAxis(moveAngle, Vector3f.UNIT_Y)
        
        vec = self.glob.tmpVec
        
        cam = glob.getApp().getCamera()
        camDir = cam.getDirection()
        vec.set(camDir.getX(), 0, camDir.getZ())
        vec.normalizeLocal()
        
        q.multLocal(vec)
        
        self.setViewDirection(vec)
        
        moveSpeed = self.internalMovementVector.getDistance()
        
        vec.multLocal(moveSpeed * MOVE_SPEED)
        self.setMoveDirection(vec)
    
    def _handleMovingStop(self):
        vecX = self.internalMovementVector.getX()
        vecY = self.internalMovementVector.getY()
        if not self.inputHandler.isDirectionMovement() and (vecX < MOVEMENT_BRAKE_MIN_SPEED and vecX > -MOVEMENT_BRAKE_MIN_SPEED and vecY < MOVEMENT_BRAKE_MIN_SPEED and vecY > -MOVEMENT_BRAKE_MIN_SPEED):
            self.stopMovingImmediate()
            return True
        return False
    

    def setCollectable(self, name, value):
        self.collectables.setCollectable(name, value)

    def changeCollectable(self, name, count):
        self.collectables.changeCollectable(name, count)

    def getCollectableCount(self, name):
        return self.collectables.getCollectableCount(name)

    
    def throwObject(self, modelUrl, relativeTranslation, targetLocation, height, tension, additionalWaypoints=()):
        from online.money_daisuki.api.monkey.basegame.model import ModelLoadAppState
        from base import forms
        from com.jme3.scene import Node
        from com.jme3.cinematic import MotionPath
        from com.jme3.cinematic.events import MotionEvent

        modelLoader = glob.getAppState(ModelLoadAppState)

        model = modelLoader.loadModel(modelUrl)
        model.setLocalTranslation(relativeTranslation)

        initLocation = forms.getSpatialFromInstance(self).getWorldTranslation()

        # TODO use tmpvec
        addLen = len(additionalWaypoints)
        if addLen > 0:
            lastLocation = additionalWaypoints[addLen - 1]
        else:
            lastLocation = targetLocation

        tmpVec = Vector3f(lastLocation).subtractLocal(initLocation)
        tmpQuat = Quaternion().lookAt(tmpVec, Vector3f.UNIT_Y)

        modelNode = Node("ThrowingModelNode")
        modelNode.setLocalTranslation(initLocation)
        modelNode.setLocalRotation(tmpQuat)
        modelNode.attachChild(model)

        glob.getApp().getRootNode().attachChild(modelNode)

        path = MotionPath()
        middleLocation = Vector3f().interpolateLocal(initLocation, targetLocation, 0.5)
        middleLocation.y+= height

        path.addWayPoint(initLocation)
        path.addWayPoint(middleLocation)
        path.addWayPoint(targetLocation)

        for wp in additionalWaypoints:
            path.addWayPoint(wp)

        motion = MotionEvent(modelNode, path)
        motion.setSpeed(8)

        self.setState(ThrowItemState(motion))
        self.stopMovingImmediate()


    def showThrowPath(self, relativeTranslation, targetLocation, height, tension, additionalWaypoints=()):
        self.hideThrowPath()

        from com.jme3.cinematic import MotionPath
        from base import forms
        path = MotionPath()
        
        sourceLocation = forms.getSpatialFromInstance(self).getWorldTranslation()
        middleLocation = Vector3f().interpolateLocal(sourceLocation, targetLocation, 0.5)
        middleLocation.y+= height

        path.addWayPoint(sourceLocation)
        path.addWayPoint(middleLocation)
        path.addWayPoint(targetLocation)

        for wp in additionalWaypoints:
            path.addWayPoint(wp)

        path.setCurveTension(tension)

        path.enableDebugShape(glob.getApp().getAssetManager(), glob.getApp().getRootNode())

        self.shownThrowPath = path

    def hideThrowPath(self):
        if hasattr(self, "shownThrowPath"):
            self.shownThrowPath.disableDebugShape()
            del self.shownThrowPath

    def registerForThrownEvent(self, target):
        if not hasattr(self, "thrownListeners"):
            self.thrownListeners = SafeSet(Object, SafeSet.HashSetFactory())
        self.thrownListeners.add(target)

    def unregisterForThrownEvent(self, target):
        self.thrownListeners.remove(target)
        if len(self.thrownListeners) == 0:
            del self.thrownListeners


    def stopMovingImmediate(self):
        self.internalMovementVector.setTranslation(0, 0)
        self.setMoveDirection(Vector3f.ZERO)

    def onAnimationEvent(self, mapping, loop, sender):
        self.glob.onAnimationEvent(mapping, loop)
        self.state.onAnimationEvent(mapping, loop)
    
    def onTouchEnter(self, myName, otherForm, otherName):
        self.glob.onTouchEnter(myName, otherForm, otherName)
        self.state.onTouchEnter(myName, otherForm, otherName)
    
    def onTouch(self, myName, otherForm, otherName):
        # print "onTouch %s - %s - %s" % (myName, otherForm, otherName)
        pass
    
    def onTouchLeave(self, myName, otherForm, otherName):
        self.glob.onTouchLeave(myName, otherForm, otherName)
        self.state.onTouchLeave(myName, otherForm, otherName)

    def setState(self, newState):
        if self.state != None:
            self.state.unregister()
        
        self.state = newState
        self.state.setInputHandler(self.inputHandler)
        self.state.setPlayer(self)
        self.state.setGlob(self.glob)

        self.state.register()

    def walkLinearTo(self, vec):
        self.setState(WalkLinearToState(vec))

    def onControlDisabled(self):
        self.stopMovingImmediate()
