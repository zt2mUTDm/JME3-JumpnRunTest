import math

from base import globals as glob
from base import forms

from base.actor import Actor

from com.jme3.math import Vector3f

from com.jme3.bullet.control import CharacterControl

from online.money_daisuki.api.monkey.basegame.character.control import CharacterControlAdapter
from online.money_daisuki.api.monkey.basegame.character.control import CharControl

class Player(Actor):
    def __init__(self):
        super(Player, self).__init__()
        self.isMoving = False
        self.controlEnabled = True
        
    def setMoveDirection(self, vector):
        spatial = forms.getSpatialFromInstance(self)
        spatial.getControl(CharacterControlAdapter).setMoveVector(vector)
        self.isMoving = (vector.x != 0 or vector.y != 0 or vector.z != 0)
        
    def setViewDirection(self, vector):
        spatial = forms.getSpatialFromInstance(self)
        spatial.getControl(CharacterControlAdapter).setViewDirection(vector)
        
    def stopMoving(self):
        if self.isMoving:
           spatial = forms.getSpatialFromInstance(self)
           spatial.getControl(CharacterControlAdapter).setMoveVector(Vector3f.ZERO)
           self.isMoving = False
           
    def jump(self):
        spatial = forms.getSpatialFromInstance(self)
        spatial.getControl(CharacterControlAdapter).jump()
        
    def getVelocity(self, vec):
        spatial = forms.getSpatialFromInstance(self)
        return spatial.getControl(CharacterControlAdapter).getLinearVelocity(vec)

    def setJumpSpeed(self, speed):
        spatial = forms.getSpatialFromInstance(self)
        #cc = spatial.getControl(CharacterControl)
        #char = cc.getCharacter()
        #char.setJumpSpeed(speed)
        cc = spatial.getControl(CharControl) # TODO
        cc.setJumpSpeed(speed)
        
    def setControlEnabled(self, b):
        if self.controlEnabled != b:
            self.controlEnabled = b

            if b:
                self.onControlEnabled()
            else:
                self.onControlDisabled()
        
    def isControlEnabled(self):
        return self.controlEnabled

    def onControlEnabled(self):
        pass

    def onControlDisabled(self):
        pass