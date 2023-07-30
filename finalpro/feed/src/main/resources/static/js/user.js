
$(function() {

    $("#btn-join").click(function(){
        save();
    });

    $("#btn-login").click(function(){
        login();
    });

    $("#btn-update").click(function(){
        update();
    });

});

function save () {
    // alert("save");
    // html 에 있는 id 들 값 바인드
    let data = {
        userid: $("#userid").val(),
        username: $("#username").val(),
        password: $("#password").val(),
        email: $("#email").val()
    };

    // console.log(data);
    // ajax 통신을 이용해 3개의 데이터를 json 으로 변경하여 insert 요청
    // ajax 호출 시 default 가 비동기 호출
    $.ajax({
        type: "POST",
        url: "/api/user/save",
        data: JSON.stringify(data), // javascript 의 data 를 JSON 로 변환하여 JAVA 로 전달
        contentType: "application/json; charset=utf-8", // body 데이터가 어떤 type 인지
        dataType: "json", // 응답된 데이터가 json 이라면 javascript 오브젝트로 받음
        success : function(res){
            if(res.data != 1) {
                alert("사용자 등록에 실패했습니다");
            } else {
                alert("사용자 등록에 성공하였습니다");
                location.href="/board/feed";
            }
        }
    }).done(function (res) {
        console.log(res);
        // alert("successful!");
    }).fail(function (error) {
        // alert(JSON.stringify(error));
    });
}


// 추가 
function login () {
    // alert("login");
    let data = {
        userid: $("#userid").val(),
        password: $("#password").val(),
    };


    $.ajax({
        type: "POST",
        url: "/api/user/login",
        data: JSON.stringify(data), 
        contentType: "application/json; charset=utf-8", 
        dataType: "json", 
        success : function(res){
            if(res.data != 1) {
                alert("아이디 비밀번호 확인해주세요");
            } else {
                // 로그인 성공
                alert("로그인 성공");
                location.href="/";
            }
        }
    }).done(function (res) {
        // console.log(res);
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
}

function update () {
    // alert("update");

    let id = $("#id").text();

    let data = {
        userid: $("#userid").val(),
        password: $("#password").val(),
        username: $("#username").val(),
        email: $("#email").val(),
    };
    // console.log("id: ", id);
    // console.log("data:", data);

    $.ajax({
        type: "PUT",
        url: "/api/user/update/" + id,
        data: JSON.stringify(data), 
        contentType: "application/json; charset=utf-8", 
        dataType: "json",
        success : function(res){
            if(res.data != 1) {
                alert("사용자 수정에 실패하였습니다");
            } else {
                // 로그인 성공
                alert("사용자 수정 성공하였습니다");
                location.href="/";
            }
        }        
    }).done(function (res) {
        console.log(res);
        // alert("업데이트 성공");
        location.href = "/";
    }).fail(function (error) {
        // alert(JSON.stringify(error));
    });
}

