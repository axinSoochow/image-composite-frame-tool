import json

import xlwt


if __name__ == '__main__':

    a = json.load(open("data.json", encoding='utf8'))
    title = list(set([j for i in a for j in i]))
    book = xlwt.Workbook()
    sheet = book.add_sheet('Sheet1', cell_overwrite_ok=True)  # 添加一个sheet页
    for i in range(len(title)):  # 循环列
        sheet.write(0, i, title[i])  # 将title数组中的字段写入到0行i列中
    for i, it in enumerate(a):
        for j, k in enumerate(title):
            sheet.write(1 + i, j, str(it[k]))
    book.save('待处理的摄影单.xls')
