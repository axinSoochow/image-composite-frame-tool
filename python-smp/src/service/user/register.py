# coding=utf-8

from src.service.user.smp_register_processer import *

# data_name = "data/register/smp_user.txt"
# data_name = "data/register/jiayou.txt"
data_name = "data/register/uc.txt"


env = "http://172.16.23.4:8080"
env_local = "http://localhost:6666"
change_key_list = ["bizAccount"]

nums = 1

# 电商中台 - 用户注册自动化脚本
if __name__ == '__main__':
    param = json.loads(open(data_name).read())
    processer = SmpUserProcesser(env_local)
    try:
        # change_key(change_key_list, param)
        api_result = processer.register_user(param)
        print(api_result.text)
    except BaseException as err:
        print("服务未调用成功，请检查url:{}是否正确!，错误原因:{}".format(env_local, err))
        quit()

    for i in range(1, nums):
        change_key(change_key_list, param)
        api_result = processer.register_user(param)
        print(api_result.text)
