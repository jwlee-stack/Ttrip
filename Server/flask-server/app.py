import os
from flask import Flask, jsonify, request
from flask_cors import CORS
from flask_mysqldb import MySQL
from dotenv import load_dotenv

load_dotenv()

# Flask 객체 인스턴스 생성
app = Flask(__name__)
CORS(app)
# mysql 객체 생성
mysql = MySQL(app)

# 접속 url 설정
@app.route('/')
def index():
    return

# MySQL 계정정보 등록
app.config['MYSQL_USER'] = os.getenv("MYSQL_USER")
app.config['MYSQL_PASSWORD'] = os.getenv("MYSQL_PASSWORD")
app.config['MYSQL_HOST'] = os.getenv("MYSQL_HOST")
app.config['MYSQL_DB'] = os.getenv("MYSQL_DB")

@app.route('/', methods=['GET', 'POST'])
def visit():
    if request.method == 'GET':
        # MySQL 서버에 접속하기
        cur = mysql.connection.cursor()
        # MySQL 명령어 실행하기
        cur.execute("SELECT * FROM visits")
        # 전체 row 가져오기
        res = cur.fetchall()
        # Flask에서 제공하는 json변환 함수
        return jsonify(res)
    
    if request.method == 'POST':
        name = request.json['visitor_name']
        # mysql 접속 후 cursor 생성하기
        cur = mysql.connection.cursor()
        # DB 데이터 삽입하기
        cur.execute("INSERT INTO visits (visitor_name) VALUES(%s)", [name])
        # DB에 수정사항 반영하기
        mysql.connection.commit()
        # mysql cursor 종료하기
        cur.close()
        return

if __name__ == '__main__':
    # 코드 수정 시 자동 반영
    app.run(debug=True)
