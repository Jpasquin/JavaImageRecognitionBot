import cv2 as cv
import numpy as np
import os
import sys

os.chdir(os.path.dirname(os.path.abspath(__file__)))

confidenceLevel = 0.96
def findClickPositions(needle_img_path, haystack_img_path, threshold= 0.96, debug_mode=None):

    haystack_img = cv.imread(haystack_img_path, cv.IMREAD_REDUCED_COLOR_2)
    needle_img = cv.imread(needle_img_path, cv.IMREAD_REDUCED_COLOR_2)

    # cv.imshow('Result', haystack_img)
    # cv.waitKey()

    # Get besst match coordinates.

    needle1_w = needle_img.shape[1]
    needle1_h = needle_img.shape[0]
    #TM_SQDIFF_NORMED , Other match Template, TM_CCOEFF_NORMED for normal
    method = cv.TM_CCORR_NORMED
    result1 = cv.matchTemplate(haystack_img, needle_img, method)

    print(result1)

    locations = np.where(result1 >= threshold)
    #converts to xy list of results
    locations = list(zip(*locations[::-1]))
    print(locations)

    # list looks for[x, y, w, h]
    rectangles = []
    for loc in locations:
        rect = [int(loc[0]), int(loc[1]), needle1_w, needle1_w]
        #To prevent throwing out single lined rectangles
        rectangles.append(rect)
        rectangles.append(rect)
    #last number (0.5) controls the intensity of rectangle merging
    rectangles, weights = cv.groupRectangles(rectangles, 1, 0.5)
    # print(rectangles)

    points = []
    if len(rectangles):
        print('found needle')

        line_color = (0, 255, 0)
        line_type = cv.LINE_4
        marker_color = (255, 0, 255)
        marker_type = cv.MARKER_CROSS

        for (x, y, w, h) in rectangles:

            #Determines the center position
            center_x = x + int(w/2)
            center_y = y + int(h/2)
            #saves the points
            points.append((center_x, center_y))

            if debug_mode == 'rectangles':
                top_left = (x, y)
                bottom_right = (x + w, y + h)
                cv.rectangle(haystack_img, top_left, bottom_right, line_color, line_type)
                cv.imwrite('result.png', haystack_img)
            elif debug_mode == 'points':
                cv.drawMarker(haystack_img, (center_x, center_y), marker_color, marker_type)
                cv.imwrite('result.png', haystack_img)

        # if debug_mode:
        #     cv.imshow('Matches', haystack_img)
        #     cv.waitkey()

    return points


points = findClickPositions('NormalTrees/Tree1.png', 'NormalTrees/ScreenShot.png', threshold= 0.96, debug_mode='rectangles')
print(points)
finalPoint = ','.join(str(x) for x in points[0])

finalX, finalY = map(int, finalPoint.split(','))

print(finalX)
print(finalY)

sys.stdout = open("Target.json", "w")
#JSON Format
print('{')
print('"MouseTargetX": {}'.format(finalX)+',')
print('"MouseTargetY": {}'.format(finalY))
print('}')

sys.stdout.close()