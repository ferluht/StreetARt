from peewee import *

ar_db = PostgresqlDatabase('arreality', user='root')

class Object(Model):
    name = CharField()
    # universe = CharField()
    augmentationType = CharField()
    textureType = CharField()
    # universe = ForeignKeyField(Universe, related_name='objects')
    # modelurl = CharField()
    # nodeurl = CharField()
    # textureurl = CharField()
    lat = FloatField()
    lng = FloatField()

    class Meta:
        database = ar_db

class Universe(Model):
    name = CharField()

    class Meta:
        database = ar_db


class User(Model):
    name = CharField()

    class Meta:
        database = ar_db
