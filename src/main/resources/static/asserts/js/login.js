/*todo:
从服务器中获取所有用户名，判断输入的用户名是否存在，若不存在则在用户名输入框下方弹出警示框，提示‘该用户名不存在’；
若存在则在用户输入密码完毕点击登录按钮后，判断密码是否正确，不正确则在密码输入框下方弹出警示框提示‘密码不正确，请再次输入’
*/ 

// 获取 输入框
const inputBox = document.getElementsByClassName('form-control')

// 获取 警示框   
const warningBox = document.getElementsByClassName("alert alert-warning fade in")
    
// 获取登录按钮
const btn = document.querySelector(".btn.btn-defaultr.center-block")

// 创建 警示框
function createWarningBox(){
    const warningBox = document.createElement("div");
    warningBox.classList.add("alert","alert-warning","fade","in");
    warningBox.setAttribute('role','alert');
    warningBox.innerHTML = "<strong>Warning!</strong> Better check yourself, you're not looking too good."
    return warningBox
}

// 显示 元素
function show(element){
    // 删除一个给定的类名
    element.classList.remove("hidden")
}
// 隐藏 元素
function hide(element){
    // 增加一个给定类名
    element.classList.add("hidden")
}

// 用户名输入框失去焦点时，验证用户名是否存在,若不存在则给出提示
inputBox[0].addEventListener("blur", () => {
    // 触发焦点事件时，若其中不存在内容，则退出函数不作处理，
    if(inputBox[0].value === ''){
        return;
    }
    // todo:验证用户名是否存在,若不存在则给出提示
    if(true){
        // 修改警示框提示内容
        warningBox[0].innerHTML = "<strong>Warning!</strong> The user name does not exist.Please check and enter it again."   
        // 弹出警示框
        show(warningBox[0]);        
    }
})

// 点击登录按钮后，验证密码是否正确,若不正确则给出相应提示
btn.addEventListener("click", () => {
    // 若用户名输入框中不存在内容，则给出相应提示
    if(inputBox[0].value === ''){
        warningBox[0].innerHTML = "<strong>Warning!</strong> Please enter the user name."   
        show(warningBox[0]);
    }
    // 若密码输入框中不存在内容，则给出相应提示
    if(inputBox[1].value === ''){
        warningBox[1].innerHTML = "<strong>Warning!</strong> Please enter the password."   
        show(warningBox[1]);
    }
    // tode:若密码不正确，则给出相应提示
    if(inputBox[0].value !== '' && inputBox[1].value !== ''){
        if(true){
            warningBox[1].innerHTML = "<strong>Warning!</strong> The password is incorrect. Please re-enter it."    
            show(warningBox[1])
        }
    }
})

// 用户在输入框输入时，隐藏相应警示框
inputBox[0].addEventListener("input",()=>{
    hide(warningBox[0])
    hide(warningBox[1])
})
inputBox[1].addEventListener("input",()=>{
    hide(warningBox[1])
})



