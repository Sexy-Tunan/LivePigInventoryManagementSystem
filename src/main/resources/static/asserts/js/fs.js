// 导入fs模块
const fs = require('fs')
// 导入path模块
const path = require('path')
//获取拼接得到的路径
const pathStr = path.join(__dirname,'./test.txt')
// console.log(pathStr)
//写入文件
fs.writeFile(pathStr,'写入的内容',(err)=>{
    if(err)
        return console.log('写入失败！'+err.message)
    else
        console.log('写入成功')
})
// 读取文件
fs.readFile(pathStr,(err,date)=>{
    if(err)
        console.log('读取失败！'+err)
    else
        console.log('读取成功！\n'+date)
})

