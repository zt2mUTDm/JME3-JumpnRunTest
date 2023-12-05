from base import globals as glob

from com.jme3.bullet import BulletAppState
from com.jme3.scene import Node
from com.jme3.bullet.control import GhostControl

from online.money_daisuki.api.monkey.basegame.py import PythonAppState

forms = {}
shapes = {}

def _addInstanceShapes(instance, spatial):
    ghost = spatial.getControl(GhostControl)
    if ghost != None:
        global shapes
        shapes[ghost.getCollisionShape()] = instance

    if isinstance(spatial, Node):
        childs = spatial.getChildren()
        for c in childs:
            _addInstanceShapes(instance, c)

def addInstance(instance, form):
    global forms
    instance.form = form
    forms[form.getId()] = instance
    _addInstanceShapes(instance, form.physicsObject.spatial)

def _removeInstanceShapes(instance, spatial):
    ghost = spatial.getControl(GhostControl)
    if ghost != None:
        global shapes
        del shapes[ghost.getCollisionShape()]

    if isinstance(spatial, Node):
        childs = spatial.getChildren()
        for c in childs:
            _removeInstanceShapes(instance, c)

def removeInstance(id):
    global forms

    form = forms[id]
    _removeInstanceShapes(form, getSpatialFromInstance(form))
    form.form = None
    forms.pop(id)
    
def getForm(id):
    global forms
    return forms[id].form

def getForm(id):
    global forms
    return forms[id].form

def getFormSpatial(id):
    return getForm(id).spatial

def getInstance(id):
    global forms
    return forms[id]

def getSpatialFromInstance(instance):
    return instance.form.physicsObject.spatial

def unload(ref):
    py = glob.getAppState(PythonAppState)
    py.removeScript(ref)
    
    spatial = getSpatialFromInstance(ref)
    spatial.removeFromParent()
    
    bullet = glob.getAppState(BulletAppState)
    bullet.getPhysicsSpace().removeAll(spatial)
    
    id = ref.form.getId()
    
    global forms
    if id in forms:
        removeInstance(id)

def getFormForShapeOrNone(shape):
    global shapes
    if shape in shapes:
        return(shapes[shape])
    return None