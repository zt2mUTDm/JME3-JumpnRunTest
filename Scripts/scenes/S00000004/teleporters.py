from base.teleporter import Teleporter

from com.jme3.math import Vector3f

class Teleporter_00000004_00000003(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000003.json",
                       Vector3f(-17.028685, -27.65, 18.704576),
                       Vector3f(0.68282413, 0.0, -0.73058283),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(-0.81917477, 0.5644531, 12.8))
