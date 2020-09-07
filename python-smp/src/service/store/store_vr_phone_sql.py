# coding=utf-8
import json


data_name = "data/phone.txt"

# 门店vr服务 - 手机号生成sql
if __name__ == '__main__':
    a = json.load(open(data_name, encoding='utf8'))
    for owner in a:
        print("UPDATE t_store_vr set owner_name = \'{}\', owner_phone = \'{}\' where store_code = \'{}\';"
              .format(owner['orderName'],owner['orderPhone'],owner["storeCode"]))
