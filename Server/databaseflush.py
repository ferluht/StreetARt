from datadescription import *
from os.path import isfile, join
from os import remove, listdir

Object.drop_table()
Universe.drop_table()
User.drop_table()

assetsPath = 'def'

assets = [f for f in listdir(assetsPath) if isfile(join(assetsPath, f))]
if assets:
    for fil in assets:
        remove(assetsPath + '/' + fil)
        print fil + " removed\r"