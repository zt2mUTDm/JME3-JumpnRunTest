from base import globals as glob
from base import cam

from com.jme3.math import Vector3f

class InitScript:
    def run(self):
        glob.loadPlayer(Vector3f(0, 20, 0), Vector3f(1, 0, 0), Vector3f(0.25, 0.25, 0.25))
        cam.setChaseCamera(Vector3f(0.0, 0.5235988, 20.0))
        glob.getPlayer().setControlEnabled(True)
        
        