import re
import numpy as np
from scipy.sparse.dia import dia_matrix

# 문서 집합 X에 대해 어휘와 idf를 업데이트하는 함수 정의
def partial_fit(self, X):
    # 문서 집합 X에 대해 어휘와 idf를 업데이트하는 함수 정의
    max_idx = max(self.vocabulary_.values())

    for a in X:
        # 어휘 업데이트
        # 토큰화를 사용하여 문서에서 토큰 추출
        tokens = re.findall(self.token_pattern, a)
        for w in tokens:
            if w not in self.vocabulary_:
                max_idx += 1    
                self.vocabulary_[w] = max_idx

        # idf 업데이트
        # 기존 idf 값을 사용하여 df를 계산
        df = (self.n_docs + self.smooth_idf) / \
            np.exp(self.idf_ - 1) - self.smooth_idf

        # 문서 개수를 1 증가시킴
        self.n_docs += 1
        # df의 크기를 어휘 사전의 크기와 동일하게 축소
        df.resize(len(self.vocabulary_), refcheck = False)
        # df = np.resize(df, (len(self.vocabulary_),))

        # 토큰을 순회하며 각 토큰의 df를 1 증가시킴
        for w in tokens:
            df[self.vocabulary_[w]] += 1
        # df와 smooth_idf를 사용하여 새로운 idf를 계산
        idf = np.log((self.n_docs + self.smooth_idf) /
                     (df + self.smooth_idf)) + 1
        
        # _idf_diag 속성을 갱신하여 idf 업데이트
        self._tfidf._idf_diag = dia_matrix(
            (idf, 0), shape=(len(idf), len(idf)))

