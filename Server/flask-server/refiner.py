import numpy as np
import re
def refiner(article_pd):
    # 정규 표현식 수행하여  'content' 열에 있는 모든 문자열에서 한글과 공백을 제외한 모든 문자를 제거
    article_pd['content'] = article_pd['content'].str.replace("[^ㄱ-ㅎㅏ-ㅣ가-힣 ]","") 
    article_pd['content'].replace('', np.nan, inplace=True) # 공백은 Null 값으로 변경
    article_pd = article_pd.dropna() # Null 값 제거
    return article_pd



def refiner_text(content):
    content = re.sub("[^ㄱ-ㅎㅏ-ㅣ가-힣 ]", "", content)
    content = content.strip()  # 양쪽 공백 제거
    return content