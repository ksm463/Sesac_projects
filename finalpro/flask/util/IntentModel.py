import tensorflow as tf
from keras.models import Model, load_model
from keras import preprocessing
from keras.utils import pad_sequences
import gc

# 단어 시퀀스 벡터 크기
MAX_SEQ_LEN = 25

# 의도 분류 모델 모듈
class IntentModel:
    def __init__(self, model_name, preprocess, labels):

        # 의도 클래스별 레이블블
        self.labels = labels

        # 의도 분류 모델 불러오기
        self.model = load_model(model_name)

        # 챗봇 텍스트 전처리기
        self.p = preprocess

    # 의도 클래스 예측
    def predict_class(self, query):
        # 형태소 분석
        pos = self.p.pos(query)

        # 문장내 키워드 추출(불용어 제거)
        keywords = self.p.get_keywords(pos, without_tag=True)
        sequences = [self.p.get_wordidx_sequence(keywords)]


        # 패딩처리
        padded_seqs = pad_sequences(sequences, maxlen=MAX_SEQ_LEN, padding='post')

        predict = self.model.predict(padded_seqs)
        predict_class = tf.math.argmax(predict, axis=1)
        return predict_class.numpy()[0]
    
    def intent_process(self, sentence):
        # 의도 파악
        intent_pred = self.predict_class(sentence)
        intent_name = self.labels[intent_pred]
        return intent_pred, intent_name