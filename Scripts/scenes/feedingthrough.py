from com.jme3.math import Vector3f
from com.jme3.scene import Node

from com.jme3.cinematic import MotionPath
from com.jme3.cinematic.events import MotionEvent

from online.money_daisuki.api.monkey.basegame.cinematic import PythonMotionPathListener

from base import globals as glob
from base import forms
from base import cam

from base.itemreceivement import ItemReceivementContainer

class FeedingTrough(ItemReceivementContainer):
    def onDone(self):
        path = MotionPath()

        path.addWayPoint(Vector3f(-127, 21.099998, -12))
        path.addWayPoint(Vector3f(-62, 21.099998, -67))
        path.addWayPoint(Vector3f(-127, 21.099998, -122))
        
        path.setCurveTension(0.5)

        path.addListener(PythonMotionPathListener(self))

        self.cameraNode = Node()
        cam.setCameraNode(self.cameraNode)
        
        forms.getRootNode().attachChild(self.cameraNode)

        event = MotionEvent(self.cameraNode, path)
        event.setDirectionType(MotionEvent.Direction.LookAt)
        event.setLookAt(Vector3f(-127, 0, -67), Vector3f.UNIT_Y)
        event.setSpeed(2)
        event.play()

        self.trigger0()

    def trigger0(self):
        genji = forms.getInstance(6004)
        genji.diveUp()

    def trigger1(self):
        genji = forms.getInstance(6004)
        genji.leaveShell()

    def onWaypointReach(self, motion, waypointIndex):
        if waypointIndex == 1:
            self.trigger1()
        elif waypointIndex == 2:
            cam.popCamera()

            forms.getRootNode().detachChild(self.cameraNode)
            del self.cameraNode


