'''Aspect Ratio,
Height off Ground
different orientations for rectangular prism'''


import cv2

import numpy as np
import math
import time

from networktables import NetworkTables
from networktables.util import ntproperty

# Connect NetworkTables
# Note:  IP address should be robot IP address ??10.41.21.2??
NetworkTables.initialize(server="169.254.213.186")

#Begin video capture (0 is port number of camera)
cap = cv2.VideoCapture(0)

#Format video to 320px by 240px and set camera brightness to 50%
cap.set(3, 320)
cap.set(4, 240)
cap.set(10, 0.5)

#Camera field of view
FOV_angle_in_degrees = 23.55

#Width of target (cube) !!This value will differ based on orientation - more work to come
width_of_target = 13
height_of_target = 11

#Minimum area required for system to create bounding rectangle (in px)
minarea = 2000

#Not sure what these do right now; theoretically stuff w/network tables
ntVidOsTimestamp = ntproperty('/vmx/videoOSTimestamp', 0)
ntNavxSensorTimestamp = ntproperty('/vmx/navxSensorTimestamp', 0)

#Counter incremented in while loop to slow down output to ~1 set per second
counter = 0

#Primary program body 
while True:

    #Save frame to variable frame    
    _,frame = cap.read()

    #This is network tables stuff
    #print("Timestamp: %d" % video_timestamp)

    '''videoTimestampString = 'video: %d' % video_timestamp
    cv2.putText(frame, videoTimestampString, (30, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255,255,255), 1, cv2.LINE_AA)
    navXSensorTimestampString = "navX: %d" % vmx.getAHRS().GetLastSensorTimestamp()
    cv2.putText(frame, navXSensorTimestampString, (30, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255,255,255), 1, cv2.LINE_AA)
    '''

    #Blur to clean noise for mask
    blur = cv2.GaussianBlur(frame, (9, 9), 0)

    #Convert BGR image to HSV
    hsv = cv2.cvtColor(blur, cv2.COLOR_BGR2HSV)

    #Mask array params (HSV) (currently set to best approximation of Power Cube colors for brightness/lighting in Mr. Alkire's classroom)
    lower = np.array([24, 94, 163])
    upper = np.array([169,255,255])

    #Create mask to filter all values outside of the above range to black
    mask = cv2.inRange(hsv, lower, upper)

    #Finds contours in image (frame)
    hsv, contours, hierarchy = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    mainContour = contours[0]
    
    minDistance = 1000.0
    minheight = 1000
    contourArr = []
    
    #Area of contours
    area = cv2.contourArea(c)

    #Check if the area is bigger than the minimum area (above)
    if area > minarea:
        #Attributes of bounding rectangle
        x, y, w, h = cv2.boundingRect(mainContour)

        #Overwrite blur with image of bounding rectangle on contour
        blur = cv2.rectangle(blur, (x, y), (x + w, y + h), (0, 0, 255), 2)

        #check if height is greater than width
        if h > w:
            if (h / w) > ((13/11) + .005) or (h / w) > ((13/11) - .005) or (h / w) < ((13 / math.sqrt(290) + .005)) or (h / w) < ((13 / math.sqrt(290) - .005)):
                #we have a cube
                distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                height = (240 * height_of_object)/(240 - (h + y))
            elif (h / w) > ((22/13) + 1) or (h / w) > ((22/13) - 1) or (h / w) < ((22/(13 * math.sqrt(2)) + 1)) or (h / w) < ((22/(13 * math.sqrt(2)) - 1)):
                #we have 2 cubes of standard rotation stacked
                distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                height = (240 * height_of_object)/(240 - ((h / 2.0) + y))
            elif (h / w) > ((33/13) + 1) or (h / w) > ((33/13) - 1) or (h / w) < ((33/(13 * math.sqrt(2)) + 1)) or (h / w) < ((33/(13 * math.sqrt(2)) - 1)):
                #we have 3 standard cubes stacked
                distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                height = (240 * height_of_object)/(240 - ((h / 3.0) + y))
            else:
                #we do not have any cubes
                distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    
        elif w < h:
            if (h / w) > ((11/13) + 1) or (h / w) > ((11/13) - 1) or (h / w) < ((11/(13 * math.sqrt(2)) + 1)) or (h / w) < ((11/(13 * math.sqrt(2)) - 1)):
                #we have 1 cube
                #do stuff
                distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                height = (240 * height_of_object)/(240 - (h + y))
            elif (h / w) > ((11/26) + 1) or (h / w) > ((11/26) - 1) or (h / w) < ((11/(2 *(13 * math.sqrt(2)) + 1))) or (h / w) < ((11/(2 * (13 * math.sqrt(2)) - 1))):
                #we have 2 cubes side-by-side
                distance = (320 * width_of_target)/(2 * (w/2.0) * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                height = (240 * height_of_object)/(240 - (h + y))
            elif (h / w) > ((11/39) + 1) or (h / w) > ((11/39) - 1) or (h / w) < ((11/(3 *(13 * math.sqrt(2)) + 1))) or (h / w) < ((11/(3 * (13 * math.sqrt(2)) - 1))):
                #we have 3 cubes side-by-side
                distance = (320 * width_of_target)/(2 * (w/3.0) * math.tan(math.radians(FOV_angle_in_degrees)))
                angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                height = (240 * height_of_object)/(240 - (h + y))
            
        #Calculate distance to target and angle between center of screen and center of target
        #distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
        #angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
        #height = 

        if distance < minDistance:
            minDistance = distance
                

        #Slows down output (outputting distance and angle currently)
        if counter % 100 == 0:
            print('Distance:', distance)
            #print('target width:', w)
            print('angle offset:', angle)

        counter += 1

    #Show the videos (color version and mask)
    cv2.imshow('blur', blur)
    cv2.imshow('mask', mask)
    
    #Escape key ends program
    if cv2.waitKey(5) == 27:
        break
        
#Stop recording
cap.release()
cv2.destroyAllWindows()

