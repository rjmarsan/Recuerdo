# Recuerdo
### Automatic photo taker for Google Glass

## What is it?
A small app to wake up glass every 5-60 minutes and take a photo. Simple, yet surprisingly addictive. 


## How it works
* At every top of the hour, and after few minutes after that, Recuerdo takes a picture.
* When the device isn't on, or put to sleep, Recuerdo won't take photos.
* The photos are just like every other photo taken by Glass. It will show up on your timeline, it can be shared and deleted, and will be automatically uploaded to Google when Glass is plugged in.
* The interval is selectable, from 5 minutes to 1 hour.

## How to use it
The Mirrors API does not support this behavior, so this app can only be installed via sideloading. I recommend Launchy (https://github.com/kaze0/launchy) for using sideloaded apps.
* Turn on debug mode (Settings -> Device Info -> Turn On Debug Mode)
* Install launchy via ADB
* Install Recuerdo via ADB
* Launch Recuerdo and enable it
