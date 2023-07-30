
$(function() {
    $("#btn-save").click(function(){
        // alert("save1")
        save();
    });

    $("#btn-delete").click(function(){
        deleteById();
    });

    $("#btn-update").click(function(){
        updateById();
    });

    $("#btn-list").click(function(){
        boardList();
    });

});

function save () {
    // alert("save2");

    let data = {
        title : $("#title").val(),
        content : $("#content").val()
    };

    $.ajax({
        type: "POST",
        url: "/api/board",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success : function(res){
            if(res.data != 1) {
                alert("글쓰기 등록에 실패했습니다");
            } else {
                alert("글쓰기 등록 성공");
                location.href="/board/feed";
            }
        }
    }).done(function (res) {
        console.log(res);
        // alert("글쓰기가 완료되었습니다!");
    }).fail(function (error) {
        // alert(JSON.stringify(error));
    });
}


function deleteById () {
    // alert("delete");
    let  id = $("#id").text();

    $.ajax({
        type: "DELETE",
        url: "/api/board/"+ id,
        dataType: "json",
        success : function(res){
            if(res.data != 1) {
                alert("글 삭제에 실패했습니다");
            } else {
                alert("글삭제 성공하였습니다");
                location.href="/board/feed";
            }
        }
    }).done(function (res) {
        console.log(res);
        // alert("삭제가 완료되었습니다!");
    }).fail(function (error) {
        // alert(JSON.stringify(error));
    });
}

function updateById() {
    // alert("update");

    let id = $("#id").val();

    let data = {
        title : $("#title").val(),
        content : $("#content").val()
    };

    $.ajax({
        type: "PUT",
        url: "/api/board/" + id,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success : function(res){
            if(res.data != 1) {
                alert("게시글 수정에 실패했습니다");
            } else {
                alert("게시글 수정 성공하였습니다");
                location.href="/board/feed";
            }
        }
    }).done(function (res) {
        console.log(res);
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
}
