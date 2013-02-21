pocketmusik
===========

= Requirements =

* Android SDK with API 4.0+ and platform tools
* Play framework 2.1rc2

= Client =

In order to simplify the next step, you can update the project configuration files according to your environment with android SDK tools:

```
$ android project update --path ./client/
```

But that's optional, you can also pass for following command an ANDROID_HOME env variable that will contain the path to your Android SDK directory.

In order to install the client to your device, execute:

```
$ ./client/cordova/BOOM
```

-or, if you haven't launched update your configuration files-

```
$ ANDROID_HOME=/path/to/android-sdk-linux ./client/cordova/BOOM
```


= Server =

Retrieve Play framework:

```
$ wget http://download.playframework.org/releases/play-2.1-RC2.zip
$ unzip play*.zip
```

Run the server for a given path:

```
$ POCKET_MUSIK_PATH=/path/to/your/musik/library ./play*/play
```


