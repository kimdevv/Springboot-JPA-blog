let index = {
    init: function() {
        $("#btn-save").on("click", ()=>{ // this를 바인딩하기 위해 function(){}을 안 쓰고 ()=>{}를 사용.
            this.save();
        });
        $("#btn-delete").on("click", ()=>{ // this를 바인딩하기 위해 function(){}을 안 쓰고 ()=>{}를 사용.
            this.deleteById();
        });
        $("#btn-update").on("click", ()=>{ // this를 바인딩하기 위해 function(){}을 안 쓰고 ()=>{}를 사용.
            this.update();
        });
        $("#btn-reply-save").on("click", ()=>{ // this를 바인딩하기 위해 function(){}을 안 쓰고 ()=>{}를 사용.
            this.replySave();
        });
    },

    save:function() {
        //alert("user의 save함수 호출됨");

        // 웹에서 가져온 값들을 username, email, password 변수로 넣음.
        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        // ajax 호출!!!
        // ajax 호출 시 default가 비동기 호출!
        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert("글쓰기가 완료되었습니다.");
            location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    deleteById:function() {
        let id = $("#id").text();

        $.ajax({
            type: "DELETE",
            url: "/api/board/"+id,
            dataType: "json"
        }).done(function(resp){
            alert("삭제가 완료되었습니다.");
            location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    update:function() {
        let id = $("#id").val();
        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        $.ajax({
            type: "PUT",
            url: "/api/board/" + id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert("수정이 완료되었습니다.");
            location.href = "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    replySave:function() {
        let data = {
            userId: $("#userId").val(),
            boardId: $("#boardId").val(),
            content: $("#reply-content").val()
        };
        let boardId = $("#boardId").val();

        $.ajax({
            type: "POST",
            url: `/api/board/${data.boardId}/reply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert("댓글이 작성되었습니다.");
            location.href = `/board/${data.boardId}`;
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    replyDelete:function() {
        $.ajax({
            type: "DELETE",
            url: `/api/board/${boardId}/reply/${replyId}`,
            dataType: "json"
        }).done(function(resp){
            alert("댓글이 삭제되었습니다.");
            location.href = `/board/${boardId}`;
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }
}

index.init();