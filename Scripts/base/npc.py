from base import globals as glob

from base import cam
from base import messages

from base.actor import Actor

from java.util import LinkedList

class BaseNpc(Actor):
    def showTestMessageBox(self, text):
        pass

class SimpleNpc(BaseNpc):
    def onInitNpc(self):
        pass

    def _addCommand(self, command, args):
        if not hasattr(self, "commands"):
            self.commands = LinkedList()
        self.commands.addLast((command, args,))

    def setPlayerControlEnabled(self, b):
        self._addCommand(self._setPlayerControlEnabled, (b,))

    def _setPlayerControlEnabled(self, args):
        glob.player.setControlEnabled(args[0])
        return True

    def pushCameraTransform(self):
        self._addCommand(self._pushCameraTransform, ())
    
    def _pushCameraTransform(self, args):
        cam.pushCameraTransform()
        return True

    def moveCameraTo(self, location, rotation, duration):
        self._addCommand(self._moveCameraTo, (location, rotation, duration,))

        if not hasattr(self, "camsToDiscard"):
            self.camsToDiscard = 1
        else:
            self.camsToDiscard = self.camsToDiscard + 1
    
    def _moveCameraTo(self, args):
        location = args[0]
        rotation = args[1]
        duration = args[2]

        self.registerForCameraEvents()
        cam.moveLinearTo(location, rotation, duration)
        return False

    def moveCameraToStored(self, duration):
        self._addCommand(self._moveCameraToStored, (duration,))

        if not hasattr(self, "camsToDiscard"):
            self.camsToDiscard = 1
        else:
            self.camsToDiscard = self.camsToDiscard + 1
    
    def _moveCameraToStored(self, args):
        data = cam.popCameraTransform()
        
        self.registerForCameraEvents()
        cam.moveLinearTo(data[0], data[1], args[0])

        return False

    def showTextString(self, text):
        self._addCommand(self._showTextString, (text,))
    
    def _showTextString(self, args):
        messages.addTextListener(self)
        messages.showTextMessageBox(args[0])

        return False

    def callMethod(self, callable, *args):
        self._addCommand(self._callMethod, (callable, args))

    def _callMethod(self, args):
        method = args[0]
        realArgs = args[1]
        method.__call__(realArgs)

        return True

    def activateTrigger(self):
        self.onInitNpc()
        self._runQueue()

    def _runQueue(self):
        if not hasattr(self, "commands"):
            return

        if self.commands.isEmpty():
            del self.commands
            self.concludeRun()
            return

        lastNonLatent = False
        while not self.commands.isEmpty():
            data = self.commands.removeFirst()
            lastNonLatent = data[0].__call__(data[1])
            if not lastNonLatent:
                break

        # End only, if the last function was non-latent
        if lastNonLatent and self.commands.isEmpty():
            del self.commands
            self.concludeRun()

    def onCameraEvent(self, eventName):
        self.unregisterForCameraEvents()
        self._runQueue()

    def onTextEvent(self, eventName):
        messages.removeTextListener(self) # TODO
        self._runQueue()

    def concludeRun(self):
        if hasattr(self, "camsToDiscard"):
            for i in range(self.camsToDiscard):
                cam.popCamera()
            del self.camsToDiscard

        self.onDone()

    def onDone(self):
        pass
        
        