from base import globals as glob

from base import physics

from base.reference import ScriptReference
from base.actor import Actor

class SwitchA(Actor):
    def __init__(self):
        super(SwitchA, self).__init__()
        self.attached = False
    
    def init(self):
        physics.registerForTouchTest(self, "SwitchMiddle")
        #self.attachTo(glob.getForm(1))
        pass
    
    def onTouch(self, myName, otherForm, otherName):
        #print "%s-%s" % (myName, otherName)
        #if otherName == "PlayerShape":
        #    self.attachTo(glob.getInstance(1))
        pass
            
    def onTouchEnter(self, myName, otherForm, otherName):
        #print "Enter: %s-%s" % (myName, otherName)
        pass
        
    def onTouchLeave(self, myName, otherForm, otherName):
        #print "Exit: %s-%s" % (myName, otherName)
        pass