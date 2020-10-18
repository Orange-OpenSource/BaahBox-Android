# How to...


## Implement a new game?

To implement a new game in the app, you must follow several steps and patterns.

First, you have to define 3 layouts for your game: one to introduce the game, another for the playing mode, end a last one for restarting the game and go to playing screen.
Navigation is based on navigation controller pattern, thus you have to update the nav_graph XML file using your fragments.

About fragments, you must subclass the _AbstractGameFragment_ class. It defines some useful behaviour shared with other games: navigation, buttons, instanciation of objects... Once you subclass this class, you will have to define veriables: the previously mentionned layouts and actions for navigations. You will have to fill also some methods used to get arguments between fragments given through the navigation graph, and also methods for animations.

The _prepareSensorObserver_ is the method which will define an observer for the data broadcast by the Baah box. Your game logic starts there.

Finally you should also update the "configuration as code" systme by updating the properties file and its parser. This feature is used to quickly enable, disable and configure a game without touching the source code. Have a look on _tools/properties_ package.


## How to add new entries in properties file?

Main configuration of the app is based on _assets/app_configuration.propeties_ file. This file contains configuration details for games or other features.
You should add the new entry with values seperated by ';' and update the _PropertiesKies.kt_ file. This enumeration is used by properties parser and hides the name of the entries kies. The you will have to update the _PropertiesReader.kt_ file which will parle the proeprties file using the enum and return a data class with the configuration data.


## How to use fake BLE frames?

So as to use fake BLE frames, you can first have a look on the file at _/MockInterruptions.kit_ (in the _androidTest_) folder.
This file has primitives which permit to load from _assets_ folder a list of fake frames with the values of each sensors.
Thus you can define and use a list of interruptions defined in files with the format:

```text
# This is a comment line

# Empty line are ignored
A=0;B=50;J=0
A=0;B=70;J=0
A=0;B=90;J=0
A=0;B=110;J=0
A=0;B=130;J=0
```

In this case sensors A and joystick (J) have no value and only the sensor B has values.
The lines must follow the regular expression "A=[0-9]+;B=[0-9]+;J=[0-9]+".
Each line modeels a frame: the values of sensors A, B and Joystick are sent to the model in the same time.
Each frame is seperatedd by a time sleep.

For more details, you can have a look at _ui/fragments/AbstractInstrumentedTestSimpleGameFragmentBLE_ (in the _androidTest_).
Delay between each frame processing is defined in propeerty _timeToWaitUntilNextMockFrame_.