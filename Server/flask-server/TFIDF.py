from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
from partition_fit import partial_fit
from tokenizer import tokenizer, stopwords
import pickle
import os
from refiner import refiner
import pandas as pd

TfidfVectorizer.partial_fit = partial_fit

# class Singleton(type):
#     _instances = {}

#     def __call__(cls, *args, **kwargs):
#         city = args[0]
#         if city not in cls._instances:
#             cls._instances[city] = super(Singleton, cls).__call__(*args, **kwargs)
#         return cls._instances[city]
    # metaclass=Singleton

# Tfidf 기반의 추천 엔진 클래스 정의
class TfidfRecommender():
    def __init__(self, city, mysql):
        self.MODEL_FOLDER = os.path.join(os.getenv("MODEL_FOLDER"), city)  # 로컬환경과 배포 환경에 따라 .env 값을 수정해야 합니다.
        self.MTX_PATH = os.path.join(self.MODEL_FOLDER, "model.mtx")
        self.TF_PATH = os.path.join(self.MODEL_FOLDER, "tf.pickle")

        # 모델 폴더 생성
        try:
            if not os.path.exists(self.MODEL_FOLDER):
                os.makedirs(self.MODEL_FOLDER)
        except OSError:
            print("Error: Failed to create the directory.")
        # TfidfVectorizer 객체 생성
        self.tf = TfidfVectorizer(
            stop_words=stopwords, tokenizer=tokenizer, ngram_range=(1, 2), max_features=200000, sublinear_tf=True)

        # MySQL 객체 생성
        self.mysql = mysql
        self.article_pd = self.mysql.query_article_pd(city)
        # first_learning() 실행
        # self.first_learning()

    # 초기 학습 진행 함수
    def first_learning(self):
        X = self.tf.fit_transform(self.article_pd["content"])
        self.tf.n_docs = len(self.article_pd)
        
        with open(self.MTX_PATH, "wb") as fw:
            pickle.dump(X, fw)
        with open(self.TF_PATH, "wb") as fw:
            pickle.dump(self.tf, fw)
    
    # 추가 학습 진행 함수
    def after_learning(self, more):
        self.tf.partial_fit([more])

    # 게시물 추천 함수
    # - article_id : main db 상 게시글 실제 id
    # - content : 추가된 데이터(유사도 검사 대상)
    # - requester_id : 본인 게시글 추천을 피하기 위한 비교 값
    # - n_recom : 추천 갯수
    def recommend_articles(self, article_id, content, requester_id, n_recom):
        # 입력 content 전처리
        # 입력 content 전처리
        content_df = refiner(pd.DataFrame([content], columns=['content']))
        if content_df.empty:  # DataFrame이 비었는지 확인
            return []

        # 학습된 모델 불러오기
        with open(self.MTX_PATH, "rb") as fr:
            X = pickle.load(fr)
        with open(self.TF_PATH, "rb") as fr:
            self.tf = pickle.load(fr)

        # 데이터 프레임에 새로운 행 추가 (인덱스를 명시적으로 설정)
        self.article_pd.loc[len(self.article_pd)] = [article_id, content_df.loc[0, 'content'], requester_id]

        # 추가된 데이터의 Tfidf 벡터 계산
        example_vector = self.tf.transform(content_df['content'])

        # 벡터의 Tfidf 변환 및 저장
        # self.tf.partial_fit(content_df['content'])
        with open(self.TF_PATH, "wb") as fw:
            pickle.dump(self.tf, fw)
        print("here it is : ")
        print(len(self.tf.vocabulary_))
        # 코사인 유사도 계산
        #  다차원 배열을 일차원 배열로 전환하여 배열의 모든 요소가 연속된 선형 구조를 갖도록 변환
        cos_similar = linear_kernel(example_vector, X).flatten()
        # 유사도 순으로 인덱스 추출
        sim_rank_idx = cos_similar.argsort()[::-1]
        sim_rank_idx = sim_rank_idx[:100]

        # 추천 게시물 선별
        i = 0
        cnt = 0
        result = []
        n_recom = int(n_recom)
        while n_recom > cnt and i < 100:
            if self.article_pd.loc[sim_rank_idx[i]]['author_id'] != int(requester_id):
                result.append(self.article_pd.loc[sim_rank_idx[i]]['id'])
                cnt += 1
            i += 1
        # 추천 게시물 반환
        return result

