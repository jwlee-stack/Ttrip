FROM python:3.10.11

RUN apt-get update && \
    apt-get install -y cmake

WORKDIR /app
COPY . .
RUN pip install --upgrade pip && \
    pip install -r requirements.txt --no-cache-dir

EXPOSE 5050

CMD ["python", "app.py"]