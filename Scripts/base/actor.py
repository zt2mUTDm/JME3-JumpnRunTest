from base import globals as glob
from base import cam
import forms
from base.reference import ScriptReference

from com.jme3.math import Vector3f
from com.jme3.math import FastMath
#from com.jme3.bullet.control import CharacterControl # TODO?
from online.money_daisuki.api.monkey.basegame.character.control import CharacterControlAdapter
from online.money_daisuki.api.monkey.basegame.mayunused import LookForFormControl
from online.money_daisuki.api.monkey.basegame.test import ActorLineOfSightState
from online.money_daisuki.api.monkey.basegame.unsorted import CharacterControlTransformControl

from online.money_daisuki.api.monkey.basegame.cam import MoveCameraLinearToAppState

from com.jme3.scene import Geometry

class Actor(ScriptReference):
    def __init__(self):
        super(Actor, self).__init__()
        
        self.attachedTo = None
        self.lastAttachedLocation = None
        self.viewReceivers = None
        self.lineOfSightState = None
    
    def firstUpdate(self):
        self.onFirstUpdate(glob.actualTpf)
        
    def onFirstUpdate(self, tpf):
        pass
    
    """ Called only from game loop """
    def update(self):
        if self.attachedTo != None:
            self._updateAttachment()
        self.onUpdate(glob.actualTpf)

    def onUpdate(self, tpf):
        # Empty default implementation
        pass
    
    def cleanup(self):
        super(Actor, self).cleanup()

        if hasattr(self, "registeredShapesForActivation"):
            for s in self.registeredShapesForActivation:
                glob.removeForActivation(self, s)

        if hasattr(self, "registeredCameraListener"):
            self.unregisterForCameraEvents()

    def attachTo(self, target):
        self.attachedTo = target.form.getPhysicsObject().getSpatial()
        
    def detachFrom(self):
        self.attachedTo = None
        self.lastAttachedLocation = None
        
    def lookForForm(self, spatialName, target, minDistance, minAngle):
        spatial = forms.getSpatialFromInstance(self)
        
        if spatial.getName() == spatialName:
            viewSpatial = spatial
        else:
            viewSpatial = spatial.getChild(spatialName)
        
        if viewSpatial == None:
            raise Exception("Spatial %s not found in %s" % (spatialName, spatial))
        
        self.stopLookingForForm()
        self.lineOfSightState = ActorLineOfSightState(self, viewSpatial, "Jaime", target, minDistance, minAngle * FastMath.DEG_TO_RAD)
        glob.getStateManager().attach(self.lineOfSightState)
        
    def stopLookingForForm(self):
        if self.lineOfSightState != None:
            return
        
        glob.getStateManager().detach(self.lineOfSightState)
        self.lineOfSightState = None
        
    def addViewReceiver(self, spatialName):
        spatial = forms.getSpatialFromInstance(self)
        targetSpatial = spatial.getChild(spatialName)
        
        if targetSpatial == None:
            raise Exception("Spatial %s not found in %s" % (spatialName, spatial))
        """elif not isinstance(targetSpatial, Geometry):
            raise Exception("Spatial %s is no geometry " % (targetSpatial))"""
        
        if self.viewReceivers == None:
            self.viewReceivers = set()
        self.viewReceivers.add(spatial)
    
    def hasViewReceiver(self):
        return(len(self.viewReceivers) > 0)
    
    def getViewReceiver(self):
        if self.viewReceivers == None:
           return None
        
        for e in self.viewReceivers:
            break
        return e
    
    def removeViewReceiver(self, spatialName):
        spatial = forms.getSpatialFromInstance(self)
        targetSpatial = spatial.getChild(spatialName)
        
        if viewSpatial == None:
            raise Exception("Spatial % not found in %s" % (spatialName, spatial))
        
        if targetSpatial in self.viewReceivers:
            if len(self.viewReceivers) == 1:
                self.viewReceivers = None
            else:
                self.viewReceivers.remove(targetSpatial)
                
    def getViewReceivers(self):
        return(self.viewReceivers)
        
    def setVelocity(self, vec):
        if not hasattr(self, "transCtrl"):
            spatial = forms.getSpatialFromInstance(self)
            self.transCtrl = spatial.getControl(CharacterControlTransformControl)
        
        self.transCtrl.setVelocity(vec)
        
        if vec.equals(Vector3f.ZERO):
            del self.transCtrl
        
    def setViewDirection(self, quat):
        if not hasattr(self, "transCtrl"):
            spatial = forms.getSpatialFromInstance(self)
            transCtrl = spatial.getControl(CharacterControlTransformControl)
            transCtrl.setViewDirection(quat)
        else:
            self.transCtrl.setViewDirection(quat)
    
    def _updateAttachment(self):
        thisAttachedLocation = self.attachedTo.getLocalTranslation();
        if self.lastAttachedLocation != None:
            mySpatial = forms.getSpatialFromInstance(self)
            
            myVec = mySpatial.getLocalTranslation();
            diffX = (thisAttachedLocation.x - self.lastAttachedLocation.x)
            diffY = (thisAttachedLocation.y - self.lastAttachedLocation.y)
            diffZ = (thisAttachedLocation.z - self.lastAttachedLocation.z)
            
            """myVec.x+= (thisAttachedLocation.x - self.lastAttachedLocation.x);
            myVec.y+= (thisAttachedLocation.y - self.lastAttachedLocation.y);
            myVec.z+= (thisAttachedLocation.z - self.lastAttachedLocation.z);"""
            
            cc = mySpatial.getControl(CharacterControlAdapter)
            if cc != None:
                vec = Vector3f()
                cc.getPhysicsLocation(vec)
                vec.addLocal(diffX, diffY, diffZ)
                cc.setPhysicsLocation(vec)
            else:
                pass
            
            """rigid = mySpatial.getControl(RigidBodyControl)
            if rigid != None:
                if rigid.isDynamic():
                    c.setLinearVelocity(null);
                else:
                    mySpatial.setLocalTranslation(myVec);
            else:
                mySpatial.setLocalTranslation(myVec);"""
            
            self.lastAttachedLocation.set(thisAttachedLocation);
        else:
            self.lastAttachedLocation = Vector3f(thisAttachedLocation);
    
    def onLineOfSightEnter(self, target, contactName):
        pass
    
    def onLineOfSightLeave(self, target, contactName):
        pass
    
    def onLineOfSight(self, target, contactName):
        pass
        
    def registerForCameraEvents(self):
        if hasattr(self, "registeredCameraListener"):
            return

        cam.addCameraListener(self)
        self.registeredCameraListener = True

    def unregisterForCameraEvents(self):
        if not hasattr(self, "registeredCameraListener"):
            return

        if self.registeredCameraListener:
            cam.removeCameraListener(self)
            del self.registeredCameraListener

    def registerForActivation(self, spatialName):
        spatial = glob.getSpatialFromSpatialByName(getSpatialFromInstance(self), spatialName)
        if spatial == None:
            raise Exception("Spatial %s not found" % spatialName)

        ghost = spatial.getControl(GhostControl)

        if ghost == None:
            raise Exception("Spatial %s not found" % spatialName)

        if not hasattr(self, "registeredShapesForActivation"):
            self.registeredShapesForActivation = []

        self.registeredShapesForActivation.append(ghost)
        glob.addForActivation(ghost, self)
        
    def unregisterForActivation(self, spatialName):
        if not hasattr(self, "registeredShapesForActivation"):
            return

        spatial = glob.getSpatialFromSpatialByName(getSpatialFromInstance(self), spatialName)
        if spatial == None:
            raise Exception("Spatial %s not found" % spatialName)

        ghost = spatial.getControl(GhostControl)

        if ghost == None:
            raise Exception("Spatial %s not found" % spatialName)

        if ghost not in self.registeredShapesForActivation:
            return

        self.registeredShapesForActivation.remove(ghost)
        glob.removeForActivation(ghost, self)

        if len(self.registeredShapesForActivation) == 0:
            del self.registeredShapesForActivation
        


