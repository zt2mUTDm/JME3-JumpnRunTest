from base.actor import Actor
from base import globals as glob

class Test2(Actor):
    def onInit(self):
        print "Init Test2"
    
    def onUpdate(self, tpf):
        pass

    def onActivation(self, tpf, activator):
        #print "Activated"
        pass
