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

#Width and height of target (cube) !!This value will differ based on orientation - more work to come
width_of_target = 13
height_of_object = 11

#Minimum area required for system to create bounding rectangle (in px)
minarea = 2500

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

    if len(contours) > 0:
            
        mainContour = contours[0]
        
        minDistance = 1000.0
        minheight = 1000
        contourArr = []
        
        #Area of contours
        area = cv2.contourArea(mainContour)

        #Check if the area is bigger than the minimum area (above)
        if area > minarea:
            #Attributes of bounding rectangle
            x, y, w, h = cv2.boundingRect(mainContour)

            #Overwrite blur with image of bounding rectangle on contour
            blur = cv2.rectangle(blur, (x, y), (x + w, y + h), (0, 0, 255), 2)

            #Deal with integer problems
            w *= 1.0
            h *= 1.0

            #Equation for width of target
            #find rotation first
            #think this is wrong right now :(                             (   x   )    
            #rotation_in_rads = ((math.acos(((h / w) -((11.0/26.0) * (1 + (math.sqrt(2) / 2)))) * (2/ ((11.0/13.0) * (1 - (math.sqrt(2)/2.0)))))) / 4.0)

            '''
            #check if height is greater than width
            if h > w:
                if (h / w) < ((13/11) + .005) or (h / w) > ((13/11) - .005) or (h / w) < ((13 / math.sqrt(290) + .005)) or (h / w) > ((13 / math.sqrt(290) - .005)):
                    #we have a single cube w/ height of 13 in
                    height_of_target = 13
                    width_of_target = (height_of_target / h) * w
                    distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    if(240 - (h + y)) <= 0.005 or (240 - (h + y)) >= -0.005:
                        height = 0
                    else:
                        height = (240 * height_of_object)/(240 - (h + y))
                    print('Statement 1')
                elif (h / w) < ((22/13) + 1) or (h / w) > ((22/13) - 1) or (h / w) < ((22/(13 * math.sqrt(2)) + 1)) or (h / w) < ((22/(13 * math.sqrt(2)) - 1)):
                    #we have 2 cubes of standard rotation stacked
                    height_of_target = 22
                    width_of_target = (height_of_target / (h / 2.0)) * w
                    distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    height = (240 * height_of_object)/(240 - ((h / 2.0) + y))
                    if(240 - ((h / 2.0) + y)) <= 0.005 or (240 - ((h / 2.0) + y)) >= -0.005:
                        height = 0
                    else:
                        height = (240 * height_of_object)/(240 - ((h/2.0) + y))
                    print('Statement 2')
                elif (h / w) > ((33/13) + 1) or (h / w) > ((33/13) - 1) or (h / w) < ((33/(13 * math.sqrt(2)) + 1)) or (h / w) < ((33/(13 * math.sqrt(2)) - 1)):
                    #we have 3 standard cubes stacked
                    width_of_target = (height_of_target / h) * w
                    distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    height = (240 * height_of_object)/(240 - ((h / 3.0) + y))
                    if(240 - ((h / 3.0) + y)) <= 0.005 or (240 - ((h / 3.0) + y)) >= -0.005:
                        height = 0
                    else:
                        height = (240 * height_of_object)/(240 - ((h / 3.0) + y))
                    print('Statement 3')
                else:
                    #we do not have any cubes
                    distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    print('Statement 4')
            elif h < w:
                
                if (h / w) > ((11/13) + 1) or (h / w) > ((11/13) - 1) or (h / w) < ((11/(13 * math.sqrt(2)) + 1)) or (h / w) < ((11/(13 * math.sqrt(2)) - 1)):
                    #we have 1 cube
                    #do stuff
                    distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    if(240 - (h + y)) <= 0.005 and (240 - (h + y)) >= -0.005:
                        height = 0
                    else:
                        height = (240 * height_of_object)/(240 - (h + y))
                    
                    print('Statement 5')
                elif (h / w) > ((11/26) + 1) or (h / w) > ((11/26) - 1) or (h / w) < ((11/(2 *(13 * math.sqrt(2)) + 1))) or (h / w) < ((11/(2 * (13 * math.sqrt(2)) - 1))):
                    #we have 2 cubes side-by-side
                    distance = (320 * width_of_target)/(2 * (w/2.0) * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    if(240 - (h + y)) <= 0.005 or (240 - (h + y)) >= -0.005:
                        height = 0
                    else:
                        height = (240 * height_of_object)/(240 - (h + y))
                    print('Statement 6')
                elif (h / w) > ((11/39) + 1) or (h / w) > ((11/39) - 1) or (h / w) < ((11/(3 *(13 * math.sqrt(2)) + 1))) or (h / w) < ((11/(3 * (13 * math.sqrt(2)) - 1))):
                    #we have 3 cubes side-by-side
                    distance = (320 * width_of_target)/(2 * (w/3.0) * math.tan(math.radians(FOV_angle_in_degrees)))
                    angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
                    if(240 - (h + y)) <= 0.005 or (240 - (h + y)) >= -0.005:
                        height = 0
                    else:
                        height = (240 * height_of_object)/(240 - (h + y))
                    print('Statement 7')
            else:
                distance = 0
                height = 0
                angle = 0
                '''

            #Based on the aspect ratio and comparison of the width and the height determine the height of the box, width, distance to it and angle of offset

            if h >= w:
                
                if (22.0 / (13 * math.sqrt(2)) - 0.005) < (h / w) and (h / w) < (22.0/13.0 + 0.005):
                    
                    #we have two stacked cubes
                    print("Statement 1")
                    height_of_target = 22

                elif (33.0/(13 * math.sqrt(2)) - 0.005) < (h / w) and ((h / w) < 33.0/13.0 + 0.005):
                    
                    #we have three stacked cubes
                    print("Statement 2")
                    height_of_target = 33

                else:
                    print("Statement 3")
                    height_of_target = 13
                    
            elif w > h:
               
                if 11/(39 * math.sqrt(2)) < (h/w) and (h/w) < 11/39:

                    #we have three cubes side-by-side
                    print("Statement 4")
                    height_of_target = 11

                elif 11/(26.0 * math.sqrt(2)) < (h/w) and (h/w) < 11/26:

                    #we have two cubes side-by-side
                    print("Statement 5")
                    height_of_target = 11
                
                elif (11.0/(13.0 * math.sqrt(2))- 0.005) < (h/w) and (h/w) < (13.0/math.sqrt(290) + 0.006):

                    #we have one cube on the 13x13 side
                    print("Statement 6")
                    height_of_target = 11

                elif (13.0/math.sqrt(290) - 0.006) < (h/w) and (h/w) < (11.0/13.0 + 0.005):
                    print("!!!Ambiguous Case!!!")

                    height_tester = 11

                    width_tester = (13.0/h) * w

                    area_tester = height_tester * width_tester

                    if 143.0 - 0.005 < area_tester and area_tester < 158.503 + 0.005:
                        print("Statement 7")
                        height_of_target = 13

                    else:
                        print("Statement 8")
                        height_of_target = 11
                    
                        
                elif (11.0/13.0 - 0.005) < (h/w) and (h/w) < 1.005:

                    #we have one cube on its 13x11 side
                    print("Statement 9")
                    height_of_target = 13
                        
                else:
                    #there is no cube
                    print("Statement 10")
                    height_of_target = 0   
            else:

                #there is no cube
                print("Statement 11")
                height_of_target = 0
            
            #Calculate distance to target and angle between center of screen and center of target
            #distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(FOV_angle_in_degrees)))
            #angle = math.degrees(math.atan2(((13.0/w) * ((x + (w/2.0)) - 160)),distance))
            #height = 
                '''
                if distance < minDistance:
                    minDistance = distance
                '''     

            #Slows down output (outputting distance and angle currently)
            #if counter % 100 == 0:
                #print('Distance:', distance)
                #print('target width:', w)
                #print('angle offset:', angle)
            print('height:', height_of_target)
                #print('width:', width_of_target)
                #print('rotation of object:',((math.acos(((h / w) -((11.0/26.0) * (1 + (math.sqrt(2) / 2)))) * (2/ ((11.0/13.0) * (1 - (math.sqrt(2)/2.0)))))) / 4.0))

            #counter += 1

    #Show the videos (color version and mask)
    cv2.imshow('blur', blur)
    cv2.imshow('mask', mask)
    
    #Escape key ends program
    if cv2.waitKey(5) == 27:
        break
        
#Stop recording
cap.release()
cv2.destroyAllWindows()
'''elif 11.0/13.0 < (h/w) and (h/w) < 1:

                    #we have one cube on its 13x11 side
                    print("Statement 9")
                    height_of_target = 13'''
