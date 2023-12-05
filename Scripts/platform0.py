from base import globals as glob
from base import physics
from base.actor import Actor

from com.jme3.math import Vector3f

from base.platform import Platform

WAYPOINTS = (Vector3f(-135, 20, -45), Vector3f(-135, 8, -45))
SPEED = 3
REPEAT = True

class Platform0(Platform):
    def onPlatformInit(self):
        self.setWaypoints(WAYPOINTS)
        self.setSpeed(SPEED)
        self.setRepeat(True)
        self.start()
