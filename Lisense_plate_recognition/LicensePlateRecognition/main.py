import cv2
import pytesseract
import re
import urllib.request
import urllib.parse
import time

#################################################################################
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files (x86)\Tesseract-OCR\tesseract.exe'
pathCascade = "Resources/Cascades/haarcascade_russian_plate_number.xml"
pathTestImage = "Resources/test_images/license_plate8.jpg"
testImageSetSize = (600, 400)
cascadeScaleFactor = 1.1
cascadeMinNeighbours = 3
lpRectangleColor = (0, 0, 255)
lpRectangleThickness = 2
foundPlateSetSize = (500, 166)
widthImg = 300
heightImg = 100
#################################################################################

# Prepare image and cascade
plateCascade = cv2.CascadeClassifier(pathCascade)

cap = cv2.VideoCapture(0)
# Set height
cap.set(3, 640)
# Set width
cap.set(4, 480)
# Set brightness
cap.set(10, 85)

while True:
    success, img = cap.read()
    # img = cv2.resize(img, testImageSetSize)
    imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # Find all the plates on the test image
    plates = plateCascade.detectMultiScale(imgGray, cascadeScaleFactor, cascadeMinNeighbours)
    imgLP = None
    for (x, y, width, height) in plates:
        imgLP = img[y:y + height, x:x + width]

    if imgLP is not None:
        imgCanny = cv2.Canny(imgLP, 200, 200)
        imgLP = cv2.resize(imgLP, foundPlateSetSize)

        imgLP = cv2.cvtColor(imgLP, cv2.COLOR_BGR2GRAY)

        text = pytesseract.image_to_string(imgLP, config='--psm 11')

        licensePlate = re.findall("([0-Z]{1,3}-[0-Z]{1,3}-[0-Z]{1,3})", text)

        url = 'http://18.198.37.93:8083/sms'

        if licensePlate:
            if len(licensePlate[0]) == 8:
                print("The license plate number is:", licensePlate[0])
                values = {}
                values['licenseplate'] = licensePlate[0]

                data = urllib.parse.urlencode(values)
                data = data.encode('ascii')
                req = urllib.request.Request(url, data)

                with urllib.request.urlopen(req) as response:
                    print(response.read())

                time.sleep(5)

    cv2.imshow("Video", img)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break


