from base import globals as glob
from base import forms

from base.actor import Actor
from base import physics

TURN_SPEED = 1

class Cucumber(Actor):
    def __init__(self):
        super(Cucumber, self).__init__()
    
    def onInit(self):
        self.registerForTouchEvent("Cucumber")

    def onUpdate(self, tpf):
        spatial = forms.getSpatialFromInstance(self)
        spatial.rotate(0, TURN_SPEED * tpf, 0)

    def onTouch(self, myName, otherForm, otherName):
        if not self.collected and otherName == "Player":
            # TODO add animation here
            self.collected = True
            glob.player.glob.cucumbersCollected = glob.player.glob.cucumbersCollected + 1
            forms.unload(self)

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
