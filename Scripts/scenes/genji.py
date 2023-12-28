from com.jme3.math import Vector3f

from com.jme3.cinematic import MotionPath
from com.jme3.cinematic.events import MotionEvent

from base import forms

from base.actor import Actor

class Genji(Actor):
    def diveUp(self):
        if hasattr(self, "divedUp"):
            return

        spatial = forms.getSpatialFromInstance(self)

        startVec = spatial.getLocalTranslation()

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(Vector3f(startVec.x, 0, startVec.z))

        motion = MotionEvent(spatial, path)
        motion.setSpeed(8)

        motion.play()

        self.divedUp = True

    def leaveShell(self):
        if hasattr(self, "leftShell"):
            return

        spatial = forms.getSpatialFromInstance(self)

        head = forms.getSpatialFromSpatialByName(spatial, "GenjiHead")
        self._startHeadMovement(head)

        legFrontLeft = forms.getSpatialFromSpatialByName(spatial, "GenjiLegFrontLeft")
        self._startLegFrontLeftMovement(legFrontLeft)

        legFrontRight = forms.getSpatialFromSpatialByName(spatial, "GenjiLegFrontRight")
        self._startLegFrontRightMovement(legFrontRight)

        legBackLeft = forms.getSpatialFromSpatialByName(spatial, "GenjiLegBackLeft")
        self._startLegBackLeftMovement(legBackLeft)

        legBackRight = forms.getSpatialFromSpatialByName(spatial, "GenjiLegBackRight")
        self._startLegBackRightMovement(legBackRight)

        tail = forms.getSpatialFromSpatialByName(spatial, "GenjiTail")
        self._startTailMovement(tail)

        self.leftShell = True
    
    def _startHeadMovement(self, head):
        startVec = head.getLocalTranslation()
        endVec = Vector3f(startVec.x + 10, startVec.y, startVec.z)

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(endVec)

        motion = MotionEvent(head, path)
        motion.setSpeed(8)

        motion.play()

    def _startLegFrontLeftMovement(self, leg):
        startVec = leg.getLocalTranslation()
        endVec = Vector3f(startVec.x + 3, startVec.y, startVec.z - 3)

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(endVec)

        motion = MotionEvent(leg, path)
        motion.setSpeed(8)

        motion.play()

    def _startLegFrontRightMovement(self, leg):
        startVec = leg.getLocalTranslation()
        endVec = Vector3f(startVec.x + 3, startVec.y, startVec.z + 3)

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(endVec)

        motion = MotionEvent(leg, path)
        motion.setSpeed(8)

        motion.play()

    def _startLegBackLeftMovement(self, leg):
        startVec = leg.getLocalTranslation()
        endVec = Vector3f(startVec.x - 3, startVec.y, startVec.z - 3)

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(endVec)

        motion = MotionEvent(leg, path)
        motion.setSpeed(8)

        motion.play()

    def _startLegBackRightMovement(self, leg):
        startVec = leg.getLocalTranslation()
        endVec = Vector3f(startVec.x - 3, startVec.y, startVec.z + 3)

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(endVec)

        motion = MotionEvent(leg, path)
        motion.setSpeed(8)

        motion.play()

    def _startTailMovement(self, tail):
        startVec = tail.getLocalTranslation()
        endVec = Vector3f(startVec.x - 10, startVec.y, startVec.z)

        path = MotionPath()
        path.addWayPoint(startVec)
        path.addWayPoint(endVec)

        motion = MotionEvent(tail, path)
        motion.setSpeed(8)

        motion.play()

    def requestLoad(self, data):
        persident = data["persistent"]
        
        if "divedUp" in persident:
            self.divedUp = persident["divedUp"]
        if "leftShell" in persident:
            self.leftShell = persident["leftShell"]
        
        return True

    def onFirstUpdate(self, tpf):
        if hasattr(self, "divedUp"):
            spatial = forms.getSpatialFromInstance(self)
            startVec = spatial.getLocalTranslation()
            spatial.setLocalTranslation(Vector3f(startVec.x, 0, startVec.z))

        if hasattr(self, "leftShell"):
            spatial = forms.getSpatialFromInstance(self)
            startVec = spatial.getLocalTranslation()
            spatial.setLocalTranslation(Vector3f(startVec.x, 0, startVec.z))

            head = forms.getSpatialFromSpatialByName(spatial, "GenjiHead")
            headStartVec = head.getLocalTranslation()
            head.setLocalTranslation(headStartVec.x + 10, headStartVec.y, headStartVec.z)

            legFrontLeft = forms.getSpatialFromSpatialByName(spatial, "GenjiLegFrontLeft")
            legFrontLeftStartVec = legFrontLeft.getLocalTranslation()
            legFrontLeft.setLocalTranslation(legFrontLeftStartVec.x + 3, legFrontLeftStartVec.y, legFrontLeftStartVec.z - 3)

            legFrontRight = forms.getSpatialFromSpatialByName(spatial, "GenjiLegFrontRight")
            legFrontRightStartVec = legFrontRight.getLocalTranslation()
            legFrontRight.setLocalTranslation(legFrontRightStartVec.x + 3, legFrontRightStartVec.y, legFrontRightStartVec.z + 3)

            legBackLeft = forms.getSpatialFromSpatialByName(spatial, "GenjiLegBackLeft")
            legBackLeftStartVec = legBackLeft.getLocalTranslation()
            legBackLeft.setLocalTranslation(legBackLeftStartVec.x - 3, legBackLeftStartVec.y, legBackLeftStartVec.z - 3)

            legBackRight = forms.getSpatialFromSpatialByName(spatial, "GenjiLegBackRight")
            legBackRightStartVec = legBackRight.getLocalTranslation()
            legBackRight.setLocalTranslation(legBackRightStartVec.x - 3, legBackRightStartVec.y, legBackRightStartVec.z + 3)

            tail = forms.getSpatialFromSpatialByName(spatial, "GenjiTail")
            tailStartVec = tail.getLocalTranslation()
            tail.setLocalTranslation(tailStartVec.x - 10, tailStartVec.y, tailStartVec.z)


    def beforeUnload(self):
        persident = {}

        if hasattr(self, "divedUp"):
            persident["divedUp"] = self.divedUp

        if hasattr(self, "leftShell"):
            persident["leftShell"] = self.leftShell

        return {
            "persistent": persident
        }
