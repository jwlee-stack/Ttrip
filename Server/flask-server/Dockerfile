FROM python:3

ADD . /app
WORKDIR /app

RUN python3 -m pip install -U pip \
    && pip install -r requirements.txt

CMD ["python3",  "-m", "flask",  "run",  "--host=0.0.0.0"]