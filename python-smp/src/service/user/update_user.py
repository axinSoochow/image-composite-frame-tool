# coding=utf-8

from src.service.user.smp_register_processer import *

# data_name = "data/update/smp_user.txt"
# data_name = "data/update/jiayou.txt"
data_name = "data/update/uc.txt"

env = "http://172.16.23.4:8080"
env_local = "http://localhost:6666"

bizAccout = "3000040000000002"

# 电商中台 - 用户注册自动化脚本
if __name__ == '__main__':
    param = json.loads(open(data_name).read())
    param["bizAccount"] = bizAccout
    processer = SmpUserProcesser(env_local)
    try:
        api_result = processer.update_user_info(param)
        print(api_result.text)
    except BaseException as err:
        print("服务未调用成功，请检查url:{}是否正确!，错误原因:{}".format(env_local, err))
        quit()
