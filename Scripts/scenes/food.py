from base import globals as glob
from base import forms

from base.collectable import Collectable

class Food(Collectable):
    def onInit(self):
        self.registerForTouchEvent("Food")

    def onUpdate(self, tpf):
        forms.getSpatialFromInstance(self).rotate(0, tpf, 0)

    def getCollisionShapeName(self):
        return "Food"

    def onCollected(self):
        glob.getPlayer().changeCollectable("Food", 1)