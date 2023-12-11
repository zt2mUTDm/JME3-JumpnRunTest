from java.lang import Object
from java.lang import Runnable


from com.jme3.bullet.control import RigidBodyControl
from com.jme3.math import Vector3f
from com.jme3.util import SafeArrayList


from online.money_daisuki.api.monkey.basegame.physobj import MoveKinematicControl


from base import globals as glob

from base import physics
from base import forms

from base.form import Form

class MoveKinematicControlListener(Runnable):
        def __init__(self, parent, control):
            self.parent = parent
            self.control = control

        def run(self):
            self.control.removeListener(self)
            self.parent._fireMovementEvents("LinearMoving")

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
    

    # Moving
    def moveLinearTo(self, target, speed):
        #self.stopMoving()
        
        spatial = forms.getSpatialFromInstance(self)
        rigid = spatial.getControl(RigidBodyControl)
        if rigid != None:
            if rigid.isStatic():
                raise Exception("A static object must not move linear")
            elif rigid.isKinematic():
                movingControl = MoveKinematicControl(target, speed)
                movingControl.addListener(MoveKinematicControlListener(self, movingControl))
                spatial.addControl(movingControl)
            elif rigid.isDynamic():
                raise Exception("Not implemented")
            else:
                raise Exception("Body is neither Static, Kinematic nor Dynamic. Shoult never reach")
        else:
            raise Exception("Not implemented")
        
    def _fireMovementEvents(self, eventName):
        if not hasattr(self, "movementListeners"):
            return

        for listener in self.movementListeners:
            listener.onMovingEvent(eventName, self)

    def registerForMovementEvents(self, target=None):
        if target is None:
            target = self

        if not hasattr(self, "movementListeners"):
            self.movementListeners = SafeArrayList(Object)
        self.movementListeners.add(target)

    def unregisterForMovementEvents(self, target=None):
        if not hasattr(self, "movementListeners"):
            return
        
        if target is None:
            target = self

        self.movementListeners.remove(target)
        if self.movementListeners.isEmpty():
            del self.movementListeners

    def onMovingEvent(self, eventName, source):
        pass
