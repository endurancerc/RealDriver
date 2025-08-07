# RealDriver

![alt text](https://github.com/endurancerc/RealDriver/blob/master/RealDriver_Screencap.jpg?raw=true)

<div align="center" markdown="1">
  *Screenshot Simulated*
</div>

## Drive an R/C vehicle over a 4G/5G network for ultimate range!

Are you bored with radio control vehicles that only go a few hundred meters? Take your R/C experience to the next level! [Other systems](https://store.cloudrc.com/products/advenx33-5g-transmitter-shipping-fee-for-igor) touting 4G/5G range require you to purchase expensive propritary hardware that requires additional sim cards and wireless plans. If you already own a [25 Servo Controller](http://www.endurance-rc.com/controllers.php#tfservo) you are ready to go; no additional hardware is neded. Best of all RealDriver is free and open source! If there is a 3rd party servo controller you wish to use simply fork this project and edit it as needed.

### Requirements:
   1. Two Android devices. Both devices MUST be able to access the Internet. A WiFi network *can* be used however you are
      limited to the range of the WiFi network. For ultimate range both devices should be registered on a cellular
      carrier network.
   2. The receiver Android device must have the Endurance R/C 25 Servo Controller attached via a USB OTG cable. 

### Receiver Setup:
   1. Power up the receiver android device, plug the USB OTG cable into the receiver device and then the 25 Servo 
      Controller into the OTG adapter. Next, connect the vehicles steering servo to CH1 and the ESC's to CH2 on the 
      25 Servo Controller board.
         
   2. Start the RealDriver app on the receiver device. For the sake of the tutorial name this device "receiver" and
      click the "START RECEIVER" button.
         
   3. Move to the transmitter device to complete the pairing. Once the camera feeds are synched initiate the 
      connection to the 25 Servo Controller by pressing the USB button on the receiver device. 

### Transmitter Setup:
   1. Start the RealDriver app on the transmitter device. Enter a unique name like "transmitter" and press the 
      "START TRANSMITTER" button. 
         
   2. When prompted for the receiver name enter it here. In our case it was "receiver". Hit the "CONNECT" button.
      At this point both devices will be paired for this session. At this point you will be presented with a 
      camera view of the respectable remote device.

### Usage:
* Most functions of RealDriver are like those found on most video confrencing apps.
      
* Once the camera feeds are synched and the USB controller is enabled you can change the view of the transmitter 
  to landscape mode by rotating the transmitter Android device. The slider bars on both devices are now synched. 
  Moving the bars on either device will cause the other device to respond.
      
* Pressing the down arrow button causes the control panel to be hiddend and joystick to be displayed. This joystick 
  is synched to the steering and throttle channels.

### Buttons:

* Mute Button: silences the remote device's audio.
* Camera Disable Button: this button disables the remote devices video feed.
* Hang Up Button: Exits the current paired state. Video, Audio and Data are all disabled.
* Rotate Screen Button: Rotates the screen to its alternate view.
* USB Connect Button: Connects the RealDriver app to the 25 Servo Controller plugged into the device.
* Map Button: Displays the current GPS coordinates and map data.
* Servo Endpoints Button: ***TODO: Sets the channel end-points***
* Down Arrow: Hides the slider and button panel and displays the joystick
* Joystick: Left-Right mapped to CH1. Up-Down mapped to CH2. The Gear Icon on the top left is displayed
  while in joystick mode. Pressing this will hide the joystick and return the controls menu.

-------------------------------------------------------------------------------------------------------------------------

This app utilizes code from the following GitHub projects:

Google Firebase Realtime Database: https://servowebrtc-b7721-default-rtdb.firebaseio.com

USBHIDTerminal by 452(emetemunoy) - https://github.com/452/USBHIDTerminal

JavaWebRTCYouTube by codewithkael - https://github.com/codewithkael/JavaWebRTCYouTube

-------------------------------------------------------------------------------------------------------------------------
        ______          __                                    ____     ________
       / ____/___  ____/ /_  ___________ _____  ________     / __ \  _/_/ ____/
      / __/ / __ \/ __  / / / / ___/ __ `/ __ \/ ___/ _ \   / /_/ /_/_// /     
     / /___/ / / / /_/ / /_/ / /  / /_/ / / / / /__/  __/  / _, _//_/ / /___   
    /_____/_/ /_/\__,_/\__,_/_/   \__,_/_/ /_/\___/\___/  /_/ /_/_/   \____/   

-------------------------------------------------------------------------------------------------------------------------
