from base import globals as glob

from base import physics
from base import forms

from base.form import Form

from com.jme3.bullet.control import RigidBodyControl
from com.jme3.math import Vector3f

from online.money_daisuki.api.monkey.basegame.physobj import MoveKinematicControl

class ScriptReference(Form):
    def __init__(self):
        super(ScriptReference, self).__init__()
        self.movingControl = None
    
    def activate(self, activator):
        self.onActivation(activator, glob.actualTpf)
    
    def onActivation(self, activator, tpf):
        # Empty default implementation
        pass
    
    def handleTouch(self, myName, otherCollisionObject, otherName):
        self.onTouch(myName, forms.getFormForShapeOrNone(otherCollisionObject.getCollisionShape()), otherName)

    def onTouch(self, myName, otherForm, otherName):
        # Empty default implementation
        pass

    def handleTouchEnter(self, myName, otherCollisionObject, otherName):
        self.onTouchEnter(myName, forms.getFormForShapeOrNone(otherCollisionObject.getCollisionShape()), otherName)
    
    def onTouchEnter(self, myName, otherForm, otherName):
        # Empty default implementation
        pass

    def handleTouchLeave(self, myName, otherCollisionObject, otherName):
        self.onTouchLeave(myName, forms.getFormForShapeOrNone(otherCollisionObject.getCollisionShape()), otherName)
    
    def onTouchLeave(self, myName, otherForm, otherName):
        # Empty default implementation
        pass
    
    def signalMovingDone(self, canceled):
        if self.movingControl == None:
            return
        spatial = forms.getSpatialFromInstance(self)
        spatial.removeControl(self.movingControl)
        self.movingControl = None
        self.onMovingDone(canceled)
    
    def onMovingDone(self, canceled):
        # Empty default implementation
        pass
    
    def registerForTouchEvent(self, shapeName):
        try:
            if not hasattr(self, "touchEventShapes"):
                self.touchEventShapes = []
            
            if shapeName not in self.touchEventShapes:
                physics.registerForTouchTest(self, shapeName)

            self.touchEventShapes.append(shapeName)
        except Exception as e:
            if hasattr(self, "touchEventShapes") and len(self.touchEventShapes) == 0:
                del self.touchEventShapes
            raise e
            
    def unregisterForTouchEvent(self, shapeName):
        if hasattr(self, "touchEventShapes") and shapeName in self.touchEventShapes:
            physics.unregisterForTouchTest(self, shapeName)
            
            self.touchEventShapes.remove(shapeName)
            if len(self.touchEventShapes) == 0:
                del self.touchEventShapes
    
    def cleanup(self):
        super(ScriptReference, self).cleanup()

        if hasattr(self, "touchEventShapes"):
            for shapeName in self.touchEventShapes:
                physics.unregisterForTouchTest(self, shapeName)
    
    def moveTowardSpatial(self, target, distance):
        spatial = forms.getSpatialFromInstance(self)
        rigid = spatial.getControl(RigidBodyControl)
        
        targetDirection = Vector3f(spatial.getLocalTranslation()).subtractLocal(target.getLocalTranslation()).normalizeLocal()
        
        targetDirection.multLocal(distance)
        if rigid != None:
            if rigid.isStatic():
                raise Exception("A static object must not move")
            elif rigid.isKinematic():
                raise Exception("Not implemented")
            elif rigid.isDynamic():
                rigid.setLinearVelocity(Vector3f.UNIT_Z)
            else:
                raise Exception("Body is neither Static, Kinematic nor Dynamic. Shoult never reach")
        else:
            raise Exception("Not implemented")
    
    def moveLinearTo(self, target, speed):
        self.stopMoving()
        
        spatial = forms.getSpatialFromInstance(self)
        rigid = spatial.getControl(RigidBodyControl)
        if rigid != None:
            if rigid.isStatic():
                raise Exception("A static object must not move linear")
            elif rigid.isKinematic():
                self.movingControl = MoveKinematicControl(target, speed, self)
                spatial.addControl(self.movingControl)
            elif rigid.isDynamic():
                raise Exception("Not implemented")
            else:
                raise Exception("Body is neither Static, Kinematic nor Dynamic. Shoult never reach")
        else:
            raise Exception("Not implemented")
        
    def stopMoving(self):
        self.signalMovingDone(True)
