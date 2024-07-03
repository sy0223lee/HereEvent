$(".btn-check").each(function () {
    $(this).click(function () {
        let tag = $('input[name=tag]').val();
        let stateList = [];
        let typeList = [];

        $('input[name=state]:checked').each(function () {
            stateList.push($(this).val());
        });
        $('input[name=type]:checked').each(function () {
            typeList.push($(this).val());
        });

        console.log(tag);
        console.log(stateList);
        console.log(typeList);

        let data = {'tag': tag, 'state': stateList, 'type': typeList};
        console.log(data);

        $.ajax({
            url: '/hereevent/map/list',
            method: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                let printData = "";
                result.forEach(event => {
                    printData += "<div class='card' style='width: 18rem;'>" +
                        "               <img src='/hereevent/download/event/" + event.img_path +"' class='card-img-top'>" +
                        "                <div class='card-body'>" +
                        "                    <h5 class='card-title'>" + event.name + "</h5>" +
                        "                    <p class='card-text'><span>" + event.start_date + "</span> ~ <span>" + event.end_date + "</span></p>" +
                        "                    <p class='card-text'>" + event.addr + "</p>" +
                        "                    <a href='/hereevent/event/" + event.event_no + "' class='btn btn-primary'>상세페이지</a>" +
                        "                </div>" +
                        "            </div>";
                });
                $("#list-box").empty(); // <tr>태그 모두 지우기
                $("#list-box").append(printData);
            },
            error: function (obj, msg) {
                console.log("요청 실패: " + obj + ", " + msg);
            }
        });
    });
});
