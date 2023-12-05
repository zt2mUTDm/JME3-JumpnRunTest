import math

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

from online.money_daisuki.projects.monkey.jumpnrun.tests import FloatLinearInterpolation2D

MOVE_SPEED = 0.3

keyMasks = {
    "PlayerUp": 1,
    "PlayerRight": 2,
    "PlayerDown": 4,
    "PlayerLeft": 8,
    "PlayerStrike": 16,
    "PlayerActionA": 32,
    "PlayerActionB": 64,
    "PlayerSpecialA": 128
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
HIGH_JUMP_SPEED = 50.0

"""
Skizze:

State - 
"""

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
        input.registerForBinaryInputEvent(self, "PlayerSpecialA")


        input.registerForBinaryInputEvent(self, "Test")
    
    def unregisterForInputs(self):
        input.unregisterForBinaryInputEvent(self, "PlayerUp")
        input.unregisterForBinaryInputEvent(self, "PlayerRight")
        input.unregisterForBinaryInputEvent(self, "PlayerDown")
        input.unregisterForBinaryInputEvent(self, "PlayerLeft")
        
        input.unregisterForBinaryInputEvent(self, "PlayerActionA")
        input.unregisterForBinaryInputEvent(self, "PlayerActionB")
        input.unregisterForBinaryInputEvent(self, "PlayerSpecialA")


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
        
        input.addJoystickButtonMappings(glob.getInputManager().getJoysticks() [0].getButtons().get(0), "PlayerActionA")
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

class BasicPlayer(Player):
    def __init__(self):
        super(BasicPlayer, self).__init__()
        self.pressedButtons = 0
        self.tmpVec = Vector3f()
        self.tmpQuad = Quaternion()
        self.state = "Standing"
        
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
        input.registerKeyMapping("PlayerSpecialA", keycodes.KEY_Q)
        
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
        
        self.registerForCameraEvents()
        
        self.internalMovementVector = FloatLinearInterpolation2D(5)
        
        self.inputHandler = PlayerKeyboardInputHandler()
        self.inputHandler.registerForInputs()
        
        self.registerForAnimationEvents()
        
        self.addViewReceiver("PlayerGeom")
        
        physics.registerForTouchTest(self, "Top")
        physics.registerForTouchTest(self, "Ground")
    
    def onCameraEvent(self, eventName):
        print "Camera event: %s" % eventName

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
        if self.state == "Standing":
            self._handleStanding(tpf)
        elif self.state == "Moving":
            self._handleMoving(tpf)
        elif self.state == "DoJump":
            self._handleDoJump(tpf)
        elif self.state == "JumpEnd":
            self._handleJumpEnd()
        elif self.state == "WaitWithSpecialA":
            self._waitWithSpecialA()
    
    def _handleStanding(self, tpf):
        #print self.inputHandler.isDirectionMovement()
        if (not self.isControlEnabled()):
            #print "No control"
            pass
        elif (self.pressedButtons & keyMasks["PlayerSpecialA"]) != 0:
            self.state = "WaitWithSpecialA"
            self._unsetPressedKey("PlayerActionA")
        elif self.inputHandler.isActionAKeyPressed():
            self.setAnimationSpeed(1)
            self.playAnimation("JumpStart", False)
            self.state = "Waiting"
        elif (self.inputHandler.isDirectionMovement()):
            self._updateMoveDirection(tpf)
            self.playAnimation("Run", True)
            self.state = "Moving"
            
    def _handleMoving(self, tpf):
        self._updateMoveDirection(tpf)
        
        vecX = self.internalMovementVector.getX()
        vecY = self.internalMovementVector.getY()
        if not self.inputHandler.isDirectionMovement() and (vecX < MOVEMENT_BRAKE_MIN_SPEED and vecX > -MOVEMENT_BRAKE_MIN_SPEED and vecY < MOVEMENT_BRAKE_MIN_SPEED and vecY > -MOVEMENT_BRAKE_MIN_SPEED):
            self.internalMovementVector.setTranslation(0, 0)
            self.setMoveDirection(Vector3f.ZERO)
            
            self.playAnimation("Idle", True)
            self.setAnimationSpeed(1)
            self.state = "Standing"
            
            return
        
        if (not self.isControlEnabled()):
            self.stopMoving()
            self.setAnimationSpeed(1)
            self.playAnimation("Idle", True)
            self.state = "Standing"
            
            self.internalMovementVector.setTranslation(0, 0)
        elif (self.pressedButtons & keyMasks["PlayerSpecialA"]) != 0:
            self.stopMoving()
            self.playAnimation("Idle", True)
            self.state = "WaitWithSpecialA"
            self._unsetPressedKey("PlayerActionA")
        elif self.inputHandler.isActionAKeyPressed():
            self.setAnimationSpeed(1)
            self.playAnimation("JumpStart", False)
            self.state = "Waiting"
        #else:
            #self.stopMoving()
            #self.playAnimation("Idle", True)
            #self.state = "Standing"
    
    def _handleDoJump(self, tpf):
        vec = self.getVelocity(self.tmpVec)
        if vec.y == 0 and self.bottomCollideWithScene:
            self.playAnimation("JumpEnd", False)
            self.state = "JumpEnd"
            
        if self.inputHandler.isDirectionMovement() and self.isControlEnabled():
            self._updateMoveDirection(tpf)
        else:
            self.stopMoving()
            self.internalMovementVector.setTranslation(0, 0)
            
    def _handleJumpEnd(self):
        self._resetHighJump()
        if self.inputHandler.isActionAKeyPressed():
            if not self.topCollideWithScene:
                self.playAnimation("JumpStart", False)
                self.state = "Waiting"
    
    def _waitWithSpecialA(self):
        if (self.pressedButtons & keyMasks["PlayerSpecialA"]) == 0:
            self.state = "Standing"
        else:
            if self.inputHandler.isActionAKeyPressed() != 0:
                self.shootingMode = True
            elif (self.pressedButtons & keyMasks["PlayerActionB"]) != 0:
                self.shootingMode = True
    
    def _updateMoveDirection(self, tpf):
        global moveRotationAngles
        
        if self.isControlEnabled() and self.inputHandler.isDirectionMovement():
            targetX = self.inputHandler.getX()
            targetY = self.inputHandler.getY()
        else:
            targetX = 0
            targetY = 0
        
        self.internalMovementVector.translate(targetX, targetY, tpf)
        moveAngle = self.internalMovementVector.getAngle()
        
        q = self.tmpQuad
        q.fromAngleNormalAxis(moveAngle, Vector3f.UNIT_Y)
        
        vec = self.tmpVec
        
        cam = glob.getApp().getCamera()
        camDir = cam.getDirection()
        vec.set(camDir.getX(), 0, camDir.getZ())
        vec.normalizeLocal()
        
        q.multLocal(vec)
        
        self.setViewDirection(vec)
        
        moveSpeed = self.internalMovementVector.getDistance()
        
        vec.multLocal(moveSpeed * MOVE_SPEED)
        self.setMoveDirection(vec)
        
        self.setAnimationSpeed(moveSpeed)
    
    def _handleMovingStop(self):
        vecX = self.internalMovementVector.getX()
        vecY = self.internalMovementVector.getY()
        if not self.inputHandler.isDirectionMovement() and (vecX < MOVEMENT_BRAKE_MIN_SPEED and vecX > -MOVEMENT_BRAKE_MIN_SPEED and vecY < MOVEMENT_BRAKE_MIN_SPEED and vecY > -MOVEMENT_BRAKE_MIN_SPEED):
            self.internalMovementVector.setTranslation(0, 0)
            self.setMoveDirection(Vector3f.ZERO)
            
            return True
        return False
    
    def _setHighJump(self):
        self.setJumpSpeed(HIGH_JUMP_SPEED)
        
    def _resetHighJump(self):
        self.setJumpSpeed(DEFAULT_JUMP_SPEED)
        
    def onAnimationEvent(self, mapping, loop):
        if mapping == "JumpStart":
            self.playAnimation("JumpMiddle", True) 
            self.state = "DoJump"
            if self.highJumpActive:
                self._setHighJump()
            self.jump()
        elif mapping == "JumpEnd":
            self.stopMoving()
            self.state = "Standing"
    
    def onTouchEnter(self, myName, otherForm, otherName):
        if myName == "Top" and otherName != "Player":
            self.topCollideWithScene = True
        elif myName == "Ground" and otherName != "Player":
            self.bottomCollideWithScene = True
            if self.canHighJump and otherName == "HighJump":
                self.highJumpActive = True
    
    def onTouch(self, myName, otherForm, otherName):
        # print "onTouch %s - %s - %s" % (myName, otherForm, otherName)
        pass
    
    def onTouchLeave(self, myName, otherForm, otherName):
        if myName == "Top" and otherName != "Player":
            self.topCollideWithScene = False
        elif myName == "Ground" and otherName != "Player":
            self.bottomCollideWithScene = False
            if otherName == "HighJump":
                self.highJumpActive = False
