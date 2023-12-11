from base import globals as glob
from base import physics
from base.actor import Actor

from com.jme3.math import Vector3f

WAYPOINTS = (Vector3f(-125.0, 24.8, -219.0), Vector3f(-85.0, 24.8, -219.0))
SPEED = 6
REPEAT = True

class Platform4(Actor):
    def init(self):
        super(Platform4, self).__init__()
        self.waypointCounter = -1
        physics.registerForTouchTest(self, "PlatformTop")
        self.registerForMovementEvents()

    def onUpdate(self, tpf):
        global WAYPOINTS
        if self.waypointCounter == -1:
            self.waypointCounter = 0
            self._startNextMove()
    
    def onMovingEvent(self, eventName, source):
        global WAYPOINTS
        global REPEAT
        
        self.waypointCounter = self.waypointCounter + 1
        if self.waypointCounter < len(WAYPOINTS):
            self._startNextMove()
        elif REPEAT:
            self.waypointCounter = 0
            self._startNextMove()
    
    def onTouchEnter(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "Ground":
            glob.player.attachTo(self)
            
    def onTouchLeave(self, myName, otherForm, otherName):
        if myName == "PlatformTop" and otherName == "Ground":
            glob.player.detachFrom()
    
    def _startNextMove(self):
        global WAYPOINTS
        global SPEED
        
        self.moveLinearTo(WAYPOINTS[self.waypointCounter], SPEED)

    def cleanup(self):
        physics.unregisterForTouchTest(self, "PlatformTop")
        