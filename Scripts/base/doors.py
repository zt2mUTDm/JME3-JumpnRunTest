from com.jme3.util import TempVars

from base import globals as glob
from base import forms

from base.actor import Actor

class OpenLiftingPersistentDoor(Actor):
    def requestLoad(self, data):
        persident = data["persistent"]
        if "open" in persident:
            self.open = persident["open"]
        return True

    def beforeUnload(self):
        persident = {}

        if hasattr(self, "open"):
            persident["open"] = self.open

        return {
            "persistent": persident
        }

    def onFirstUpdate(self, tpf):
        if hasattr(self, "open") and self.open:
            tmp = TempVars.get()

            spatial = forms.getSpatialFromInstance(self)
            tmp.vect1.set(spatial.getLocalTranslation())
            tmp.vect1.y = tmp.vect1.y + 4
            spatial.setLocalTranslation(tmp.vect1)

            tmp.release()

    def onActivation(self, activator, tpf):
        if hasattr(self, "open") and self.open:
            return

        tmp = TempVars.get()

        spatial = forms.getSpatialFromInstance(self)
        tmp.vect1.set(spatial.getLocalTranslation())
        tmp.vect1.y = tmp.vect1.y + 4
        
        self.registerForMovementEvents()
        self.moveLinearTo(tmp.vect1, 2)

        tmp.release()

    def onMovingEvent(self, eventName, source):
        self.open = True
        self.unregisterForMovementEvents()