from com.jme3.math import Vector3f

from base import globals as glob
from base import forms

from base.actor import Actor

# TODO: Make more generic
class ItemReceivementContainer(Actor):
    def onInit(self):
        self.registerForTouchEvent("ActionRadius")

        self.aniName = "ReceiveItem"
        self.collectableName = "Food"
        self.relativeModelLocation = Vector3f(0, 4, 0)

    def onTouchEnter(self, myName, otherForm, otherName):
        if otherName == "PlayerShape" and self.toThrow >= 1:
            if not hasattr(self, "active") or not self.active:
                player = glob.getPlayer()
                if player.getCollectableCount("Food") >= 1:
                    self.active = True

                    player.setControlEnabled(False)
                    player.registerForThrownEvent(self)
                    
                    self.throwFood()

    def throwFood(self):
        player = glob.getPlayer()
        spatial = forms.getSpatialFromInstance(self)

        throwTarget = spatial.getWorldTranslation()
        endTarget = Vector3f(throwTarget)
        endTarget.y-= 6

        player.throwObject("Models/Scenes/00000000/Food/Food.json",
                self.relativeModelLocation,
                throwTarget,
                10,
                0.5,
                (endTarget,)
        )

    def concludeThrow(self):
        player = glob.getPlayer()
        player.changeCollectable("Food", -1)
        self.toThrow-= 1
        if player.getCollectableCount("Food") >= 1:
            self.throwFood()
        else:
            player.unregisterForThrownEvent(self)
            del self.active

            if self.toThrow == 0:
                self.onDone()
            player.setControlEnabled(True)

    def onPlayerThrowDone(self):
        self.registerForAnimationEvents()
        self.playAnimation(self.aniName, False)

    def onAnimationEvent(self, aniName, loop, source):
        if aniName == self.aniName:
            self.unregisterForAnimationEvents()
            self.concludeThrow()

    def onDone(self):
        pass
    
    def requestLoad(self, data):
        if "toThrow" in data["persistent"]:
            self.toThrow = data["persistent"]["toThrow"]
        else:
            self.toThrow = 3
        return True

    def beforeUnload(self):
        if not self.toThrow:
            return None
        
        return ({
            "persistent": {
                "toThrow": self.toThrow
            }
        })
