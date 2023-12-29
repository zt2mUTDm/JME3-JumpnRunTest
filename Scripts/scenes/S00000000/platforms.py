from base import globals as glob
from base import physics
from base.actor import Actor

from com.jme3.math import Vector3f

from base.platform import SimplePlatform
from base.platform import WaypointPlatform

class Platform0(SimplePlatform):
    def onPlatformInit(self):
        self.setWaypoints(Vector3f(-135, 38, -45), Vector3f(-135, 22, -45))
        self.setSpeed(6)
        self.setRepeat(True)
        self.start()

class Platform1(SimplePlatform):
    def onPlatformInit(self):
        self.setWaypoints(Vector3f(-135, 38, -15), Vector3f(-135, 50, -15))
        self.setSpeed(6)
        self.setRepeat(True)
        self.start()

class Platform2(SimplePlatform):
    def onPlatformInit(self):
        self.setWaypoints(Vector3f(-135, 50, -81), Vector3f(-135, 50, -45))
        self.setSpeed(6)
        self.setRepeat(True)
        self.start()

class Platform3(SimplePlatform):
    def onPlatformInit(self):
        self.setWaypoints(Vector3f(-85.0, 24.8, -249.0), Vector3f(-125.0, 24.8, -249.0))
        self.setSpeed(6)
        self.setRepeat(True)
        self.start()

class Platform4(SimplePlatform):
    def onPlatformInit(self):
        self.setWaypoints(Vector3f(-125.0, 24.8, -219.0), Vector3f(-85.0, 24.8, -219.0))
        self.setSpeed(6)
        self.setRepeat(True)
        self.start()

