function send () {
    // alert("send");
    // html 에 있는 id 들 값 바인드
    let data = {
        msg: $("#send").val(),
    };

    // console.log(data);
    // ajax 통신을 이용해 3개의 데이터를 json 으로 변경하여 insert 요청
    // ajax 호출 시 default 가 비동기 호출
    $.ajax({
        type: "POST",
        url: "/api/chatbot/send",
        data: JSON.stringify(data), // javascript 의 data 를 JSON 로 변환하여 JAVA 로 전달
        contentType: "application/json; charset=utf-8", // body 데이터가 어떤 type 인지
        dataType: "json" // 응답된 데이터가 json 이라면 javascript 오브젝트로 받음
    }).done(function (res) {
        // console.log(res);
        // alert("successful!");
    }).fail(function (error) {
        // alert(JSON.stringify(error));
    });
}

$(function() {

    $("#send").click(function(){
        send();
    });

});