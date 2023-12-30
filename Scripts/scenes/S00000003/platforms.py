from com.jme3.animation import LoopMode
from com.jme3.math import Vector3f
from com.jme3.math.Spline import SplineType

from base import globals as glob
from base import physics
from base.actor import Actor

from base.platform import SimplePlatform
from base.platform import WaypointPlatform

class Platform0(WaypointPlatform):
    def onPlatformInit(self):
        self.addWaypoints(Vector3f(-10, 4, 10), Vector3f(-10, 15, 10), Vector3f(0, 15, 0), Vector3f(0, 25, 0))
        self.setSpeed(2)

class Platform1(WaypointPlatform):
    def onPlatformInit(self):
        distance = 20
        self.addWaypoints(Vector3f(0, 25, distance), Vector3f(distance, 25, 0), Vector3f(0, 25, -distance), Vector3f(-distance, 25, 0), Vector3f(0, 25, distance))
        self.setLoopMode(LoopMode.Loop)
        self.setType(SplineType.CatmullRom)
        self.setSpeed(0.5)
        self.setTension(0.5)
