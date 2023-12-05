from base import globals as glob
from base import forms
from base.script import Script

from online.money_daisuki.api.monkey.basegame.character.anim import AnimControl
from online.money_daisuki.api.monkey.basegame.character.anim import OnAnimationEventListener

class Form(Script):
    def __init__(self):
        super(Form, self).__init__()
        self.animationListener = None
    
    def requestLoad(self, data):
        return True

    def beforeUnload(self):
        return None

    def onBinaryInputEvent(self, mapping, isPressed, tpf):
        pass
    
    def onAnalogInputEvent(self, mapping, value, tpf):
        pass
    
    def onAnimationEvent(self, animation, loop):
        pass
    
    def playAnimation(self, animation, loop):
        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        
        if anim == None: 
            return False
        anim.play(animation, loop)
        return True
        
    def setAnimationSpeed(self, speed):
        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        if anim == None:
            return False
        
        anim.setSpeed(speed)
        return True
        
    def cleanup(self):
        self.onCleanup()
    
    def onCleanup(self):
        pass
    
    def registerForAnimationEvents(self):
        if self.animationListener != None:
            return
        
        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        
        if anim == None:
            return
        
        self.animationListener = OnAnimationEventListener(self)
        anim.addAnimationListener(self.animationListener)
        
    def unregisterForAnimationEvents(self):
        if self.animationListener == None:
            return
        
        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        
        b = anim.removeAnimationListener(self.animationListener)
        self.animationListener = None
        
        return b
        
        
