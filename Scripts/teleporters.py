from base.teleporter import Teleporter

from com.jme3.math import Vector3f

"""Jinja cam location: 2.3474307, 11.299786, 1.1437591"""

class Teleporter_00000000_00000001(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000001.json",
                       Vector3f(-6.6693344, 0.9000001, 0.8815934),
                       Vector3f(1, 0, 0),
                       Vector3f(0.25, 0.25, 0.25))
        #self.setChaseCamera(Vector3f(0.71695817, 0.5371094, 12.8))
        self.setFixedLocationLookAtPlayerCamera(Vector3f(2.3474307, 11.299786, 1.1437591))

class Teleporter_00000001_00000000(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000000.json",
                       Vector3f(0.16180758, 3.3000002, -83.51888),
                       Vector3f(0, 0, 1),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(1.576333, 0.5546875, 13.599998))

class Teleporter_00000000_00000002(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000002.json",
                       Vector3f(0.16180758, 0, 0),
                       Vector3f(0, 0, 1),
                       Vector3f(0.25, 0.25, 0.25))
        #self.setChaseCamera(Vector3f(0.71695817, 0.5371094, 12.8))
        self.setFixedLocationLookAtPlayerCamera(Vector3f(-5.998874, 9.899775, 0.13646314))

class Scene0ToScene3Teleporter(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000003.json",
                       Vector3f(0.16180758, 0, 0),
                       Vector3f(0, 0, 1),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(1.576333, 0.5546875, 13.599998))

class Scene3ToScene0Teleporter0(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000000.json",
                       Vector3f(0.16180758, 0, 0),
                       Vector3f(0, 0, 1),
                       Vector3f(0.25, 0.25, 0.25))
        self.setChaseCamera(Vector3f(1.576333, 0.5546875, 13.599998))

class Teleporter_00000002_00000001(Teleporter):
    def initTeleporter(self):
        self.setTarget("Scenes/00000001.json",
                       Vector3f(8.715357, 12.8916664, 2.0525952),
                       Vector3f(0, 0, 1),
                       Vector3f(0.25, 0.25, 0.25))
        self.setFixedLocationLookAtPlayerCamera(Vector3f(2.3474307, 11.299786, 1.1437591))
