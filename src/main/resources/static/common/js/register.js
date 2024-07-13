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
                if(result === '사용 불가능한 닉네임') {
                    $("#check-duplicate-nick").text(result).css('color', 'red');
                }else {
                    $("#check-duplicate-nick").text(result).css('color', 'green');
                }
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
            if(result === '사용 불가능한 이메일') {
                $("#check-duplicate-email").text(result).css('color', 'red');
            }else {
                $("#check-duplicate-email").text(result).css('color', 'green');
            }
        },
        error: function(){
            alert("요청 실패");
        }
    });
});
});