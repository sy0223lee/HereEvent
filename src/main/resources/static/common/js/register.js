function pwCheck(){
            if($('#pass').val() == $('#passchk').val()){
                $('#check-same-password').text('비밀번호 일치').css('color', 'green')
            }else{
                $('#check-same-password').text('비밀번호 불일치').css('color', 'red')
            }
        }
$(document).ready(function(){
    $("#nick").keyup(function(){
        let data1 = {"nick" : $("#nick").val()};
        $.ajax({
            url:"/hereevent/check-nick",
            type:"post",
            data: data1,
            dataType: 'text',
            success: function(result){
                $("#check-duplicate-nick").text(result);
            },
            error: function(){
                alert("요청 실패");
            }
        });
    });
$("#email").keyup(function(){
    let data2 = {"email" : $("#email").val()};
    $.ajax({
        url:"/hereevent/check-email",
        type:"post",
        data: data2,
        dataType: 'text',
        success: function(result){
            $("#check-duplicate-email").text(result);
        },
        error: function(){
            alert("요청 실패");
        }
    });
});
});