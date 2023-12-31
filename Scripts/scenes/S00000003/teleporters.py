from base.teleporter import Teleporter

from com.jme3.math import Vector3f

class Teleporter_00000003_00000004(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000004.json",
                       Vector3f(-9.781729, -8.650001, 0.002518229),
                       Vector3f(0.9999406, 0.0, 0.01090309),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(0.5362941, 0.49609375, 10.400002))
