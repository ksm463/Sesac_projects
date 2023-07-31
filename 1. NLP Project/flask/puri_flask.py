# -*- coding: utf-8 -*-
from flask import Flask, jsonify, request, redirect, render_template, url_for
from http import HTTPStatus
from models import Stores
import json
import numpy as np
import sqlalchemy 
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from util.chatbot import Chatbot
from util.IntentModel import IntentModel
from util.Preprocess import Preprocess
import pymysql

# num 1
puri_daily = Chatbot('./data/daily_puri.csv')
puri_daily.load_model(model_name='./weight_daily/daily_weight_200_230403', puri_model_name='puri_daily')

# num 2
puri_emotion = Chatbot('./data/emotion_puri.csv')
puri_emotion.load_model(model_name='./weight_emotion/emotion_weight_200_230403', puri_model_name='puri_emotion')

# num 3
puri_boast = Chatbot('./data/boast_puri.csv')
puri_boast.load_model(model_name='./weight_boast/boast_weight_200_230403', puri_model_name='puri_boast')

# num 4
puri_recommend = Chatbot('./data/recommend_puri.csv')
puri_recommend.load_model(model_name='./weight_recommend/recommend_weight_200_230403', puri_model_name='puri_recommend')

# num 5
puri_plant = Chatbot('./data/plant_puri.csv')
puri_plant.load_model(model_name='./weight_plant/plant_weight_200_230403', puri_model_name='puri_plant')

# 말뭉치 데이터에서 키워드만 추출해서 사전 리스트 생성
p1 = Preprocess(word2index_dic='./dict/emotion_dict.bin' , userdic='./dict/user_dic.tsv')  #감정 의도
p2 = Preprocess(word2index_dic='./dict/puri_dict.bin' , userdic='./dict/user_dic.tsv') #푸리 의도

# 감정 의도 파악 모델
emotion_labels = {0:"중립", 1:"부정", 2:"긍정"}
try:
    intent_emotion = IntentModel(model_name='./model/emotion_model_100_230331.h5', preprocess=p1, labels=emotion_labels)
    print("감정 의도 파악 모델 로드 완료..")
except: 
    print("감정 의도 파악 모델 로드 실패..")


# 푸리 의도 파악 모델
puri_labels = {1: "일상", 2: "감정", 3: "자랑", 4:"추천", 5:"진단"} 
try:
    intent_puri = IntentModel(model_name='./model/intent_total_100_230403.h5', preprocess=p2, labels=puri_labels)
    print("푸리 의도 파악 모델 로드 완료..")
except: 
    print("푸리 의도 파악 모델 로드 실패..")


# puri_bot2 = Chatbot2()
# puri_bot2.load_model()

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

url = 'mariadb+pymysql://root:ssac12#$@puri-db.c0z1pznbpt0r.us-east-2.rds.amazonaws.com:3306/puri'
engine = sqlalchemy.create_engine(url)

# nongsaro_rank 테이블을 판다스 데이터프레임으로 불러오기
try:
    plant_df = pd.read_sql_table('nongsaro_rank', engine)

    # 'watering_spring'이 없는 품종들 제거
    plant_df = plant_df[plant_df['watering_spring'] != '내용없음']
    # 사용할 열을 정의하고 사용 벡터 추출
    feature_cols = ['mng_demand', 'light_demand', 'op_temp', 'win_min_temp', 'humdity', 'watering_spring']
    feature_vectors = plant_df[feature_cols]
    print("농사로 RANK 불러오기 성공..")
except:
    print("농사로 RANK 불러오기 실패..")

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

    output =  puri_plant.predict(user_msg)
    print(output)

    m_map = {}
    m_map['msg_num']= msg_num
    m_map['bot_id']= "puri_chat"
    m_map['message']= output

    print(m_map)
    data = json.dumps(m_map, indent = 2, ensure_ascii=False) 

    return data

@app.route('/emotion', methods=['POST'])
def emotion():
    req_data = request.get_json()
    print(req_data)

    msg_num = req_data['msg_num']
    user_id = req_data['user_id']
    user_msg = req_data['user_msg']

    m_intent, m_intent_name = intent_emotion.intent_process(user_msg)
    print(m_intent, m_intent_name)

    m_map = {}
    m_map['msg_num']= msg_num
    m_map['bot_id']= "puri_chat"
    m_map['message']= m_intent_name

    print(m_map)
    data = json.dumps(m_map, indent = 2, ensure_ascii=False) 

    return data

@app.route('/chatbot', methods=['POST'])
def puri_chatbot():
    req_data = request.get_json()

    print("user: ", req_data)

    user_id = req_data['cid']
    msg_num = req_data['cnum']
    user_type = req_data['ctype']
    user_msg = req_data['cmsg']

    m_intent, m_intent_name = intent_puri.intent_process(user_msg)
    print("intent: " , m_intent, m_intent_name)

    output = ""
    
    if (m_intent == 1) :
        output = puri_daily.predict(user_msg)
    elif (m_intent == 2) :
        output = puri_emotion.predict(user_msg)
    elif (m_intent == 3) :
        output = puri_boast.predict(user_msg)
    elif (m_intent == 4) :
        output = puri_recommend.predict(user_msg)
    elif (m_intent == 5) :
        output = puri_plant.predict(user_msg)

    print(output)

    m_map = {}
    m_map['cid']= "puri_chat"
    m_map['cnum']= msg_num
    m_map['ctype']= 0
    m_map['cmsg']= output

    print("chatbot: ", m_map)
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
