import os
from flask import Flask, jsonify, request
from flask_cors import CORS
from dotenv import load_dotenv
from TFIDF import TfidfRecommender
from mysql_connector import MySqlConnector


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

@app.route('/recommend-articles', methods=['GET'])
def recommend_articles():
    if request.method == 'GET':
        article_id = request.args.get('article_id')
        content = request.args.get('content')
        requester_id = request.args.get('author_id')
        city = request.args.get('city')
        n_recom = request.args.get('n_recom')

        if not all([article_id, content, requester_id, city, n_recom]):
            return 'Missing required data.', 400

        recommender = TfidfRecommender(city, mysql)
        recommendations = recommender.recommend_articles(article_id, content, requester_id, n_recom)
        return jsonify(recommendations)
    
    return 'unexpected exception occured', 500

@app.route('/city-learning', methods=['POST'])
def city_learning():

    if request.method == 'POST':
        data = request.get_json()
        city = data.get('city')
        recommender = TfidfRecommender(city, mysql)
        recommender.first_learning()
        return 'Success', 200
    
    return 'unexpected exception occured', 500

if __name__ == '__main__':
    # 코드 수정 시 자동 반영
    app.run(host='0.0.0.0', port=5000)
