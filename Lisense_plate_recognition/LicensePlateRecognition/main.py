import cv2
import pytesseract

#################################################################################
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files (x86)\Tesseract-OCR\tesseract.exe'
pathCascade = "Resources/Cascades/haarcascade_russian_plate_number.xml"
pathTestImage = "Resources/test_images/license_plate12.jpg"
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
img = cv2.imread(pathTestImage)
# img = cv2.resize(img, testImageSetSize)
imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

# Find all the plates on the test image
plates = plateCascade.detectMultiScale(imgGray, cascadeScaleFactor, cascadeMinNeighbours)
for (x, y, width, height) in plates:
    imgLP = img[y:y + height, x:x + width]
    # cv2.rectangle(img, (x, y), (x + width, y + height), lpRectangleColor, lpRectangleThickness)

# Apply all methods
imgCanny = cv2.Canny(imgLP, 200, 200)
imgLP = cv2.resize(imgLP, foundPlateSetSize)
# imgLP = cv2.resize(imgLP, (400, 200))

imgLP = cv2.cvtColor(imgLP, cv2.COLOR_BGR2GRAY)
cv2.imshow("ff", imgLP)


text = pytesseract.image_to_string(imgLP, config='--psm 11')
print("The license plate number is:", text)
cv2.waitKey(0)

