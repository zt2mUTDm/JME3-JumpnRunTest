from base import globals as glob
from base import forms

from base.actor import Actor

from com.jme3.math import Vector3f
from com.jme3.util import TempVars
from com.jme3.math import FastMath
from com.jme3.bullet.control import RigidBodyControl

NOT_SEEN_FOLLOW_TIME = 5

class ViewTest(Actor):
    def __init__(self):
        super(ViewTest, self).__init__()
    
    def onInit(self):
        self.seePlayer = False
    
    def onFirstUpdate(self, tpf):
        self.lookForForm("Jaime", glob.getPlayer(), 45, 25)
    
    def onUpdate(self, tpf):
        if self.seePlayer:
            self._updateMovement()
        elif hasattr(self, "notSeeCounter"):
            self.notSeeCounter-= tpf
            if self.notSeeCounter <= 0:
                self.setVelocity(Vector3f.ZERO)
                del self.notSeeCounter
            else:
                self._updateMovement()
            
            #spatial.rotate(0, tpf, 0)
    
    def _updateMovement(self):
        spatial = forms.getSpatialFromInstance(self)
        
        tmp = TempVars.get()
        
        ownLocation = spatial.getLocalTranslation()
        playerLocation = glob.getPlayerSpatial().getLocalTranslation()
            
        vec = tmp.vect1.set(playerLocation)
        vec.subtractLocal(ownLocation).normalizeLocal().multLocal(0.2)
        self.setVelocity(vec)
        
        angel = FastMath.atan2(ownLocation.x - playerLocation.x, ownLocation.z - playerLocation.z)
        
        tmp.quat1.fromAngleNormalAxis(angel + 3.1412, Vector3f.UNIT_Y)
        self.setViewDirection(tmp.quat1)
        
        tmp.release()
    
    def onLineOfSightEnter(self, target, contactName):
        self.seePlayer = True
    
    def onLineOfSightLeave(self, target, contactName):
        self.seePlayer = False
        self.notSeeCounter = NOT_SEEN_FOLLOW_TIME
            
    def onLineOfSight(self, target, contactName):
        pass #print "Line of sight"