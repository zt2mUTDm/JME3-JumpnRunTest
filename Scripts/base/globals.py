from com.jme3.util import TempVars
from com.jme3.bullet import BulletAppState
from com.jme3.math import Vector3f
from com.jme3.math import Quaternion
from com.jme3.bullet.control import CharacterControl
from online.money_daisuki.api.monkey.basegame.character.control import CharControl
from online.money_daisuki.api.monkey.basegame.cam import FixedLocationLookAtPlayerCamera

from online.money_daisuki.api.monkey.basegame.py import PythonAppState
from online.money_daisuki.api.monkey.basegame.form import FormLoadAppState
from online.money_daisuki.api.monkey.basegame import SceneGraphAppState

actualTpf = 0
app = None
player = None
playerForm = None
activatables = set()

def setActualTpf(tpf):
    global actualTpf
    actualTpf = tpf

def addInstance(instance, form):
    raise Exception("Deprecated")

def addActivatable(ghost):
    activatables.add(ghost)

def removeActivatable(ghost):
    activatables.remove(ghost)

def removeInstance(id):
    raise Exception("Deprecated")

def getForm(id):
    raise Exception("Deprecated")

def getFormSpatial(id):
    raise Exception("Deprecated")

def getInstance(id):
    raise Exception("Deprecated")

def getSpatialFromInstance(instance):
    raise Exception("Deprecated")

def getPlayerForm():
    return(playerForm)

def getPlayerPhysicsObject():
    return(getPlayerForm().getPhysicsObject())

def getPlayerSpatial():
    return(getPlayerPhysicsObject().spatial)

def setApp(a):
    global app
    app = a
    
def getApp():
    global app
    return(app)

def getInputManager():
    global app
    return(app.getInputManager())

def getStateManager():
    global app
    return(app.getStateManager())

def attachState(state):
    return(getStateManager().attach(state))

def getState(className):
    return(getStateManager().getState(className))

def detachState(state):
    return(getStateManager().detach(state))

def getCamera():
    return(getApp().getCamera())

def getAssetManager():
    return(app.getAssetManager())

def getAppState(className):
    return(getStateManager().getState(className))

def getTempVars():
    return(TempVars.get())

def unload(ref):
    raise Exception("Deprecated")

def getPlayer():
    global player
    return player

def loadPlayer(translation=Vector3f(), rotation=Vector3f(0, 0, 0), scale=Vector3f(1, 1, 1)):
    formState = getAppState(FormLoadAppState)
    form = formState.load("Forms/Player.json")
    
    global playerForm
    playerForm = form
    
    py = getAppState(PythonAppState)
    py.addScript(form)
    
    obj = form.getPhysicsObject()
    spatial = obj.getSpatial()
    
    #from online.money_daisuki.projects.monkey.jumpnrun import OrbitMovingCamera
    #spatial.addControl(OrbitMovingCamera(getAppState(BulletAppState).getPhysicsSpace()))

    spatial.setLocalTranslation(translation)
    spatial.setLocalScale(scale)
    
    cc = spatial.getControl(CharControl)
    cc.setPhysicsLocation(translation)
    cc.setViewDirection(rotation)
   
    bullet = getAppState(BulletAppState)
    bullet.getPhysicsSpace().addAll(spatial)
    
    scene = getAppState(SceneGraphAppState)
    root = scene.getRootNode()
    root.attachChild(spatial)
    
    getPlayer().setControlEnabled(False)
