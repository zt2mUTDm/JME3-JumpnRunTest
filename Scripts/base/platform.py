from base import misc
from base import forms
from base import physics
from base.actor import Actor

from com.jme3.cinematic import MotionPath
from com.jme3.cinematic.events import MotionEvent
from com.jme3.math import Vector3f

class SimplePlatform(Actor):
    def onInit(self):
        super(SimplePlatform, self).onInit()
        
        self.waypointCounter = -1
        self.started = False
        self.moving = False
        self.speed = 3
        self.waypoints = None
        self.repeat = False
        
        physics.registerForTouchTest(self, "PlatformTop")
        
        self.registerForMovementEvents()
        
        self.onPlatformInit()
    
    def onPlatformInit(self):
        pass
    
    def onUpdate(self, tpf):
        if self.started:
            if self.waypointCounter == -1:
                self.waypointCounter = 0
                self._startNextMove()
                self.moving = True
    
    def onMovingEvent(self, eventName, source):
        self.waypointCounter = self.waypointCounter + 1
        if self.waypointCounter < len(self.waypoints):
            self._startNextMove()
        elif self.repeat:
            self.waypointCounter = 0
            self._startNextMove()
        else:
            self.stop()
    
    def onTouchEnter(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "PlayerGround":
            misc.getPlayer().attachTo(self)
            
    def onTouchLeave(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "PlayerGround":
            misc.getPlayer().detachFrom()
    
    def _startNextMove(self):
        self.moveLinearTo(self.waypoints[self.waypointCounter], self.speed)
    
    def start(self):
        if self.waypoints == None:
            raise Exception("Please set waypoints before calling start")
        self.started = True
    
    def stop(self):
        self.started = False
        if self.moving:
            self.stopMoving()
            
    def setWaypoints(self, *args):
        self.waypoints = args
    
    def setSpeed(self, speed):
        self.speed = speed
        
    def setRepeat(self, repeat):
        self.repeat = repeat
    
    def onCleanup(self):
        physics.unregisterForTouchTest(self, "PlatformTop")

class WaypointPlatform(Actor):
    def onInit(self):
        super(WaypointPlatform, self).onInit()

        physics.registerForTouchTest(self, "PlatformTop")
    
    def onPlatformInit(self):
        pass
    
    def onFirstUpdate(self, tpf):
        spatial = forms.getSpatialFromInstance(self)

        self.path = MotionPath()
        self.path.setCycle(True)
        self.motion = MotionEvent(spatial, self.path)

        self.onPlatformInit()

        del self.path
        self.motion.play()

    def onTouchEnter(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "PlayerGround":
            misc.getPlayer().attachTo(self)
            
    def onTouchLeave(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "PlayerGround":
            misc.getPlayer().detachFrom()
    
    def addWaypoint(self, waypoint):
        self.path.addWayPoint(waypoint)

    def addWaypoints(self, *args):
        for wp in args:
            self.path.addWayPoint(wp)
    
    def setDuration(self, duration):
        self.motion.setSpeed(duration)
    
    def onCleanup(self):
        physics.unregisterForTouchTest(self, "PlatformTop")

