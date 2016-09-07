from pywinauto.application import Application
import time
from os import listdir, remove
from os.path import isfile, join
import win32api, win32con
from pywinauto.timings import Timings

Timings.Slow()

winProjectPath = 'D:\python_server\wincontrol'
rawFilesPath = 'D://YandexDisk/_uploads_temp'
winRawFilesPath = 'D:\YandexDisk\_uploads_temp'
winAssetsPath = 'D:\YandexDisk\_assets_ready'

def click(x,y):
    win32api.SetCursorPos((x,y))
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN,x,y,0,0)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP,x,y,0,0)

def addMarkerPress():
    click(350, 740)

def exportMarkerPress():
    click(680, 40)

def markerConverterPress():
    click(50, 40)

def modelConverterPress():
    click(50, 70)

def alphaConverterPress():
    click(50, 100)

def openModelPress():
    click(660, 110)

def openAlphaPress():
    click(660, 70)

def saveAlphaPress():
    click(660, 200)

def selectSaveLocationPress():
    click(660, 230)

def convertModelPress():
    click(420, 740)

def convertAlphaPress():
    click(660, 290)

def selectMarkerPress():
    click(450, 180)

def removeMarkerPress():
    click(440, 740)

def removeAlphaPress():
    click(210, 750)

def markerProjectOpenPress():
    click(240, 50)

def kudanStartMaximize():
    app = Application().start("KudanARToolkit.exe")
    time.sleep(3)
    print "Kudan started"
    app.connect_(path = "Kudan AR Toolkit")
    KudanWindow = app.KudanARToolkit
    KudanWindow.Maximize()

def markerProjectOpen(projectPath, projectName):
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    markerProjectOpenPress()
    OpenWindow = app.Open
    OpenWindow.Wait('ready')
    OpenWindow.Edit.SetText(projectPath+'\\'+projectName)
    OpenWindow.TypeKeys("{ENTER}")

def addMarker(markerPath, markerName):
    addMarkerPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    OpenWindow = app.Open
    OpenWindow.Wait('ready')
    OpenWindow.Edit.SetText(markerPath+'\\'+markerName)
    OpenWindow.TypeKeys("{ENTER}")

def addModel(modelPath, modelName):
    openModelPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    OpenWindow = app.Open
    OpenWindow.Wait('ready')
    OpenWindow.Edit.SetText(modelPath+'\\'+modelName)
    OpenWindow.TypeKeys("{ENTER}")

def addAlpha(alphaPath, alphaName):
    openAlphaPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    OpenWindow = app.Open
    OpenWindow.Wait('ready')
    OpenWindow.Edit.SetText(alphaPath+'\\'+alphaName)
    OpenWindow.TypeKeys("{ENTER}")

def selectSave(modelPath, modelName):
    selectSaveLocationPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    OpenWindow = app.Open
    OpenWindow.Wait('ready')
    OpenWindow.Edit.SetText(modelPath+'\\'+modelName)
    OpenWindow.TypeKeys("{ENTER}")

def exportMarker(markerPath, markerName):
    exportMarkerPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    SaveWindow = app.SaveAs
    SaveWindow.Wait('ready')
    SaveWindow.Edit.SetText(markerPath+'\\'+markerName)
    SaveWindow.TypeKeys("{ENTER}")

def exportModel(modelPath, modelName):
    selectSaveLocationPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    SaveWindow = app.SaveAs
    SaveWindow.Wait('ready')
    SaveWindow.Edit.SetText(modelPath+'\\'+modelName)
    SaveWindow.TypeKeys("{ENTER}")
    convertModelPress()

def exportAlpha(alphaPath, alphaName):
    saveAlphaPress()
    app = Application()
    app.connect_(path = "Kudan AR Toolkit")
    SaveWindow = app.SaveAs
    SaveWindow.Wait('ready')
    SaveWindow.Edit.SetText(alphaPath+'\\'+alphaName)
    SaveWindow.TypeKeys("{ENTER}")
    convertAlphaPress()

def removeMarker():
    selectMarkerPress()
    removeMarkerPress()

def removeAlpha():
    removeAlphaPress()

def markerConverterOpen():
    markerConverterPress()

def modelConverterOpen():
    modelConverterPress()

def alphaConverterOpen():
    alphaConverterPress()
    

kudanStartMaximize()
markerProjectOpen(winProjectPath, 'temp.ARProject')

while(True):
    rawfiles = [f for f in listdir(rawFilesPath) if isfile(join(rawFilesPath, f))]
    if rawfiles:
        for fil in rawfiles:
            
            extension = fil.split(".")[-1]
            if extension == "jpg":
                markerConverterOpen()
                addMarker(winRawFilesPath, fil)
                exportMarker(winAssetsPath, fil.split(".")[0])
                time.sleep(1)
                removeMarker()
                remove(rawFilesPath + '/' + fil)
                print fil + " generated"

            if extension == "png":
                alphaConverterOpen()
                for i in range(0,50):
                    addAlpha(winRawFilesPath, fil)
                exportAlpha(winAssetsPath, fil.split(".")[0])
                time.sleep(5)
                removeAlpha()
                remove(rawFilesPath + '/' + fil)
                print fil + " generated"
                
            if extension == "fbx" or extension == "obj":
                modelConverterOpen()
                addModel(winRawFilesPath, fil)
                exportModel(winAssetsPath, fil.split(".")[0])
                time.sleep(5)
                remove(rawFilesPath + '/' + fil)
                print fil + " generated"
