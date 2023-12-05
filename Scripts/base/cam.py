from java.lang import Object
from java.lang import Runnable

from collections import deque

from base import globals as glob
from base import misc

from com.jme3.math import Vector3f
from com.jme3.math import Quaternion
from com.jme3.input import ChaseCamera
from com.jme3.util import SafeArrayList

from online.money_daisuki.api.monkey.basegame.cam import MoveCameraLinearToAppState
from online.money_daisuki.api.monkey.basegame.cam import FixedLocationLookAtPlayerCamera
from online.money_daisuki.api.monkey.basegame.cam import GameChaseCamera
from online.money_daisuki.api.monkey.basegame.cam import MoveCameraLinearToCamera
from online.money_daisuki.api.monkey.basegame.collections import SafeSet


camStack = None
activeCamera = None

camListeners = None
camTransformStack = None

class FireCameraEventsRunnable(Runnable):
    def __init__(self, eventName):
        self.eventName = eventName

    def run(self):
        global camListeners
        if camListeners is not None:
            buf = camListeners.getArray()
            for l in buf:
                l.onCameraEvent(self.eventName)

def setChaseCamera(camDirection):
    spatial = glob.getPlayerSpatial()

    cam = glob.getApp().getCamera()
    cam.setRotation(Quaternion(0, 1, 0, 0))
    
    chaseCam = GameChaseCamera(glob.getApp().getCamera(), spatial, glob.getInputManager())

    pushCamera(chaseCam)

    chaseCam.setMinDistance(7.0)
    chaseCam.setInvertVerticalAxis(True)
    chaseCam.setDragToRotate(False)
    chaseCam.setDefaultHorizontalRotation(camDirection.getX())
    chaseCam.setDefaultVerticalRotation(camDirection.getY())
    chaseCam.setDefaultDistance(camDirection.getZ())

def setFixedLocationLookAtPlayerCamera(camLocation, resetRotation=True):
    cam = glob.getCamera()
    if resetRotation:
        cam.setRotation(Quaternion(0, 1, 0, 0))
    
    cam.setLocation(camLocation)
    
    control = FixedLocationLookAtPlayerCamera(cam, misc.getSpatialFromInstance(glob.player), Vector3f.UNIT_Y)
    pushCamera(control)

def moveLinearTo(location, rotation, duration):
    c = MoveCameraLinearToCamera(glob.getStateManager(), location, rotation, duration)
    pushCamera(c)
    # Add after aquire
    c.addListener(FireCameraEventsRunnable("MoveCameraLinearTo"))

def pushCamera(cam):
    global camStack
    if camStack is None:
        camStack = deque()

    global activeCamera
    if activeCamera != None:
        enabled = activeCamera.isEnabled()
        activeCamera.setEnabled(False)
        camStack.append((activeCamera, enabled))
    
    cam.acquire()
    activeCamera = cam

def popCamera():
    global camStack
    global activeCamera

    activeCamera.dispose()

    data = camStack.pop()
    activeCamera = data[0]
    activeCamera.setEnabled(data[1])

    if len(camStack) == 0:
        camStack = None

def clearCameras():
    if camStack is not None:
        for i in range(len(camStack)):
            popCamera()
    
    if activeCamera is not None:
        activeCamera.dispose()
        activeCamera = None

def addCameraListener(listener):
    global camListeners
    if camListeners is None:
        camListeners = SafeSet(Object, SafeSet.HashSetFactory())
    camListeners.add(listener)

def removeCameraListener(listener):
    if camListeners is None:
        return False

    global camListeners
    b = camListeners.remove(listener)
    
    if camListeners.isEmpty():
        camListeners = None

def pushCameraTransform():
    camera = glob.getCamera()

    global camTransformStack
    if camTransformStack is None:
        camTransformStack = deque()

    camTransformStack.append((Vector3f(camera.getLocation()), Quaternion(camera.getRotation(),)))

def popCameraTransform():
    global camTransformStack

    data = camTransformStack.pop()
    if len(camTransformStack) == 0:
        camTransformStack = None
    return(data)
