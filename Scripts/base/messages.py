from java.lang import Object
from java.lang import Runnable

from base import globals as glob

from online.money_daisuki.api.monkey.basegame.text import ShowMessageAppState
from online.money_daisuki.api.monkey.basegame.collections import SafeSet

textListeners = None

def addTextListener(listener):
    global textListeners
    if textListeners is None:
        textListeners = SafeSet(Object, SafeSet.HashSetFactory())
    textListeners.add(listener)

def removeTextListener(listener):
    global textListeners
    if textListeners is None:
        return False

    b = textListeners.remove(listener)
    
    if textListeners.isEmpty():
        textListeners = None

def fireTextListeners(eventName):
    global textListeners
    if textListeners is not None:
        buf = textListeners.getArray()
        for l in buf:
            l.onTextEvent(eventName)

class ShowMessageAppStateListener(Runnable):
    def __init__(self, myState):
        self.myState = myState

    def run(self):
        glob.detachState(self.myState)
        self.myState.unregisterInputListener(glob.getInputManager())
        self.myState.unregisterDefaultTriggers(glob.getInputManager())
        fireTextListeners("ShowMessageAppStateDone")

def showTextMessageBox(text):
    state = ShowMessageAppState(text)
    state.registerInputListener(glob.getInputManager())
    state.registerDefaultTriggers(glob.getInputManager())
    state.addDoneListener(ShowMessageAppStateListener(state))
    glob.attachState(state)
    pass
