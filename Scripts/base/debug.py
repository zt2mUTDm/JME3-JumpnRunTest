from base import globals as glob

from com.jme3.bullet import BulletAppState

def setBulletDebugEnabled(b):
    bullet = glob.getAppState(BulletAppState)
    if bullet == None:
        raise "BulletAppState not found"
    bullet.setDebugEnabled(b)
    
def isBulletDebugEnabled():
    bullet = glob.getAppState(BulletAppState)
    if bullet == None:
        raise "BulletAppState not found"
    return bullet.isDebugEnabled()

def setStatsVisible(b):
    app = glob.getApp()
    app.setDisplayFps(b)
    app.setDisplayStatView(b)