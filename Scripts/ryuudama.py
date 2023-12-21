from base import misc

from base.actor import Actor

from com.jme3.effect import ParticleEmitter
from com.jme3.effect import ParticleMesh
from com.jme3.material import Material
from com.jme3.math import Vector3f
from com.jme3.scene.Spatial import CullHint

DEFAULT_SPEED = 1.0
ACCELATION = 10.0
MAX_SPEED = 16.0

SHRINK_SPEED = 1
GROW_SPEED = 1
MAX_GROW_SIZE = 0.5

WAITTIME = 1

PARTICLES_AMOUNT = 240
PARTICLE_WAIT = 1

class Ryuudama(Actor):
    def onInit(self):
        self.state = "Init"
        self.speed = DEFAULT_SPEED
        
        self.size = 1

        self.registerForTouchEvent("Ryuudama")

    def onUpdate(self, tpf):
        spatial = misc.getSpatialFromInstance(self)
        spatial.rotate(0, tpf * self.speed, 0)
        
        if self.state == "Accelate":
            self.speed+= ACCELATION * tpf
            if self.speed > MAX_SPEED:
                self.speed = MAX_SPEED
                self.state = "Shrink"
        elif self.state == "Shrink":
            spatial = misc.getSpatialFromInstance(self)
            self.size-= tpf * SHRINK_SPEED
            if self.size <= 0:
                self.size = 0
                spatial.setLocalScale(0)
                spatial.setLocalTranslation(0, 0, 0)
                
                attachPoint = misc.getChildSpatial(misc.getPlayerSpatial(), "RyuudamaCollectAttach")
                
                spatial.removeFromParent()
                attachPoint.attachChild(spatial)
                
                self.state = "Grow"
            else:
                spatial.setLocalScale(self.size)
        elif self.state == "Grow":
            spatial = misc.getSpatialFromInstance(self)
            self.size+= tpf * MAX_GROW_SIZE
            if self.size >= MAX_GROW_SIZE:
                spatial.setLocalScale(MAX_GROW_SIZE)
                self.waittime = WAITTIME
                self.state = "Wait"
            else:
                spatial.setLocalScale(self.size)
        elif self.state == "Wait":
            self.waittime-= tpf
            if self.waittime <= 0:
                misc.getSpatialFromInstance(self).setCullHint(CullHint.Always)
                
                emitter = ParticleEmitter("My explosion effect", ParticleMesh.Type.Triangle, PARTICLES_AMOUNT);
                emitter.setStartSize(0.5)
                emitter.setEndSize(0.5)
                emitter.setFacingVelocity(True)
                emitter.setParticlesPerSec(0)
                emitter.setGravity(0, 5, 0)
                emitter.setLowLife(1.1)
                emitter.setHighLife(1.5)
                emitter.getParticleInfluencer().setInitialVelocity(Vector3f(0, 20, 0))
                emitter.getParticleInfluencer().setVelocityVariation(1)
                emitter.setImagesX(1)
                emitter.setImagesY(1)
                
                asset = misc.getAssetManager()
                mat = Material(asset, "Common/MatDefs/Misc/Particle.j3md")
                mat.setTexture("Texture", asset.loadTexture("Effects/Explosion/spark.png"))
                emitter.setMaterial(mat)
                        
                attachPoint = misc.getChildSpatial(misc.getPlayerSpatial(), "RyuudamaCollectAttach")
                attachPoint.attachChild(emitter)
                
                emitter.emitAllParticles()
                
                self.waittime = PARTICLE_WAIT
                self.state = "Particles"
        elif self.state == "Particles":
            self.waittime-= tpf
            if self.waittime <= 0:
                self.state = "Done"
                misc.unload(self)
    
    def onTouch(self, myName, otherForm, otherName):
        if not self.collected and otherName == "PlayerShape":
            self.collected = True
            self.unregisterForTouchEvent("Ryuudama")
            
            self.state = "Accelate"

    def requestLoad(self, data):
        if "collected" in data["persistent"]:
            self.collected = data["persistent"]["collected"]
        else:
            self.collected = False
        return not self.collected
            
    def beforeUnload(self):
        if not self.collected:
            return None
        
        return ({
            "persistent": {
                "collected": self.collected
            }
        })