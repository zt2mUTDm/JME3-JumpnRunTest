from base import globals as glob
from base import forms
from base.script import Script

from online.money_daisuki.api.monkey.basegame.character.anim import AnimControl
from online.money_daisuki.api.monkey.basegame.character.anim import OnAnimationEventListener

class Form(Script):
    def __init__(self):
        super(Form, self).__init__()
    
    def requestLoad(self, data):
        return True

    def beforeUnload(self):
        return None

    def onBinaryInputEvent(self, mapping, isPressed, tpf):
        pass
    
    def onAnalogInputEvent(self, mapping, value, tpf):
        pass
    

    def cleanup(self):
        self.clearAnimationEvents()
        self.onCleanup()
    
    def onCleanup(self):
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
        if anim is None:
            return False
        
        anim.setSpeed(speed)
        return True

    def getAnimationSpeed(self):
        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        if anim is None:
            return 0
        
        return anim.getSpeed()

    def registerForAnimationEvents(self, target=None):
        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        
        if anim is None:
            return
        
        if target is None:
            target = self

        animationListener = OnAnimationEventListener(target, self)
        anim.addAnimationListener(animationListener)

        if not hasattr(self, "animationListeners"):
            self.animationListeners = {}

        if target not in self.animationListeners:
            self.animationListeners[target] = []

        self.animationListeners[target].append(animationListener)
        
    def unregisterForAnimationEvents(self, target=None):
        if not hasattr(self, "animationListeners"):
            return
        
        if target is None:
            target = self

        if target not in self.animationListeners:
            return

        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        
        for listener in self.animationListeners[target]:
            anim.removeAnimationListener(listener)
        del self.animationListeners[target]

        if len(self.animationListeners) == 0:
            del self.animationListeners

    def clearAnimationEvents(self):
        if not hasattr(self, "animationListeners"):
            return

        spatial = forms.getSpatialFromInstance(self)
        anim = spatial.getControl(AnimControl)
        
        for key in self.animationListeners:
            for listener in self.animationListeners[key]:
                anim.removeAnimationListener(listener)
            del self.animationListeners[key]
        del self.animationListeners

    def onAnimationEvent(self, animation, loop, source):
        pass
