# ApiTest
使用springboot+RSA加解密写的一个小demo

使用的是前后端分离，现在贴上前端，其实就一个页面而已

前端测试页面，就一个ajax请求
~~~~<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>第三方接口测试</title>
</head>
<body>
<form>
    <table align="center">
        <tr>
            <td>内容：<input type="text" name="content" id="content"></td>
            <td><input type="button" name="send" id="send" value="发送"></td>
        </tr>
    </table>
</form>

<script src="../js/jquery-1.11.3.min.js"></script>
<script>
    $("#send").click(function () {
        let content = $("#content").val();
        $.get("http://localhost:8080/get?content="+content,function (result) {
            var data = result.data;
            console.log(data);
        })
    })
</script>
</body>
</html>~~~~
