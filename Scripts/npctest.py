from base import globals as glob

from base.npc import SimpleNpc

from com.jme3.math import Quaternion
from com.jme3.math import Vector3f

class NpcTest(SimpleNpc):
    def onInitNpc(self):
        glob.player.setControlEnabled(False)

        self.pushCameraTransform()
        self.moveCameraTo(Vector3f(-76.96213, 5.8436713, -232.50043), Quaternion(-0.092955, 0.89253294, -0.21540838, -0.38515398), 1)

        if glob.player.glob.canDoubleJump:
            self.showTextString("Good luck with your double jump.")
        else:
            if glob.player.glob.cucumbersCollected >= 4:
                self.callMethod(self.activateDoubleJump)
                self.showTextString("You have 4 Cucumbers.\nYou can now double jump.\nBe happy!")
            else:
                self.showTextString("Bring me 4 Cucumbers.")

        self.moveCameraToStored(1)

    def onDone(self):
        glob.player.setControlEnabled(True)

    def activateDoubleJump(self, args):
        glob.player.glob.canDoubleJump = True