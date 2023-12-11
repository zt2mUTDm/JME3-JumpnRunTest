from base import globals as glob

from base.actor import Actor

class SPSTTouchKeepButton(Actor):
    def requestLoad(self, data):
        persident = data["persistent"]
        if "pressed" in persident:
            self.pressed = persident["pressed"]
        return True

    def beforeUnload(self):
        persident = {}

        if hasattr(self, "pressed"):
            persident["pressed"] = self.pressed

        return {
            "persistent": persident
        }

    def onInit(self):
        if hasattr(self, "pressed") and self.pressed:
            self._setPressed()
        else:
            self.registerForTouchEvent("SPSTTouchKeepButton0")
            self.registerForAnimationEvents()

    def onTouchEnter(self, myName, otherForm, otherName):
        if otherForm == glob.getPlayer() and otherName == "Ground":
            if self._isUnpressed() or self._isUnpressing():
                self._setPressing()

    def onTouchLeave(self, myName, otherForm, otherName):
        if otherForm == glob.getPlayer():
            if self._isPressing():
                self._setUnpressing()

    def _hasState(self):
        return hasattr(self, "state")

    def _isUnpressed(self):
        return not self._hasState()

    def _setUnpressed(self):
        self.playAnimation("Unpressed", False)
        if self._hasState:
            del self.state
    
    def _isUnpressing(self):
        return self._hasState() and self.state == "unpressing"

    def _setUnpressing(self):
        self.playAnimation("Unpressing", False)
        self.state = "unpressing"

    def _isPressing(self):
        return self._hasState() and self.state == "pressing"

    def _setPressing(self):
        self.playAnimation("Pressing", False)
        self.state = "pressing"
        
    def _isPressed(self):
        return self._hasState() and self.state == "pressed"

    def _setPressed(self):
        self.playAnimation("Pressed", False)
        self.state = "pressed"
        self.pressed = True

    def onAnimationEvent(self, animationName, loop, source):
        if animationName == "Pressing":
            self._setPressed()
            self.onSwitchActivation("default")

    def onSwitchActivation(self, type):
        pass