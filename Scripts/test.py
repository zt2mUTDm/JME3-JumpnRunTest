from base.actor import Actor
from base import globals as glob

class Test(Actor):
    def onInit(self):
        print "Init Test"
        
    def onUpdate(self, tpf):
        glob.forms[1].activate(self)

