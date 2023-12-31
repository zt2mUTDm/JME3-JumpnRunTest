from base.teleporter import Teleporter

from com.jme3.math import Vector3f

class Teleporter_00000005_00000003(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000003.json",
                       Vector3f(1.9157643, 36.6, -58.346603),
                       Vector3f(0.014882491, 0.0, 0.9998892),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(33.01969, 0.30566406, 12.8))

class Teleporter_00000005_00000003_1(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000003.json",
                       Vector3f(-9.565186, 36.6, 23.478575),
                       Vector3f(0.3814771, 0.0, -0.9243784),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(42.802895, 0.22949219, 12.8))