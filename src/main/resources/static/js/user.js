let index = {
    init: function() {
        $("#btn-save").on("click", ()=>{ // this를 바인딩하기 위해 function(){}을 안 쓰고 ()=>{}를 사용.
            this.save();
        });
        $("#btn-update").on("click", ()=>{ // this를 바인딩하기 위해 function(){}을 안 쓰고 ()=>{}를 사용.
            this.update();
        });
    },

    save:function() {
        //alert("user의 save함수 호출됨");

        // 웹에서 가져온 값들을 username, email, password 변수로 넣음.
        let data = {
            username: $("#username").val(),
            email: $("#email").val(),
            password: $("#password").val()
        };

        // ajax 호출!!!
        // ajax 호출 시 default가 비동기 호출!
        $.ajax({ // 회원가입 수행 요청
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data), // http body 데이터. 여기서 사용하는 data는 js 변수라서 자바가 이해 불가 -> JSON으로 변환해서 전송!
            contentType: "application/json; charset=utf-8", // 위에 꺼랑 세트임. body 데이터가 어떤 타입인지(MIME)
            dataType: "json" // 요청을 서버로 보낸 후, 응답이 발생하면 기본적으로 모두 String 타입임. 근데 이 형태가 JSON 형태라면 js 객체로 바꾼 후 done()이나 fail()의 function()에 매개변수로 들어감. 근데 요새는 이거 안 써도 그냥 JSON으로 변환해줌...
        }).done(function(resp){ // 응답의 결과가 정상인 경우. resp는 응답받은 데이터가 JSON일 경우 들어가는 것.
            if (resp.status === 500) {
                alert("회원가입에 실패하였습니다.");
            } else {
                alert("회원가입이 완료되었습니다.");
                location.href = "/"; // 돌아갈 페이지
            }
        }).fail(function(error){ // 응답의 결과가 실패한 경우. error은 응답받은 데이터가 JSON일 경우 들어가는 것.
            alert(JSON.stringify(error));
        }); // ajax 통신을 이용해서 3개의 파라미터를 JSON으로 변경하여 insert 요청.
    },

    update:function() {
        //alert("user의 save함수 호출됨");

        // 웹에서 가져온 값들을 username, email, password 변수로 넣음.
        let data = {
            id: $("#id").val(),
            username: $("#username").val(),
            email: $("#email").val(),
            password: $("#password").val()
        };

        // ajax 호출!!!
        // ajax 호출 시 default가 비동기 호출!
        $.ajax({ // 회원가입 수행 요청
            type: "PUT",
            url: "/user",
            data: JSON.stringify(data), // http body 데이터. 여기서 사용하는 data는 js 변수라서 자바가 이해 불가 -> JSON으로 변환해서 전송!
            contentType: "application/json; charset=utf-8", // 위에 꺼랑 세트임. body 데이터가 어떤 타입인지(MIME)
            dataType: "json" // 요청을 서버로 보낸 후, 응답이 발생하면 기본적으로 모두 String 타입임. 근데 이 형태가 JSON 형태라면 js 객체로 바꾼 후 done()이나 fail()의 function()에 매개변수로 들어감. 근데 요새는 이거 안 써도 그냥 JSON으로 변환해줌...
        }).done(function(resp){ // 응답의 결과가 정상인 경우. resp는 응답받은 데이터가 JSON일 경우 들어가는 것.
            alert("회원 수정이 완료되었습니다.");
            location.href = "/"; // 돌아갈 페이지
        }).fail(function(error){ // 응답의 결과가 실패한 경우. error은 응답받은 데이터가 JSON일 경우 들어가는 것.
            alert(JSON.stringify(error));
        }); // ajax 통신을 이용해서 3개의 파라미터를 JSON으로 변경하여 insert 요청.
    }

    /*login:function() {
        //alert("user의 save함수 호출됨");

        // 웹에서 가져온 값들을 username, email, password 변수로 넣음.
        let data = {
            username: $("#username").val(),
            email: $("#email").val(),
            password: $("#password").val()
        };

        // ajax 호출!!!
        // ajax 호출 시 default가 비동기 호출!
        $.ajax({ // 회원가입 수행 요청
            type: "POST",
            url: "/api/user/login",
            data: JSON.stringify(data), // http body 데이터. 여기서 사용하는 data는 js 변수라서 자바가 이해 불가 -> JSON으로 변환해서 전송!
            contentType: "application/json; charset=utf-8", // 위에 꺼랑 세트임. body 데이터가 어떤 타입인지(MIME)
            dataType: "json" // 요청을 서버로 보낸 후, 응답이 발생하면 기본적으로 모두 String 타입임. 근데 이 형태가 JSON 형태라면 js 객체로 바꾼 후 done()이나 fail()의 function()에 매개변수로 들어감. 근데 요새는 이거 안 써도 그냥 JSON으로 변환해줌...
        }).done(function(resp){ // 응답의 결과가 정상인 경우. resp는 응답받은 데이터가 JSON일 경우 들어가는 것.
            alert("로그인이 완료되었습니다.");
            location.href = "/"; // 돌아갈 페이지
        }).fail(function(error){ // 응답의 결과가 실패한 경우. error은 응답받은 데이터가 JSON일 경우 들어가는 것.
            alert(JSON.stringify(error));
        }); // ajax 통신을 이용해서 3개의 파라미터를 JSON으로 변경하여 insert 요청.

    }*/
}

index.init();