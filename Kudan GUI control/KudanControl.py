from pywinauto.application import Application
import time
from os import listdir, remove
from os.path import isfile, join
import win32api, win32con
from pywinauto.timings import Timings
import pywinauto.keyboard
import pywinauto.mouse

Timings.Slow()

delay = 1

winProjectPath = '.'
rawFilesPath = 'D://YandexDisk/_uploads_temp'
winRawFilesPath = 'D:\YandexDisk\_uploads_temp'
winAssetsPath = 'D:\YandexDisk\_assets_ready'

def click(x,y):
    pywinauto.mouse.click(button='left', coords=(x, y))

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
    app = Application().start("KudanARToolkit\KudanARToolkit.exe")
    time.sleep(delay*2)
    print ("Kudan started")
    # app.connect_(path = "Kudan AR Toolkit")
    KudanWindow = app.KudanARToolkit
    KudanWindow.Maximize()
    return app

def markerProjectOpen(app, projectPath, projectName):
    markerProjectOpenPress()
    OpenWindow = app.Open
    time.sleep(delay)
    prefix = ''
    if projectPath != '.':
        prefix = projectPath + '\\'
    pywinauto.keyboard.SendKeys(prefix + projectName + "{ENTER}")

def addMarker(app, markerPath, markerName):
    addMarkerPress()
    OpenWindow = app.Open
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(markerPath+'\\'+markerName + "{ENTER}")

def addModel(app, modelPath, modelName):
    openModelPress()
    OpenWindow = app.Open
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(modelPath+'\\'+modelName + "{ENTER}")

def addAlpha(app, alphaPath, alphaName):
    openAlphaPress()
    OpenWindow = app.Open
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(alphaPath+'\\'+alphaName + "{ENTER}")

def selectSave(app, modelPath, modelName):
    selectSaveLocationPress()
    OpenWindow = app.Open
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(modelPath+'\\'+modelName + "{ENTER}")

def exportMarker(app, markerPath, markerName):
    exportMarkerPress()
    SaveWindow = app.SaveAs
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(markerPath+'\\'+markerName + "{ENTER}")

def exportModel(app, modelPath, modelName):
    selectSaveLocationPress()
    SaveWindow = app.SaveAs
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(modelPath+'\\'+modelName + "{ENTER}")
    convertModelPress()

def exportAlpha(app, alphaPath, alphaName):
    saveAlphaPress()
    SaveWindow = app.SaveAs
    time.sleep(delay)
    pywinauto.keyboard.SendKeys(alphaPath+'\\'+alphaName + "{ENTER}")
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
    

app = kudanStartMaximize()
markerProjectOpen(app, winProjectPath, 'prj.ARProject')
while(True):
    rawfiles = [f for f in listdir(rawFilesPath) if isfile(join(rawFilesPath, f))]
    if rawfiles:
        for fil in rawfiles:
            
            extension = fil.split(".")[-1]
            if extension == "jpg":
                markerConverterOpen()
                addMarker(app, winRawFilesPath, fil)
                exportMarker(app, winAssetsPath, fil.split(".")[0])
                time.sleep(delay)
                removeMarker()
                remove(rawFilesPath + '/' + fil)
                print (fil + " generated")

            if extension == "png":
                alphaConverterOpen()
                for i in range(0,50):
                    addAlpha(app, winRawFilesPath, fil)
                exportAlpha(app, winAssetsPath, fil.split(".")[0])
                time.sleep(delay+5)
                removeAlpha()
                remove(rawFilesPath + '/' + fil)
                print (fil + " generated")
                
            if extension == "fbx" or extension == "obj":
                modelConverterOpen()
                addModel(app, winRawFilesPath, fil)
                exportModel(app, winAssetsPath, fil.split(".")[0])
                time.sleep(delay+5)
                remove(rawFilesPath + '/' + fil)
                print (fil + " generated")
