from base import globals as glob
from base import forms

from java.lang import Runnable

from com.jme3.bullet import BulletAppState
from com.jme3.scene import Node

from online.money_daisuki.api.monkey.basegame.py import PythonAppState
from online.money_daisuki.api.monkey.basegame import SceneGraphAppState
from online.money_daisuki.api.monkey.basegame.scene import SceneLoadAppState

class UnloadSceneTask(Runnable):
    def __init__(self, callback):
        self.callback = callback
        
    def run(self):
        py = glob.getAppState(PythonAppState)
    
        bullet = glob.getAppState(BulletAppState)
        
        for key in forms.forms:
            forms.unload(forms.forms[key])
        
        scene = glob.getAppState(SceneGraphAppState)
        root = scene.getRootNode()
        
        bullet.getPhysicsSpace().removeAll(root)
        scene.clearRoot()
        
        if self.callback is not None:
            self.callback.run()

def unloadScene(callback=None):
    glob.getApp().enqueue(UnloadSceneTask(callback))
    
def loadScene(url):
    load = glob.getAppState(SceneLoadAppState)
    load.load(url)
    
class Out(Runnable):
    def __init__(self, str):
        self.str = str
    def run(self):
        print "%s: %s" % (self.str, glob.getAppState(SceneGraphAppState).getRootNode().getChildren())

def getChildSpatial(spatial, name):
    if name == spatial.getName():
        return(spatial)
    
    for child in spatial.getChildren():
        if isinstance(child, Node):
            found = getChildSpatial(child, name)
            if found != None:
                return found
        elif name == child.getName():
            return(spatial)
    
    return None
    
