from dotenv import load_dotenv
from refiner import refiner
import pandas as pd
import mysql.connector
import os

load_dotenv()

class MySqlConnector:

    def __init__(self):
        self.conn = mysql.connector.connect(
            host = os.getenv("MYSQL_HOST"),
            user = os.getenv("MYSQL_USER"),
            password = os.getenv("MYSQL_PASSWORD"),
            database = os.getenv("MYSQL_DB"),
            port = os.getenv("MYSQL_PORT"),
        )
        if self.conn is None:
            raise ValueError("Failed to establish a connection with the MySQL database.")
        
    # 특정 도시의 게시글 조회 후 pandas DataFrame 타입으로 반환
    def query_article_pd(self, city):
        query = "SELECT id, content, author_id FROM coredb.article where end_date >= now() and status = 'T'and city = '{}';".format(city)
        cursor = self.conn.cursor()
        cursor.execute(query)
        article_list = cursor.fetchall()
        # article_pd의 id값을 maindb article_id값과 일치시켜서 관리한다.
        article_pd = pd.DataFrame(article_list, columns=['id', 'content', 'author_id'])
        return refiner(article_pd)