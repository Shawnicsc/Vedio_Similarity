<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>视频相似度检测演示</title>
</head>
<body>
<h1>上传视频并计算相似度</h1>
<form action="/similarity_war/similarity" enctype='multipart/form-data' method='POST'>
    <input type="file" name="ori_video" style="margin-top:20px;"/>
    <br>
    <input type="file" name="mon_video" style="margin-top:20px;"/>
    <br>
    <input type="submit" value="上传" class="button-new" style="margin-top:15px;"/>
</form>
</body>
</html>