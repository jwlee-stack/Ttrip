import os
from flask import Flask, jsonify, request
from flask_cors import CORS
from dotenv import load_dotenv
from TFIDF import TfidfRecommender
from mysql_connector import MySqlConnector, NoDataException

load_dotenv()

# Flask 객체 인스턴스 생성
app = Flask(__name__)
CORS(app)

# MySQL 계정정보 등록
app.config['MYSQL_HOST'] = os.environ.get('MYSQL_HOST')
app.config['MYSQL_USER'] = os.environ.get('MYSQL_USER')
app.config['MYSQL_PASSWORD'] = os.environ.get('MYSQL_PASSWORD')
app.config['MYSQL_DB'] = os.environ.get('MYSQL_DB')
app.config['MYSQL_CURSORCLASS'] = 'DictCursor'

mysql = MySqlConnector()


# 접속 url 설정
@app.route('/')
def index():
    return "hello flask server."

@app.route('/recommend-articles', methods=['POST'])
def recommend_articles():
    if request.method == 'POST':
        data = request.get_json()
        city = data.get('city')
        content = data.get('content')
        article_id = data.get('article_id')
        requester_id = data.get('author_id')
        n_recom = data.get('num_of_articles')

        if not all([article_id, content, requester_id, city, n_recom]):
            return 'Missing required data.', 400
        
        file_path = os.path.join(os.getenv("MODEL_FOLDER"), city, "model.mtx")
        recommender = TfidfRecommender(city, mysql)
        if not os.path.exists(file_path):
            try:
                recommender.first_learning()
            except Exception as e:
                return f"Error: {e}", 500

        try:
            recommendations = recommender.recommend_articles(article_id, content, requester_id, n_recom)
            return jsonify(recommendations), 200
        except NoDataException as e:
            return f"Error: {e}", 400
        except Exception as e:
            return f"Error: {e}", 500
    return 'bad request method', 400

@app.route('/city-learning', methods=['POST'])
def city_learning():
    if request.method == 'POST':
        data = request.get_json()
        city = data.get('city')
        try:
            recommender = TfidfRecommender(city, mysql)
            recommender.first_learning()
        except NoDataException as e:
            return f"Error: {e}", 400
        except Exception as e:
            return f"Error: {e}", 500
        return 'Success', 200
    return 'bad request method', 400

@app.route('/learning-status/<city>', methods=['GET'])
def is_prelearning(city):
    if request.method == 'GET':
        file_path = os.path.join(os.getenv("MODEL_FOLDER"), city, "model.mtx")
        if not os.path.exists(file_path):
            try:
                recommender = TfidfRecommender(city, mysql)
                recommender.first_learning()
            except Exception as e:
                return f"Error: {e}", 500
            return "learning completed", 200
        return "learning already completed", 200
    return 'bad request method', 400


if __name__ == '__main__':
    # 코드 수정 시 자동 반영
    app.run(host='0.0.0.0', port=5000)