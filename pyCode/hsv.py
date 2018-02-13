import cv2
import numpy as np

cap = cv2.VideoCapture(0)

def nothing(x):

    pass

cv2.namedWindow("result")
cap.set(3, 320)
cap.set(4, 240)
cap.set(10, 0.5)

h, s, v = 100, 100, 100

cv2.createTrackbar('lower b', 'result', 0, 255, nothing)
cv2.createTrackbar('lower g', 'result', 0, 255, nothing)
cv2.createTrackbar('lower r', 'result', 0, 255, nothing)
cv2.createTrackbar('upper b', 'result', 0, 255, nothing)
cv2.createTrackbar('upper g', 'result', 0, 255, nothing)
cv2.createTrackbar('upper r', 'result', 0, 255, nothing)


while True:

    ret, frame = cap.read()

    #blur = cv2.GaussianBlur(frame, (9,9), 0)
    #bgr = cv2.cvtColor(blur, cv2.COLOR_BGR2HSV)

    b1 = cv2.getTrackbarPos('lower b', 'result')
    g1 = cv2.getTrackbarPos('lower g', 'result')
    r1 = cv2.getTrackbarPos('lower r', 'result')
    b2 = cv2.getTrackbarPos('upper b', 'result')
    g2 = cv2.getTrackbarPos('upper g', 'result')
    r2 = cv2.getTrackbarPos('upper r', 'result')
    lower = np.array([b1, g1, r1])
    upper = np.array([b2, g2, r2])

    mask = cv2.inRange(frame, lower, upper)

    result = cv2.bitwise_and(frame, frame, mask = mask)

    cv2.imshow('result', result)
    cv2.imshow('mask', mask)

    k = cv2.waitKey(5) and 0xFF
    if k == 27:
        break

cap.release()
cv2.destroyAllWindows()
