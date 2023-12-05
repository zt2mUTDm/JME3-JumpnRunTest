from base import misc
from base import physics
from base.actor import Actor

from com.jme3.math import Vector3f

class Platform(Actor):
    def onInit(self):
        super(Platform, self).onInit()
        
        self.waypointCounter = -1
        self.started = False
        self.moving = False
        self.speed = 1
        self.waypoints = None
        self.repeat = False
        
        physics.registerForTouchTest(self, "PlatformTop")
        
        self.onPlatformInit()
        
    def onPlatformInit(self):
        pass

    def onUpdate(self, tpf):
        if self.started:
            if self.waypointCounter == -1:
                self.waypointCounter = 0
                self._startNextMove()
                self.moving = True
    
    def onMovingDone(self, canceled):
        self.waypointCounter = self.waypointCounter + 1
        if self.waypointCounter < len(self.waypoints):
            self._startNextMove()
        elif self.repeat:
            self.waypointCounter = 0
            self._startNextMove()
        else:
            self.stop()
    
    def onTouchEnter(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "Ground":
            misc.getPlayer().attachTo(self)
            
    def onTouchLeave(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "Ground":
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
            
    def setWaypoints(self, waypoints):
        self.waypoints = waypoints
    
    def setSpeed(self, speed):
        self.speed = speed
        
    def setRepeat(self, repeat):
        self.repeat = repeat
    
    def onCleanup(self):
        physics.unregisterForTouchTest(self, "PlatformTop")

