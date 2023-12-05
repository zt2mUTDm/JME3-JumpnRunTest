from base import globals as glob

from com.jme3.input.controls import KeyTrigger
from com.jme3.input.controls import MouseButtonTrigger

from online.money_daisuki.api.monkey.basegame.input import PressedInputMappingListener
from online.money_daisuki.api.monkey.basegame.input import OnBinaryInputListener
from online.money_daisuki.api.monkey.basegame.input import OnAnalogInputListener
from online.money_daisuki.api.monkey.basegame.input import JoystickEventListener

pressedListener = PressedInputMappingListener();

registersOnBinaryKeyEventsMappings = {}
registersOnBinaryKeyEventsListeners = {}
registersOnAnalogKeyEventsMappings = {}
registersOnAnalogKeyEventsListeners = {}

def registerKeyMapping(mapping, keyCode):
    _registerBinaryMapping(mapping, KeyTrigger(keyCode))
    
def registerMouseButtonMapping(mapping, mouseButton):
    _registerBinaryMapping(mapping, MouseButtonTrigger(mouseButton))
    
def _registerBinaryMapping(mapping, trigger):
    input = glob.getInputManager()
    input.addMapping(mapping, trigger)
    input.addListener(pressedListener, mapping)
    
def unregisterMapping(mapping):
    input = glob.getInputManager()
    input.deleteMapping(mapping);

def isKeyPressed(mapping):
    return pressedListener.isKeyPressed(mapping)

def registerForBinaryInputEvent(target, *args):
    if len(args) == 0:
        return
    
    global registersOnBinaryKeyEventsMappings
    global registersOnBinaryKeyEventsListeners
    
    input = glob.getInputManager()
    
    if target not in registersOnBinaryKeyEventsMappings:
        registersOnBinaryKeyEventsMappings[target] = set()
        registersOnBinaryKeyEventsListeners[target] = OnBinaryInputListener(target)
    
    listener = registersOnBinaryKeyEventsListeners[target]
    
    for arg in args:
        if arg not in registersOnBinaryKeyEventsMappings:
            input.addListener(listener, arg)
            registersOnBinaryKeyEventsMappings[target].add(arg)
            
def unregisterForBinaryInputEvent(target, *args):
    if len(args) == 0:
        raise "Expect at least one mapping"
    
    global registersOnBinaryKeyEventsMappings
    global registersOnBinaryKeyEventsListeners
    
    if target not in registersOnBinaryKeyEventsMappings:
        return
    
    input = glob.getInputManager()
    
    listener = registersOnBinaryKeyEventsListeners[target]
    input.removeListener(listener)
    
    mappings = registersOnBinaryKeyEventsMappings[target]
    
    for arg in args:
        if arg in mappings:
            mappings.remove(arg)
    
    if len(mappings) > 0:
        for arg in mappings:
            input.addListener(listener, arg)
    else:
        registersOnBinaryKeyEventsMappings.pop(target)
        registersOnBinaryKeyEventsListeners.pop(target)
        
def registerForAnalogInputEvent(target, *args):
    if len(args) == 0:
        return
    
    global registersOnAnalogKeyEventsMappings
    global registersOnAnalogKeyEventsListeners
    
    input = glob.getInputManager()
    
    if target not in registersOnAnalogKeyEventsMappings:
        registersOnAnalogKeyEventsMappings[target] = set()
        registersOnAnalogKeyEventsListeners[target] = OnAnalogInputListener(target)
    
    listener = registersOnAnalogKeyEventsListeners[target]
    
    for arg in args:
        if arg not in registersOnAnalogKeyEventsMappings:
            input.addListener(listener, arg)
            registersOnAnalogKeyEventsMappings[target].add(arg)
            
def unregisterForAnalogInputEvent(target, *args):
    if len(args) == 0:
        raise "Expect at least one mapping"
    
    global registersOnAnalogKeyEventsMappings
    global registersOnAnalogKeyEventsListeners
    
    if target not in registersOnAnalogKeyEventsMappings:
        return
    
    input = glob.getInputManager()
    
    listener = registersOnAnalogKeyEventsListeners[target]
    
    input.removeListener(listener)
    
    for arg in args:
        registersOnAnalogKeyEventsMappings[target].remove(arg)
    
    if len(registersOnAnalogKeyEventsMappings[target]) > 0:
        for arg in registersOnAnalogKeyEventsMappings[target]:
            input.addListener(listener, arg)
    else:
        registersOnAnalogKeyEventsMappings.remove(target)
        registersOnAnalogKeyEventsListeners.remove(target)
        
# Joystick
def _isJoystickListenerSet():
    return "joystickListener" in globals()

def _ensureJoystickListener():
    if not _isJoystickListenerSet():
        imgr = glob.getInputManager()
        global joystickListener
        joystickListener = JoystickEventListener(imgr)
        imgr.addRawInputListener(joystickListener)
        
def _removeJoystickListenerIfEmpty():
    global joystickListener
    if _isJoystickListenerSet() and joystickListener.getAxisMappingCount() == 0 and joystickListener.getAxisListenerCount() == 0 \
            and joystickListener.getButtonMappingCount() == 0 and joystickListener.getButtonListenerCount() == 0:
        imgr = glob.getInputManager()
        global joystickListener
        imgr.removeRawInputListener(joystickListener)
        del globals()["joystickListener"]

def addJoystickAxisMappings(axis, *args):
    _ensureJoystickListener()
    joystickListener.addAxisMappings(axis, *args)
    
def registerForJoystickAxisInputEvents(target, *args):
    _ensureJoystickListener()
    joystickListener.addAxisListener(target, *args)
    
def removeJoystickAxisMappings(*args):
    if _isJoystickListenerSet():
        joystickListener.removeAxisMappings(args)
        _removeJoystickListenerIfEmpty()
    
def unregisterForJoystickAxisInputEvents(target):
    if _isJoystickListenerSet():
        joystickListener.removeAxisListener(target)
        _removeJoystickListenerIfEmpty()
        
def addJoystickButtonMappings(button, *args):
    _ensureJoystickListener()
    joystickListener.addButtonMappings(button, *args)
    
def removeJoystickButtonMappings(*args):
    if _isJoystickListenerSet():
        joystickListener.removeButtonMappings(args)
        _removeJoystickListenerIfEmpty()
        
def registerForJoystickButtonInputEvents(target, *args):
    _ensureJoystickListener()
    joystickListener.addButtonListener(target, *args)
    
def unregisterForJoystickButtonInputEvents(target):
    if _isJoystickListenerSet():
        joystickListener.removeButtonListener(target)
        _removeJoystickListenerIfEmpty()
    