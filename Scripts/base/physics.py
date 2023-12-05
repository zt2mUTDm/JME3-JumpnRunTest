from base import globals as glob

from online.money_daisuki.api.monkey.basegame.physobj import PythonCollisionState

def getPhysicsCollisionObject(ref, shapeName):
    return(ref.form.physicsObject.getCollision(shapeName))
    
def registerForTouchTest(ref, shapeName):
    colObj = getPhysicsCollisionObject(ref, shapeName)
    if colObj == None:
        raise Exception("PhysicsCollisionObject %s not found" % shapeName)
    
    state = glob.getAppState(PythonCollisionState)
    state.addCollisionTester(shapeName, colObj, ref)
    
def registerForTouchTest2(ref, shapeName, target):
    colObj = getPhysicsCollisionObject(ref, shapeName)
    if colObj == None:
        raise Exception("PhysicsCollisionObject %s not found" % shapeName)
    
    state = glob.getAppState(PythonCollisionState)
    state.addCollisionTester(shapeName, colObj, target)

def unregisterForTouchTest(ref, shapeName):
    colObj = getPhysicsCollisionObject(ref, shapeName)
    if colObj == None:
        raise Exception("PhysicsCollisionObject %s not found" % shapeName)
    
    state = glob.getAppState(PythonCollisionState)
    state.removeCollisionTester(colObj)
