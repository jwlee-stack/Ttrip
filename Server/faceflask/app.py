from flask import Flask, request, jsonify
import math
from sklearn import neighbors
import os
import os.path
import pickle
from PIL import Image, ImageDraw
from io import BytesIO
import face_recognition
from face_recognition.face_recognition_cli import image_files_in_folder

ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg','JPG'}
app = Flask("__name__")

class DataRes:
    def __init__(self,status,message,data):
        self.status=status
        self.message=message
        self.data=data

def train(train_dir, model_save_path=None, n_neighbors=None, knn_algo='ball_tree', verbose=True):
    """
    Trains a k-nearest neighbors classifier for face recognition.

    :param train_dir: directory that contains a sub-directory for each known person, with its name.

     (View in source code to see train_dir example tree structure)

     Structure:
        <train_dir>/
        ├── <person1>/
        │   ├── <somename1>.jpeg
        │   ├── <somename2>.jpeg
        │   ├── ...
        ├── <person2>/
        │   ├── <somename1>.jpeg
        │   └── <somename2>.jpeg
        └── ...

    :param model_save_path: (optional) path to save model on disk
    :param n_neighbors: (optional) number of neighbors to weigh in classification. Chosen automatically if not specified
    :param knn_algo: (optional) underlying data structure to support knn.default is ball_tree
    :param verbose: verbosity of training
    :return: returns knn classifier that was trained on the given data.
    """
    X = []
    y = []

    # Loop through each person in the training set
    for class_dir in os.listdir(train_dir):
        if not os.path.isdir(os.path.join(train_dir, class_dir)):
            continue

        # Loop through each training image for the current person
        for img_path in image_files_in_folder(os.path.join(train_dir, class_dir)):
            # 이미지 파일 자체의 회전 정보 제거
            try:
                pil_image = Image.open(img_path)
                if hasattr(pil_image, '_getexif'):
                    exifdata = pil_image._getexif()
                    if exifdata is not None:
                        orientation = exifdata.get(274)
                        if orientation == 3:
                            pil_image = pil_image.transpose(Image.ROTATE_180)
                        elif orientation == 6:
                            pil_image = pil_image.transpose(Image.ROTATE_270)
                        elif orientation == 8:
                            pil_image = pil_image.transpose(Image.ROTATE_90)
                        pil_image.save(img_path, quality=100)
            except:
                pass
            image = face_recognition.load_image_file(img_path)
            face_bounding_boxes = face_recognition.face_locations(image)

            #학습할 사진에 사람이 없음
            if len(face_bounding_boxes) != 1:
                # If there are no people (or too many people) in a training image, skip the image.
                if verbose:
                    print("학습할 사진에 사람이 한 명이 아님!!")
                    if len(face_bounding_boxes) < 1:
                        result="얼굴을 찾지 못함"
                    else: 
                        result="얼굴이 2개 이상"
                    
                    print("Image {} not suitable for training: {}".format(img_path, result))
                    return DataRes(400,result,"false"),None
            else:
                # Add face encoding for current image to the training set
                X.append(face_recognition.face_encodings(image, known_face_locations=face_bounding_boxes)[0])
                y.append(class_dir)
    
    if len(X)==0:
        print("에러: 사람이 인식된 사진이 한 장도 없음")
        
    # Determine how many neighbors to use for weighting in the KNN classifier
    if n_neighbors is None:
        n_neighbors = int(round(math.sqrt(len(X))))
        if verbose:
            print("Chose n_neighbors automatically:", n_neighbors)

    # Create and train the KNN classifier
    knn_clf = neighbors.KNeighborsClassifier(n_neighbors=n_neighbors, algorithm=knn_algo, weights='distance')
    knn_clf.fit(X, y)

    # Save the trained KNN classifier
    if model_save_path is not None:
        with open(model_save_path, 'wb') as f:
            pickle.dump(knn_clf, f)

    return DataRes(200,"얼굴 학습 성공","true"),knn_clf

def predict(X_img_path, knn_clf=None, model_path=None, distance_threshold=0.4):
    """
    Recognizes faces in given image using a trained KNN classifier

    :param X_img_path: path to image to be recognized
    :param knn_clf: (optional) a knn classifier object. if not specified, model_save_path must be specified.
    :param model_path: (optional) path to a pickled knn classifier. if not specified, model_save_path must be knn_clf.
    :param distance_threshold: (optional) distance threshold for face classification. the larger it is, the more chance
           of mis-classifying an unknown person as a known one.
    :return: a list of names and face locations for the recognized faces in the image: [(name, bounding box), ...].
        For faces of unrecognized persons, the name 'unknown' will be returned.
    """
    # 이미지 파일 자체의 회전 정보 제거
    try:
        pil_image = Image.open(X_img_path)
        if hasattr(pil_image, '_getexif'):
            exifdata = pil_image._getexif()
            if exifdata is not None:
                orientation = exifdata.get(274)
                if orientation == 3:
                    pil_image = pil_image.transpose(Image.ROTATE_180)
                elif orientation == 6:
                    pil_image = pil_image.transpose(Image.ROTATE_270)
                elif orientation == 8:
                    pil_image = pil_image.transpose(Image.ROTATE_90)
                pil_image.save(X_img_path, quality=100)
    except:
        pass

    if not os.path.isfile(X_img_path) or os.path.splitext(X_img_path)[1][1:] not in ALLOWED_EXTENSIONS:
        print("Invalid image path: {}".format(X_img_path))
        return DataRes(500,"유효하지 않은 파일 경로","false"),[]
    if knn_clf is None and model_path is None:
        print("Must supply knn classifier either thourgh knn_clf or model_path")
        return DataRes(500,"가중치 파일이 없음","false"),[]

    # Load a trained KNN model (if one was passed in)
    if knn_clf is None:
        with open(model_path, 'rb') as f:
            knn_clf = pickle.load(f)

    # Load image file and find face locations
    X_img = face_recognition.load_image_file(X_img_path)
    X_face_locations = face_recognition.face_locations(X_img)
    print()
    print(X_face_locations)
    print()
    # If no faces are found in the image, return an empty result.
    if len(X_face_locations) == 0:
        print("사진에 인물이 없음!!")
        return DataRes(400,"사진에 인물이 없음","false"),[]
    elif len(X_face_locations)>1:
        print("사진에 인물이 2명 이상!!")
        return DataRes(400,"사진에 인물이 2명 이상","false"),[]
    # Find encodings for faces in the test iamge
    faces_encodings = face_recognition.face_encodings(X_img, known_face_locations=X_face_locations)

    # Use the KNN model to find the best matches for the test face
    closest_distances = knn_clf.kneighbors(faces_encodings, n_neighbors=1)
    are_matches = [closest_distances[0][i][0] <= distance_threshold for i in range(len(X_face_locations))]

    # Predict classes and remove classifications that aren't within the threshold
    return DataRes(200,"본인 인증 성공","true"),[(pred, loc) if rec else ("unknown", loc) for pred, loc, rec in zip(knn_clf.predict(faces_encodings), X_face_locations, are_matches)]

def show_prediction_labels_on_image(img_path, predictions):
    """
    Shows the face recognition results visually.

    :param img_path: path to image to be recognized
    :param predictions: results of the predict function
    :return:
    """

    image = Image.open(img_path)
    

    draw = ImageDraw.Draw(image)

    for name, (top, right, bottom, left) in predictions:
        # Draw a box around the face using the Pillow module
        draw.rectangle(((left, top), (right, bottom)), outline=(0, 0, 255))

        # There's a bug in Pillow where it blows up with non-UTF-8 text
        # when using the default bitmap font
        name = name.encode("UTF-8")

        # Draw a label with a name below the face
        text_width, text_height = draw.textsize(name)
        draw.rectangle(((left, bottom - text_height - 10), (right, bottom)), fill=(0, 0, 255), outline=(0, 0, 255))
        draw.text((left + 6, bottom - text_height - 5), name, fill=(255, 255, 255, 255))

    # Remove the drawing library from memory as per the Pillow docs
    del draw

    # Display the resulting image
    image.show()

def delete_folder(folder_path):
    """
    Deletes the given folder and all its contents (subfolders and files).
    """
    for root, dirs, files in os.walk(folder_path, topdown=False):
        for name in files:
            os.remove(os.path.join(root, name))
        for name in dirs:
            os.rmdir(os.path.join(root, name))
    os.rmdir(folder_path)

@app.route('/face',methods=['GET'])
def hell():
    return jsonify("success")

@app.route('/face',methods=['POST'])
def hello():
    # 여러 개의 파일 받기
    trainImgs=[request.files.get('trainImg1'),request.files.get('trainImg2'),request.files.get('trainImg3')]
    testImg = request.files.get('testImg')

    # 여러 개의 텍스트 데이터 받기
    nickname = request.form.get('nickname')

    trainFolderPath = os.path.join("train",nickname)
    trainImgsFolderPath=os.path.join(trainFolderPath,nickname)
    #train 폴더 생성
    if not os.path.exists(trainImgsFolderPath):
        os.makedirs(trainImgsFolderPath)

    for trainImg in trainImgs:   
        # 파일 객체를 BytesIO에 쓰기
        img_bytes = trainImg.read()

        # BytesIO에서 Image 객체로 변환하기
        img = Image.open(BytesIO(img_bytes))
        fileName=trainImg.filename
        #이미지 저장하기
        img.save(os.path.join(trainImgsFolderPath,fileName), quality=100)

    # STEP 1: Train the KNN classifier and save it to disk
    # Once the model is trained and saved, you can skip this step next time.
    print("Training KNN classifier...")
    dataRes, classifier = train(trainFolderPath, model_save_path="trained_knn_model.clf", n_neighbors=None)
    
    #학습할 사진에 얼굴이 없거나 여러 개임
    if(dataRes.data=="false"):
        delete_folder(trainFolderPath)
        return jsonify(dataRes.__dict__)
    
    print("Training complete!")

    testFolderPath = os.path.join("test",nickname)

    #test 폴더 생성
    if not os.path.exists(testFolderPath):
        os.makedirs(testFolderPath)

    # .DS_Store 파일 삭제
    ds_store_path = os.path.join(testFolderPath, ".DS_Store")
    if os.path.exists(ds_store_path):
        os.remove(ds_store_path)
        print(".DS_Store file removed successfully.")

    # 파일 객체를 BytesIO에 쓰기
    img_bytes = testImg.read()

    # BytesIO에서 Image 객체로 변환하기
    img = Image.open(BytesIO(img_bytes))
    fileName=testImg.filename
    #이미지 저장하기
    img.save(os.path.join(testFolderPath,fileName), quality=100)

    # STEP 2: Using the trained classifier, make predictions for unknown images
    for image_file in os.listdir(testFolderPath):
        full_file_path = os.path.join(testFolderPath, fileName)

        print("Looking for faces in {}".format(image_file))

        # Find all people in the image using a trained classifier model
        # Note: You can pass in either a classifier file name or a classifier model instance
        dataRes,predictions = predict(full_file_path, model_path="trained_knn_model.clf")

        #테스트할 사진에 얼굴이 없거나 여러개임
        if dataRes.data=="false":
            delete_folder(trainFolderPath)
            delete_folder(testFolderPath)
            return jsonify(dataRes.__dict__)

        # Print results on the console
        for name, (top, right, bottom, left) in predictions:
            print("- Found {} at ({}, {})".format(name, left, top))
            #얼굴을 탐지했으나, 본인이 아님
            if name=="unknown":
                delete_folder(trainFolderPath)
                delete_folder(testFolderPath)
                return jsonify(DataRes(200,"본인 얼굴이 아님","false").__dict__)
        # Display results overlaid on an image
        # show_prediction_labels_on_image(os.path.join(testFolderPath, image_file), predictions)

        #본인 얼굴임
        delete_folder(trainFolderPath)
        delete_folder(testFolderPath)
        return jsonify(dataRes.__dict__)


if __name__ == '__main__':
    # app.logger.setLevel(logging.DEBUG)
    app.run(host='0.0.0.0',port=5050,debug=True)
