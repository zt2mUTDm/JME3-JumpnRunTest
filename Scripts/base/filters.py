from base import globals as glob

from misc import CallPythonRunnable

from online.money_daisuki.api.monkey.basegame.filter import FadeAppState

onFadeListeners = {}

def registerForFadeEvents(target):
    onFadeIn = getattr(target, 'onFadeIn')
    onFadeOut = getattr(target, 'onFadeOut')
    
    onFadeInListener = CallPythonRunnable(onFadeIn)
    onFadeOutListener = CallPythonRunnable(onFadeOut)
    
    global onFadeListeners
    onFadeListeners[target] = (onFadeInListener, onFadeOutListener)
    
    fade = glob.getAppState(FadeAppState)
    fade.addFadeInListener(onFadeInListener)
    fade.addFadeOutListener(onFadeOutListener)
    
def unregisterForFadeEvents(target):
    if target not in onFadeListeners:
        return
    
    global onFadeListeners
    listeners = onFadeListeners.pop(target)
    
    fade = glob.getAppState(FadeAppState)
    fade.removeFadeInListener(listeners[0])
    fade.removeFadeOutListener(listeners[1])
    
def fadeOut():
    fade = glob.getAppState(FadeAppState)
    fade.fadeOut()

def fadeIn():
    fade = glob.getAppState(FadeAppState)
    fade.fadeIn()
