var username = "";
var chat_msg = "";

function askUsername() {
  send_message("안녕? 이름이 뭐니?");
}

function chat_send (message) {
  // alert("chat send");
  // html 에 있는 id 들 값 바인드
  let data = {
      cmsg: message,
  };

  // console.log("data", data);
  // ajax 통신을 이용해 3개의 데이터를 json 으로 변경하여 insert 요청
  // ajax 호출 시 default 가 비동기 호출
  $.ajax({
      type: "POST",
      url: "/api/chatbot/send",
      data: JSON.stringify(data), // javascript 의 data 를 JSON 로 변환하여 JAVA 로 전달
      contentType: "application/json; charset=utf-8", // body 데이터가 어떤 type 인지
      dataType: "text", // 응답된 데이터가 json 이라면 javascript 오브젝트로 받음
  }).done(function (res) {
      // console.log("res", res);
      // alert("successful!");

      send_message(res);
  }).fail(function (error) {
      // alert(JSON.stringify(error));
  });
}

function send_message(message) {
  var prevMessage = $("#container").html();
  if(prevMessage!=0)
  prevMessage = prevMessage+ "";

  $("#container").html(prevMessage+ "Chatbot: " + message +"<br/>");
  $(".current_message").hide();
  $(".current_message").delay(400).fadeIn();
  $(".current_message").removeClass("current_message");

}

function ai(message) {

  if (username.length <3) {
    username = message;
    send_message("안녕 " + message + ", 어떻게지내?");
  }
  else if(message.indexOf("어떻게")>=0|| message.indexOf("지내")>=0)
  {
    send_message( "좋아");
  }
  else if(message.indexOf("시간")>=0|| message.indexOf("시간 알려줘")>=0)
  {
    var date = new Date();
    var h = date.getHours();
    var m = date.getMinutes();
    send_message("현재 시간:  "+ h+":"+m );
  }
  else{
   chat_send(message);
  }

}

$(function() {

  askUsername();

  $("#textbox").keypress(function(event) {
    if (event.which == 13) {
      if ($("#enter").prop("checked")) {
        event.preventDefault();
        $("#send").click();
      }
    }
  });

  $("#send").click(function() {


    var username = "You : "
    var message = $("#textbox").val();
    $("#textbox").val("");

    var prevMessage = $("#container").html();

    // console.log(prevMessage.length);
    if (prevMessage.length != 0 || prevMessage != "")
      prevMessage = prevMessage + "";

    $("#container").html(prevMessage + username + message + "<br/>");

    $("#container").scrollTop($("#container").prop("scrollHeight"));

    ai(message);

  });

});

