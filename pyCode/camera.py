import cv2

import numpy as np
import math

from networktables import NetworkTables

cap = cv2.VideoCapture(0)

cap.set(3, 320)
cap.set(4, 240)

boundaries = [([21,100,110], [142,184,240])]

angle_in_degrees = 23.55

width_of_target = 13

minarea = 2000

while True:
        
    _,frame = cap.read()
    blur = cv2.GaussianBlur(frame, (9, 9), 0)

    hsv = cv2.cvtColor(blur, cv2.COLOR_BGR2HSV)

    lower = np.array([21,120,110])
    upper = np.array([143,164,240])

    mask = cv2.inRange(hsv, lower, upper)

    hsv, contours, hierachy = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
                                                 
    for c in contours:
        area = cv2.contourArea(c)
        if area > minarea:
            x, y, w, h = cv2.boundingRect(c)
            blur = cv2.rectangle(blur, (x, y), (x + w, y + h), (0, 0, 255), 2)
            distance = (320 * width_of_target)/(2 * w * math.tan(math.radians(angle_in_degrees)))
            print('Distance:', distance)
            print('target width:', w)
            
    cv2.imshow('blur', blur)
    cv2.imshow('mask', mask)
    

    if cv2.waitKey(5) == 27:
        break
        

cap.release()
cv2.destroyAllWindows()

