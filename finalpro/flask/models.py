import pymysql

class Stores():
    def get_all():
        conn = pymysql.connect(host='puri-db.c0z1pznbpt0r.us-east-2.rds.amazonaws.com', port=3306, db='puri', user='root', passwd='ssac12#$', charset='utf8')
        curdic = conn.cursor(pymysql.cursors.DictCursor)
        sql = "SELECT * FROM nongsaro limit 3"
        curdic.execute(sql)
        store_lists = curdic.fetchall()
        return store_lists