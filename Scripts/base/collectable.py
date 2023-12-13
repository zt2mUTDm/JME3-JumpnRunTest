from base import globals as glob
from base import forms

from base.actor import Actor
from base import physics

class Collectable(Actor):
    def __init__(self):
        super(Collectable, self).__init__()
    
    def onInit(self):
        shapeName = getCollisionShapeName()
        if shapeName is None:
            raise Exception("A collectable needs to implement getCollisionShapeName(self)")

        self.registerForTouchEvent(shapeName)

    def getCollisionShapeName(self):
        return None

    def onTouch(self, myName, otherForm, otherName):
        if not self.collected and otherName == "Player":
            self.onCollected()
            self.collected = True
            forms.unload(self)

    def onCollected(self):
        pass

    def requestLoad(self, data):
        if "collected" in data["persistent"]:
            self.collected = data["persistent"]["collected"]
        else:
            self.collected = False
        return not self.collected

    def beforeUnload(self):
        if not self.collected:
            return None
        
        return ({
            "persistent": {
                "collected": self.collected
            }
        })
