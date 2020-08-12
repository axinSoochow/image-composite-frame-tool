# coding=utf-8
import json

from src.service.store.smp_register_processer import SmpVrProcesser, change_vr_key

data_name = "data/vr_data.txt"

env = "http://172.16.23.4:8080"
env_local = "http://localhost:6666"
change_key_list = ["storeCode"]

nums = 1

# 门店vr服务 - 初始化数据
if __name__ == '__main__':
    param = json.loads(open(data_name).read())
    processer = SmpVrProcesser(env_local)
    try:
        change_vr_key(change_key_list, param)
        # 向mysql中插入一条门店数据
        processer.init_mysql_data(param)
        api_result = processer.store_vr_data_init(param)
        print(api_result.text)
    except BaseException as err:
        print(err)
        quit()

    for i in range(1, nums):
        change_vr_key(change_key_list, param)
        processer.init_mysql_data(param)
        api_result = processer.store_vr_data_init(param)
        print(api_result.text)
