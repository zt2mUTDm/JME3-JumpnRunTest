from base import globals as glob

from base import forms
from base import switches
from base import cam

from com.jme3.math import Vector3f
from com.jme3.math import Quaternion

class Switch0(switches.SPSTTouchKeepButton):
    def onSwitchActivation(self, type):
        self.step = 0

        glob.getPlayer().setControlEnabled(False)

        cam.pushCameraTransform()
        self.registerForCameraEvents()
        cam.moveLinearTo(Vector3f(-76.63241, 7.578371, -142.84584), Quaternion(-0.06557904, 0.9201565, -0.17551114, -0.3438128), 2)

    def onCameraEvent(self, eventName):
        if self.step == 0:
            self.unregisterForCameraEvents()

            activatable = forms.getInstance(2001)
            activatable.registerForMovementEvents(self)
            activatable.activate(self)

            self.step = 1
        elif self.step == 2:
            self.unregisterForCameraEvents()

            cam.popCamera()
            cam.popCamera()
            glob.getPlayer().setControlEnabled(True)

            del self.step

    def onMovingEvent(self, eventName, source):
        if self.step == 1:
            activatable = forms.getInstance(2001)
            activatable.unregisterForMovementEvents(self)
            
            self.registerForCameraEvents()

            data = cam.popCameraTransform()
            cam.moveLinearTo(data[0], data[1], 2)
            
            self.step = 2