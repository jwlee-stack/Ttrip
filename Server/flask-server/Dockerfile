FROM python:3

# 상호작용을 요구하는 메시지 피하기
ENV DEBIAN_FRONTEND=noninteractive

# vi 설치
RUN apt-get update && apt-get install -y --no-install-recommends vim

ADD . /app

WORKDIR /app

RUN apt-get update \
    && python3 -m pip install -U pip \
    && apt-get install -y --no-install-recommends mecab libmecab-dev mecab-ipadic-utf8 \
    && pip install -r requirements.txt

COPY ./custom_libs/_mecab.py /usr/local/lib/python3.11/site-packages/eunjeon/_mecab.py

EXPOSE 5000
CMD ["python", "app.py"]
