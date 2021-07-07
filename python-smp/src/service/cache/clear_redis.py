# coding=utf-8
import redis

# ad-building-test
host = "m-redis-k8s.shtest.ke.com"
port = "37479"
pwd = 'redis.shtest'
db = 6
keys_file = 'ad_building_cache_key.txt'

# ad-building-生产
host = "m11371.zeus.redis.ljnode.com"
port = "11371"
pwd = None
db = 0
keys_file = 'ad_building_cache_key.txt'

if __name__ == '__main__':
    if pwd is not None:
        conn = redis.Redis(host=host, port=port, db=db, password=pwd)
    else:
        conn = redis.Redis(host=host, port=port, db=db)
    lines = open(keys_file).read().splitlines()
    for line in lines:
        cursor = 0
        while True:
            keys = conn.scan(cursor, line, 1000)
            for key in keys[1]:
                print('%s被移除' % key.decode('utf-8'))
                conn.delete(key)
            if keys[0] == 0:
                break
            cursor = keys[0]
