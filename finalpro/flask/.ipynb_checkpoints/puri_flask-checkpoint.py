# -*- coding: utf-8 -*-
from flask import Flask, jsonify, request, redirect, render_template, url_for
from http import HTTPStatus
from models import Stores
import json
import numpy as np
import sqlalchemy 
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from chatbot import Chatbot
import pymysql

puri_bot = Chatbot()
puri_bot.load_model()

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

url = 'mariadb+pymysql://root:ssac12#$@puri-db.c0z1pznbpt0r.us-east-2.rds.amazonaws.com:3306/puri'
engine = sqlalchemy.create_engine(url)

# nongsaro_rank 테이블을 판다스 데이터프레임으로 불러오기
plant_df = pd.read_sql_table('nongsaro_rank', engine)

# 'watering_spring'이 없는 품종들 제거
plant_df = plant_df[plant_df['watering_spring'] != '내용없음']

# 사용할 열을 정의하고 사용 벡터 추출
feature_cols = ['mng_demand', 'light_demand', 'op_temp', 'win_min_temp', 'humdity', 'watering_spring']
feature_vectors = plant_df[feature_cols]

@app.route("/echo")
def suggest():  
    return "echo"
    
@app.route('/hello_world') #test api
def hello_world():
    return 'Hello, World!'

@app.route("/index") 
def render_html():

    return render_template("index.html")

@app.route('/chat', methods=['POST'])
def chatbot():
    req_data = request.get_json()
    print(req_data)

    msg_num = req_data['msg_num']
    user_id = req_data['user_id']
    user_msg = req_data['user_msg']

    output =  puri_bot.predict(user_msg)
    print(output)

    m_map = {}
    m_map['msg_num']= "msg_num"
    m_map['bot_id']= "puri_chat"
    m_map['message']= output

    print(m_map)
    data = json.dumps(m_map, indent = 2, ensure_ascii=False) 

    return data

@app.route('/recommend', methods=['POST'])
def recommend():
    # 스프링에서 유저 프로필 가져오기
    req_data = request.get_json()
    print(req_data)

    msg_num = req_data['msg_num']
    user_id = req_data['user_id']

    req_list = []

    req_list.append(int(req_data['mng_demand']))
    req_list.append(int(req_data['light_demand']))
    req_list.append(int(req_data['op_temp']))
    req_list.append(int(req_data['win_min_temp']))
    req_list.append(int(req_data['humdity']))
    req_list.append(int(req_data['watering_spring']))

    # 유저 프로필을 numpy array로 가져와서 형상 변환
    profile = np.array(req_list)
    profile = profile.reshape(1,-1)
    
    # 유저 프로필 벡터와 각 식물 벡터간 코사인 유사도 계산
    similarity_scores = cosine_similarity(profile, feature_vectors)
    
    # 데이터프레임에 유사성 점수 추가
    plant_df['similarity'] = similarity_scores[0]
    
    # 식물 종 순위를 유사도 내림차순으로 정렬
    # recommendations = plant_df.sort_values('Similarity', ascending=False)['plant_name'].tolist()
    recommendations = plant_df.sort_values('similarity', ascending=False)[['contents_num', 'plant_name']].to_dict('records')
    
    # 상위 3종을 선택
    recommendations = recommendations[:3]

    rcm_dict = {}
    m_map = {}
    count = 0
    for item in recommendations:
        count += 1
        m_map = item
        rcm_dict.setdefault("recommendations", []).append({"sNo": count, "sNum":m_map['contents_num'], "sName": m_map['plant_name']})

    print(rcm_dict)
    data = json.dumps(rcm_dict, indent = 2, ensure_ascii=False) 
    
    return data

@app.route('/recommend/<param>') #get echo api
def get_recommend_param(param):
    return jsonify({"param": param})

@app.route('/recommend') #get echo api
def get_recommend():

    dict1 = {"recommendations" :[
        {"sNo": 1, "sNum": 11, "sName": "가을테리아1"},
        {"sNo": 2,"sNum": 22, "sName": "가을테리아2"},
        {"sNo": 3,"sNum": 33, "sName": "가을테리아3"}
        ]}

    dict2 = {}
    dict2.setdefault("recommendations", []).append({"sNo": 11, "sNum": 111, "sName": "가을테리아11"})
    dict2.setdefault("recommendations", []).append({"sNo": 22, "sNum": 222, "sName": "가을테리아22"})
    dict2.setdefault("recommendations", []).append({"sNo": 33, "sNum": 333, "sName": "가을테리아33"})
    print(dict2)

    data = json.dumps(dict2, indent = 2,ensure_ascii=False) 
    return data

@app.route('/recommend', methods=['POST']) #post echo api
def post_suggest():
    print(request.is_json)
    param = request.get_json()

    print(param)

    dict1 = {}
    dict1.setdefault("recommendations", []).append({"sNo": 1, "sNum": 111, "sName": "가을테리아11"})
    dict1.setdefault("recommendations", []).append({"sNo": 2, "sNum": 222, "sName": "가을테리아22"})
    dict1.setdefault("recommendations", []).append({"sNo": 3, "sNum": 333, "sName": "가을테리아33"})
    print(dict1)

    data = json.dumps(dict1, indent = 2,ensure_ascii=False) 
    print(data)

    return data

@app.route('/test')
def test():
    data = Stores.get_all()
    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=False,host="0.0.0.0",port=5000)
