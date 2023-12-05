from base import globals as glob
from base import forms
from base import scene

from java.lang import Runnable

from com.jme3.math import Vector3f

from online.money_daisuki.api.monkey.basegame.physobj import PythonCollisionState

class CallPythonRunnable(Runnable):
    def __init__(self, call):
        self.call = call
        
    def run(self):
        self.call()

def getSpatialFromInstance(instance):
    return forms.getSpatialFromInstance(instance)

def getPlayer():
    return glob.player

def getInputManager():
    return glob.getInputManager()

def getAppState(className):
    return(glob.getAppState(className))

def loadPlayer(translation=Vector3f(), rotation=Vector3f(0, 0, 0), scale=Vector3f(1, 1, 1)):
    return glob.loadPlayer(translation, rotation, scale)

def setCollisionEnabled(e):
    collState = getAppState(PythonCollisionState)
    collState.setEnabled(e)

def unload(ref):
    forms.unload(ref)

def getAssetManager():
    return glob.getAssetManager()

def getChildSpatial(spatial, name):
    return scene.getChildSpatial(spatial, name)

def getPlayerSpatial():
    return glob.getPlayerSpatial()