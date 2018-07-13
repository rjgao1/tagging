# tagging
You can also find the following instructions in help.txt under phase2

To run the application on the teaching lab computer, please run the following command:
$ cd phase2
$ cd src
$ javac -cp ".:./junit-4.12.jar" */*.java
$ java -cp ".:./junit-4.12.jar" FrontEnd.Main

=========

Specification:
1. After changing configurations, please restart the application or open a new window and close the old window.

2. Please do not move or rename image files under root directory using the OS, or you will lose the history of the file. If you move and rename image files under root directory when application is running, the application may crash.

3. Please do not change the files under log directory, tag.txt or config.txt. Manually deleting these files may lead to the loss of history and tag set and you may need to set the root directory in the configuration again.

4. Please make sure that the path to any image file does not have the semicolon (":") in it.

To open a new window: -> File -> New Window
To close the current window: -> File -> Close
To change the configuration: -> File -> Open Configuration
