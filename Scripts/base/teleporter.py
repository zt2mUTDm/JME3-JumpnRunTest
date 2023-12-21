from base import globals as glob
from base import cam
from base import misc

from base import physics
from base import scene
from base import filters

from base.reference import ScriptReference

from java.lang import Runnable

from com.jme3.input import ChaseCamera
from com.jme3.math import Vector3f

class SetChaseCameraRunnable(Runnable):
    def __init__(self, cameraLocation):
        self.cameraLocation = cameraLocation
        
    def run(self):
        cam.setChaseCamera(self.cameraLocation)

class SetFixedLocationLookAtPlayerCamera(Runnable):
    def __init__(self, cameraLocation):
        self.cameraLocation = cameraLocation
        
    def run(self):
        cam.setFixedLocationLookAtPlayerCamera(self.cameraLocation)


class Teleporter(ScriptReference):
    def __init__(self):
        super(Teleporter, self).__init__()
        
        self.targetScene = None
        self.targetLocation = None
        self.targetRotation = None
        self.targetScale = None
        
        self.postLoads = None

        self.triggered = False
    
    def onInit(self):
        self.registerForTouchEvent("Dummy")
        pass
    
    def onTouchEnter(self, myName, otherForm, otherName):
        if not self.triggered and otherName == "PlayerShape":
            self.triggered = True

            self.initTeleporter()
            
            if self.targetScene == None:
                raise Exception("Target scene not set in teleporter")
            
            cam = misc.getSpatialFromInstance(glob.player).getControl(ChaseCamera)
            if cam != None:
                cam.setEnabled(False)
                cam.cleanupWithInput(misc.getInputManager())
            
            misc.getPlayer().setControlEnabled(False)
            
            #glob.player.walkLinearTo(Vector3f(0, 0, 0))

            filters.registerForFadeEvents(self)
            filters.fadeOut()

    def run(self):
        scene.loadScene(self.targetScene)
        misc.loadPlayer(self.targetLocation, self.targetRotation, self.targetScale)
        
        if self.postLoads != None:
            for postLoad in self.postLoads:
                postLoad.run()
        
        filters.fadeIn()

    def onCleanup(self):
        #physics.unregisterForTouchTest(self, "Dummy")
        pass
        
    def onFadeOut(self):
        misc.setCollisionEnabled(False)
        scene.unloadScene(self)
        cam.clearCameras()
        
    def onFadeIn(self):
        filters.unregisterForFadeEvents(self)
        misc.setCollisionEnabled(True)
        misc.getPlayer().setControlEnabled(True)
        self.onTeleportDone()
    
    def setTarget(self, scene, location=Vector3f(0, 0, 0), rotation=Vector3f(1, 0, 0),
                  scale=Vector3f(1, 1, 1)):
        self.targetScene = scene
        self.targetLocation = location
        self.targetRotation = rotation
        self.targetScale = scale
        
    def setChaseCamera(self, setting):
        self.postLoads = [ SetChaseCameraRunnable(setting) ]
    
    def setFixedLocationLookAtPlayerCamera(self, location):
        self.postLoads = [ SetFixedLocationLookAtPlayerCamera(location) ]
    
    def onTeleportDone(self):
        pass
