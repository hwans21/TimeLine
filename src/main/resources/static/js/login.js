$(document).on('click','#loginBtn', function(e){
    $('#loginForm').removeAttr('onsubmit');
    var str = $('#inputStreamer').val();
    if(str != ''){
    $('#loginForm').attr("method","post");
    $('#loginForm').attr("action","/loginStart");
    $('#loginForm').submit();
    }else{
        alert("값을 입력해주세요");
    }
});
$(document).on('keydown','#inputStreamer', function(e){
    if(e.keyCode === 13){
        $('#loginForm').removeAttr('onsubmit');
        var str = $('#inputStreamer').val();
        if(str != ''){
        $('#loginForm').attr("method","post");
        $('#loginForm').attr("action","/loginStart");
        $('#loginForm').submit();
        }else{
            alert("값을 입력해주세요");
        }
    }
});
