from base import globals as glob
from base import physics
from base.actor import Actor

from com.jme3.math import Vector3f

from base.platform import Platform

class Platform0(Platform):
    def onPlatformInit(self):
        self.setWaypoints((Vector3f(-135, 20, -45), Vector3f(-135, 8, -45)))
        self.setSpeed(6)
        self.setRepeat(True)
        self.start()
